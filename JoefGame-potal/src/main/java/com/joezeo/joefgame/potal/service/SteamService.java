package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.dao.pojo.UserFavoriteApp;
import com.joezeo.joefgame.common.dto.HistoryPriceDTO;
import com.joezeo.joefgame.common.dto.SteamAppDTO;

import java.util.List;

public interface SteamService {
    PaginationDTO<?> listApps(Integer page, Integer size, Integer type);

    SteamAppDTO queryApp(Integer appid, Integer type);

    HistoryPriceDTO queryHistoryPrice(Integer appid, Integer type);

    boolean isExist(Integer appid);

    List<UserFavoriteApp> getFavorites(Long userid);

    List<UserFavoriteApp> favoriteApp(Long userid, Integer appid, Integer type);

    List<UserFavoriteApp> unFavoriteApp(Long userid, Integer appid, Integer type);
}
