package com.joezeo.community.spider;

import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.*;
import com.joezeo.community.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class PageResolver {

    @Autowired
    private SteamUrlMapper steamUrlMapper;
    @Autowired
    private SteamAppInfoMapper steamAppInfoMapper;
    @Autowired
    private SteamSubInfoMapper steamSubInfoMapper;
    @Autowired
    private SteamHistoryPriceMapper steamHistoryPriceMapper;
    @Autowired
    private ProxyIPMapper proxyIPMapper;

    /**
     * 解析获取Steam各个搜索页的总页数
     */
    public int resolvSteamTotalPage(String page) {
        Document doc = Jsoup.parse(page);
        Elements searchDivs = doc.getElementsByClass("search_pagination_right");
        String pageStr = "";
        for (Element e : searchDivs) {
            Elements as = e.getElementsByTag("a");
            int i = 0;
            for (Element a : as) {
                if (i == 2) {
                    pageStr = a.html();
                    break;
                }
                i++;
            }
        }
        return Integer.parseInt(pageStr);
    }

    /**
     * 解析获取西刺代理页面的总页数
     */
    public int resolvIPTotalPage(String string) {
        Document doc = Jsoup.parse(string);

        Elements paginations = doc.getElementsByClass("pagination");
        int totalPage = 0;
        for (Element e : paginations) {
            Elements as = e.getElementsByTag("a");
            int index = 0;
            for (Element a : as) {
                if (index == 9) {
                    totalPage = Integer.parseInt(a.html());
                }
                index++;
            }
            break;
        }
        return totalPage;
    }

    /**
     * 解析西刺代理网页的代理ip
     */
    public void resolvProxyIP(String page) {
        Document doc = Jsoup.parse(page);

        String ip = "";
        Integer port = 0;
        String type = "";
        Double speed = 0d;
        Double connectTime = 0d;
        String survive = "";

        Element ipList = doc.getElementById("ip_list");
        Elements trs = ipList.getElementsByTag("tr");
        for (Element tr : trs) {
            Elements tds = tr.getElementsByTag("td");
            int index = 0;
            for (Element td : tds) {
                if (index == 1) { // ip地址
                    ip = td.html();
                } else if (index == 2) { // 端口号
                    port = Integer.parseInt(td.html());
                } else if (index == 5) { // 类型 HTTP/HTTPS
                    type = td.html();
                } else if (index == 6) { // 速度
                    Elements divs = td.getElementsByTag("div");
                    Element e = divs.get(0);
                    String speedStr = e.attr("title").replace("秒", "");
                    speed = Double.parseDouble(speedStr);
                } else if (index == 7) { //
                    Elements divs = td.getElementsByTag("div");
                    Element e = divs.get(0);
                    String connectStr = e.attr("title").replace("秒", "");
                    connectTime = Double.parseDouble(connectStr);
                } else if (index == 8) {
                    survive = td.html();
                }
                index++;
            }

            if (speed < 1 && connectTime < 1 && survive.indexOf("天") != -1) {// 速度小于1秒，连接时间小于1秒，存活天数至少为1天的才做记录
                // 查询此ip在数据库是否存在
                ProxyIPExample example = new ProxyIPExample();
                example.createCriteria().andAddressEqualTo(ip);
                List<ProxyIP> proxyIPS = proxyIPMapper.selectByExample(example);
                if (proxyIPS == null || proxyIPS.size() == 0) {// 如果不存在再插入此条代理ip
                    ProxyIP proxyip = new ProxyIP();
                    proxyip.setAddress(ip);
                    proxyip.setPort(port);
                    proxyip.setType(type);
                    proxyip.setSpeed(speed);
                    proxyip.setConnecttime(connectTime);
                    proxyip.setSurvive(survive);
                    proxyip.setGmtCreate(System.currentTimeMillis());
                    proxyip.setGmtModify(proxyip.getGmtCreate());
                    int idx = proxyIPMapper.insert(proxyip);
                    if (idx != 1) {
                        log.error("插入代理ip失败");
                    }
                }
            }
        }
    }

    /**
     * 解析特惠商品的价格，存入t_steam_history_price表中
     */
    public void dailySpideSpecialPrice(String page, Integer appid) {
        Document doc = Jsoup.parse(page);

        Integer finalPrice = 0;
        Elements purchaseGameDiv = doc.getElementsByClass("game_area_purchase_game");
        for (Element ele : purchaseGameDiv) {
            Elements finDiv = ele.getElementsByClass("discount_final_price");
            if (finDiv.size() != 0) { // 降价了
                for (Element subele : finDiv) {
                    String finalPriceStrWithTag = subele.html();
                    String finalPriceStr = finalPriceStrWithTag.substring(finalPriceStrWithTag.lastIndexOf(" ") + 1);
                    finalPrice = Integer.parseInt(finalPriceStr);
                    break;
                }
            }
        }
        SteamHistoryPrice historyPrice = new SteamHistoryPrice();
        historyPrice.setAppid(appid);
        historyPrice.setPrice(finalPrice);
        historyPrice.setGmtCreate(System.currentTimeMillis());
        int index = steamHistoryPriceMapper.insert(historyPrice);
        if (index != 1) {
            log.error("存储特惠价格失败，appid=" + appid);
        }
    }

    /**
     * 解析steam搜索页面，获取各个app的url
     * 用于日常/初始化爬取搜索页面
     * 与数据库中的做对比，如果没有的则插入
     */
    public void initOrCheckUrl(String page, String type) {
        Document doc = Jsoup.parse(page);

        // 从doc对象获取数据
        Element content = doc.getElementById("search_resultsRows");
        Elements links = content.getElementsByTag("a");
        Map<String, String> href = links.stream()
                .collect(Collectors.toMap(link -> {
                    String appKey = link.attr("data-ds-itemkey");// ep:App_901583
                    String appid = appKey.substring(appKey.lastIndexOf("_") + 1);
                    return appid;
                }, link -> {
                    String url = link.attr("href");
                    return url.substring(0, url.lastIndexOf("?"));
                }));
        // steam上每天搜索页的app都会加上一个不同的参数，如?snr=1_7_7_230_150_1364，存储时去掉这个参数
        if (!"special".equals(type)) { // 当前并非存储特惠商品的url
            for (Map.Entry<String, String> entry : href.entrySet()) {
                List<SteamUrl> urlList = steamUrlMapper.selectByAppid(Integer.parseInt(entry.getKey()), type);
                if (urlList == null || urlList.size() == 0) {
                    // 说明该app地址不存在,存入数据库中
                    int index = steamUrlMapper.insert(entry.getKey(), entry.getValue(), type);
                } else if (urlList.size() == 1) {
                    String memUrl = urlList.get(0).getUrl();
                    String newUrl = entry.getValue();
                    if (!memUrl.equals(newUrl)) { // 因为steam的礼包（sub）和软件（app）的appid有可能相同,但是url不同
                        int index = steamUrlMapper.insert(entry.getKey(), entry.getValue(), type);
                        if (index != 1) {
                            log.error("存储App Url失败,appid=" + entry.getKey());
                        }
                    }
                }
            }
        } else { // 存储特惠商品的url
            // 清空 t_steam_special_url
            int idx = steamAppInfoMapper.emptySpecialUrl();
            if(idx < 0){
                log.error("数据库发生异常");
            }

            for (Map.Entry<String, String> entry : href.entrySet()) {
                int index = steamUrlMapper.insert(entry.getKey(), entry.getValue(), type);
                if (index != 1) {
                    log.error("存储特惠商品url失败,appid=" + entry.getKey());
                }
            }
        }
    }

    /**
     * 解析app页面，获取软件的详细信息
     * 初始化/日常检查app信息是否存在缺漏，需要从数据库中检查该app是否已经存在
     *
     * @Param isSub 该页面是否是关于礼包的
     */
    public void initOrCheckAppInfo(String page, String type, Integer appid, boolean isSub) {
        if (isSub) {
            resolvSub(page, appid);
            return;
        }

        Document doc = Jsoup.parse(page);

        // 软件名称
        String appName = "";
        Elements nameDiv = doc.getElementsByClass("apphub_AppName");
        appName = nameDiv.stream().map(name -> name.html()).collect(Collectors.joining());

        // 介绍图地址
        String imgSrc = "";
        Elements imgDiv = doc.getElementsByClass("game_header_image_full");
        imgSrc = imgDiv.stream().map(img -> img.attr("src")).collect(Collectors.joining());

        // 软件简介
        String description = "";
        if ("dlc".equals(type)) { // dlc 的简介被另外class修饰
            Elements elements = doc.getElementsByClass("glance_details");
            for (Element ele : elements) {
                Elements ps = ele.getElementsByTag("p");
                for (Element p : ps) {
                    description = p.html();
                    break;
                }
                break;
            }
        } else {
            Elements descDiv = doc.getElementsByClass("game_description_snippet");
            description = descDiv.stream().map(desc -> desc.html()).collect(Collectors.joining());
        }

        // 发行日期
        String date = "";
        Elements dateDiv = doc.getElementsByClass("date");
        date = dateDiv.stream().map(datediv -> datediv.html()).collect(Collectors.joining());

        // 开发商
        String devlop = "";
        Element devpDiv = doc.getElementById("developers_list");
        if (devpDiv != null) {
            Elements devps = devpDiv.getElementsByTag("a");
            if (devps != null) {
                devlop = devps.stream().map(devp -> devp.html()).collect(Collectors.joining(";"));
            }
        }

        // 发行商
        String publisher = "";
        Elements divs = doc.getElementsByClass("summary column");
        for (Element div : divs) {
            Elements as = div.getElementsByTag("a");
            // 发行商刚好是最后一个
            if (as.size() != 0) { // 有些软件没有发行商
                publisher = as.stream().map(a -> a.html()).collect(Collectors.joining());
            } else {
                publisher = "";
            }
        }

        Integer originalPrice = 0;
        Integer finalPrice = 0;
        Elements purchaseGameDiv = doc.getElementsByClass("game_area_purchase_game");
        for (Element ele : purchaseGameDiv) {
            Elements oriDiv = ele.getElementsByClass("discount_original_price");
            Elements finDiv = ele.getElementsByClass("discount_final_price");
            if (oriDiv.size() != 0) { // 降价了
                for (Element subele : oriDiv) {
                    String oriPriceStrWithTag = subele.html();
                    String oriPriceStr = oriPriceStrWithTag.substring(oriPriceStrWithTag.lastIndexOf(" ") + 1);
                    originalPrice = Integer.parseInt(oriPriceStr);
                    break;
                }
                for (Element subele : finDiv) {
                    String finalPriceStrWithTag = subele.html();
                    String finalPriceStr = finalPriceStrWithTag.substring(finalPriceStrWithTag.lastIndexOf(" ") + 1);
                    finalPrice = Integer.parseInt(finalPriceStr);
                    break;
                }
            } else { // 没有降价
                Elements priceDiv = ele.getElementsByClass("game_purchase_price price");
                for (Element subele : priceDiv) {
                    if (subele.html() == null || "免费游玩".equals(subele.html())
                            || "免费开玩".equals(subele.html())
                            || "免费".equals(subele.html())
                            || "".equals(subele.html())) {
                        originalPrice = 0;
                        finalPrice = 0;
                        break;
                    } else {
                        String priceStr = subele.attr("data-price-final");
                        if ("".equals(priceStr)) {
                            originalPrice = 0;
                            finalPrice = 0;
                            break;
                        } else {
                            originalPrice = Integer.parseInt(priceStr) / 100;
                            finalPrice = originalPrice;
                            break;
                        }
                    }
                }
            }
            break; // 第二个是捆绑包的价格
        }

        // 用户评测
        String summary = "";
        Elements summryDivs = doc.getElementsByClass("user_reviews_summary_row");
        summary = summryDivs.stream()
                .map(div -> div.attr("data-tooltip-html"))
                .collect(Collectors.joining("|"));

        SteamAppInfo steamAppInfo = steamAppInfoMapper.selectByAppid(appid, type);
        if (steamAppInfo == null || steamAppInfo.getAppid() == null) { // 该app不存在才存入数据库中
            steamAppInfo = new SteamAppInfo();
            steamAppInfo.setAppid(appid);
            steamAppInfo.setName(appName);
            steamAppInfo.setImgUrl(imgSrc);
            steamAppInfo.setDescription(description);
            steamAppInfo.setReleaseDate(date);
            steamAppInfo.setDevloper(devlop);
            steamAppInfo.setPublisher(publisher);
            steamAppInfo.setOriginalPrice(originalPrice);
            steamAppInfo.setFinalPrice(finalPrice);
            steamAppInfo.setSummary(summary);
            steamAppInfo.setGmtCreate(System.currentTimeMillis());
            steamAppInfo.setGmtModify(steamAppInfo.getGmtCreate());
            int index = steamAppInfoMapper.insert(steamAppInfo, type);
            if (index != 1) {
                log.error("存储app信息失败,appid=" + appid);
            } else {
                log.info("存储app信息成功,appid=" + appid);
            }
        }
    }

    /*
            解析礼包信息
     */
    private void resolvSub(String page, Integer appid) {
        Document doc = Jsoup.parse(page);

        String name = "";
        Elements nameDivs = doc.getElementsByClass("page_title_area game_title_area");
        for (Element e : nameDivs) {
            Elements h2s = e.getElementsByTag("h2");
            for (Element h2 : h2s) {
                name = h2.html();
            }
        }

        String developer = "";
        String publisher = "";
        String date = "";
        Elements detailDiv = doc.getElementsByClass("details_block");
        for (Element ele : detailDiv) {
            Elements bs = ele.getElementsByTag("b");
            for (Element b : bs) {
                if ("开发商:".equals(b.html())) {
                    while (true) {
                        Element a = b.nextElementSibling();
                        if ("a".equals(a.tagName())) {
                            developer += a.html() + ",";
                        } else {
                            break;
                        }
                        b = a;
                    }
                } else if ("发行商:".equals(b.html())) {
                    while (true) {
                        Element a = b.nextElementSibling();
                        if ("a".equals(a.tagName())) {
                            publisher += a.html() + ",";
                        } else {
                            break;
                        }
                        b = a;
                    }
                } else if ("发行日期:".equals(b.html())) {
                    date = b.nextSibling().toString().trim();
                }
            }
        }
        // 去掉最后一个逗号
        if (!"".equals(developer)) {
            developer.substring(0, developer.length() - 1);
        }
        if (!"".equals(publisher)) {
            publisher.substring(0, publisher.length() - 1);
        }

        Integer price = 0;
        Elements priceDivs = doc.getElementsByClass("game_purchase_price price");
        for (Element e : priceDivs) {
            price = Integer.parseInt(e.attr("data-price-final")) / 100;
        }

        String contains = "";
        Elements containsDivs = doc.getElementsByClass("tab_item");
        contains = containsDivs.stream()
                .map(div -> {
                    String appKey = div.attr("data-ds-itemkey"); // 如App_203510
                    String subappid = appKey.substring(appKey.lastIndexOf("_") + 1); // 取出后面的数字
                    return subappid;
                }).collect(Collectors.joining(","));


        SteamSubInfo steamSubInfo = steamSubInfoMapper.selectByAppid(appid);
        if (steamSubInfo == null || steamSubInfo.getAppid() == null) { // 数据库中没有该礼包信息，则插入
            steamSubInfo = new SteamSubInfo();
            steamSubInfo.setAppid(appid);
            steamSubInfo.setName(name);
            steamSubInfo.setDeveloper(developer);
            steamSubInfo.setPublisher(publisher);
            steamSubInfo.setDate(date);
            steamSubInfo.setOriginalPrice(price);
            steamSubInfo.setFinalPrice(price);
            steamSubInfo.setContains(contains);
            steamSubInfo.setGmtCreate(System.currentTimeMillis());
            steamSubInfo.setGmtModify(steamSubInfo.getGmtCreate());
            int index = steamSubInfoMapper.insert(steamSubInfo);
            if (index != 1) {
                log.error("插入steam礼包信息失败,appid" + appid);
            }
        }

    }

}
