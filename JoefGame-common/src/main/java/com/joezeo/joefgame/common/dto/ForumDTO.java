package com.joezeo.joefgame.common.dto;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import lombok.Data;

/**
 * 往index传输json数据所用的dto
 */
@Data
public class ForumDTO<T> {
    private PaginationDTO<T> pagination;
    private Integer page;
    private Integer size;
    private String tab;
}
