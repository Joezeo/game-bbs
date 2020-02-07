package com.joezeo.community.spider;

import com.joezeo.community.enums.SpiderJobTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    @Scheduled(cron = "0/30 * * * * ?")
    public void restorePage() {
        // 判断游戏url
        List<String> game = spiderComponent.gameFailUrl;
        List<String> gameCopy = game.stream().collect(Collectors.toList());
        game.removeAll(game);
        if (gameCopy.size() != 0) {
            for (String url : gameCopy) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "game", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断捆绑包url
        List<String> bundle = spiderComponent.gameFailUrl;
        List<String> bundleCopy = bundle.stream().collect(Collectors.toList());
        bundle.removeAll(bundle);
        if (bundleCopy.size() != 0) {
            for (String url : bundleCopy) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "bundle", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断软件url
        List<String> software = spiderComponent.softwareFailUrl;
        List<String> softwareCopy = software.stream().collect(Collectors.toList());
        if (softwareCopy.size() != 0) {
            for (String url : softwareCopy) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "software", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断 dlc url
        List<String> dlc = spiderComponent.dlcFailUrl;
        List<String> dlcCopy = dlc.stream().collect(Collectors.toList());
        dlc.removeAll(dlc);
        if (dlcCopy.size() != 0) {
            for (String url : dlcCopy) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "dlc", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断试玩游戏url
        List<String> demo = spiderComponent.demoFailUrl;
        List<String> demoCopy = demo.stream().collect(Collectors.toList());
        if (demoCopy.size() != 0) {
            for (String url : demoCopy) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "demo", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }

        // 判断原声带url
        List<String> sound = spiderComponent.soundFailUrl;
        List<String> soundCopy = sound.stream().collect(Collectors.toList());
        sound.removeAll(sound);
        if (soundCopy.size() != 0) {
            for (String url : soundCopy) {
                System.out.println("再次爬取页面："+url);
                pageGetter.spiderUrlAsyn(url, "sound", null, SpiderJobTypeEnum.DAILY_CHECK_URL);
            }
        }
    }
}
