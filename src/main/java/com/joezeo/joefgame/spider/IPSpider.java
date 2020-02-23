package com.joezeo.joefgame.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IPSpider {
    @Autowired
    private PageGetter pageGetter;

    public void spideProxyIP() {
        String url = "https://www.xicidaili.com/nn/";
        int totalPage = 0;
        while (totalPage == 0) {
            totalPage = pageGetter.getIPTotalPage(url);
        }
        for(int i=1; i<=totalPage; i++){
            pageGetter.getProxyIpPage(url+i);
        }
    }
}
