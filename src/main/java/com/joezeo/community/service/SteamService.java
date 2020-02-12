package com.joezeo.community.service;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.SteamAppDTO;

public interface SteamService {
    PaginationDTO<?> listApps(Integer page, Integer size, Integer type);

    SteamAppDTO queryApp(Integer appid, Integer type);
}
