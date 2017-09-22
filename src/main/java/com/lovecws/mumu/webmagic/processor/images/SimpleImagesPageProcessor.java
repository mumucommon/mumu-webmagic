package com.lovecws.mumu.webmagic.processor.images;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author babymm
 * @version 1.0-SNAPSHOT
 * @Description: 基本图片抓取
 * @date 2017-09-22 13:48
 */
public class SimpleImagesPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(1000);
    private static final String[] DEFAULT_IMAGE_PATTERNS = new String[]{"jpg", "png", "jif", "bmp", "jpeg", "tiff"};

    private String[] urlPatterns = null;
    private String[] pagePatterns = null;

    public SimpleImagesPageProcessor(final String[] urlPatterns) {
        this.urlPatterns = urlPatterns(urlPatterns);
        this.pagePatterns = DEFAULT_IMAGE_PATTERNS;
    }

    public SimpleImagesPageProcessor(final String[] urlPatterns, final String[] pagePatterns) {
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
        for (String urlPattern : this.urlPatterns) {
            page.addTargetRequests(page.getHtml().links().regex("(" + urlPattern + ")").all());
        }
        List<String> images = page.getHtml().xpath("//img/@src").all();
        if (images != null) {
            List<String> imagesList = new ArrayList<String>();
            for (String image : images) {
                if (!image.contains("HTTP") && !image.contains("http")) {
                    continue;
                }
                if (pagePatterns != null) {
                    //获取到文件的类型
                    int lastIndexOf = image.lastIndexOf(".");
                    String fileType = "";
                    if (lastIndexOf > -1) {
                        fileType = image.substring(lastIndexOf + 1);
                    }
                    boolean isOK = false;
                    for (String pagePattern : pagePatterns) {
                        if (pagePattern.equalsIgnoreCase(fileType)) {
                            isOK = true;
                            break;
                        }
                    }
                    if (!isOK) {
                        continue;
                    }
                }
                imagesList.add(image);
            }
            page.putField("images", imagesList);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
