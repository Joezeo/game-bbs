package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.enums.SolrCoreNameEnum;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.common.dto.SteamAppDTO;
import com.joezeo.joefgame.common.dto.TopicDTO;
import com.joezeo.joefgame.common.dto.UserDTO;
import com.joezeo.joefgame.potal.service.SearchService;
import com.joezeo.joefgame.potal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 *  由于自己租用的 vps 内存过于小了，SOLR 运行一段时间后会被 killed 导致搜索异常
 *  加入数据库搜索容灾机制
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    private static final byte SOLR = 0; // 通过SOLR进行搜索
    private static final byte MYSQL = 1; // 通过MYSQL进行搜索
    private static volatile int status = SOLR;

    @Autowired
    private HttpSolrClient solrClient;
    @Autowired
    private UserService userService;

    @Override
    public PaginationDTO<SteamAppDTO> searchSteam(String condition, Integer page) {
        PaginationDTO<SteamAppDTO> paginationDTO = null;
        if(status == SOLR){
            paginationDTO = searchSteamBySolr(condition, page);
        }

        if(status == MYSQL){
            paginationDTO = searchSteamByMysql(condition, page);
        }

        if(paginationDTO == null){
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        }

        return paginationDTO;
    }

    @Override
    public PaginationDTO<TopicDTO> searchTopic(String condition, Integer page) {
        PaginationDTO<TopicDTO> paginationDTO = null;
        if(status == SOLR){
            paginationDTO = searchTopicBySolr(condition, page);
        }

        if(status == MYSQL){
            paginationDTO = searchTopicByMysql(condition, page);
        }

        if(paginationDTO == null){
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        }

        return paginationDTO;
    }

    @Override
    public PaginationDTO<UserDTO> searchUser(String condition, Integer page) {
        PaginationDTO<UserDTO> paginationDTO = null;
        if(status == SOLR){
            paginationDTO = searchUserBySolr(condition, page);
        }

        if(status == MYSQL){
            paginationDTO = searchUserByMysql(condition, page);
        }

        if(paginationDTO == null){
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        }

        return paginationDTO;
    }

    /*-----------------private methods--------------------*/
    private void switchMysql(){
        status = MYSQL;
    }
    private void switchSolr(){
        status = SOLR;
    }

    private PaginationDTO<SteamAppDTO> searchSteamBySolr(String condition, Integer page) {
        // 多core分布式查询，需要在solr.xml中配置whiteList
        List<String> steamCores = SolrCoreNameEnum.listSteamCores();
        StringBuilder shardsBuilder = new StringBuilder();
        steamCores.stream().forEach(coreName -> {
            shardsBuilder.append(solrClient.getBaseURL() + "/" + coreName + ",");
        });
        String shards = shardsBuilder.substring(0, shardsBuilder.lastIndexOf(","));

        // 判断搜索条件是否是数字，如果是则通过appid搜索
        boolean isNum = StringUtils.isNumeric(condition);

        // 设置搜索参数
        ModifiableSolrParams solrParams = new ModifiableSolrParams();
        if (isNum) {
            solrParams.set("q", "appid:" + condition);
        } else {
            solrParams.set("q", "name:" + condition);
        }
        solrParams.set("start", (page - 1) + "");
        solrParams.set("rows", "30");
        solrParams.set("shards", shards);
        solrParams.set("echoParams", "all");
        try {
            PaginationDTO<SteamAppDTO> paginationDTO = new PaginationDTO<>();

            QueryResponse query = solrClient.query(steamCores.get(0), solrParams);

            int totalCount = (int) query.getResults().getNumFound();
            paginationDTO.setPagination(page, 30, totalCount);

            List<SteamAppDTO> apps = query.getBeans(SteamAppDTO.class);
            paginationDTO.setDatas(apps);

            return paginationDTO;
        } catch (SolrServerException e) {
            switchMysql(); // 切换为用MySQL进行搜索
            log.error("solr查询steam app失败，启动MySQL容灾机制");
        } catch (IOException e) {
            switchMysql(); // 切换为用MySQL进行搜索
            log.error("solr查询steam app失败，启动MySQL容灾机制");
        }
        return null;
    }

    private PaginationDTO<TopicDTO> searchTopicBySolr(String condition, Integer page) {
        // 设置搜索参数
        ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("q", "title:" + condition);
        solrParams.set("start", (page - 1) + "");
        solrParams.set("rows", "30");

        try {
            PaginationDTO<TopicDTO> paginationDTO = new PaginationDTO<>();
            QueryResponse query = solrClient.query(SolrCoreNameEnum.TOPIC.getName(), solrParams);

            int totalCount = (int) query.getResults().getNumFound();
            paginationDTO.setPagination(page, 30, totalCount);

            List<TopicDTO> topics = query.getBeans(TopicDTO.class);

            for (TopicDTO topic : topics) {
                User user = userService.queryByUserid(topic.getUserid());
                topic.setUser(user);
            }

            paginationDTO.setDatas(topics);

            return paginationDTO;
        } catch (SolrServerException e) {
            switchMysql();
            log.error("solr查询帖子失败，启动MySQL容灾机制");
        } catch (IOException e) {
            switchMysql();
            log.error("solr查询帖子失败，启动MySQL容灾机制");
        }
        return null;
    }

    private PaginationDTO<UserDTO> searchUserBySolr(String condition, Integer page) {
        // 设置搜索参数
        ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("q", "name:" + condition);
        solrParams.set("start", (page - 1) + "");
        solrParams.set("rows", "30");

        try {
            PaginationDTO<UserDTO> paginationDTO = new PaginationDTO<>();
            QueryResponse query = solrClient.query(SolrCoreNameEnum.USER.getName(), solrParams);

            int totalCount = (int) query.getResults().getNumFound();
            paginationDTO.setPagination(page, 30, totalCount);

            List<UserDTO> topics = query.getBeans(UserDTO.class);

            paginationDTO.setDatas(topics);

            return paginationDTO;
        } catch (SolrServerException e) {
            log.error("solr查询用户失败，启动MySQL容灾机制");
        } catch (IOException e) {
            log.error("solr查询用户失败，启动MySQL容灾机制");
        }
        return null;
    }

    private PaginationDTO<TopicDTO> searchTopicByMysql(String condition, Integer page) {
        // TODO:完成MySQL模糊查询
        return null;
    }

    private PaginationDTO<SteamAppDTO> searchSteamByMysql(String condition, Integer page) {
        // TODO:完成MySQL模糊查询
        return null;
    }

    private PaginationDTO<UserDTO> searchUserByMysql(String condition, Integer page) {
        // TODO:完成MySQL模糊查询
        return null;
    }
}
