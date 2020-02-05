package com.joezeo.community.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SteamSpider {
    @Autowired
    private PageGetter pageGetter;

    public void storeUrl(){
        getAllGameUrl();
        getAllSoundTapeUrl();
        getAllSoftwareUrl();
        getAllDemoGameUrl();
        getAllDlcUrl();
    }
    private void getAllGameUrl(){
        String url = "https://store.steampowered.com/search/?category1=996%2C998&page=";
        for(int i=1; i<=1632; i++){
            pageGetter.spiderAsyn(url+i, "game");
        }
    }
    private void getAllSoftwareUrl(){
        String url = "https://store.steampowered.com/search/?category1=996%2C994&page=";
        for(int i=1; i<=41; i++){
            pageGetter.spiderAsyn(url+i, "software");
        }
    }
    private void getAllDlcUrl(){
        String url = "https://store.steampowered.com/search/?category1=996%2C21&page=";
        for(int i=1; i<=970; i++){
            pageGetter.spiderAsyn(url+i, "dlc");
        }
    }
    private void getAllDemoGameUrl(){
        String url = "https://store.steampowered.com/search/?category1=996%2C10&page=";
        for(int i=1; i<=135; i++){
            pageGetter.spiderAsyn(url+i, "demo");
        }
    }
    private void getAllSoundTapeUrl(){
        String url = "https://store.steampowered.com/search/?category1=996%2C990&page=";
        for(int i=1; i<=38; i++){
            pageGetter.spiderAsyn(url+i, "sound");
        }
    }
}
