package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.dto.SteamAppDTO;
import com.joezeo.joefgame.common.dto.TopicDTO;
import com.joezeo.joefgame.common.dto.UserDTO;

public interface SearchService {
    PaginationDTO<SteamAppDTO> searchSteam(String condition, Integer page);

    PaginationDTO<TopicDTO> searchTopic(String condition, Integer page);

    PaginationDTO<UserDTO> searchUser(String condition, Integer page);
}
