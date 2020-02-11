package com.joezeo.community.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.joezeo.community.dao.RedisDao;
import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.SteamAppDTO;
import com.joezeo.community.enums.SteamAppTypeEnum;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.SteamAppInfoMapper;
import com.joezeo.community.mapper.SteamHistoryPriceMapper;
import com.joezeo.community.pojo.SteamAppInfo;
import com.joezeo.community.pojo.SteamHistoryPrice;
import com.joezeo.community.service.SteamService;
import com.joezeo.community.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SteamServiceImpl implements SteamService {
    @Autowired
    private SteamAppInfoMapper steamAppInfoMapper;
    @Autowired
    private SteamHistoryPriceMapper steamHistoryPriceMapper;
    @Autowired
    private RedisDao redisDao;

    @Override
    public PaginationDTO<SteamAppInfo>
    listApps(Integer page, Integer size, Integer typeIndex) {
        // 获取所要获取的软件列表的类型(String)
        String type = SteamAppTypeEnum.typeOf(typeIndex);

        // 获取该类型软件的总数
        int totalCount = steamAppInfoMapper.selectCount(type);

        // 设置分页对象
        PaginationDTO<SteamAppInfo> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(page, size, totalCount);
        // 避免page传入异常值
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

        // 获取该页的数据list
        List<SteamAppInfo> list = steamAppInfoMapper.selectPage(index, size, type);
        if (list == null || list.size() == 0) {
            paginationDTO.setDatas(new ArrayList<>());
            return paginationDTO;
        }

        try {
            // 获取当天零点的时间戳
            Long preTimeAtZero = TimeUtils.getTimestampAtZero();

            // 获取当天新获取的特惠商品价格 gmt_create>preTimeAtZero
            List<SteamHistoryPrice> specials = steamHistoryPriceMapper.selectByTime(preTimeAtZero);
            if(specials != null && specials.size() != 0){
                // 将特惠商品以appid为key，价格为value放入map中
                Map<Integer, Integer> specialMap = specials.stream().distinct()
                        .collect(Collectors.toMap(special -> special.getAppid(), special -> special.getPrice()));

                // 重新设定list中特惠商品的finalPrice
                for (SteamAppInfo appInfo : list) {
                    Integer price = specialMap.get(appInfo.getAppid());
                    if (price != null) {
                        appInfo.setFinalPrice(price);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取特惠价格失败，所有商品价格将以历史记录展示: stackTrace=" + e.getMessage());
        }

        // 将获取到的所有商品放入Redis中，凌晨4点对缓存中的商品信息进行删除
        // key: app-类型-appid
        list.stream().forEach(app -> {
            String key = "app-" + type + "-" + app.getAppid();
            if (!redisDao.hasKey(key)) { // redis缓存中不存在该app信息
                int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
                try {
                    // 获取现在时间与下一天凌晨4点的时间差，单位秒
                    difftime = TimeUtils.getDifftimeFromNextZero();
                } catch (ParseException e) {
                    log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
                }
                // 每天凌晨4点清除缓存信息
                // 由于缓存中的信息并不需要修改，所以使用String的方式存储
                redisDao.set(key, app, difftime);
            }
        });

        paginationDTO.setDatas(list);
        return paginationDTO;
    }

    @Override
    public SteamAppDTO queryApp(Integer appid, Integer type) {
        String typeStr = SteamAppTypeEnum.typeOf(type);
        SteamAppDTO appDTO = new SteamAppDTO();
        appDTO.setType(type);

        /*
            首先从redis缓存中查询是否存在该app的信息
            key: app-类型-appid
         */
        String key = "app-" + typeStr + "-" +appid;
        if(redisDao.hasKey(key)){
            JSONObject object = (JSONObject) redisDao.get(key);
            SteamAppInfo appInfo = JSONObject.parseObject(object.toJSONString(), SteamAppInfo.class);
            BeanUtils.copyProperties(appInfo, appDTO);
        } else { // 如redis中不存在该app的信息，从数据库中查询，再放入到redis缓存中
            SteamAppInfo app = steamAppInfoMapper.selectByAppid(appid, typeStr);
            int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
            try {
                // 获取现在时间与下一天凌晨4点的时间差，单位秒
                difftime = TimeUtils.getDifftimeFromNextZero();
            } catch (ParseException e) {
                log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
            }
            // 每天凌晨4点清除缓存信息
            // 由于缓存中的信息并不需要修改，所以使用String的方式存储
            redisDao.set(key, app, difftime);
        }
        return appDTO;
    }
}
