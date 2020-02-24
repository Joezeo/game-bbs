package com.joezeo.joefgame.dao.mapper;

import com.joezeo.joefgame.dao.pojo.SteamSubBundleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SteamSubBundleInfoMapper {
    SteamSubBundleInfo selectByAppid(@Param("appid") Integer appid,@Param("type") String type);

    int insert(SteamSubBundleInfo steamSubBundleInfo);

    int selectCount(String type);

    List<SteamSubBundleInfo> selectPage(@Param("index") Integer index,
                                        @Param("size") Integer size,
                                        @Param("type") String type);

    int updateByAppidSelective(SteamSubBundleInfo subBundleInfo);
}
