package com.joezeo.community.service.impl;

import com.joezeo.community.service.SpiderService;
import com.joezeo.community.spider.SteamSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpiderServiceImpl implements SpiderService {
    @Autowired
    private SteamSpider steamSpider;

    @Override
    public void spideUrl() {
        steamSpider.storeUrl();
    }
}
