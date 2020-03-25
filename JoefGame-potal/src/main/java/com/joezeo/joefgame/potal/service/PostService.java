package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.UserPostDTO;

import java.util.List;

/**
 * 用户动态服务
 */
public interface PostService {
    List<UserPostDTO> getUserPosts(Long userid);
}
