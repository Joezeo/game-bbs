package com.joezeo.joefgame.common.provider;

import com.alibaba.fastjson.JSON;
import com.joezeo.joefgame.common.dto.SteamResponse;
import com.joezeo.joefgame.common.dto.SteamUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class SteamProvider {
    /*
    Openid Manager
     */
    @Autowired
    private ConsumerManager consumerManager;

    /*
    OkHttpClient
     */
    @Autowired
    private OkHttpClient okHttpClient;

    @Value("${steam.api.private.key}")
    private String steamApiPrivateKey;

    public String auth() {
        try {
            List discover = consumerManager.discover("https://steamcommunity.com/openid/");
            DiscoveryInformation associate = consumerManager.associate(discover);
            String returnToUrl = "http://www.joefgame.com/steam/callback";
            AuthRequest authRequest = consumerManager.authenticate(associate, returnToUrl);
            return authRequest.getDestinationUrl(true);
        } catch (DiscoveryException e) {
            e.printStackTrace();
        } catch (ConsumerException e) {
            e.printStackTrace();
        } catch (MessageException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSteamid(HttpServletRequest request) {
        HttpSession session = request.getSession();
        //获取响应参数列表
        ParameterList params = new ParameterList(request.getParameterMap());
        DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute("discovered");
        StringBuffer url = request.getRequestURL();
        String query = request.getQueryString();
        if (StringUtils.isNotBlank(query)) {
            url.append("?").append(query);
        }

        //根据参数列表，关联句柄以及url_query验证是否通过认证
        VerificationResult verification = null;
        try {
            verification = consumerManager.verify(url.toString(), params, discovered);
        } catch (MessageException e) {
            e.printStackTrace();
        } catch (DiscoveryException e) {
            e.printStackTrace();
        } catch (AssociationException e) {
            e.printStackTrace();
        }

        // 获取用户属性steam id
        String steamid = null;
        if (verification != null) {
            AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();
            String clamiedID = authSuccess.getClaimed(); // steam以clamiedID的形式包裹steamID，格式：http://steamcommunity.com/openid/id/<steamid>
            if (clamiedID != null && !"".equals(clamiedID)) {
                log.info("用户使用Steam三方登录，clamiedID=" + clamiedID);
                steamid = clamiedID.substring(clamiedID.lastIndexOf("/") + 1);
            }
        }
        return steamid;
    }

    public String getSteamName(String steamid) {
        String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + steamApiPrivateKey + "&steamids=" + steamid;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String jsonResult = response.body().string();
            SteamResponse steamResponse = JSON.parseObject(jsonResult, SteamResponse.class);
            List<SteamUser> players = steamResponse.getResponse().getPlayers();
            if(players != null && players.size()!=0){
                return players.get(0).getPersonaname();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
