package com.joezeo.community.service;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.SteamAppDTO;
import com.joezeo.community.pojo.SteamAppInfo;

public interface SteamService {
    PaginationDTO<SteamAppInfo> listApps(Integer page, Integer size, Integer type);

    SteamAppDTO queryApp(Integer appid, Integer type);
}