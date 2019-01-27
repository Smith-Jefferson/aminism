package org.aminism.spider.service;

import org.aminism.spider.App;
import org.aminism.spider.constant.Urltag;
import org.aminism.spider.dao.DoubanDataRep;
import org.aminism.spider.dao.TaskUrlDao;
import org.aminism.spider.entity.Reponse;
import org.aminism.spider.entity.TaskUrlEntity;
import org.aminism.spider.log.CLogManager;
import org.aminism.spider.strategy.DoubanBookComment;
import org.aminism.spider.strategy.DoubanBookDetail;
import org.aminism.spider.strategy.DoubanBookReview;
import org.aminism.spider.strategy.DoubanbookOffer;
import org.aminism.spider.tool.WorkContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class DoubanBookTask {
    @Autowired
    private InitailTask initailTask;
    @Autowired
    private DoubanDataRep doubanDataRep;
    private static WorkContext ctx = new WorkContext();
    public static volatile Queue<String> bookSchedule = new ConcurrentLinkedDeque<String>();

    public void run() {
        initail();
        while (true) {
            bookSchedule.addAll(doubanDataRep.listTaskUrl());
            if (bookSchedule.isEmpty())
                break;
            try {
                spiderTask();
                Thread.sleep(200000);
            } catch (Exception e) {
                CLogManager.error("DoubanBookTask", e);
            }
        }
    }

    private void initail() {
        ctx.info("初始化taskUrl");

        ctx.info("初始化豆瓣已爬取书籍...");
        initailTask.initailDoubanBook();
        ctx.info("初始化豆瓣已爬取书籍短评...");
        initailTask.initailDoubanBookComment();
        ctx.info("初始化豆瓣已爬取书籍书评...");
        initailTask.initialDoubanBookReview();
        initailTask.initialDoubanBookReviewComment();
        ctx.info("初始化豆瓣已爬取豆瓣用户");
        initailTask.initailDoubanUser();
        ctx.info("初始化工作序列...");
        initailTask.initailDoubanbookSchedule();
    }

    @Autowired
    DoubanBookDetail doubanBookDetailetial;
    @Autowired
    DoubanBookComment comment;
    @Autowired
    DoubanBookReview review;
    @Autowired
    DoubanbookOffer offer;
    @Autowired
    TaskUrlDao taskUrlDao;
    private void spiderTask() {
        Set<String> detailurls;
        synchronized (DoubanBookTask.bookSchedule) {
            detailurls = new HashSet<String>(bookSchedule.size());
            ctx.info("新的任务清单长度：" + bookSchedule.size());
            for (int i = 0; i < bookSchedule.size(); i++) {
                detailurls.add(bookSchedule.poll());
            }
            bookSchedule.clear();
        }

        List<String> commentTaskUrl = new ArrayList<>();
        List<String> reviewTaskUrl = new ArrayList<>();
        for (String url : detailurls) {
            doubanBookDetailetial.run(url);
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            commentTaskUrl.add(url + "/comments/");
            reviewTaskUrl.add(url + "/reviews");
        }
        for (String url : commentTaskUrl){
            try {
                comment.run(url);
            } catch (Exception e) {
                CLogManager.error("task", e, url);
            }
        }
        for (String url:reviewTaskUrl){
            try {
                review.run(url);
            } catch (Exception e) {
                CLogManager.error("task", e, url);
            }
        }
        List<TaskUrlEntity> offerTaskUrl = taskUrlDao.queryAllByTag(Urltag.bookoff);
        if(CollectionUtils.isNotEmpty(offerTaskUrl)){
            for(TaskUrlEntity u:offerTaskUrl){
                offer.run(u.getUrl());
            }
        }


        ctx.info(Thread.currentThread().getName() + "当前任务结束");
        ctx.flush();
    }
}