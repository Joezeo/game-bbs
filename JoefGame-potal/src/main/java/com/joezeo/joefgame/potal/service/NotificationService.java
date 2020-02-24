package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.PaginationDTO;

public interface NotificationService {
    PaginationDTO listPage(Long id, Integer page, Integer size);

    int countUnread(Long id);

    Long readNotification(Long notificationID, Long userID);

    void readAll(Long id);
}
