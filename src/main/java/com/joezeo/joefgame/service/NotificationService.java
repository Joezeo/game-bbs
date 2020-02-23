package com.joezeo.joefgame.service;

import com.joezeo.joefgame.dto.PaginationDTO;

public interface NotificationService {
    PaginationDTO listPage(Long id, Integer page, Integer size);

    int countUnread(Long id);

    Long readNotification(Long notificationID, Long userID);

    void readAll(Long id);
}
