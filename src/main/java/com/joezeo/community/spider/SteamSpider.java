package com.joezeo.community.spider;

import com.joezeo.community.enums.SpiderJobTypeEnum;
import com.joezeo.community.mapper.SteamUrlMapper;
import com.joezeo.community.pojo.SteamUrl;
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
        spideAllGameUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllBundleUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllSoundTapeUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllSoftwareUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllDemoGameUrl(SpiderJobTypeEnum.INIT_URL_DATA);
        spideAllDlcUrl(SpiderJobTypeEnum.INIT_URL_DATA);
    }

    public void daliyChekcUrl() {
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

    /*
            使用爬虫爬取所有app的url地址,存储至数据库中
            初始化/日常检查
     */
    private void spideAllGameUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=998&page=";
        for (int i = 1; i <= 1499; i++) {
            pageGetter.spiderUrlAsyn(url + i, "game", null, jobTypeEnum);
        }
    }

    private void spideAllBundleUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=996&page=";
        for (int i = 0; i <= 168; i++) {
            pageGetter.spiderUrlAsyn(url + i, "bundle", null, jobTypeEnum);
        }
    }

    private void spideAllSoftwareUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=994&page=";
        for (int i = 1; i <= 38; i++) {
            pageGetter.spiderUrlAsyn(url + i, "software", null, jobTypeEnum);
        }
    }

    private void spideAllDlcUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=21&page=";
        for (int i = 1; i <= 883; i++) {
            pageGetter.spiderUrlAsyn(url + i, "dlc", null, jobTypeEnum);
        }
    }

    private void spideAllDemoGameUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=10&page=";
        for (int i = 1; i <= 135; i++) {
            pageGetter.spiderUrlAsyn(url + i, "demo", null, jobTypeEnum);
        }
    }

    private void spideAllSoundTapeUrl(SpiderJobTypeEnum jobTypeEnum) {
        String url = "https://store.steampowered.com/search/?category1=990&page=";
        for (int i = 1; i <= 29; i++) {
            pageGetter.spiderUrlAsyn(url + i, "sound", null, jobTypeEnum);
        }
    }

    /*
            通过存储的url地址，爬取所有steam app的基本信息
     */
    private void spideAllGameInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl(), "game", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllSoftwareInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl(), "software", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllDlcInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl(), "dlc", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllDemoInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl(), "demo", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllSoundInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl(), "sound", steamUrl.getAppid(), jobTypeEnum);
        }
    }

    private void spideAllBundleInfo(List<SteamUrl> list, SpiderJobTypeEnum jobTypeEnum) {
        for (SteamUrl steamUrl : list) {
            pageGetter.spiderUrlAsyn(steamUrl.getUrl(), "bundle", steamUrl.getAppid(), jobTypeEnum);
        }
    }
}
