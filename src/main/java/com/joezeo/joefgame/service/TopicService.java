package com.joezeo.joefgame.service;

import com.joezeo.joefgame.dto.PaginationDTO;
import com.joezeo.joefgame.dto.TopicDTO;
import com.joezeo.joefgame.pojo.Topic;

public interface TopicService {

    PaginationDTO<TopicDTO> listPage(Integer page, Integer size, String condition, String tab);

    PaginationDTO<TopicDTO> listPage(Long userid, Integer page, Integer size);

    TopicDTO queryById(Long id);

    void createOrUpdate(Topic topic);

    void incVie(Long id);

    boolean isExist(Long id);

    TopicDTO likeTopic(Long topicid, Long userid);

    TopicDTO unlikeTopic(Long topicid, Long userid);
}
