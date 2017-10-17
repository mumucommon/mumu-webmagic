package com.lovecws.mumu.webmagic.pipeline;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author babymm
 * @version 1.0-SNAPSHOT
 * @Description: 将多个新闻数据保存在一个json文件中
 * @date 2017-09-22 15:01
 */
public class MultiJsonFilePipeline extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private BlockingQueue<Map<String, Object>> JSONQUEUE = null;
    private int counter = 0;

    public MultiJsonFilePipeline() {
        this.setPath("/data/webmagic");
        this.counter = 100;
        JSONQUEUE = new LinkedBlockingDeque<Map<String, Object>>(this.counter);
    }

    public MultiJsonFilePipeline(String path, int counter) {
        this.setPath(path);
        this.counter = counter;
        JSONQUEUE = new LinkedBlockingDeque<Map<String, Object>>(this.counter);
    }

    @Override
    public void process(final ResultItems resultItems, final Task task) {
        if (JSONQUEUE.size() == counter) {
            save();
        } else {
            try {
                JSONQUEUE.put(resultItems.getAll());
                logger.info("队列数量:" + JSONQUEUE.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void beforeExit() {
        if (JSONQUEUE.size() > 0) {
            save();
        }
    }

    public void save() {
        Object[] objects = JSONQUEUE.toArray();
        JSONQUEUE.clear();
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(this.getFile(path + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".json")));
            for (Object object : objects) {
                printWriter.write(JSON.toJSONString(object) + "\n");
            }
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
