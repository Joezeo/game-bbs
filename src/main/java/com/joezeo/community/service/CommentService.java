package com.joezeo.community.service;

import com.joezeo.community.dto.CommentDTO;

public interface CommentService {
    void addComment(CommentDTO commentDTO, Long userid);
}
