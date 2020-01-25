package com.joezeo.community.service;

import com.joezeo.community.pojo.Tag;

import java.util.List;

public interface TagService {
    List<Tag> listTagsByCategory(Integer index);
}
