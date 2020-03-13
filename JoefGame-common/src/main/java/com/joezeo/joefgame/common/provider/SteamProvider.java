package com.joezeo.joefgame.common.provider;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Slf4j
public class SteamProvider {
    /*
    Openid Manager
     */
    @Autowired
    private ConsumerManager consumerManager;

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
}
