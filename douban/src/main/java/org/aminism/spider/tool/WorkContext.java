package org.aminism.spider.tool;

import com.google.common.base.Stopwatch;
import org.aminism.spider.log.CLogManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ygxie on 2017/10/26.
 */
@Component
public class WorkContext {
    //计时器
    private Stopwatch runWatch=Stopwatch.createStarted();
    //订单id
    private Integer orderId;
    private String url;
    //工作流日志
    private List<String> logItems;

    public WorkContext(){
        logItems = new ArrayList<>();
        logItems.add("aminism日志");
    }
    public WorkContext(String url){
        this();
        this.url=url;
    }
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        orderId = orderId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void info(String message)
    {
        if (logItems == null || StringUtils.isEmpty(message)) return;
        logItems.add(DateUtil.now()+message+"\n");
    }

    public void flush(){
        if(logItems==null || logItems.isEmpty())
            return;
        info("总耗时(秒钟)："+runWatch.elapsed(TimeUnit.MILLISECONDS));
        runWatch.stop();
        StringBuilder builder = new StringBuilder();
        logItems.forEach(x->builder.append(x));
        if(orderId>0){
            CLogManager.info("日志总计",builder.toString(),Integer.toString(orderId));
        }
        CLogManager.info("日志总计",builder.toString());
    }
}
