package com.joezeo.community.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SteamSpider {
    @Autowired
    private PageGetter pageGetter;

    public void storeUrl(){
        getAllGameUrl();
        getAllBundleUrl();
        getAllSoundTapeUrl();
        getAllSoftwareUrl();
        getAllDemoGameUrl();
        getAllDlcUrl();
    }

    /*
            使用爬虫爬取所有app的url地址,存储至数据库中
     */
    private void getAllGameUrl(){
        String url = "https://store.steampowered.com/search/?category1=998&page=";
        for(int i=1; i<=1499; i++){
            pageGetter.spiderAsyn(url+i, "game");
        }
    }
    private void getAllBundleUrl(){
        String url = "https://store.steampowered.com/search/?category1=996&page=";
        for(int i=0; i<=168; i++){
            pageGetter.spiderAsyn(url+i, "bundle");
        }
    }
    private void getAllSoftwareUrl(){
        String url = "https://store.steampowered.com/search/?category1=994&page=";
        for(int i=1; i<=38; i++){
            pageGetter.spiderAsyn(url+i, "software");
        }
    }
    private void getAllDlcUrl(){
        String url = "https://store.steampowered.com/search/?category1=21&page=";
        for(int i=1; i<=883; i++){
            pageGetter.spiderAsyn(url+i, "dlc");
        }
    }
    private void getAllDemoGameUrl(){
        String url = "https://store.steampowered.com/search/?category1=10&page=";
        for(int i=1; i<=135; i++){
            pageGetter.spiderAsyn(url+i, "demo");
        }
    }
    private void getAllSoundTapeUrl(){
        String url = "https://store.steampowered.com/search/?category1=990&page=";
        for(int i=1; i<=29; i++){
            pageGetter.spiderAsyn(url+i, "sound");
        }
    }

    /*
            通过存储的url地址，爬取所有steam app的基本信息
     */
}
