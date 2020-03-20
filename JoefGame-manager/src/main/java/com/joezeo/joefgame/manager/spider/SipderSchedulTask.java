package com.joezeo.joefgame.manager.spider;

import com.joezeo.joefgame.common.enums.SpiderJobTypeEnum;
import com.joezeo.joefgame.common.enums.SteamAppTypeEnum;
import com.joezeo.joefgame.common.utils.TimeUtils;
import com.joezeo.joefgame.dao.mapper.SteamAppInfoMapper;
import com.joezeo.joefgame.dao.mapper.SteamHistoryPriceMapper;
import com.joezeo.joefgame.dao.mapper.SteamSubBundleInfoMapper;
import com.joezeo.joefgame.dao.mapper.SteamUrlMapper;
import com.joezeo.joefgame.dao.pojo.SteamAppInfo;
import com.joezeo.joefgame.dao.pojo.SteamHistoryPrice;
import com.joezeo.joefgame.dao.pojo.SteamSubBundleInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 爬虫模块的定时任务
 */
@Component
@Slf4j
@Lazy(false) // 取消延迟初始化，否则定时任务无法运行
public class SipderSchedulTask {

    @Autowired
    private PageGetter pageGetter;
    @Autowired
    private SteamSpider steamSpider;
    @Autowired
    private SteamUrlMapper steamUrlMapper;
    @Autowired
    private SpiderComponent spiderComponent;
    @Autowired
    private SteamAppInfoMapper steamAppInfoMapper;
    @Autowired
    private SteamHistoryPriceMapper steamHistoryPriceMapper;
    @Autowired
    private SteamSubBundleInfoMapper steamSubBundleInfoMapper;

    /**
     * 凌晨00:00
     * <p>
     * 清空特惠商品url
     */
    @Scheduled(cron = "0 0 0 1/1 * ?") // 每天凌晨00:00执行
    public void emptySpecialUrl() {
        log.info("Spider定时任务 [清空特惠商品url]");
        // 清空 t_steam_special_url
        int idx = steamUrlMapper.emptySpecialUrl();
        if (idx < 0) {
            log.error("数据库发生异常");
        }
    }

    /**
     * 凌晨00:05
     * <p>
     * 爬取所有app的url信息
     */
    @Scheduled(cron = "0 5 0 1/1 * ?") // 每天凌晨00:05执行
    public void spideUrl() {
        log.info("Spider定时任务 [爬取所有app的url信息]");
        steamSpider.daliyChekcUrl();
    }

    /**
     * 凌晨01:00
     * <p>
     * 爬取所有特惠商品的价格
     */
    @Scheduled(cron = "0 0 1 1/1 * ?") // 每天凌晨01:00执行
    public void spideSpecialPrice() {
        log.info("Spider定时任务 [爬取所有特惠商品的价格]");
        steamSpider.updateHistoryPrice();
    }

    /**
     * 凌晨02:00
     * <p>
     * 检查t_history_price
     * 找出price=0的商品，重新进行查询
     */
    @Scheduled(cron = "0 0 2 1/1 * ?") // 每天凌晨02:00执行
    public void checkHistoryPrice() {
        log.info("Spider定时任务 [重新查询price=0的特惠商品]");
        try {
            //获取当天零点的时间戳
            long timestampAtZero = TimeUtils.getTimestampAtZero();
            List<SteamHistoryPrice> steamHistoryPrices = steamHistoryPriceMapper.selectByTime(timestampAtZero);
            int idx = steamHistoryPriceMapper.deleteIllegal(); // 这里需要先删除，否则之后会因为数据库存在相同的app而重复
            if (idx < 0) {
                log.error("删除非法的特惠商品历史价格失败");
            }

            List<SteamHistoryPrice> failds = steamHistoryPrices.stream()
                    .filter(price -> price.getPrice() == 0)
                    .collect(Collectors.toList());
            String url = "https://store.steampowered.com/app/";
            for (SteamHistoryPrice price : failds) {
                pageGetter.spiderUrlAsyn(url + price.getAppid() + "/?cc=cn", "special", price.getAppid(), SpiderJobTypeEnum.DAILY_SPIDE_SPECIAL_PRICE);
            }
        } catch (ParseException e) {
            log.error("获取零点时间戳失败,stackTrace=" + e.getMessage());
        }
    }

    /**
     * 凌晨02:40
     * <p>
     * 经过第二轮查询价格依然为0的商品，从数据库中删除
     */
    @Scheduled(cron = "0 40 2 1/1 * ?") // 每天凌晨02:40执行
    public void deleteIllegalHistoryPrice() {
        log.info("Spider定时任务 [删除price=0的特惠商品]");
        int idx = steamHistoryPriceMapper.deleteIllegal();
        if (idx < 0) {
            log.error("数据库删除非法特惠商品价格出现异常");
        }
    }

    /**
     * 凌晨02:45
     * <p>
     * 根据当天的特惠列表修改 App Info的finalPrice字段
     */
    @Scheduled(cron = "0 45 2 1/1 * ?") // 每天凌晨02:45执行
    public void updateAppFinalPrice() {
        log.info("Spider定时任务 [根据特惠商品列表,修改app的finalPrice]");
        try {
            // 获取当天的特惠商品集合
            Long timeAtZero = TimeUtils.getTimestampAtZero();
            List<SteamHistoryPrice> prices = steamHistoryPriceMapper.selectByTime(timeAtZero);

            prices.stream().forEach(item -> {
                String type = item.getType();
                if("sub".equals(type) || "bundle".equals(type)){
                    SteamSubBundleInfo subBundleInfo = new SteamSubBundleInfo();
                    subBundleInfo.setType(type);
                    subBundleInfo.setAppid(item.getAppid());
                    subBundleInfo.setFinalPrice(item.getPrice());
                    subBundleInfo.setGmtModify(System.currentTimeMillis());
                    int idx = steamSubBundleInfoMapper.updateByAppidSelective(subBundleInfo);
                    if(idx != 1){
                        log.error("修改特惠商品["+ type +"]info的finalPrice失败");
                    }
                } else { // app
                    List<String> types = SteamAppTypeEnum.listType();
                    types.remove("sub");
                    types.remove("bundle");
                    for (String typ : types) {
                        SteamAppInfo steamAppInfo = steamAppInfoMapper.selectByAppid(item.getAppid(), typ);
                        if(steamAppInfo != null){
                            steamAppInfo.setFinalPrice(item.getPrice());
                            steamAppInfo.setGmtModify(System.currentTimeMillis());
                            int idx = steamAppInfoMapper.updateByAppidSelective(steamAppInfo, typ);
                            if(idx != 1){
                                log.error("修改特惠商品[" + typ + "]info的finalPrice失败");
                            }
                            break;
                        }
                    }
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("解析时间失败，修改finalPrice失败");
        }
    }

    /**
     * 凌晨02:50
     * <p>
     * 检查数据中appInfo的非法项，将非法项删除
     */
    @Scheduled(cron = "0 50 2 1/1 * ?") // 每天凌晨2:30执行
    public void checkAppInfoDB() {
        log.info("Spider定时任务 [检查App Info非法项，并删除]");
        int idx = 0;
        List<String> types = SteamAppTypeEnum.listType();
        for (String type : types) {
            idx = steamAppInfoMapper.deleteIllegal(type);
            if (idx < 0) {
                log.error("数据库删除非法App Info出现异常,type=" + type);
            }
        }
    }

    /**
     * 凌晨03:00 每隔7天
     * <p>
     * 重新爬取app info
     */
    @Scheduled(cron = "0 0 3 1/7 * ?") // 每7天执行一次，凌晨3:00执行
    public void spideAppInfo() {
        log.info("Spider定时任务 [重新爬取所有 app info]");
        steamSpider.daliyChekcApp();
    }


    @Scheduled(cron = "0 0/2 * * * ?")
    public void restorePage() {
        // 判断游戏url
        Map<String, Integer> game = spiderComponent.gameFailMap;
        List<String> gameCopy = game.keySet().stream().collect(Collectors.toList());
        if (gameCopy.size() != 0) {
            for (String url : gameCopy) {
                System.out.println("再次爬取页面：" + url);
                pageGetter.spiderUrlAsyn(url, "game", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断捆绑包url
        Map<String, Integer> bundle = spiderComponent.gameFailMap;
        List<String> bundleCopy = bundle.keySet().stream().collect(Collectors.toList());
        if (bundleCopy.size() != 0) {
            for (String url : bundleCopy) {
                System.out.println("再次爬取页面：" + url);
                pageGetter.spiderUrlAsyn(url, "bundle", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断软件url
        Map<String, Integer> software = spiderComponent.softwareFailMap;
        List<String> softwareCopy = software.keySet().stream().collect(Collectors.toList());
        if (softwareCopy.size() != 0) {
            for (String url : softwareCopy) {
                System.out.println("再次爬取页面：" + url);
                pageGetter.spiderUrlAsyn(url, "software", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断 dlc url
        Map<String, Integer> dlc = spiderComponent.dlcFailMap;
        List<String> dlcCopy = dlc.keySet().stream().collect(Collectors.toList());
        if (dlcCopy.size() != 0) {
            for (String url : dlcCopy) {
                System.out.println("再次爬取页面：" + url);
                pageGetter.spiderUrlAsyn(url, "dlc", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断试玩游戏url
        Map<String, Integer> demo = spiderComponent.demoFailMap;
        List<String> demoCopy = demo.keySet().stream().collect(Collectors.toList());
        if (demoCopy.size() != 0) {
            for (String url : demoCopy) {
                System.out.println("再次爬取页面：" + url);
                pageGetter.spiderUrlAsyn(url, "demo", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断原声带url
        Map<String, Integer> sound = spiderComponent.soundFailMap;
        List<String> soundCopy = sound.keySet().stream().collect(Collectors.toList());
        if (soundCopy.size() != 0) {
            for (String url : soundCopy) {
                System.out.println("再次爬取页面：" + url);
                pageGetter.spiderUrlAsyn(url, "sound", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }
    }
}
