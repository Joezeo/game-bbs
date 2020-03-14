package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.enums.SolrCoreNameEnum;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.potal.dto.SteamAppDTO;
import com.joezeo.joefgame.potal.dto.TopicDTO;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Autowired
    private HttpSolrClient solrClient;

    @Override
    public PaginationDTO<SteamAppDTO> searchSteamBySolr(String condition, Integer page) {

        // 多core分布式查询，需要在solr.xml中配置whiteList
        List<String> steamCores = SolrCoreNameEnum.listSteamCores();
        StringBuilder shardsBuilder = new StringBuilder();
        steamCores.stream().forEach(coreName -> {
            shardsBuilder.append(solrClient.getBaseURL() + "/" + coreName + ",");
        });
        String shards = shardsBuilder.substring(0, shardsBuilder.lastIndexOf(","));
        System.out.println(shards);

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
            log.error("solr查询steam app失败，stackTrace=" + e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        } catch (IOException e) {
            log.error("solr查询steam app失败，stackTrace=" + e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        }
    }

    @Override
    public PaginationDTO<TopicDTO> searchTopicBySolr(String condition, Integer page) {
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
            paginationDTO.setDatas(topics);

            return paginationDTO;
        } catch (SolrServerException e) {
            log.error("solr查询帖子失败，stackTrace=" + e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        } catch (IOException e) {
            log.error("solr查询帖子失败，stackTrace=" + e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        }
    }

    @Override
    public PaginationDTO<UserDTO> searchUserBySolr(String condition, Integer page) {
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
            log.error("solr查询用户失败，stackTrace=" + e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        } catch (IOException e) {
            log.error("solr查询用户失败，stackTrace=" + e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.SEARCH_FAILED);
        }
    }
}
