package com.joezeo.joefgame.manager.spider;

import com.joezeo.joefgame.dao.mapper.SpiderFailedUrlMapper;
import com.joezeo.joefgame.dao.pojo.SpiderFailedUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("ALL")
@Component
@Slf4j
public class SpiderComponent {

    @Autowired
    private SpiderFailedUrlMapper spiderFailedUrlMapper;

    public final Map<String, Integer> gameFailMap = new ConcurrentHashMap<>();
    public final Map<String, Integer> bundleFailMap = new ConcurrentHashMap<>();
    public final Map<String, Integer> softwareFailMap = new ConcurrentHashMap<>();
    public final Map<String, Integer> dlcFailMap = new ConcurrentHashMap<>();
    public final Map<String, Integer> demoFailMap = new ConcurrentHashMap<>();
    public final Map<String, Integer> soundFailMap = new ConcurrentHashMap<>();
    public final Map<String, Integer> subFailMap = new ConcurrentHashMap<>();

    public void add2Fail(String url, String type) {
        if ("game".equals(type)) {
            if (gameFailMap.containsKey(url)) {
                Integer count = gameFailMap.get(url);
                if (count > 5) { // 重试超过五次就不再重试，将失败的url存入数据库中
                    removeFail(url, type);
                    logInDatabase(url, type);
                    return;
                }
                gameFailMap.put(url, count + 1);
            } else {
                gameFailMap.put(url, 1);
            }
        } else if ("bundle".equals(type)) {
            if (bundleFailMap.containsKey(url)) {
                Integer count = bundleFailMap.get(url);
                if (count > 5) {
                    removeFail(url, type);
                    logInDatabase(url, type);
                    return;
                }
                bundleFailMap.put(url, count + 1);
            } else {
                bundleFailMap.put(url, 1);
            }
        } else if ("software".equals(type)) {
            if (softwareFailMap.containsKey(url)) {
                Integer count = softwareFailMap.get(url);
                if (count > 5) {
                    removeFail(url, type);
                    logInDatabase(url, type);
                    return;
                }
                softwareFailMap.put(url, count + 1);
            } else {
                softwareFailMap.put(url, 1);
            }
        } else if ("dlc".equals(type)) {
            if (dlcFailMap.containsKey(url)) {
                Integer count = dlcFailMap.get(url);
                if (count > 5) {
                    removeFail(url, type);
                    logInDatabase(url, type);
                    return;
                }
                dlcFailMap.put(url, count + 1);
            } else {
                dlcFailMap.put(url, 1);
            }
        } else if ("demo".equals(type)) {
            if (demoFailMap.containsKey(url)) {
                Integer count = demoFailMap.get(url);
                if (count > 5) {
                    removeFail(url, type);
                    logInDatabase(url, type);
                    return;
                }
                demoFailMap.put(url, count + 1);
            } else {
                demoFailMap.put(url, 1);
            }
        } else if ("sound".equals(type)) {
            if (soundFailMap.containsKey(url)) {
                Integer count = soundFailMap.get(url);
                if (count > 5) {
                    removeFail(url, type);
                    logInDatabase(url, type);
                    return;
                }
                soundFailMap.put(url, count + 1);
            } else {
                soundFailMap.put(url, 1);
            }
        } else if ("sub".equals(type)) {
            if (subFailMap.containsKey(url)) {
                Integer count = subFailMap.get(url);
                if (count > 5) {
                    removeFail(url, type);
                    logInDatabase(url, type);
                    return;
                }
                subFailMap.put(url, count + 1);
            } else {
                subFailMap.put(url, 1);
            }
        }
    }

    private void removeFail(String url, String type) {
        if ("game".equals(type)) {
            if (gameFailMap.containsKey(url)) {
                gameFailMap.remove(url);
            }
        } else if ("bundle".equals(type)) {
            if (bundleFailMap.containsKey(url)) {
                bundleFailMap.remove(url);
            }
        } else if ("software".equals(type)) {
            if (softwareFailMap.containsKey(url)) {
                softwareFailMap.remove(url);
            }
        } else if ("dlc".equals(type)) {
            if (dlcFailMap.containsKey(url)) {
                dlcFailMap.remove(url);
            }
        } else if ("demo".equals(type)) {
            if (demoFailMap.containsKey(url)) {
                demoFailMap.remove(url);
            }
        } else if ("sound".equals(type)) {
            if (soundFailMap.containsKey(url)) {
                soundFailMap.remove(url);
            }
        } else if ("sub".equals(type)) {
            if (subFailMap.containsKey(url)) {
                subFailMap.remove(url);
            }
        }
    }

    private void logInDatabase(String url, String type){
        SpiderFailedUrl failedUrl = new SpiderFailedUrl();
        failedUrl.setUrl(url);
        failedUrl.setType(type);
        failedUrl.setGmtCreate(System.currentTimeMillis());
        failedUrl.setGmtModify(failedUrl.getGmtCreate());
        int idx = spiderFailedUrlMapper.insertSelective(failedUrl);
        if(idx != 1){
            log.error("爬虫模块：记录失败的url失败,url=" + url + "\ttype=" + type);
        }
    }
}
