package com.joezeo.joefgame.spider;

import com.joezeo.joefgame.enums.SpiderJobTypeEnum;
import com.joezeo.joefgame.mapper.SteamUrlMapper;
import com.joezeo.joefgame.pojo.SteamUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SteamSpider {
    @Autowired
    private PageGetter pageGetter;
    @Autowired
    private SteamUrlMapper steamUrlMapper;

    public void initUrl() {
        spideAllSpecial(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllGameUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllBundleUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllSoundTapeUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllSoftwareUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllDemoGameUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllDlcUrl(SpiderJobTypeEnum.INIT_URL_DATA);
    }

    public void daliyChekcUrl() {
        spideAllSpecial(SpiderJobTypeEnum.DAILY_CHECK_URL);
        spideAllGameUrl(SpiderJobTypeEnum.DAILY_CHECK_URL);
        spideAllBundleUrl(SpiderJobTypeEnum.DAILY_CHECK_URL);
        spideAllSoundTapeUrl(SpiderJobTypeEnum.DAILY_CHECK_URL);
        spideAllSoftwareUrl(SpiderJobTypeEnum.DAILY_CHECK_URL);
        spideAllDemoGameUrl(SpiderJobTypeEnum.DAILY_CHECK_URL);
        spideAllDlcUrl(SpiderJobTypeEnum.DAILY_CHECK_URL);
    }

    public void initAppInfo() {
        List<SteamUrl> game = steamUrlMapper.selectAll("game");
        List<SteamUrl> software = steamUrlMapper.selectAll("software");
        List<SteamUrl> dlc = steamUrlMapper.selectAll("dlc");
        List<SteamUrl> demo = steamUrlMapper.selectAll("demo");
        List<SteamUrl> sound = steamUrlMapper.selectAll("sound");
        List<SteamUrl> bundle = steamUrlMapper.selectAll("bundle");

        spideAllGameInfo(game, SpiderJobTypeEnum.INIT_APP_INFO);
        spideAllSoftwareInfo(software, SpiderJobTypeEnum.INIT_APP_INFO);
        spideAllDlcInfo(dlc, SpiderJobTypeEnum.INIT_APP_INFO);
        spideAllDemoInfo(demo, SpiderJobTypeEnum.INIT_APP_INFO);
        spideAllSoundInfo(sound, SpiderJobTypeEnum.INIT_APP_INFO);
        spideAllBundleInfo(bundle, SpiderJobTypeEnum.INIT_APP_INFO);
    }

    public void daliyChekcApp() {
        List<SteamUrl> game = steamUrlMapper.selectAll("game");
        List<SteamUrl> software = steamUrlMapper.selectAll("software");
        List<SteamUrl> dlc = steamUrlMapper.selectAll("dlc");
        List<SteamUrl> demo = steamUrlMapper.selectAll("demo");
        List<SteamUrl> sound = steamUrlMapper.selectAll("sound");
        List<SteamUrl> bundle = steamUrlMapper.selectAll("bundle");

        spideAllGameInfo(game, SpiderJobTypeEnum.DAILY_CHECK_APP_INFO);
        spideAllSoftwareInfo(software, SpiderJobTypeEnum.DAILY_CHECK_APP_INFO);
        spideAllDlcInfo(dlc, SpiderJobTypeEnum.DAILY_CHECK_APP_INFO);
        spideAllDemoInfo(demo, SpiderJobTypeEnum.DAILY_CHECK_APP_INFO);
        spideAllSoundInfo(sound, SpiderJobTypeEnum.DAILY_CHECK_APP_INFO);
        spideAllBundleInfo(bundle, SpiderJobTypeEnum.DAILY_CHECK_APP_INFO);
    }

    /*
    每天检查特惠商品的价格，将每天的特惠商品的价格放入t_steam_history_price表中
     */
    public void updateHistoryPrice() {
        List<SteamUrl> special = steamUrlMapper.selectAll("special");

        spideSpecialPrice(special, SpiderJobTypeEnum.DAILY_SPIDE_SPECIAL_PRICE);
    }

    /*
            使用爬虫爬取所有app的url地址,存储至数据库中
            初始化/日常检查
     */
    private void spideAllGameUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=998&ignore_preferences=1&page=";
        // 如果获取页面失败那么totalPage会一直为0，直到成功获取了总页数才继续
        int totalPage = 0;
        while (totalPage == 0) {
            totalPage = pageGetter.getSteamTotalPage(url + 1);
        }
        for (int i = 1; i <= totalPage; i++) {
            pageGetter.spiderUrlAsyn(url + i, "game", null, jobTypeEnum);
        }
    }

    private void spideAllBundleUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=996&ignore_preferences=1&page=";
        int totalPage = 0;
        while (totalPage == 0) {
            totalPage = pageGetter.getSteamTotalPage(url + 1);
        }
        for (int i = 0; i <= totalPage; i++) {
            pageGetter.spiderUrlAsyn(url + i, "bundle", null, jobTypeEnum);
        }
    }

    private void spideAllSoftwareUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=994&ignore_preferences=1&page=";
        int totalPage = 0;
        while (totalPage == 0) {
            totalPage = pageGetter.getSteamTotalPage(url + 1);
        }
        for (int i = 1; i <= totalPage; i++) {
            pageGetter.spiderUrlAsyn(url + i, "software", null, jobTypeEnum);
        }
    }

    private void spideAllDlcUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=21&ignore_preferences=1&page=";
        int totalPage = 0;
        while (totalPage == 0) {
            totalPage = pageGetter.getSteamTotalPage(url + 1);
        }
        for (int i = 1; i <= totalPage; i++) {
            pageGetter.spiderUrlAsyn(url + i, "dlc", null, jobTypeEnum);
        }
    }

    private void spideAllDemoGameUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=10&ignore_preferences=1&page=";
        int totalPage = 0;
        while (totalPage == 0) {
            totalPage = pageGetter.getSteamTotalPage(url + 1);
        }
        for (int i = 1; i <= totalPage; i++) {
            pageGetter.spiderUrlAsyn(url + i, "demo", null, jobTypeEnum);
        }
    }

    private void spideAllSoundTapeUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=990&ignore_preferences=1&page=";
        int totalPage = 0;
        while (totalPage == 0) {
            totalPage = pageGetter.getSteamTotalPage(url + 1);
        }
        for (int i = 1; i <= totalPage; i++) {
            pageGetter.spiderUrlAsyn(url + i, "sound", null, jobTypeEnum);
        }
    }

    // 特惠搜索页面，用于获取所有特惠商品的地址
    private void spideAllSpecial(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?filter=topsellers&specials=1&ignore_preferences=1&page=";
        int totalPage = 0;
        while (totalPage == 0) {
            totalPage = pageGetter.getSteamTotalPage(url + 1);
        }
        for (int i = 1; i <= totalPage; i++) {
            pageGetter.spiderUrlAsyn(url + i, "special", null, jobTypeEnum);
        }
    }

    /*
            通过存储的url地址，爬取所有steam app的基本信息
     */
    private void spideAllGameInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl() + "?cc=cn", "game", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllSoftwareInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl() + "?cc=cn", "software", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllDlcInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl() + "?cc=cn", "dlc", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllDemoInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl() + "?cc=cn", "demo", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllSoundInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl() + "?cc=cn", "sound", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllBundleInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl() + "?cc=cn", "bundle", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    /*
            通过存储的url地址，获取steam当前特惠商品的价格
     */
    private void spideSpecialPrice(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl() + "?cc=cn", "special", steamUrl.getAppid(), jobTypeEnum);
        }
    }

}
