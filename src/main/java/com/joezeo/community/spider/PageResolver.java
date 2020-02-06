package com.joezeo.community.spider;

import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.SteamAppInfoMapper;
import com.joezeo.community.mapper.SteamUrlMapper;
import com.joezeo.community.pojo.SteamAppInfo;
import com.joezeo.community.pojo.SteamUrl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PageResolver {

    @Autowired
    private SteamUrlMapper steamUrlMapper;

    @Autowired
    private SteamAppInfoMapper steamAppInfoMapper;

    /**
     * 解析steam搜索页面，获取各个app的url
     * 用于初次初始化,获取全部url
     */
    public void initUrl(String page, String type) {
        Document doc = Jsoup.parse(page);

        // 从doc对象获取数据
        Element content = doc.getElementById("search_resultsRows");
        Elements links = content.getElementsByTag("a");
        Map<String, String> href = links.stream()
                .collect(Collectors.toMap(link -> {
                    String appKey = link.attr("data-ds-itemkey");// ep:App_901583
                    String appid = appKey.substring(appKey.lastIndexOf("_") + 1);
                    return appid;
                }, link -> link.attr("href")));

        int index = steamUrlMapper.insertMap(href, type);
        if (index == href.size()) {
            System.out.println("解析" + type + "成功：存储了" + index + "条数据");
        } else {
            throw new CustomizeException(CustomizeErrorCode.SPIDE_STEAM_URL_ERROR);
        }
    }

    /**
     * 解析steam搜索页面，获取各个app的url
     * 用于日常爬取搜索页面
     * 与数据库中的做对比，如果没有的则插入
     */
    public void dailyUrlCheck(String page, String type) {
        Document doc = Jsoup.parse(page);

        // 从doc对象获取数据
        Element content = doc.getElementById("search_resultsRows");
        Elements links = content.getElementsByTag("a");
        Map<String, String> href = links.stream()
                .collect(Collectors.toMap(link -> {
                    String appKey = link.attr("data-ds-itemkey");// ep:App_901583
                    String appid = appKey.substring(appKey.lastIndexOf("_") + 1);
                    return appid;
                }, link -> link.attr("href")));
        for (Map.Entry<String, String> entry : href.entrySet()) {
            SteamUrl steamUrl = steamUrlMapper.selectByAppid(Integer.parseInt(entry.getKey()), type);
            if (steamUrl == null) {
                // 说明该app地址不存在,存入数据库中
                int index = steamUrlMapper.insert(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 解析app页面，获取软件的详细信息
     * 初始化app info，无需检查
     */
    public void initAppInfo(String page, String type, Integer appid) {
        Document doc = Jsoup.parse(page);

        String appName = "";
        Elements nameDiv = doc.getElementsByClass("apphub_AppName");
        appName = nameDiv.stream().map(name -> name.html()).collect(Collectors.joining());

        String imgSrc = "";
        Elements imgDiv = doc.getElementsByClass("game_header_image_full");
        imgSrc = imgDiv.stream().map(img -> img.attr("src")).collect(Collectors.joining());

        String description = "";
        Elements descDiv = doc.getElementsByClass("game_description_snippet");
        description = descDiv.stream().map(desc -> desc.html()).collect(Collectors.joining());

        String date = "";
        Elements dateDiv = doc.getElementsByClass("date");
        date = dateDiv.stream().map(datediv -> datediv.html()).collect(Collectors.joining());

        String devlop = "";
        Element devpDiv = doc.getElementById("developers_list");
        Elements devps = devpDiv.getElementsByTag("a");
        devlop = devps.stream().map(devp -> devp.html()).collect(Collectors.joining(";"));

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
                        originalPrice = Integer.parseInt(priceStr) / 100;
                        finalPrice = originalPrice;
                        break;
                    }
                }
            }
            break; // 第二个是捆绑包的价格
        }

        SteamAppInfo steamAppInfo = new SteamAppInfo();
        steamAppInfo.setAppid(appid);
        steamAppInfo.setName(appName);
        steamAppInfo.setDescription(description);
        steamAppInfo.setReleaseDate(date);
        steamAppInfo.setDevloper(devlop);
        steamAppInfo.setPublisher(publisher);
        steamAppInfo.setOriginalPrice(originalPrice);
        steamAppInfo.setFinalPrice(finalPrice);
        steamAppInfo.setGmtCreate(System.currentTimeMillis());
        steamAppInfo.setGmtModify(steamAppInfo.getGmtCreate());
        int index = steamAppInfoMapper.insert(steamAppInfo, type);
        if (index != 1) {
            throw new ServiceException("存储app信息失败");
        } else {
            System.out.println("存储app信息成功");
        }
    }

    /**
     * 解析app页面，获取软件的详细信息
     * 日常检查app信息是否存在缺漏，需要从数据库中检查该app是否已经存在
     */
    public void dailyCheckAppInfo(String page, String type, Integer appid) {
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
        Elements descDiv = doc.getElementsByClass("game_description_snippet");
        description = descDiv.stream().map(desc -> desc.html()).collect(Collectors.joining());

        // 发行日期
        String date = "";
        Elements dateDiv = doc.getElementsByClass("date");
        date = dateDiv.stream().map(datediv -> datediv.html()).collect(Collectors.joining());

        // 开发商
        String devlop = "";
        Element devpDiv = doc.getElementById("developers_list");
        Elements devps = devpDiv.getElementsByTag("a");
        devlop = devps.stream().map(devp -> devp.html()).collect(Collectors.joining(";"));

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
                        originalPrice = Integer.parseInt(priceStr) / 100;
                        finalPrice = originalPrice;
                        break;
                    }
                }
            }
            break; // 第二个是捆绑包的价格
        }

        SteamAppInfo steamAppInfo = steamAppInfoMapper.selectByAppid(appid, type);
        if (steamAppInfo == null) { // 该app不存在才存入数据库中
            steamAppInfo = new SteamAppInfo();
            steamAppInfo.setAppid(appid);
            steamAppInfo.setName(appName);
            steamAppInfo.setDescription(description);
            steamAppInfo.setReleaseDate(date);
            steamAppInfo.setDevloper(devlop);
            steamAppInfo.setPublisher(publisher);
            steamAppInfo.setOriginalPrice(originalPrice);
            steamAppInfo.setFinalPrice(finalPrice);
            steamAppInfo.setGmtCreate(System.currentTimeMillis());
            steamAppInfo.setGmtModify(steamAppInfo.getGmtCreate());
            int index = steamAppInfoMapper.insert(steamAppInfo, type);
            if (index != 1) {
                throw new ServiceException("存储app信息失败");
            } else {
                System.out.println("存储app信息成功");
            }
        }
    }
}
