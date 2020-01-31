package com.joezeo.community.service.impl;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.TopicDTO;
import com.joezeo.community.enums.TopicTypeEnum;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.TopicExtMapper;
import com.joezeo.community.mapper.TopicMapper;
import com.joezeo.community.mapper.UserMapper;
import com.joezeo.community.pojo.Topic;
import com.joezeo.community.pojo.TopicExample;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.TopicService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private TopicExtMapper topicExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<TopicDTO> list() {
        TopicExample topicExample = new TopicExample();
        topicExample.createCriteria().andIdIsNotNull();
        List<Topic> topics = topicMapper.selectByExample(topicExample);
        if (topics == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        List<TopicDTO> list = new ArrayList<>();
        for (Topic topic : topics) {
            User user = userMapper.selectByPrimaryKey(topic.getUserid());
            if (user == null) {
                throw new ServiceException("获取用户失败");
            }

            TopicDTO topicDTO = new TopicDTO();
            BeanUtils.copyProperties(topic, topicDTO);
            topicDTO.setUser(user);
            list.add(topicDTO);
        }

        return list;
    }

    @Override
    public PaginationDTO<TopicDTO> listPage(Integer page, Integer size, String condition, String tab) {
        // 获取帖子主题
        Integer type = 0; // 初始值为0，为0时查询条件不包含主题
        if (tab != null && !"".equals(tab)) {
            type = TopicTypeEnum.typeOfName(tab);
        }

        // 判断搜索条件是否为空
        if (condition != null && !"".equals(condition)) {
            String[] conds = condition.split(" ");
            condition = Arrays.stream(conds).collect(Collectors.joining("|"));
        }

        int count = topicExtMapper.countSearch(condition, type);

        PaginationDTO<TopicDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(page, size, count);

        // 防止页码大于总页数 或者小于1
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

        List<Topic> topics = topicExtMapper.selectSearch(index, size, condition, type);
        if (topics == null || topics.size() == 0) {
            paginationDTO.setDatas(new ArrayList<>());
            return paginationDTO;
        }

        List<TopicDTO> list = new ArrayList<>();
        for (Topic topic : topics) {
            User user = userMapper.selectByPrimaryKey(topic.getUserid());
            if (user == null) {
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
        if (topics == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        List<TopicDTO> list = new ArrayList<>();
        for (Topic topic : topics) {
            User user = userMapper.selectByPrimaryKey(topic.getUserid());
            if (user == null) {
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
            throw new ServiceException("TopicService-queryById 参数id异常");
        }

        // 获取指定id帖子
        Topic topic = topicMapper.selectByPrimaryKey(id);
        if (topic == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        TopicDTO topicDTO = new TopicDTO();
        BeanUtils.copyProperties(topic, topicDTO);

        // 获取当前帖子的发起人
        User user = userMapper.selectByPrimaryKey(topic.getUserid());
        if (user == null) {
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

        return topicDTO;
    }

    @Override
    public void createOrUpdate(Topic topic) {
        if (topic == null) {
            throw new RuntimeException("参数question不可为null");
        }

        if (topic.getId() == null) { // 进行新增帖子操作
            topic.setCommentCount(0);
            topic.setLikeCount(0);
            topic.setViewCount(0);
            topic.setGmtCreate(System.currentTimeMillis());
            topic.setGmtModify(topic.getGmtCreate());

            int count = topicMapper.insert(topic);
            if (count != 1) {
                throw new RuntimeException("发布新帖子失败");
            }
        } else { // 进行更新帖子操作
            topic.setGmtModify(System.currentTimeMillis());

            int count = topicMapper.updateByPrimaryKeySelective(topic);
            if (count != 1) {
                throw new RuntimeException("编辑帖子失败");
            }
        }

    }

    @Override
    public void incVie(Long id) {
        if (id == null || id <= 0) {
            throw new ServiceException("传入id值异常");
        }
        Topic topic = new Topic();
        topic.setId(id);
        topic.setViewCount(1);

        int count = topicExtMapper.incView(topic);
        if (count != 1) {
            throw new ServiceException("累加阅读数失败");
        }
    }
}
