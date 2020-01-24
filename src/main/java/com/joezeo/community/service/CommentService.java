package com.joezeo.community.service;

import com.joezeo.community.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    void addComment(CommentDTO commentDTO, Long userid);

    List<CommentDTO> listByQuestionid(Long questionid);
}
