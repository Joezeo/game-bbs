package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.dao.pojo.Tag;

import java.util.List;

public interface TagService {
    List<Tag> listTagsByCategory(Integer index);
}
