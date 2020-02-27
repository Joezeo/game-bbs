package com.joezeo.joefgame.manager.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProcessDTO implements Serializable {

    private static final long serialVersionUID = -7233337218914167212L;

    private String id;
    private String name;
    private Integer version;
    private String key;
}
