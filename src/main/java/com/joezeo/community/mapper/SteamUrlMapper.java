package com.joezeo.community.mapper;

import com.joezeo.community.pojo.SteamUrl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SteamUrlMapper {
    int insertMap(@Param("map") Map<String, String> map, @Param("type") String type);

    List<SteamUrl> selectByAppid(@Param("appid") Integer appid ,@Param("type") String type);

    int insert(@Param("appid") String appid, @Param("url") String url, @Param("type") String type);

    List<SteamUrl> selectAll(String type);

    int emptySpecialUrl();
}
