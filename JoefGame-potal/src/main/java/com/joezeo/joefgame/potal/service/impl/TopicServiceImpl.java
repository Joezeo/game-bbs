package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.SolrCoreNameEnum;
import com.joezeo.joefgame.common.enums.TopicTypeEnum;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.common.exception.ServiceException;
import com.joezeo.joefgame.common.utils.RedisUtil;
import com.joezeo.joefgame.common.dto.TopicDTO;
import com.joezeo.joefgame.common.utils.TimeUtils;
import com.joezeo.joefgame.dao.mapper.TopicExtMapper;
import com.joezeo.joefgame.dao.mapper.TopicLikeUserMapper;
import com.joezeo.joefgame.dao.mapper.TopicMapper;
import com.joezeo.joefgame.dao.mapper.UserMapper;
import com.joezeo.joefgame.dao.pojo.*;
import com.joezeo.joefgame.common.provider.UCloudProvider;
import com.joezeo.joefgame.potal.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicExtMapper topicExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TopicLikeUserMapper topicLikeUserMapper;

    @Autowired
    private UCloudProvider uCloudProvider;
    @Autowired
    private SolrClient solrClient;

    /*
    * Forum页面的分页查询
    * */
    @Override
    public PaginationDTO<TopicDTO> listPage(Integer page, Integer size, String tab) {
        // 获取帖子主题
        Integer type = 0; // 初始值为0，为0时查询条件不包含主题
        if (tab != null && !"".equals(tab)) {
            type = TopicTypeEnum.typeOfName(tab);
        } else {
            log.error("函数listPage(index页面)：参数tab异常");
            throw new ServiceException("参数异常");
        }

        TopicExample example = new TopicExample();
        example.createCriteria().andTopicTypeEqualTo(type);
        int count = (int) topicMapper.countByExample(example);

        PaginationDTO<TopicDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(page, size, count);

        // 防止页码大于总页数 或者小于1
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

        // 分页查询帖子
        TopicExample pageExample = new TopicExample();
        pageExample.createCriteria().andTopicTypeEqualTo(type);
        pageExample.setOrderByClause("gmt_create desc");
        RowBounds rowBounds = new RowBounds(index, size);
        List<Topic> topics = topicMapper.selectByExampleWithRowbounds(pageExample, rowBounds);
        if (topics == null || topics.size() == 0) { // 帖子数量为0
            paginationDTO.setDatas(new ArrayList<>());
            return paginationDTO;
        }

        List<TopicDTO> list = new ArrayList<>();
        for (Topic topic : topics) {
            User user = userMapper.selectByPrimaryKey(topic.getUserid());
            if (user == null) {
                log.error("函数listPage：获取用户失败");
                throw new ServiceException("获取用户失败");
            }

            TopicDTO topicDTO = new TopicDTO();
            BeanUtils.copyProperties(topic, topicDTO);
            topicDTO.setUser(user);
            list.add(topicDTO);
        }
        paginationDTO.setDatas(list);

        return paginationDTO;
    }

    /*
        用于profile页面的'我的帖子'分页查询
     */
    @Override
    public PaginationDTO<TopicDTO> listPage(Long userid, Integer page, Integer size) {
        TopicExample topicExample = new TopicExample();
        topicExample.createCriteria().andUseridEqualTo(userid);
        int count = (int) topicMapper.countByExample(topicExample);

        PaginationDTO<TopicDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(page, size, count);

        // 防止页码大于总页数 或者小于1
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

        RowBounds rowBounds = new RowBounds(index, size);
        topicExample.setOrderByClause("gmt_create desc");
        List<Topic> topics = topicMapper.selectByExampleWithRowbounds(topicExample, rowBounds);
        if (topics == null || topics.size() == 0) { // 帖子数量为0
            paginationDTO.setDatas(new ArrayList<>());
            return paginationDTO;
        }

        List<TopicDTO> list = new ArrayList<>();
        for (Topic topic : topics) {
            User user = userMapper.selectByPrimaryKey(topic.getUserid());
            if (user == null) {
                log.error("函数listPage(Profile)：获取用户失败");
                throw new ServiceException("获取用户失败");
            }

            TopicDTO topicDTO = new TopicDTO();
            BeanUtils.copyProperties(topic, topicDTO);
            topicDTO.setUser(user);
            list.add(topicDTO);
        }
        paginationDTO.setDatas(list);

        return paginationDTO;
    }

    @Override
    public TopicDTO queryById(Long id) {
        if (id == null || id <= 0) {
            log.error("函数queryById：参数id异常,id=" + id);
            throw new ServiceException("TopicService-queryById 参数id异常");
        }

        // 获取指定id帖子
        Topic topic = topicMapper.selectByPrimaryKey(id);
        if (topic == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        TopicDTO topicDTO = new TopicDTO();
        BeanUtils.copyProperties(topic, topicDTO);

        /*
        获取帖子内容:
        1.先从redis中获取缓存的帖子内容
            如不存在：1.1从UCloud下载帖子内容
                     1.2将帖子内容缓存至redis
        */
        if (redisUtil.hasKey("topic-" + id)) {
            String desc = (String) redisUtil.get("topic-" + id);
            topicDTO.setDescription(desc);
        } else {
            // 从UCloud下载帖子内容
            InputStream inputStream = uCloudProvider.downloadTopic(topic.getDescription());
            try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                String desc = result.toString("UTF-8");
                topicDTO.setDescription(desc);

                // 将帖子内容缓存至redis中 缓存一小时的时间
                redisUtil.set("topic-" + id, desc, 60 * 60);
            } catch (UnsupportedEncodingException e) {
                log.error("从UCloud下载帖子失败,请检查相关配置");
                throw new CustomizeException(CustomizeErrorCode.DOWNLOAD_TOPIC_FAILED);
            } catch (IOException e) {
                log.error("从UCloud下载帖子失败,请检查相关配置");
                throw new CustomizeException(CustomizeErrorCode.DOWNLOAD_TOPIC_FAILED);
            }
        }

        // 获取当前帖子的发起人
        User user = userMapper.selectByPrimaryKey(topic.getUserid());
        if (user == null) {
            log.error("函数queryById：获取用户数据失败");
            throw new ServiceException("获取用户数据失败");
        }
        topicDTO.setUser(user);

        // 获取当前帖子的相关帖子
        String tagRegex = topicDTO.getTag().replaceAll(",", "|");
        List<Topic> related = topicExtMapper.selectRelated(topicDTO.getId(), tagRegex);
        if (related == null || related.size() == 0) {
            // 如果没有获取到相关帖子直接设置一个空集合
            topicDTO.setRelateds(new ArrayList<>());
        }
        topicDTO.setRelateds(related);

        // 查询出该帖子所有的点赞记录
        TopicLikeUserExample example = new TopicLikeUserExample();
        example.createCriteria().andTopicidEqualTo(id);
        List<TopicLikeUser> topicLikeUsers = topicLikeUserMapper.selectByExample(example);

        List<Long> userids = topicLikeUsers.stream().map(item -> item.getUserid()).collect(Collectors.toList());
        topicDTO.setLikeUsersId(userids);

        return topicDTO;
    }

    @Override
    public void createOrUpdate(Topic topic) {
        if (topic == null) {
            log.error("函数createOrUpdate：参数异常=null");
            throw new ServiceException("参数question不可为null");
        }

        if (topic.getId() == null) { // 进行新增帖子操作
            topic.setCommentCount(0);
            topic.setLikeCount(0);
            topic.setViewCount(0);
            topic.setGmtCreate(System.currentTimeMillis());
            topic.setGmtModify(topic.getGmtCreate());

            // 将帖子的内容上传至UCloud服务器
            String url = uCloudProvider.uploadTopic(topic.getDescription(), "text/plan");
            topic.setDescription(url);

            int count = topicMapper.insert(topic);
            if (count != 1) {
                log.error("函数createOrUpdate：发布新帖子失败");
                throw new ServiceException("发布新帖子失败");
            }

            /*
            将新发布的帖子上传至Solr
             */
            TopicDTO topicDTO = new TopicDTO();
            BeanUtils.copyProperties(topic, topicDTO);
            topicDTO.setTopicid(topic.getId()+"");

            String coreName = SolrCoreNameEnum.TOPIC.getName();
            try {
                solrClient.addBean(topicDTO);
                solrClient.commit("/" + coreName);
            } catch (IOException e) {
                log.error("新增Solr数据失败：[core name:"+coreName+"]" +
                        "[steamApp:+"+topicDTO.toString()+"+]");
                log.error("StackTrace:" + e.getStackTrace());
            } catch (SolrServerException e) {
                log.error("新增Solr数据失败：[core name:"+coreName+"]" +
                        "[steamApp:+"+topicDTO.toString()+"+]");
                log.error("StackTrace:" + e.getStackTrace());
            }

        } else { // 进行更新帖子操作
            // 获取贴子存储在UCloud的url地址
            Topic memtopic = topicMapper.selectByPrimaryKey(topic.getId());
            String url = memtopic.getDescription();
            // 将帖子的内容从UCloud服务器更新
            url = uCloudProvider.updateTopic(topic.getDescription(), "text/plan", url);

            // 将之前redis的帖子缓存删除
            String key = "topic-" + memtopic.getId();
            redisUtil.del(key);

            topic.setGmtModify(System.currentTimeMillis());
            topic.setDescription(url);

            int count = topicMapper.updateByPrimaryKeySelective(topic);
            if (count != 1) {
                log.error("函数createOrUpdate：编辑帖子失败");
                throw new RuntimeException("编辑帖子失败");
            }

            /*
            将编辑后的帖子上传至Solr
             */
            TopicDTO topicDTO = new TopicDTO();
            BeanUtils.copyProperties(topic, topicDTO);
            topicDTO.setTopicid(topic.getId()+"");

            String coreName = SolrCoreNameEnum.TOPIC.getName();
            try {
                solrClient.addBean(topicDTO);
                solrClient.commit("/" + coreName);
            } catch (IOException e) {
                log.error("更新Solr数据失败：[core name:"+coreName+"]" +
                        "[steamApp:+"+topicDTO.toString()+"+]");
                log.error("StackTrace:" + e.getStackTrace());
            } catch (SolrServerException e) {
                log.error("更新Solr数据失败：[core name:"+coreName+"]" +
                        "[steamApp:+"+topicDTO.toString()+"+]");
                log.error("StackTrace:" + e.getStackTrace());
            }
        }

    }

    @Override
    public void incVie(Long id) {
        if (id == null || id <= 0) {
            log.error("函数incVie：传入参数异常id=" + id);
            throw new ServiceException("传入id值异常");
        }
        Topic topic = new Topic();
        topic.setId(id);
        topic.setViewCount(1);

        int count = topicExtMapper.incView(topic);
        if (count != 1) {
            log.error("函数incVie：累加阅读数失败");
            throw new ServiceException("累加阅读数失败");
        }
    }

    @Override
    public boolean isExist(Long id) {
        Topic topic = topicMapper.selectByPrimaryKey(id);
        if (topic == null) {
            return false;
        }
        return true;
    }

    /**
     * 点赞帖子
     * @param topicid 被点赞的帖子id
     * @param userid 点赞的用户id
     * @return
     */
    @Override
    public TopicDTO likeTopic(Long topicid, Long userid) {
        TopicDTO topicDTO = new TopicDTO();

        // 增加点赞数
        Topic topic = new Topic();
        topic.setId(topicid);
        topic.setLikeCount(1);
        int idx = topicExtMapper.incLike(topic);
        if(idx != 1){
            throw new CustomizeException(CustomizeErrorCode.LIKE_TOPIC_FAILED);
        }

        // 在t_topic_like_user添加数据
        TopicLikeUser record = new TopicLikeUser();
        record.setTopicid(topicid);
        record.setUserid(userid);
        record.setGmtCreate(System.currentTimeMillis());
        record.setGmtModify(record.getGmtCreate());
        topicLikeUserMapper.insertSelective(record);

        // 查询出该帖子所有的点赞记录
        TopicLikeUserExample example = new TopicLikeUserExample();
        example.createCriteria().andTopicidEqualTo(topicid);
        List<TopicLikeUser> topicLikeUsers = topicLikeUserMapper.selectByExample(example);

        List<Long> userids = topicLikeUsers.stream().map(item -> item.getUserid()).collect(Collectors.toList());
        topicDTO.setLikeUsersId(userids);

        return topicDTO;
    }

    @Override
    public TopicDTO unlikeTopic(Long topicid, Long userid) {
        TopicDTO topicDTO = new TopicDTO();

        // 减少点赞数
        Topic topic = new Topic();
        topic.setId(topicid);
        topic.setLikeCount(1);
        int idx = topicExtMapper.decLike(topic);
        if(idx != 1){
            throw new CustomizeException(CustomizeErrorCode.UNLIKE_TOPIC_FAILED);
        }

        // 在t_topic_like_user删除数据
        TopicLikeUserExample example1 = new TopicLikeUserExample();
        example1.createCriteria().andUseridEqualTo(userid).andTopicidEqualTo(topicid);
        topicLikeUserMapper.deleteByExample(example1);

        // 查询出该帖子所有的点赞记录
        TopicLikeUserExample example2 = new TopicLikeUserExample();
        example2.createCriteria().andTopicidEqualTo(topicid);
        List<TopicLikeUser> topicLikeUsers = topicLikeUserMapper.selectByExample(example2);

        List<Long> userids = topicLikeUsers.stream().map(item -> item.getUserid()).collect(Collectors.toList());
        topicDTO.setLikeUsersId(userids);

        return topicDTO;
    }

    @Override
    public List<Topic> queryByUserdAndTime(Long userid) {
        try {
            Long time = TimeUtils.getZeroAtThirtyDaysAgo();
            TopicExample example = new TopicExample();
            example.createCriteria().andUseridEqualTo(userid).andGmtCreateGreaterThan(time);
            List<Topic> topics = topicMapper.selectByExample(example);
            return  topics;
        } catch (ParseException e) {
            log.error("解析时间失败,stackTrace=" + e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.FETCH_POSTS_FAILD);
        }
    }
}
