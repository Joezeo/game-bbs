package com.joezeo.community.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDTO implements Serializable {
    private static final long serialVersionUID = -7912662336865259339L;

    private Long parentId;
    private Integer parentType;
    private String content;
}
