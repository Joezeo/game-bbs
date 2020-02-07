package com.joezeo.community.mapper;

import com.joezeo.community.pojo.SteamSubInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SteamSubInfoMapper {
    SteamSubInfo selectByAppid(Integer appid);

    int insert(SteamSubInfo steamSubInfo);
}
