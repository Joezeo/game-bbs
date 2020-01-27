package com.joezeo.community.service;

import com.joezeo.community.dto.PaginationDTO;

public interface NotificationService {
    PaginationDTO listPage(Long id, Integer page, Integer size);

    int countUnread(Long id);

    Long readNotification(Long notificationID, Long userID);
}
