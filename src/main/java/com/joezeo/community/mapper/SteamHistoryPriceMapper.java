package com.joezeo.community.mapper;

import com.joezeo.community.pojo.SteamHistoryPrice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SteamHistoryPriceMapper {

    int insert(SteamHistoryPrice historyPrice);
}
