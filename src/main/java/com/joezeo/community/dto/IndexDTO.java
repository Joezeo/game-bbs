package com.joezeo.community.dto;

import lombok.Data;

/**
 * 往index传输json数据所用的dto
 */
@Data
public class IndexDTO<T> {
    private PaginationDTO<T> pagination;
    private Integer page;
    private Integer size;
    private String condition;
    private String tab;
}