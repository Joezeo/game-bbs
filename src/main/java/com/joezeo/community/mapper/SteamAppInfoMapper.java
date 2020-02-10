package com.joezeo.community.mapper;

import com.joezeo.community.pojo.SteamAppInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SteamAppInfoMapper {
    SteamAppInfo selectByAppid(@Param("appid") Integer appid, @Param("type") String type);

    int insert(@Param("appInfo") SteamAppInfo steamAppInfo, @Param("type") String type);

    int selectCount(@Param("type") String type);

    List<SteamAppInfo> selectPage(@Param("index") Integer index,
                                  @Param("size") Integer size,
                                  @Param("type") String type);
}
