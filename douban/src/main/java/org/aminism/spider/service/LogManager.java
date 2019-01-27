package org.aminism.spider.service;

import org.aminism.spider.dao.LogDao;
import org.aminism.spider.entity.LogEntity;
import org.aminism.spider.model.LogLevel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xieyigang on 2017/6/14.
 */
@Component
public class LogManager {
    @Autowired
    LogDao logDao;
    public void writeLog(String log) {
        writeLog(log, LogLevel.ERROR);
    }

    public void writeLog(String log, int level) {
        writeLog(log, level, null);
    }

    public void writeLog(String log, int level, String tag) {
        LogEntity logEntity = new LogEntity();
        logEntity.setMessage(log);
        logEntity.setLevel(level);
        logEntity.setTag(tag);
        logDao.save(logEntity);
    }

    public void writeLog(Exception e, int level, String tag) {
        StringBuilder log = new StringBuilder();
        for (StackTraceElement msg : e.getStackTrace()) {
            log.append(msg.toString()).append("/n");
        }
        writeLog(log.toString(), level, tag);
    }

    public void writeLog(Exception e) {
        writeLog(e, LogLevel.ERROR, null);
    }
}

