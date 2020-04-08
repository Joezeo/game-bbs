package com.joezeo.joefgame.web.aspect;

import com.joezeo.joefgame.common.exception.ServiceException;
import com.joezeo.joefgame.common.utils.RedisUtil;
import com.joezeo.joefgame.potal.service.IPService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 记录下
 * @author Joezeo
 * @date 2020/4/7 12:21
 */
@Aspect
@Component
public class IpRecordAspect {

    @Autowired
    private IPService ipService;

    // 切点为整个项目下web.controller包中的所有获取页面方法
    @Pointcut("execution(public String com.joezeo.joefgame.web.controller.*.*(..))")
    public void recordUserIP() {
    }

    @Before("recordUserIP()")
    public void recordIP(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        // 由于 nginx 设置了反向代理，故必须在 nginx 中做相应的配置，才能获取真实用户的信息
        // 在开发环境下，localhost会被解析成ipv6地址：0:0:0:0:0:0:0:1
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if(ipService.isInBlackList(ip)){
            throw new ServiceException("您的访问速度过快，系统已将您判断为机器人，请明天再来吧");
        }

        ipService.recordIPByMysql(ip);
        ipService.recordIPByRedis(ip);
    }
}
