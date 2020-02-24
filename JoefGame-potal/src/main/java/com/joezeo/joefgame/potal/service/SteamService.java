package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.potal.dto.HistoryPriceDTO;
import com.joezeo.joefgame.potal.dto.SteamAppDTO;

public interface SteamService {
    PaginationDTO<?> listApps(Integer page, Integer size, Integer type);

    SteamAppDTO queryApp(Integer appid, Integer type);

    HistoryPriceDTO queryHistoryPrice(Integer appid, Integer type);
}
