package com.lovecws.mumu.webmagic.processor.news;

import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @author babymm
 * @version 1.0-SNAPSHOT
 * @Description: 新浪新闻数据爬取
 * @date 2017-09-22 8:49
 */
public class SinaNewsPageProcessor implements PageProcessor {

    private static final String[] DEFAULT_PAGE_PATTERNS = new String[]{".shtml", ".html"};

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(1000);
    private String[] urlPatterns = null;
    private String[] pagePatterns = null;

    /**
     * @param urlPatterns url 规则
     */
    public SinaNewsPageProcessor(final String[] urlPatterns) {
        this.urlPatterns = urlPatterns(urlPatterns);
        this.pagePatterns = DEFAULT_PAGE_PATTERNS;
    }

    /**
     * @param urlPatterns  url 规则
     * @param pagePatterns page 规则（.htmls、.html、.htm）
     */
    public SinaNewsPageProcessor(final String[] urlPatterns, String[] pagePatterns) {
        this.urlPatterns = urlPatterns(urlPatterns);
        this.pagePatterns = pagePatterns;
    }

    private String[] urlPatterns(final String[] urlPatterns) {
        if (urlPatterns == null) {
            throw new RuntimeException();
        }
        String[] newUrlPatterns = new String[urlPatterns.length];
        for (int i = 0; i < urlPatterns.length; i++) {
            String urlPattern = urlPatterns[i];
            if (!urlPattern.endsWith("/")) {
                urlPattern = urlPattern + "/";
            }
            urlPattern = urlPattern.replace(".", "\\.");
            urlPattern = urlPattern + "[A-Za-z0-9_/\\.]+";
            newUrlPatterns[i] = urlPattern;
        }
        return newUrlPatterns;
    }

    @Override
    public void process(final Page page) {
        //爬取该页面下的链接
        for (String url : urlPatterns) {
            page.addTargetRequests(page.getHtml().links().regex("(" + url + ")").all());
        }
        String url = page.getUrl().get();
        //过滤规则
        if (pagePatterns != null) {
            boolean skip = true;
            for (String pagePattern : pagePatterns) {
                if (url.contains(pagePattern)) {
                    skip = false;
                }
            }
            if (skip) {
                page.setSkip(true);
            }
        }
        //页面相同的属性
        page.putField("htitle", page.getHtml().xpath("//title/text()").get());
        page.putField("keywords", page.getHtml().xpath("//meta[@name='keywords']/@content").get());
        page.putField("description", page.getHtml().xpath("//meta[@name='description']/@content").get());
        page.putField("url", page.getUrl().get());

        //不同分类不同页面属性
        if (url.contains("finance")) {
            sinaNewsFinanceProcessor(page);
        }
        //开启页面属性过滤阶段
        Object content = page.getResultItems().get("content");
        if (content == null || "".equalsIgnoreCase(content.toString().trim())) {
            page.setSkip(true);
        }
    }

    /**
     * 新浪金融新闻
     *
     * @param page
     */
    private void sinaNewsFinanceProcessor(final Page page) {
        //抽取文章摘要
        String sumary = page.getHtml().xpath("//div[@id=artibody]/p/text()").get();
        if (sumary == null || sumary.length() < 20) {
            sumary = page.getHtml().xpath("//div[@id=artibody]/p[1]/text()").get();
        }
        page.putField("sumary", sumary);
        List<String> contents = page.getHtml().xpath("//div[@id=artibody]/p").all();
        page.putField("content", StringUtils.join(contents, ""));
        page.putField("logo", page.getHtml().xpath("//div[@id=artibody]//img/@src").get());
        String title = page.getHtml().xpath("//h1[@id=artibodyTitle]/text()").get();
        if (title == null) {
            title = page.getHtml().xpath("//h1[@id=artibodyTitle]/h1/text()").get();
        }
        page.putField("title", title);

        //新闻详情 包括新闻时间、新闻来源、新闻来源地址
        String artInfo = page.getHtml().xpath("//div[@class=artInfo]").get();
        String pageInfo = page.getHtml().xpath("//div[@class=page-info]").get();
        String from_info = page.getHtml().xpath("//div[@class=from_info]").get();
        if (artInfo != null && !"".equalsIgnoreCase(artInfo)) {
            page.putField("pubDate", page.getHtml().xpath("//span[@id=pub_date]/text()").get());
            String mediaName = page.getHtml().xpath("//span[@id=media_name]/text()").get();
            if (mediaName == null || "".equals(mediaName.trim())) {
                mediaName = page.getHtml().xpath("//span[@id=media_name]/a/text()").get();
            }
            page.putField("mediaName", mediaName);
            page.putField("mediaUrl", page.getHtml().xpath("//span[@id=media_name]/a/@href").get());
        } else if (pageInfo != null && !"".equalsIgnoreCase(pageInfo)) {
            page.putField("pubDate", page.getHtml().xpath("//span[@class=time-source]/text()").get());
            page.putField("mediaName", page.getHtml().xpath("//span[@class=time-source]/span/a/text()").get());
            page.putField("mediaUrl", page.getHtml().xpath("//span[@class=time-source]/span/a/@href").get());
        } else if (from_info != null && !"".equalsIgnoreCase(from_info)) {
            String pubDate = page.getHtml().xpath("//span[@class=from_info]/text()").get();
            pubDate = pubDate.replace("http://www.sina.com.cn", "").trim();
            page.putField("pubDate", pubDate);
            page.putField("mediaName", page.getHtml().xpath("//span[@class=from_info]/span/a/text()").get());
            page.putField("mediaUrl", page.getHtml().xpath("//span[@class=from_info]/span/a/@href").get());
        }
        page.putField("category", "finance");
        String url = page.getUrl().get();
        int firstIndex = url.indexOf("/", 10);
        int secondIndex = url.indexOf("/", firstIndex + 1);
        page.putField("type", url.substring(firstIndex + 1, secondIndex));
    }

    @Override
    public Site getSite() {
        return site;
    }
}
