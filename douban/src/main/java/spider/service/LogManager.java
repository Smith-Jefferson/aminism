package spider.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import spider.model.LogEntity;
import spider.model.LogLevel;
import spider.pool.SessionPool;

/**
 * Created by xieyigang on 2017/6/14.
 */
public class LogManager {
    public static void writeLog(String log){
        writeLog(log,LogLevel.ERROR);
    }

    public static void writeLog(String log,int level){
        writeLog(log,level,null);
    }

    public static void writeLog(String log,int level,String tag){
        LogEntity logEntity=new LogEntity();
        logEntity.setMessage(log);
        logEntity.setLevel(level);
        logEntity.setTag(tag);
        Session session= SessionPool.getSession();
        Transaction transaction=session.beginTransaction();
        session.save(logEntity);
        try {
            transaction.commit();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        SessionPool.freeSession(session);
    }

    public static void writeLog(Exception e,int level,String tag){
        StringBuilder log=new StringBuilder();
        for (StackTraceElement msg:e.getStackTrace()){
            log.append(msg.toString()).append("/n");
        }
        writeLog(log.toString(),level,tag);
    }

    public static void writeLog(Exception e){
        writeLog(e,LogLevel.ERROR,null);
    }
}

