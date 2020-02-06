package com.joezeo.community.mapper;

import com.joezeo.community.pojo.SteamAppInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SteamAppInfoMapper {
    SteamAppInfo selectByAppid(@Param("appid") Integer appid, @Param("type") String type);

    int insert(@Param("appInfo") SteamAppInfo steamAppInfo, @Param("type") String type);
}
