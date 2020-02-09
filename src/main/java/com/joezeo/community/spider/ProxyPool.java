package com.joezeo.community.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.ProxyIPMapper;
import com.joezeo.community.pojo.ProxyIP;
import com.joezeo.community.pojo.ProxyIPExample;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 代理IP池
 */
@SuppressWarnings("ALL")
@Component
public class ProxyPool {
    @Autowired
    private ProxyIPMapper proxyIPMapper;

    private static List<Proxy> pool = new ArrayList<>(); // 包含http和https代理的代理池
    private static List<Proxy> httpPool = new ArrayList<>();// 只有Http代理的代理池
    private static List<Proxy> httpsPool = new ArrayList<>();// 只有Https// 代理的代理池
    private static boolean isInit = false;

    public void init() {
        if (this.isInit) {
            return;
        }
        ProxyIPExample httpexample = new ProxyIPExample();
        httpexample.createCriteria().andTypeEqualTo("HTTP").andUsableEqualTo(1);
        List<ProxyIP> httpIPS = proxyIPMapper.selectByExample(httpexample);
        initHttpPool(httpIPS);

        ProxyIPExample httpsexample = new ProxyIPExample();
        httpsexample.createCriteria().andTypeEqualTo("HTTPS").andUsableEqualTo(1);
        List<ProxyIP> httpsIPS = proxyIPMapper.selectByExample(httpsexample);
        initHttpsPool(httpsIPS);

        pool.addAll(httpPool);
        pool.addAll(httpsPool);
        Collections.shuffle(pool); // 将pool的代理顺序打乱
        this.isInit = true;
    }


    /**
     * 从代理池中取出http或https代理
     */
    public Proxy getProxy() {
        init();
        Proxy proxy = pool.get(0);
        pool.remove(0);
        return proxy;
    }

    /**
     * 从代理池中取出HTTP代理对象
     */
    public Proxy getHttpProxy() {
        init();
        Proxy proxy = httpPool.get(0);
        httpPool.remove(0);
        return proxy;
    }

    /**
     * 从代理池中取出HTTPS代理对象
     */
    public Proxy getHttpsProxy() {
        init();
        Proxy proxy = httpsPool.get(0);
        httpsPool.remove(0);
        return proxy;
    }

    /**
     * 将代理放回pool代理池中，不区分http/https
     */
    public void closeProxy(Proxy proxy) {
        pool.add(pool.size(), proxy);
    }

    /**
     * 将HTTP代理对象再次放入代理池中
     */
    public void closeHttpProxy(Proxy proxy) {
        httpPool.add(httpPool.size(), proxy);
    }

    /**
     * 将HTTPS代理对象再次放入代理池中
     */
    public void closeHttpsProxy(Proxy proxy) {
        httpsPool.add(httpsPool.size(), proxy);
    }

    /**
     * 禁用不可用的代理ip
     */
    public void disableProxy(Proxy proxy) {
        InetSocketAddress iaddress = (InetSocketAddress) proxy.address();
        String ip = iaddress.getAddress().getHostAddress().split(":")[0].replace("/", "");

        ProxyIPExample example = new ProxyIPExample();
        example.createCriteria().andAddressEqualTo(ip);

        ProxyIP record = new ProxyIP();
        record.setAddress(ip);
        record.setUsable(0);
        record.setGmtModify(System.currentTimeMillis());
        int idx = proxyIPMapper.updateByExampleSelective(record, example);
        if (idx != 1) {
            throw new ServiceException("禁用ip代理失败");
        }
    }

    public boolean checkProxy(Proxy proxy){
        // 通过访问 https://httpbin.org/ip 来判断该代理是否可用

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .proxy(proxy);
        OkHttpClient okHttpClient = builder.build();

        Request req = new Request.Builder()
                .url("https://httpbin.org/ip")
                .build();
        String jsonResult = null;
        try {
            jsonResult = okHttpClient.newCall(req).execute().body().string();
        } catch (IOException e) {
            System.out.println("连接代理服务器超时");
        }
        if (jsonResult == null){
            return false;
        }
        return true;
    }

    /*
    ---------------------------------- private methos ---------------------------------
     */
    /*
    初始化http代理池
     */
    private void initHttpPool(List<ProxyIP> httpIPs) {
        List<Proxy> collect = httpIPs.stream()
                .map(ip -> new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip.getAddress(), ip.getPort())))
                .collect(Collectors.toList());
        httpPool.addAll(collect);
    }

    /*
    初始化https代理池
     */
    private void initHttpsPool(List<ProxyIP> httpsIPs) {
        List<Proxy> collect = httpsIPs.stream()
                .map(ip -> new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip.getAddress(), ip.getPort())))
                .collect(Collectors.toList());
        httpPool.addAll(collect);
    }
}
