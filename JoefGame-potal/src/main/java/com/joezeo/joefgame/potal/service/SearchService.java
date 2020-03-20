package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.dto.SteamAppDTO;
import com.joezeo.joefgame.common.dto.TopicDTO;
import com.joezeo.joefgame.common.dto.UserDTO;

public interface SearchService {
    PaginationDTO<SteamAppDTO> searchSteamBySolr(String condition, Integer page);

    PaginationDTO<TopicDTO> searchTopicBySolr(String condition, Integer page);

    PaginationDTO<UserDTO> searchUserBySolr(String condition, Integer page);
}
