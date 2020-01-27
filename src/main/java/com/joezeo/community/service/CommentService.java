package com.joezeo.community.service;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.enums.CommentTypeEnum;
import com.joezeo.community.pojo.User;

import java.util.List;

public interface CommentService {
    void addComment(CommentDTO commentDTO, User notifier);

    List<CommentDTO> listByParentId(Long parentId, CommentTypeEnum typeEnum);
}
