package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.potal.dto.NotificationDTO;
import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.NotificationStatusEnum;
import com.joezeo.joefgame.common.enums.NotificationTypeEnum;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.common.exception.ServiceException;
import com.joezeo.joefgame.dao.mapper.NotificationMapper;
import com.joezeo.joefgame.dao.pojo.Notification;
import com.joezeo.joefgame.dao.pojo.NotificationExample;
import com.joezeo.joefgame.potal.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public PaginationDTO<NotificationDTO> listPage(Long id, Integer page, Integer size) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        int count = (int) notificationMapper.countByExample(notificationExample);

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(page, size, count);

        // 防止页码大于总页数 或者小于1
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

        RowBounds rowBounds = new RowBounds(index, size);
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, rowBounds);
        if (notifications == null) {
            // 没有新通知
            paginationDTO.setDatas(new ArrayList());
            return paginationDTO;
        }

        List<NotificationDTO> list = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setType(NotificationTypeEnum.nameOf(notification.getRelatedtype()));
            list.add(notificationDTO);
        }

        paginationDTO.setDatas(list);

        return paginationDTO;
    }

    @Override
    public int countUnread(Long id) {
        if (id == null || id <= 0) {
            log.error("函数countUnread：参数异常，id=" + id);
            throw new ServiceException("传入id值异常，id=" + id);
        }

        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        Long count = notificationMapper.countByExample(example);
        return count.intValue();
    }

    @Override
    public Long readNotification(Long notificationID, Long userID) {
        if (notificationID == null || notificationID <= 0) {
            log.error("函数readNotification：参数异常,notifivationId=" + notificationID);
            throw new ServiceException("传入的id值异常,notificationID=" + notificationID);
        }
        if (userID == null || userID <= 0) {
            log.error("函数readNotification：参数异常，userId=" + userID);
            throw new ServiceException("传入的id值异常，userID=" + userID);
        }

        // 从数据库获取该条通知
        Notification notification = notificationMapper.selectByPrimaryKey(notificationID);
        if (notification == null) {
            log.error("函数readNotification：通知未能正常取得");
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }

        // 前台用户和查询出来的receiver不一致，说明系统逻辑肯定有问题，抛出系统错误提示
        if (userID != notification.getReceiver()) {
            log.error("函数readNotification：前台传来的用户和数据库查询出的接收者不符合，程序逻辑存在问题，请排查！！！");
            throw new ServiceException("系统出现逻辑异常");
        }

        // 修改该条通知的已读状态
        notification.setStatus(NotificationStatusEnum.READED.getStatus());
        int count = notificationMapper.updateByPrimaryKey(notification);
        if (count != 1) {
            log.error("函数readNotification：读取通知失败");
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAILED);
        }

        return notification.getTopicid();
    }

    @Override
    public void readAll(Long id) {
        if (id == null || id <= 0) {
            log.error("函数readAll：参数异常，id=" + id);
            throw new ServiceException("参数id异常");
        }

        Notification notification = new Notification();
        notification.setStatus(1);
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        int count = notificationMapper.updateByExampleSelective(notification, notificationExample);
        if (count < 0) {
            log.error("函数readAll：全部已读通知失败");
            throw new CustomizeException(CustomizeErrorCode.READ_ALL_FIALED);
        }
    }
}
