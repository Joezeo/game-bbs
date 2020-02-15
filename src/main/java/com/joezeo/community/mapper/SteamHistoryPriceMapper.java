package com.joezeo.community.mapper;

import com.joezeo.community.pojo.SteamHistoryPrice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SteamHistoryPriceMapper {

    int insert(SteamHistoryPrice historyPrice);

    List<SteamHistoryPrice> selectByTime(long preTimeAtZero);

    int deleteIllegal();

    List<SteamHistoryPrice> selectByAppid(Integer appid);
}
