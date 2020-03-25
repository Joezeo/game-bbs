package com.joezeo.joefgame.common.provider;

import com.alibaba.fastjson.JSON;
import com.joezeo.joefgame.common.dto.SteamAppNew;
import com.joezeo.joefgame.common.dto.SteamGame;
import com.joezeo.joefgame.common.dto.SteamResponse;
import com.joezeo.joefgame.common.dto.SteamUser;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
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
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SteamProvider {
    /*
    OkHttpClient
     */
    private static OkHttpClient okHttpClient;
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        okHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();
    }

    /*
    Openid Manager
     */
    @Autowired
    private ConsumerManager consumerManager;


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
            log.error("使用Steam三方认证失败:StackTrace="+e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.STEAM_AUTH_FAILD);
        } catch (ConsumerException e) {
            log.error("使用Steam三方认证失败:StackTrace="+e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.STEAM_AUTH_FAILD);
        } catch (MessageException e) {
            log.error("使用Steam三方认证失败:StackTrace="+e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.STEAM_AUTH_FAILD);
        }
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
            log.error("获取SteamID失败:StackTrace="+e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.STEAM_AUTH_FAILD);
        } catch (DiscoveryException e) {
            log.error("获取SteamID失败:StackTrace="+e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.STEAM_AUTH_FAILD);
        } catch (AssociationException e) {
            log.error("获取SteamID失败:StackTrace="+e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.STEAM_AUTH_FAILD);
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
            log.error("获取Steam Name失败:StackTrace="+e.getStackTrace());
            throw new CustomizeException(CustomizeErrorCode.STEAM_CONNECTION_FAILD);
        }
        return null;
    }

    public SteamResponse getOwnedGames(String steamId) {
        String url = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + steamApiPrivateKey + "&steamids=" + steamId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String jsonResult = response.body().string();
            SteamResponse steamResponse = JSON.parseObject(jsonResult, SteamResponse.class);
            return steamResponse.getResponse();
        } catch (IOException e) {
            log.error("获取Steam Name失败:StackTrace="+e.getStackTrace());
        } catch (Exception e){ // 服务器在本地时无法连接至Steam api
            log.error("服务器在本地时无法连接至Steam api");
        }
        return null;
    }

    public List<SteamAppNew> getAppNews(Integer appid) {
        String url = "http://api.steampowered.com/ISteamNews/GetNewsForApp/v0002/?appid="+appid+"&count=3&maxlength=300&format=json";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String jsonResult = response.body().string();
            SteamResponse steamResponse = JSON.parseObject(jsonResult, SteamResponse.class);
            return steamResponse.getAppnews().getNewsitems();
        } catch (IOException e) {
            log.error("获取Steam App News失败:StackTrace="+e.getStackTrace());
        } catch (Exception e){ // 服务器在本地时无法连接至Steam api
            log.error("服务器在本地时无法连接至Steam api");
        }
        return null;
    }
}
