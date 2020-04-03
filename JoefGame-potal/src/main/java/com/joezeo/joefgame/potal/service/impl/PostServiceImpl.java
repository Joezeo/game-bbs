package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.common.dto.UserDTO;
import com.joezeo.joefgame.common.dto.UserPostDTO;
import com.joezeo.joefgame.common.enums.PostTypeEnum;
import com.joezeo.joefgame.dao.pojo.Topic;
import com.joezeo.joefgame.potal.service.PostService;
import com.joezeo.joefgame.potal.service.TopicService;
import com.joezeo.joefgame.potal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;
    /**
     *  获取id为userid的用户关注的用户的post动态信息
     * @param userid
     * @return
     */
    @Override
    public List<UserPostDTO> getUserPosts(Long userid) {
        // 获取当前用户关注的用户集合
        List<UserPostDTO> userPostDTOS = new ArrayList<>();
        //获取关注的用户
        List<UserDTO> userDTOS = userService.listAllFollowUser(userid);
        //获取关注的用户近期内的发帖信息（暂时默认30天内的）
        for (UserDTO userDTO : userDTOS) {
            List<Topic> topics = topicService.queryByUserdAndTime(userDTO.getId());
            topics.stream().forEach(topic -> {
                UserPostDTO userPostDTO = new UserPostDTO();
                userPostDTO.setPosterId(topic.getUserid());
                userPostDTO.setPosterName(userDTO.getName());
                userPostDTO.setPostTitle(topic.getTitle());
                userPostDTO.setPostTime(topic.getGmtCreate());
                userPostDTO.setAvatarUrl(userDTO.getAvatarUrl());
                userPostDTO.setType(PostTypeEnum.TOPIC.getIndex());
                userPostDTOS.add(userPostDTO);
            });
        }

        // TODO:获取关注的用户近期内的动态信息
        // TODO:建立t_post动态表、动态相关功能
        return userPostDTOS;
    }
}
