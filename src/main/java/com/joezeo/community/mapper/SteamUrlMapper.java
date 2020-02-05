package com.joezeo.community.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SteamUrlMapper {
    int insertList(@Param("list") List<String> list, @Param("type") String type);
}
