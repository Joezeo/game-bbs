package com.joezeo.joefgame.service;

import com.joezeo.joefgame.dto.CommentDTO;
import com.joezeo.joefgame.enums.CommentTypeEnum;
import com.joezeo.joefgame.pojo.User;

import java.util.List;

public interface CommentService {
    void addComment(CommentDTO commentDTO, User notifier);

    List<CommentDTO> listByParentId(Long parentId, CommentTypeEnum typeEnum);

    CommentDTO like(Long commentid, Long userid);

    CommentDTO unlike(Long commentid, Long userid);
}
