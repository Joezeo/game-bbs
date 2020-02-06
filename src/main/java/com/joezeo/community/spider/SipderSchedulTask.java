package com.joezeo.community.spider;

import com.joezeo.community.enums.SpiderJobTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时检查SiderComponent的failUrl容器是否含有上一次执行失败的url
 * 如有则继续调用PageGetter爬取网页
 */
@Component
public class SipderSchedulTask {

    @Autowired
    private SpiderComponent spiderComponent;

    @Autowired
    private PageGetter pageGetter;

    @Scheduled(cron = "0/5 * * * * ?")
    public void restorePage() {
        // 判断游戏url
        List<String> game = spiderComponent.gameFailUrl;
        if (game.size() != 0) {
            for (String url : game) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "game", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断捆绑包url
        List<String> bundle = spiderComponent.gameFailUrl;
        if (bundle.size() != 0) {
            for (String url : bundle) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "bundle", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断软件url
        List<String> software = spiderComponent.softwareFailUrl;
        if (software.size() != 0) {
            for (String url : software) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "software", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断 dlc url
        List<String> dlc = spiderComponent.dlcFailUrl;
        if (dlc.size() != 0) {
            for (String url : dlc) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "dlc", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断试玩游戏url
        List<String> demo = spiderComponent.demoFailUrl;
        if (demo.size() != 0) {
            for (String url : demo) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "demo", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断原声带url
        List<String> sound = spiderComponent.soundFailUrl;
        if (sound.size() != 0) {
            for (String url : sound) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "sound", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }
    }
}
