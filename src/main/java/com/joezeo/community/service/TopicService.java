package com.joezeo.community.service;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.TopicDTO;
import com.joezeo.community.pojo.Topic;

import java.util.List;

public interface TopicService {

    PaginationDTO<TopicDTO> listPage(Integer page, Integer size, String condition, String tab);

    PaginationDTO<TopicDTO> listPage(Long userid, Integer page, Integer size);

    TopicDTO queryById(Long id);

    void createOrUpdate(Topic topic);

    void incVie(Long id);

    boolean isExist(Long id);
}
