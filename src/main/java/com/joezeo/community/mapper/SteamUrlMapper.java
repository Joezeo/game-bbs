package com.joezeo.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SteamUrlMapper {
    int insertMap(@Param("map") Map<String, String> map, @Param("type") String type);
}
