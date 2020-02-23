package com.joezeo.joefgame.spider;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Vector;

@Component
@Scope("singleton")
public class SpiderComponent {
    // ArrayList线程安全，Vector使用同步锁线程安全
    public final List<String> gameFailUrl = new Vector<>();
    public final List<String> bundleFailUrl = new Vector<>();
    public final List<String> softwareFailUrl = new Vector<>();
    public final List<String> dlcFailUrl = new Vector<>();
    public final List<String> demoFailUrl = new Vector<>();
    public final List<String> soundFailUrl = new Vector<>();

    public void add2Fail(String url, String type){
        if("game".equals(type)){
            this.gameFailUrl.add(url);
        }
        else if("bundle".equals(type)){
            this.bundleFailUrl.add(url);
        }
        else if("software".equals(type)){
            this.softwareFailUrl.add(url);
        }
        else if("dlc".equals(type)){
            this.dlcFailUrl.add(url);
        }
        else if("demo".equals(type)){
            this.demoFailUrl.add(url);
        }
        else if("sound".equals(type)){
            this.soundFailUrl.add(url);
        }
    }

    public void removeFail(String url, String type) {
        if("game".equals(type)){
            if(gameFailUrl.contains(url)){
                gameFailUrl.remove(url);
            }
        }
        else if("bundle".equals(type)){
            if(bundleFailUrl.contains(url)){
                bundleFailUrl.remove(url);
            }
        }
        else if("software".equals(type)){
            if(softwareFailUrl.contains(url)){
                softwareFailUrl.remove(url);
            }
        }
        else if("dlc".equals(type)){
            if(dlcFailUrl.contains(url)){
                dlcFailUrl.remove(url);
            }
        }
        else if("demo".equals(type)){
            if(demoFailUrl.contains(url)){
                demoFailUrl.remove(url);
            }
        }
        else if("sound".equals(type)){
            if(soundFailUrl.contains(url)){
                soundFailUrl.remove(url);
            }
        }
    }
}
