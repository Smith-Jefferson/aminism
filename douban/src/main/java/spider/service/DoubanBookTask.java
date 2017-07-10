package spider.service;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import spider.App;
import spider.database.SaveTrendsAmazon;
import spider.database.TrendAmazonDataOp;
import spider.model.Item;
import spider.strategy.DoubanBookDetail;
import spider.strategy.ItemList;
import spider.strategy.TrendsAmazonItemDetial;
import spider.strategy.TrendsAmazonList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubanBookTask implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(DoubanBookTask.class);
	public static volatile Queue<String> bookSchedule=new ConcurrentLinkedDeque<>();
	public void run(){
		initail();
		while (!bookSchedule.isEmpty())
			spiderTask();
	}

	public void initail(){
		log.info("初始化豆瓣已爬取书籍...");
		InitailTask.initailDoubanBook();
		log.info("初始化豆瓣已爬取书籍短评...");
		InitailTask.initailDoubanBookComment();
		log.info("初始化豆瓣已爬取书籍书评...");
		InitailTask.initialDoubanBookReview();
		InitailTask.initialDoubanBookReviewComment();
		log.info("初始化豆瓣已爬取豆瓣用户");
		InitailTask.initailDoubanUser();
		log.info("初始化工作序列...");
		InitailTask.initailDoubanbookSchedule();
	}
	public static void spiderTask(){
		String[] detailurls=null;
		synchronized (DoubanBookTask.bookSchedule){
			detailurls=new String[bookSchedule.size()];
			for (int i=0;i<bookSchedule.size();i++) {
				detailurls[i]=bookSchedule.poll();
			}
			bookSchedule.clear();
		}
		DoubanBookDetail Detial=new DoubanBookDetail(detailurls);
		Detial.run();
		System.out.println(Thread.currentThread().getName() + "当前任务结束");
	}
}