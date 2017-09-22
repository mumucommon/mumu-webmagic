package com.lovecws.mumu.webmagic.processor.images;

import com.lovecws.mumu.webmagic.downloader.HttpClientDownloader;
import org.junit.Test;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

/**
 * @author babymm
 * @version 1.0-SNAPSHOT
 * @Description: 百度图片抓取
 * @date 2017-09-22 12:45
 */
public class SimpleImagesPageProcessorTest {

    @Test
    public void baidu() {
        Spider spider = Spider.create(new SimpleImagesPageProcessor(new String[]{"https://image.baidu.com/"}))
                .addUrl("https://image.baidu.com")
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }

    @Test
    public void umei() {
        Spider spider = Spider.create(new SimpleImagesPageProcessor(new String[]{"http://www.umei.cc"}))
                .addUrl("http://www.umei.cc")
                .setDownloader(new HttpClientDownloader())
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }

    @Test
    public void mm131() {
        String url = "http://www.mm131.com/";
        Spider spider = Spider.create(new SimpleImagesPageProcessor(new String[]{url}))
                .addUrl(url)
                .setDownloader(new HttpClientDownloader())
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }

    @Test
    public void mm27270() {
        String url = "http://www.27270.com/";
        Spider spider = Spider.create(new SimpleImagesPageProcessor(new String[]{url}))
                .addUrl(url)
                .setDownloader(new HttpClientDownloader())
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }

    @Test
    public void mm7160() {
        String url = "http://www.7160.com/";
        Spider spider = Spider.create(new SimpleImagesPageProcessor(new String[]{url}))
                .addUrl(url)
                .setDownloader(new HttpClientDownloader())
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }

    @Test
    public void yesky() {
        String url = "http://pic.yesky.com/";
        Spider spider = Spider.create(new SimpleImagesPageProcessor(new String[]{url}))
                .addUrl(url)
                .setDownloader(new HttpClientDownloader())
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
    }

    @Test
    public void mmonly() {
        String url = "http://www.mmonly.cc/";
        Spider spider = Spider.create(new SimpleImagesPageProcessor(new String[]{url}))
                .addUrl(url)
                .setDownloader(new HttpClientDownloader())
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
        spider.getPageCount();
    }

    @Test
    public void feizl() {
        String url = "http://www.feizl.com/meinv/";
        Spider spider = Spider.create(new SimpleImagesPageProcessor(new String[]{url}))
                .addUrl(url)
                .setDownloader(new HttpClientDownloader())
                .addPipeline(new ConsolePipeline())
                .thread(5);
        spider.run();
        spider.getPageCount();
    }
}
