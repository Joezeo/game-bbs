package com.joezeo.community.service;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.enums.CommentTypeEnum;

import java.util.List;

public interface CommentService {
    void addComment(CommentDTO commentDTO, Long userid);

    List<CommentDTO> listByParentId(Long parentId, CommentTypeEnum typeEnum);
}
