package com.joezeo.joefgame.potal.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joezeo.joefgame.common.dto.*;
import com.joezeo.joefgame.common.provider.SteamProvider;
import com.joezeo.joefgame.common.utils.RedisUtil;
import com.joezeo.joefgame.dao.mapper.UserFavoriteAppMapper;
import com.joezeo.joefgame.dao.pojo.*;
import com.joezeo.joefgame.common.enums.SteamAppTypeEnum;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.dao.mapper.SteamAppInfoMapper;
import com.joezeo.joefgame.dao.mapper.SteamHistoryPriceMapper;
import com.joezeo.joefgame.dao.mapper.SteamSubBundleInfoMapper;
import com.joezeo.joefgame.potal.service.SteamService;
import com.joezeo.joefgame.common.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private SteamSubBundleInfoMapper steamSubBundleInfoMapper;
    @Autowired
    private UserFavoriteAppMapper userFavoriteAppMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SteamProvider steamProvider;

    @Override
    public PaginationDTO<?>
    listApps(Integer page, Integer size, Integer typeIndex) {
        // 获取所要获取的软件列表的类型(String)
        String type = SteamAppTypeEnum.typeOf(typeIndex);

        // 如果类型为sub、bundle 礼包，则单独处理逻辑
        if ("sub".equals(type) || "bundle".equals(type)) {
            return listSubsOrBundles(page, size, type);
        }

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
            List<SteamHistoryPrice> specials = steamHistoryPriceMapper.selectByTimeAndType(preTimeAtZero, "app");
            if (specials != null && specials.size() != 0) {
                // 将特惠商品以appid为key，价格为value放入map中
                Map<Integer, Integer> specialMap = specials.stream().distinct()
                        .collect(Collectors.toMap(special -> special.getAppid(), special -> special.getPrice()));

                // 重新设定list中特惠商品的finalPrice
                for (SteamAppInfo appInfo : list) {
                    Integer price = specialMap.get(appInfo.getAppid());
                    if (price != null) {
                        appInfo.setFinalPrice(price);
                    } else { // 说明没有降价
                        appInfo.setFinalPrice(appInfo.getOriginalPrice());
                    }
                }
            } else {
                for (SteamAppInfo steamAppInfo : list) {
                    steamAppInfo.setFinalPrice(steamAppInfo.getOriginalPrice());
                }
            }
        } catch (Exception e) {
            log.error("获取特惠价格失败，所有商品价格将以历史记录展示: stackTrace=" + e.getMessage());
        }

        // 将获取到的所有商品放入Redis中，凌晨4点对缓存中的商品信息进行删除
        // key: app-类型-appid
        list.stream().forEach(app -> {
            String key = "app-" + type + "-" + app.getAppid();
            if (!redisUtil.hasKey(key)) { // redis缓存中不存在该app信息
                int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
                try {
                    // 获取现在时间与下一天凌晨4点的时间差，单位秒
                    difftime = TimeUtils.getDifftimeFromNextZero();
                } catch (ParseException e) {
                    log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
                }
                // 每天凌晨4点清除缓存信息
                // 由于缓存中的信息并不需要修改，所以使用String的方式存储
                redisUtil.set(key, app, difftime);
            }
        });

        paginationDTO.setDatas(list);
        return paginationDTO;
    }

    /*
        获取礼包、捆绑包信息list
     */
    @NotNull
    private PaginationDTO<SteamSubBundleInfo> listSubsOrBundles(Integer page, Integer size, String type) {
        // 获取该类型软件的总数
        int totalCount = steamSubBundleInfoMapper.selectCount(type);

        // 设置分页对象
        PaginationDTO<SteamSubBundleInfo> paginationDTO = new PaginationDTO<>();
        paginationDTO.setPagination(page, size, totalCount);
        // 避免page传入异常值
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

        // 获取该页的数据list
        List<SteamSubBundleInfo> list = steamSubBundleInfoMapper.selectPage(index, size, type);
        if (list == null || list.size() == 0) {
            paginationDTO.setDatas(new ArrayList<>());
            return paginationDTO;
        }

        // 将获取到的所有商品放入Redis中，凌晨4点对缓存中的商品信息进行删除
        // key: app-类型-appid
        list.stream().forEach(app -> {
            String key = "app-" + type + "-" + app.getAppid();
            if (!redisUtil.hasKey(key)) { // redis缓存中不存在该app信息
                SteamAppDTO appDTO = new SteamAppDTO();
                BeanUtils.copyProperties(app, appDTO);
                int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
                try {
                    // 获取现在时间与下一天凌晨4点的时间差，单位秒
                    difftime = TimeUtils.getDifftimeFromNextZero();
                } catch (ParseException e) {
                    log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
                }
                // 查询出该礼包包含的app信息，放入list中
                List<SteamAppDTO> includes = new ArrayList<>();
                String[] contains = app.getContains().split(",");
                for (String appidStr : contains) {
                    if ("".equals(appidStr)) {
                        break;
                    }
                    Integer typeInt = 1; // 默认游戏
                    SteamAppInfo include = steamAppInfoMapper.selectByAppid(Integer.parseInt(appidStr), "game");
                    if (include == null) {
                        include = steamAppInfoMapper.selectByAppid(Integer.parseInt(appidStr), "dlc");
                        typeInt = 3;
                    }
                    if (include != null) {
                        // 这里使用SteamAppDTO是为了记录下包含物件的类型
                        SteamAppDTO appdto = new SteamAppDTO();
                        appdto.setType(typeInt);
                        BeanUtils.copyProperties(include, appdto);
                        includes.add(appdto);
                    }
                }
                appDTO.setIncludes(includes);

                // 每天凌晨4点清除缓存信息
                // 由于缓存中的信息并不需要修改，所以使用String的方式存储
                redisUtil.set(key, appDTO, difftime);
            }
        });

        try {
            // 获取当天零点的时间戳
            Long preTimeAtZero = TimeUtils.getTimestampAtZero();

            // 获取当天新获取的特惠商品价格 gmt_create>preTimeAtZero
            List<SteamHistoryPrice> specials = steamHistoryPriceMapper.selectByTimeAndType(preTimeAtZero, type);
            if (specials != null && specials.size() != 0) {
                // 将特惠商品以appid为key，价格为value放入map中
                Map<Integer, Integer> specialMap = specials.stream().distinct()
                        .collect(Collectors.toMap(special -> special.getAppid(), special -> special.getPrice()));

                // 重新设定list中特惠商品的finalPrice
                for (SteamSubBundleInfo appInfo : list) {
                    Integer price = specialMap.get(appInfo.getAppid());
                    if (price != null) {
                        appInfo.setFinalPrice(price);
                    } else { // 说明没有降价
                        appInfo.setFinalPrice(appInfo.getOriginalPrice());
                    }
                }
            } else {
                for (SteamSubBundleInfo info : list) {
                    info.setFinalPrice(info.getOriginalPrice());
                }
            }
        } catch (Exception e) {
            log.error("获取特惠价格失败，所有商品价格将以历史记录展示: stackTrace=" + e.getMessage());
        }

        paginationDTO.setDatas(list);
        return paginationDTO;
    }

    @Override
    public SteamAppDTO queryApp(Integer appid, Integer type) {
        if (type == 0) {
            return queryWithoutType(appid);
        }

        if (type == 5 || type == 7) { // 获取礼包、捆绑包信息
            return querySubOrBundle(appid, type);
        }

        return queryOthers(appid, type);
    }

    private SteamAppDTO queryWithoutType(Integer appid) {
        SteamAppDTO appDTO;
        List<Integer> typeIdxes = Arrays.asList(1, 2, 3, 4, 6);

        for (Integer typeIdx : typeIdxes) {
            appDTO = queryOthers(appid, typeIdx);
            if (appDTO != null) {
                return appDTO;
            }
        }

        typeIdxes = Arrays.asList(5,7);
        for (Integer typeIdx : typeIdxes) {
            appDTO = querySubOrBundle(appid, typeIdx);
            if(appDTO != null){
                return appDTO;
            }
        }

        throw new CustomizeException(CustomizeErrorCode.APP_NOT_FOUND);
    }

    private SteamAppDTO querySubOrBundle(Integer appid, Integer type) {
        SteamAppDTO appDTO = new SteamAppDTO();

        String typeStr = SteamAppTypeEnum.typeOf(type);

        String key = "app-" + typeStr + "-" + appid;

        if (redisUtil.hasKey(key)) {
            JSONObject object = (JSONObject) redisUtil.get(key);
            SteamAppDTO appInfo = JSONObject.parseObject(object.toJSONString(), SteamAppDTO.class);
            BeanUtils.copyProperties(appInfo, appDTO);
        } else {
            SteamSubBundleInfo sub = steamSubBundleInfoMapper.selectByAppid(appid, typeStr);
            if (sub == null) {
                return null;
            }
            int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
            try {
                // 获取现在时间与下一天凌晨4点的时间差，单位秒
                difftime = TimeUtils.getDifftimeFromNextZero();
            } catch (ParseException e) {
                log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
            }
            BeanUtils.copyProperties(sub, appDTO);

            // 查询出该礼包包含的app信息，放入list中
            List<SteamAppDTO> includes = new ArrayList<>();
            String[] contains = sub.getContains().split(",");
            for (String appidStr : contains) {
                Integer typeInt = 1; // 默认游戏
                SteamAppInfo include = steamAppInfoMapper.selectByAppid(Integer.parseInt(appidStr), "game");
                if (include == null) {
                    include = steamAppInfoMapper.selectByAppid(Integer.parseInt(appidStr), "dlc");
                    typeInt = 3;
                }
                if (include != null) {
                    // 这里使用SteamAppDTO是为了记录下包含物件的类型
                    SteamAppDTO appdto = new SteamAppDTO();
                    appdto.setType(typeInt);
                    BeanUtils.copyProperties(include, appdto);
                    includes.add(appdto);
                }
            }
            appDTO.setIncludes(includes);

            try {
                // 获取当天零点的时间戳
                Long preTimeAtZero = TimeUtils.getTimestampAtZero();

                // 获取当天新获取的特惠商品价格 gmt_create>preTimeAtZero
                SteamHistoryPrice special = steamHistoryPriceMapper.selectByTimeAndTypeAndAppid(preTimeAtZero, typeStr, appid);
                if (special != null) {
                    // 重新设定特惠商品的finalPrice
                    appDTO.setFinalPrice(special.getPrice());
                } else { // 没有降价
                    appDTO.setFinalPrice(appDTO.getOriginalPrice());
                }
            } catch (Exception e) {
                log.error("获取特惠价格失败，所有商品价格将以历史记录展示: stackTrace=" + e.getMessage());
            }

            // 每天凌晨4点清除缓存信息
            // 由于缓存中的信息并不需要修改，所以使用String的方式存储
            redisUtil.set(key, appDTO, difftime);
        }

        appDTO.setType(type);
        return appDTO;
    }

    private SteamAppDTO queryOthers(Integer appid, Integer type) {
        SteamAppDTO appDTO = new SteamAppDTO();

        String typeStr = SteamAppTypeEnum.typeOf(type);

        /*
            首先从redis缓存中查询是否存在该app的信息
            key: app-类型-appid
         */
        String key = "app-" + typeStr + "-" + appid;
        if (redisUtil.hasKey(key)) {
            JSONObject object = (JSONObject) redisUtil.get(key);
            SteamAppInfo appInfo = JSONObject.parseObject(object.toJSONString(), SteamAppInfo.class);
            BeanUtils.copyProperties(appInfo, appDTO);
        } else { // 如redis中不存在该app的信息，从数据库中查询，再放入到redis缓存中
            SteamAppInfo app = steamAppInfoMapper.selectByAppid(appid, typeStr);
            if (app == null) {
                return null;
            }
            int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
            try {
                // 获取现在时间与下一天凌晨4点的时间差，单位秒
                difftime = TimeUtils.getDifftimeFromNextZero();
            } catch (ParseException e) {
                log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
            }

            try {
                // 获取当天零点的时间戳
                Long preTimeAtZero = TimeUtils.getTimestampAtZero();

                // 获取当天新获取的特惠商品价格 gmt_create>preTimeAtZero
                SteamHistoryPrice special = steamHistoryPriceMapper.selectByTimeAndTypeAndAppid(preTimeAtZero, "app", appid);
                if (special != null) {
                    // 重新设定特惠商品的finalPrice
                    app.setFinalPrice(special.getPrice());
                } else { // 没有降价
                    app.setFinalPrice(app.getOriginalPrice());
                }
            } catch (Exception e) {
                log.error("获取特惠价格失败，所有商品价格将以历史记录展示: stackTrace=" + e.getMessage());
            }
            // 每天凌晨4点清除缓存信息
            // 由于缓存中的信息并不需要修改，所以使用String的方式存储
            redisUtil.set(key, app, difftime);
            BeanUtils.copyProperties(app, appDTO);
        }
        appDTO.setType(type);
        return appDTO;
    }

    @Override
    public HistoryPriceDTO queryHistoryPrice(Integer appid, Integer type) {
        if(type == 0){
            return queryPriceWithoutType(appid);
        }

        if (type == 5 || type ==7) {
            return querySubOrBundlePrice(appid, type);
        }

        return queryOthersPrice(appid, type);
    }

    private HistoryPriceDTO queryPriceWithoutType(Integer appid) {
        HistoryPriceDTO historyPriceDTO;
        List<Integer> typeIdxes = Arrays.asList(1, 2, 3, 4, 6);

        for (Integer typeIdx : typeIdxes) {
            historyPriceDTO = queryOthersPrice(appid, typeIdx);
            if (historyPriceDTO != null) {
                return historyPriceDTO;
            }
        }

        typeIdxes = Arrays.asList(5,7);
        for (Integer typeIdx : typeIdxes) {
            historyPriceDTO = querySubOrBundlePrice(appid, typeIdx);
            if(historyPriceDTO != null){
                return historyPriceDTO;
            }
        }

        return null;
    }

    private HistoryPriceDTO queryOthersPrice(Integer appid, Integer type){
        HistoryPriceDTO historyPriceDTO = new HistoryPriceDTO();
        // 首先获取该appid对应的app信息
        SteamAppInfo appInfo = null;

        String typeStr = SteamAppTypeEnum.typeOf(type);

        // 首先redis缓存中获取
        String key = "app-" + typeStr + "-" + appid;
        if (redisUtil.hasKey(key)) {
            JSONObject object = (JSONObject) redisUtil.get(key);
            appInfo = JSONObject.parseObject(object.toJSONString(), SteamAppInfo.class);
        } else { // 如redis中不存在该app的信息，从数据库中查询，再放入到redis缓存中
            SteamAppInfo app = steamAppInfoMapper.selectByAppid(appid, typeStr);
            if(app == null){
                return null;
            }
            int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
            try {
                // 获取现在时间与下一天凌晨4点的时间差，单位秒
                difftime = TimeUtils.getDifftimeFromNextZero();
            } catch (ParseException e) {
                log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
            }
            // 每天凌晨4点清除缓存信息
            // 由于缓存中的信息并不需要修改，所以使用String的方式存储
            redisUtil.set(key, app, difftime);
            appInfo = app;
        }

        // 获取该app的原价格
        Integer basePrice = appInfo.getOriginalPrice();

        List<SteamHistoryPrice> historyPriceList = new ArrayList<>();
        // 首先从redis缓存中获取该app的历史价格
        key = "price-" + typeStr + "-" + appid;
        if (redisUtil.hasKey(key)) {
            JSONArray jsonArray = (JSONArray) redisUtil.get(key);
            for (int i = 0; i < jsonArray.size(); i++) {
                historyPriceList.add(JSONObject.parseObject(jsonArray.getJSONObject(i).toJSONString(), SteamHistoryPrice.class));
            }
        } else {// redis缓存中不存在数据，从数据库中获取数据
            historyPriceList = steamHistoryPriceMapper.selectByAppidAndType(appid, "app");
            if (historyPriceList.size() != 0) {
                // 将获取的数据放入redis缓存中
                int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
                try {
                    // 获取现在时间与下一天凌晨4点的时间差，单位秒
                    difftime = TimeUtils.getDifftimeFromNextZero();
                } catch (ParseException e) {
                    log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
                }
                // 每天凌晨4点清除缓存信息
                // 由于缓存中的信息并不需要修改，所以使用String的方式存储
                redisUtil.set(key, historyPriceList, difftime);
            }
        }

        // 将app的历史价格转换成时间为key(格式:20200215)，价格为value的map
        Map<Integer, Integer> priceMap = historyPriceList.stream().collect(
                Collectors.toMap(item -> TimeUtils.timeToInt(item.getGmtCreate()), item -> item.getPrice()));

        // 设置起始时间和当前的时间(格式:20200215)
        Integer farTime = 20200217; // 默认起始时间2020-2-17
        Integer preTime = TimeUtils.timeToInt(System.currentTimeMillis());

        List<String> times = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();
        for (int i = farTime; i <= preTime; i = TimeUtils.dateAddOneDay(i)) {
            Integer price = priceMap.get(i);
            if (price == null) { // 该时间节点app没有降价，以原价展示
                price = basePrice;
            }

            try {
                times.add(TimeUtils.tansferInt(i)); // 把时间从20200215转换成2020-02-15
                prices.add(price);
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("转换时间格式失败");
                throw new CustomizeException(CustomizeErrorCode.INIT_PRICE_CHART_FAILED);
            }
        }

        historyPriceDTO.setTime(times);
        historyPriceDTO.setPrice(prices);
        return historyPriceDTO;
    }

    private HistoryPriceDTO querySubOrBundlePrice(Integer appid, Integer type) {
        HistoryPriceDTO historyPriceDTO = new HistoryPriceDTO();

        SteamSubBundleInfo subInfo = null;

        String typeStr = SteamAppTypeEnum.typeOf(type);

        // 首先从redis中获取
        String key = "app-" + typeStr + "-" + appid;
        if (redisUtil.hasKey(key)) {
            JSONObject object = (JSONObject) redisUtil.get(key);
            subInfo = JSONObject.parseObject(object.toJSONString(), SteamSubBundleInfo.class);
        } else {
            // redis缓存中没有则从数据库中查询
            // 由于礼包的dto比较复杂，这里获取的只是简单的信息，所以这里就不存入redis中了
            // 由于querySubOrBundle这个方法先于本方法执行，所以存储redis缓存就在querySubOrBundle中执行了
            subInfo = steamSubBundleInfoMapper.selectByAppid(appid, typeStr);
            if(subInfo == null){
                return null;
            }
        }

        // 获取该app的原价格
        Integer basePrice = subInfo.getOriginalPrice();

        List<SteamHistoryPrice> historyPriceList = new ArrayList<>();
        // 首先从redis缓存中获取该app的历史价格
        key = "price-" + type + "-" + appid;
        if (redisUtil.hasKey(key)) {
            JSONArray jsonArray = (JSONArray) redisUtil.get(key);
            for (int i = 0; i < jsonArray.size(); i++) {
                historyPriceList.add(JSONObject.parseObject(jsonArray.getJSONObject(i).toJSONString(), SteamHistoryPrice.class));
            }
        } else {// redis缓存中不存在数据，从数据库中获取数据
            historyPriceList = steamHistoryPriceMapper.selectByAppidAndType(appid, typeStr);
            if (historyPriceList.size() != 0) {
                // 将获取的数据放入redis缓存中
                int difftime = 60 * 60; // 如果获取时间差失败则默认保存1小时
                try {
                    // 获取现在时间与下一天凌晨4点的时间差，单位秒
                    difftime = TimeUtils.getDifftimeFromNextZero();
                } catch (ParseException e) {
                    log.error("获取时间差失败,将默认保存1小时,stackTrace=" + e.getMessage());
                }
                // 每天凌晨4点清除缓存信息
                // 由于缓存中的信息并不需要修改，所以使用String的方式存储
                redisUtil.set(key, historyPriceList, difftime);
            }
        }

        // 将app的历史价格转换成时间为key(格式:20200215)，价格为value的map
        Map<Integer, Integer> priceMap = historyPriceList.stream().collect(
                Collectors.toMap(item -> TimeUtils.timeToInt(item.getGmtCreate()), item -> item.getPrice()));

        // 设置起始时间和当前的时间(格式:20200215)
        Integer farTime = 20200217; // 默认起始时间2020-2-17
        Integer preTime = TimeUtils.timeToInt(System.currentTimeMillis());

        List<String> times = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();
        for (int i = farTime; i <= preTime; i = TimeUtils.dateAddOneDay(i)) {
            Integer price = priceMap.get(i);
            if (price == null) { // 该时间节点app没有降价，以原价展示
                price = basePrice;
            }
            try {
                times.add(TimeUtils.tansferInt(i)); // 把时间从20200215转换成2020-02-15
                prices.add(price);
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("转换时间格式失败");
                throw new CustomizeException(CustomizeErrorCode.INIT_PRICE_CHART_FAILED);
            }
        }

        historyPriceDTO.setTime(times);
        historyPriceDTO.setPrice(prices);
        return historyPriceDTO;
    }

    @Override
    public boolean isExist(Integer appid) {
        List<String> types = SteamAppTypeEnum.listType();
        types.remove("bundle");
        types.remove("sub");
        boolean flag = false;
        for (String type : types) {
            SteamAppInfo steamAppInfo = steamAppInfoMapper.selectByAppid(appid, type);
            if (steamAppInfo != null) {
                flag = true;
                break;
            }
        }
        if (steamSubBundleInfoMapper.selectByAppid(appid, "bundle") != null) {
            flag = true;
        }
        if (steamSubBundleInfoMapper.selectByAppid(appid, "sub") != null) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<UserFavoriteApp> getFavorites(Long userid) {
        UserFavoriteAppExample example = new UserFavoriteAppExample();
        example.createCriteria().andUseridEqualTo(userid);
        List<UserFavoriteApp> favoriteApps = userFavoriteAppMapper.selectByExample(example);
        return favoriteApps;
    }

    @Override
    public List<UserFavoriteApp> favoriteApp(Long userid, Integer appid, Integer type) {
        UserFavoriteApp record = new UserFavoriteApp();
        record.setAppid(appid);
        record.setUserid(userid);
        record.setType(type);
        record.setGmtCreate(System.currentTimeMillis());
        record.setGmtModify(record.getGmtCreate());
        int idx = userFavoriteAppMapper.insertSelective(record);
        if(idx != 1){
            throw new CustomizeException(CustomizeErrorCode.FAVORITE_APP_FAILED);
        }

        List<UserFavoriteApp> favorites = getFavorites(userid);
        return favorites;
    }

    @Override
    public List<UserFavoriteApp> unFavoriteApp(Long userid, Integer appid, Integer type) {
        UserFavoriteAppExample example = new UserFavoriteAppExample();
        example.createCriteria().andUseridEqualTo(userid).andAppidEqualTo(appid).andTypeEqualTo(type);
        int idx = userFavoriteAppMapper.deleteByExample(example);
        if(idx != 1){
            throw new CustomizeException(CustomizeErrorCode.UNFAVORITE_APP_FAILED);
        }

        List<UserFavoriteApp> favorites = getFavorites(userid);
        return favorites;
    }

    @Override
    public List<Integer> getOwnedGames(String steamId) {
        SteamResponse response = steamProvider.getOwnedGames(steamId);
        List<Integer> appids = null;
        if(response != null){
            List<SteamGame> games = response.getGames();
            appids = games.stream().map(game -> game.getAppid()).collect(Collectors.toList());
        }
        return appids;
    }

    @Override
    public List<SteamAppNew> getAppNews(List<Integer> ownedGames) {
        List<SteamAppNew> appNews = new ArrayList<>();
        if(ownedGames!=null){
            ownedGames.stream().forEach(appid -> appNews.addAll(steamProvider.getAppNews(appid)));
        }
        return appNews;
    }

    @Override
    public Integer getPlayers(Integer appid) {
        return steamProvider.getPlayers(appid);
    }

}
