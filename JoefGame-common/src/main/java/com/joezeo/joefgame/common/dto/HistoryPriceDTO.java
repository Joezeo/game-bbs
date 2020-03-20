package com.joezeo.joefgame.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HistoryPriceDTO implements Serializable {
    private static final long serialVersionUID = 7065094900288400350L;

    private Integer appid;
    private Integer type;

    // 图表横轴-时间
    private List<String> time;

    // 图表纵轴-价格
    private List<Integer> price;
}
