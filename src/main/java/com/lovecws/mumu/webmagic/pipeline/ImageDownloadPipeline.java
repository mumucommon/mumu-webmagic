package com.lovecws.mumu.webmagic.pipeline;

import com.lovecws.mumu.webmagic.util.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * @author babymm
 * @version 1.0-SNAPSHOT
 * @Description: 图片文件下载
 * @date 2017-09-25 11:45
 */
public class ImageDownloadPipeline extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean storeByUrl;//是否按照url路径模式来保存

    public ImageDownloadPipeline() {
        this.setPath("/data/webmagic");
        this.storeByUrl = false;
    }

    public ImageDownloadPipeline(String path) {
        this.setPath(path);
        this.storeByUrl = false;
    }

    public ImageDownloadPipeline(String path, boolean storeByUrl) {
        this.setPath(path);
        this.storeByUrl = storeByUrl;
    }

    @Override
    public void process(final ResultItems resultItems, final Task task) {
        String url = resultItems.getRequest().getUrl();
        Object imagesObject = resultItems.get("images");
        if (imagesObject instanceof Set) {
            Set<String> imageList = (Set<String>) imagesObject;
            for (String image : imageList) {
                //文件路径
                String filePath = getPath();
                //String fileName = UUID.randomUUID().toString().replace("-", "") + image.substring(image.lastIndexOf("."));
                String fileName = DigestUtils.md5Hex(image) + image.substring(image.lastIndexOf("."));
                if (storeByUrl) {
                    filePath = image.substring(image.indexOf("/") + 1, image.lastIndexOf("/") + 1);
                    fileName = image.substring(image.lastIndexOf("/") + 1);
                }
                //文件名称
                try {
                    File file = getFile(filePath + fileName);
                    if (file.exists()) {
                        continue;
                    }
                    FileOutputStream out = new FileOutputStream(file);
                    CloseableHttpResponse response = HttpClientUtil.response(image);
                    out.write(EntityUtils.toByteArray(response.getEntity()));
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
