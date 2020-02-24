package com.joezeo.joefgame.manager.service.impl;

import com.joezeo.joefgame.manager.spider.IPSpider;
import com.joezeo.joefgame.manager.spider.SteamSpider;
import com.joezeo.joefgame.manager.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpiderServiceImpl implements SpiderService {
    @Autowired
    private SteamSpider steamSpider;
    @Autowired
    private IPSpider ipSpider;

    @Override
    public void spideUrl() {
        steamSpider.initUrl();
    }

    @Override
    public void spideApp() {
        steamSpider.initAppInfo();
    }

    @Override
    public void checkUrl() {
        steamSpider.daliyChekcUrl();
    }

    @Override
    public void checkApp() {
        steamSpider.daliyChekcApp();
    }

    @Override
    public void spideSpecialPrice() {
        steamSpider.updateHistoryPrice();
    }

    @Override
    public void spideProxyIP() {
        ipSpider.spideProxyIP();
    }
}
