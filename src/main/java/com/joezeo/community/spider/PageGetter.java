package com.joezeo.community.spider;

import com.joezeo.community.enums.SpiderJobTypeEnum;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PageGetter {
    @Autowired
    private OkHttpClient client4Steam;
    @Autowired
    private ProxyPool proxyPool;
    @Autowired
    private PageResolver pageResolver;
    @Autowired
    private SpiderComponent spiderComponent;

    /**
     * 初始化OkHttpClient
     * 设置读取，请求时间
     * 设置拦截器
     * 设置代理
     */
    private OkHttpClient initClient(boolean useProxy) {
        Proxy proxy = null;
        if (useProxy) {
            boolean usable = false;
            while(!usable){
                proxy = proxyPool.getProxy();//获取代理
                usable = proxyPool.checkProxy(proxy);
                if (usable) {
                    proxyPool.closeProxy(proxy);//代理池回收代理,放回代理池尾部
                } else {
                    proxyPool.disableProxy(proxy);
                }
            }
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60 * 10, TimeUnit.SECONDS)
                .writeTimeout(60 * 10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .proxy(proxy)
                .addInterceptor(chain -> {
                    // 设置延迟时间delay 防止ip被ban
                    int delay = 500;
                    try {
                        System.out.println("爬虫 delay 500毫秒");
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return chain.proceed(chain.request());
                });
        OkHttpClient okHttpClient = builder.build();

        return okHttpClient;
    }

    public void spiderUrlAsyn(String url, String type, Integer appid, SpiderJobTypeEnum jobType) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .addHeader("cookie", "birthtime=470678401;steamCountry=CN%7C09aa4598793d568e5132e0ae6eed2b3a;sessionid=c2bef61856aa2dfff43f0946;steamLoginSecure=76561198102551656%7C%7C881F186195D68D0AB7108296B327ECA75E813EDB;steamMachineAuth76561198102551656=52E8A01B3441E245A9CB20640EBB584F6C0F8377;app_impressions=304212@1_7_7_2300_150_1|1167450@1_7_7_2300_150_1|625960@1_7_7_2300_150_1|227300@1_7_7_2300_150_1|994280@1_7_7_2300_150_1|1146630@1_7_7_2300_150_1|558244@1_7_7_2300_150_1|1037020@1_7_7_2300_150_1|570770@1_7_7_2300_150_1|548570@1_7_7_2300_150_1|531130@1_7_7_2300_150_1|227310@1_7_7_2300_150_1|925580@1_7_7_2300_150_1|241260@1_7_7_2300_150_1|526160@1_7_7_2300_150_1|671510@1_7_7_2300_150_1|1056760@1_7_7_2300_150_1|724970@1_7_7_2300_150_1|644930@1_7_7_2300_150_1") // 认证年龄
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9") // 设置语言
                .get()
                .build();

        Call call = client4Steam.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("爬取网页失败 - 可能等待时间过长 - 将再次重试：" + url);
                spiderComponent.add2Fail(url, type);
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String page = response.body().string();
                System.out.println("获取页面成功 ： " + url);

                if (jobType == SpiderJobTypeEnum.INIT_URL_DATA) {
                    pageResolver.initOrCheckUrl(page, type);
                } else if (jobType == SpiderJobTypeEnum.DAILY_CHECK_URL) {
                    pageResolver.initOrCheckUrl(page, type);
                } else if (jobType == SpiderJobTypeEnum.INIT_APP_INFO) {
                    pageResolver.initOrCheckAppInfo(page, type, appid, url.lastIndexOf("/sub/") != -1);
                } else if (jobType == SpiderJobTypeEnum.DAILY_CHECK_APP_INFO) {
                    pageResolver.initOrCheckAppInfo(page, type, appid, url.lastIndexOf("/sub/") != -1);
                } else if (jobType == SpiderJobTypeEnum.DAILY_SPIDE_SPECIAL_PRICE) {
                    pageResolver.dailySpideSpecialPrice(page, appid);
                }
            }
        });
    }

    public int getSteamTotalPage(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .addHeader("cookie", "birthtime=470678401") // 认证年龄
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9") // 设置语言
                .get()
                .build();

        Call call = client4Steam.newCall(request);
        // 这里不能使用异步获取，因为这里获得的总页数在接下来的逻辑中是有用的
        try {
            Response response = call.execute();
            int totalPage = pageResolver.resolvSteamTotalPage(response.body().string());
            return totalPage;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getIPTotalPage(String url) {
        OkHttpClient okHttpClient = initClient(true);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        // 这里不能使用异步获取，因为这里获得的总页数在接下来的逻辑中是有用的
        try {
            Response response = call.execute();
            int totalPage = pageResolver.resolvIPTotalPage(response.body().string());
            return totalPage;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void getProxyIpPage(String url) {
        OkHttpClient okHttpClient = initClient(true);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String page = response.body().string();
                pageResolver.resolvProxyIP(page);
            }
        });
    }
}
