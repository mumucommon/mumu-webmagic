package com.lovecws.mumu.webmagic.downloader;

import com.lovecws.mumu.webmagic.util.HttpClientUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;

import java.io.IOException;

/**
 * @author babymm
 * @version 1.0-SNAPSHOT
 * @Description: 自定义下载器
 * @date 2017-09-22 13:30
 */
public class HttpClientDownloader extends us.codecraft.webmagic.downloader.HttpClientDownloader {

    @Override
    public Page download(final Request request, final Task task) {
        if (task != null && task.getSite() != null) {
            CloseableHttpResponse httpResponse = HttpClientUtil.response(request.getUrl());
            if(httpResponse==null){
                httpResponse = HttpClientUtil.response(request.getUrl());
            }
            try {
                Page page = this.handleResponse(request, request.getCharset() != null ? request.getCharset() : task.getSite().getCharset(), httpResponse, task);
                this.onSuccess(request);
                return page;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
