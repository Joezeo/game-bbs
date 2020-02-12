package com.joezeo.community.mapper;

import com.joezeo.community.pojo.SteamAppInfo;
import com.joezeo.community.pojo.SteamSubInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SteamSubInfoMapper {
    SteamSubInfo selectByAppid(Integer appid);

    int insert(SteamSubInfo steamSubInfo);

    int selectCount();

    List<SteamSubInfo> selectPage(@Param("index") Integer index, @Param("size") Integer size);
}
