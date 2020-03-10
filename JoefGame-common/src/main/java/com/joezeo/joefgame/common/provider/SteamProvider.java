package com.joezeo.joefgame.common.provider;

import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ax.FetchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SteamProvider {
    /*
    Openid Manager
     */
    @Autowired
    private ConsumerManager consumerManager;

    public void auth(){
        try {
            List discover = consumerManager.discover("https://steamcommunity.com/openid/");
            DiscoveryInformation associate = consumerManager.associate(discover);
            String returnToUrl = "http://www.joefgame.com/steam/callback";
            AuthRequest authRequest = consumerManager.authenticate(associate, returnToUrl);
            FetchRequest fetch = FetchRequest.createFetchRequest();
            fetch.addAttribute("id", " http://steamcommunity.com/openid/id", true);
            authRequest.addExtension(fetch);
        } catch (DiscoveryException e) {
            e.printStackTrace();
        } catch (ConsumerException e) {
            e.printStackTrace();
        } catch (MessageException e) {
            e.printStackTrace();
        }
    }

}
