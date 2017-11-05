package com.ctrip.flight.backendservice.backofficetool.aminism.spider.service;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.App;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.database.DoubanDataRep;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.strategy.DoubanBookDetail;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.CLogManager;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.WorkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class DoubanBookTask implements Runnable {
	@Autowired
	private InitailTask initailTask;
	@Autowired
	private DoubanDataRep doubanDataRep;
	private static WorkContext ctx=new WorkContext();
	public static volatile Queue<String> bookSchedule=new ConcurrentLinkedDeque<String>();
	public void run(){
		initail();
		while (true){
			bookSchedule.addAll(doubanDataRep.listTaskUrl());
			if(bookSchedule.isEmpty())
				break;
			try {
				spiderTask();
				Thread.sleep(200000);
			}catch (Exception e){
				CLogManager.error("DoubanBookTask",e);
			}
		}
	}

	 private void initail(){
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
	private void spiderTask(){
		Set<String> detailurls;
		synchronized (DoubanBookTask.bookSchedule){
			detailurls=new HashSet<String>(bookSchedule.size());
			ctx.info("新的任务清单长度："+bookSchedule.size());
			for (int i=0;i<bookSchedule.size();i++) {
				detailurls.add(bookSchedule.poll());
			}
			bookSchedule.clear();
		}
		int taskNum=5;
		int singleRuns=1;
		Set<String> taskSet=new HashSet<String>(taskNum);
		for (String url:detailurls) {
			if(--taskNum>0){
				taskSet.add(url);
			}else{
				taskNum=5;
				ctx.info("开启新线程,线程内任务数："+taskNum);
				DoubanBookDetail doubanBookDetailetial=new DoubanBookDetail(taskSet);
				if(singleRuns-->0)
					doubanBookDetailetial.run();
				else
					App.mainTaskThreadPool.execute(doubanBookDetailetial);
				taskSet=new HashSet<String>(taskNum);
			}
		}
		ctx.info(Thread.currentThread().getName() + "当前任务结束");
		ctx.flush();
	}
}