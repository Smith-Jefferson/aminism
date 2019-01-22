package org.aminism.spider.log;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ygxie on 2017/10/25.
 */
public class CLogManager {
    //clog实例
    private static Logger logger = LogManager.getLogger(CLogManager.class);

    /**
     * info日志
     * @param title 日志标题
     * @param message 日志内容
     * @param addInfo tag内容
     */
    public static void info(String title,String message,Map<String,String> addInfo){
        logger.info(new LogBean(title, message, addInfo));
    }

    public static void info(String title,String message){
        logger.info(new LogBean(title,message));
    }

    public static void info(String title,String message,String orderid){
        Map<String, String> tags=new HashMap<String, String>();
        tags.put("orderid",orderid);
        logger.info(new LogBean(title,message,tags));
    }

    /**
     * warn日志
     * @param title 日志标题
     * @param message 日志内容
     * @param addInfo tag内容
     */
    public static void warn(String title,String message,Map<String,String> addInfo){
        logger.warn(new LogBean(title, message, addInfo));
    }
    /**
     * warn日志
     * @param title 日志标题
     * @param addInfo tag内容
     */
    public static void warn(String title,Exception ex,Map<String,String> addInfo){
        logger.warn(new LogBean(title, ex.getMessage(),addInfo));
    }


    public static void error(String title,Exception ex){
        logger.error(title,ex);
    }

    public static void error(String title,Throwable e){
        logger.error(title,e);
    }

    public static void error(Exception ex){
        error("aminisim",ex);
    }

    public static void error(String title,Exception ex,String orderid){
        Map<String, String> tags=new HashMap<String, String>();
        tags.put("orderid",orderid);
        logger.error(new LogBean(title, ex.getMessage(),tags),ex);
    }
}
