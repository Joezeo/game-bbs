package com.joezeo.joefgame.service;

import com.joezeo.joefgame.pojo.Tag;

import java.util.List;

public interface TagService {
    List<Tag> listTagsByCategory(Integer index);
}
