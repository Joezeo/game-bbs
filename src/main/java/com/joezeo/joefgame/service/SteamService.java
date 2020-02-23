package com.joezeo.joefgame.service;

import com.joezeo.joefgame.dto.HistoryPriceDTO;
import com.joezeo.joefgame.dto.PaginationDTO;
import com.joezeo.joefgame.dto.SteamAppDTO;

public interface SteamService {
    PaginationDTO<?> listApps(Integer page, Integer size, Integer type);

    SteamAppDTO queryApp(Integer appid, Integer type);

    HistoryPriceDTO queryHistoryPrice(Integer appid, Integer type);
}
