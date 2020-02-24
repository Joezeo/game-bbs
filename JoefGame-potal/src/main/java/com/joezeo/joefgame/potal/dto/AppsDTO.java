package com.joezeo.joefgame.potal.dto;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppsDTO implements Serializable {

    private static final long serialVersionUID = -2649925908123965304L;

    private Integer size;
    private Integer page;
    private Integer appType;
    PaginationDTO<?> pagination;
}
