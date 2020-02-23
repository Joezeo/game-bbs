package com.joezeo.joefgame.mapper;

import com.joezeo.joefgame.pojo.SteamHistoryPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SteamHistoryPriceMapper {

    int insert(SteamHistoryPrice historyPrice);

    List<SteamHistoryPrice> selectByTime(long preTimeAtZero);

    List<SteamHistoryPrice> selectByTimeAndType(@Param("time") long preTimeAtZero, @Param("type") String type);

    int deleteIllegal();

    List<SteamHistoryPrice> selectByAppid(Integer appid);

    List<SteamHistoryPrice> selectByAppidAndType(@Param("appid") Integer appid, @Param("type") String type);

    SteamHistoryPrice selectByTimeAndTypeAndAppid(@Param("time") Long preTimeAtZero,
                                                  @Param("type") String type,
                                                  @Param("appid") Integer appid);
}
