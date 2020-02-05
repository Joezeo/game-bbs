package com.joezeo.community.spider;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PageGetter {
    private OkHttpClient client = new OkHttpClient();

    @Autowired
    private PageResolver pageResolver;

    public void spiderAsyn(String url, String type) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("爬取网页失败 - 可能等待时间过长 - 将再次重试");
                try {
                    call.execute();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String page = response.body().string();
                System.out.println("spide page done ： " + url);
                pageResolver.resolver(page, type);
            }
        });
    }
}
