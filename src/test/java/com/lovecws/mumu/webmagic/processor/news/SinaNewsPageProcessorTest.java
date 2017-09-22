package com.lovecws.mumu.webmagic.processor.news;

import com.lovecws.mumu.webmagic.pipeline.MultiJsonFilePipeline;
import com.lovecws.mumu.webmagic.util.HttpClientUtil;
import org.junit.Test;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.selector.Html;

/**
 * @author babymm
 * @version 1.0-SNAPSHOT
 * @Description: 新浪新闻测试
 * @date 2017-09-22 11:34
 */
public class SinaNewsPageProcessorTest {
    private String url = "http://finance.sina.com.cn/stock/hyyj/20150109/180621264045.shtml";

    @Test
    public void title() {
        String page = HttpClientUtil.get(url);
        Html html = new Html(page, url);
        //System.out.println(html);
        String title = html.xpath("//h1[@id=artibodyTitle]/text()").get();
        System.out.println(title);
    }

    @Test
    public void content() {
        url = "http://finance.sina.com.cn/china/gncj/2017-09-21/doc-ifymenmt5999177.shtml";
        String page = HttpClientUtil.get(url);
        Html html = new Html(page, url);
        String sumary = html.xpath("//div[@id=artibody]/p").get();
        System.out.println(sumary);

        String content = html.xpath("//div[@id=artibody]/p").all().toString();
        System.out.println(content);

        String img = html.xpath("//div[@id=artibody]//img/@src").get();
        System.out.println(img);
    }

    @Test
    public void artInfo() {
        url = "http://finance.sina.com.cn/money/future/fmnews/20140119/234018004041.shtml";
        String page = HttpClientUtil.get(url);
        Html html = new Html(page, url);
        String pubDate = html.xpath("//span[@id=pub_date]/text()").get();
        System.out.println(pubDate);

        String mediaName = html.xpath("//span[@id=media_name]/text()").get();
        if (mediaName == null || "".equals(mediaName.trim())) {
            mediaName = html.xpath("//span[@id=media_name]/a/text()").get();
        }
        System.out.println(mediaName);

        String mediaUrl = html.xpath("//span[@id=media_name]/a/@href").get();
        System.out.println(mediaUrl);
    }

    @Test
    public void pageInfo() {
        url = "http://finance.sina.com.cn/china/gncj/2017-09-21/doc-ifymenmt5999177.shtml";
        url = "http://finance.sina.com.cn/money/future/fmnews/2017-09-21/doc-ifymenmt5990125.shtml";
        url = "http://finance.sina.com.cn/stock/hyyj/2017-09-22/doc-ifymfcih2507213.shtml";
        String page = HttpClientUtil.get(url);
        Html html = new Html(page, url);
        String pubDate = html.xpath("//span[@class=time-source]/text()").get();
        System.out.println(pubDate);

        String mediaName = html.xpath("//span[@class=time-source]/span/a/text()").get();
        System.out.println(mediaName);

        String mediaUrl = html.xpath("//span[@class=time-source]/span/a/@href").get();
        System.out.println(mediaUrl);
    }

    @Test
    public void newsType() {
        //url = "http://finance.sina.com.cn/china/gncj/2017-09-21/doc-ifymenmt5999177.shtml";
        int firstIndex = url.indexOf("/", 10);
        int secondIndex = url.indexOf("/", firstIndex + 1);
        System.out.println(url.substring(firstIndex + 1, secondIndex));
    }

    @Test
    public void finance() {
        Spider spider = Spider.create(new SinaNewsPageProcessor(new String[]{"http://finance.sina.com.cn"}))
                .addUrl("http://finance.sina.com.cn")
                .addPipeline(new MultiJsonFilePipeline("d:/data/webmagic", 100))
                .thread(5);
        spider.run();
    }

    /**
     * 新浪财经 股票
     */
    @Test
    public void financeStock() {
        String url = "http://finance.sina.com.cn/stock/";
        Spider spider = Spider.create(new SinaNewsPageProcessor(new String[]{url}))
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }

    /**
     * 新浪财经 基金
     */
    @Test
    public void financeFund() {
        String url = "http://finance.sina.com.cn/fund/";
        Spider spider = Spider.create(new SinaNewsPageProcessor(new String[]{url}))
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }
    
    /**
     * 新浪财经 理财
     */
    @Test
    public void financeMoney() {
        String url = "http://finance.sina.com.cn/money/";
        Spider spider = Spider.create(new SinaNewsPageProcessor(new String[]{url}))
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }
}
