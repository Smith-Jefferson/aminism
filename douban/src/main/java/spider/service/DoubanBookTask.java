package spider.service;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

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
	public static volatile Queue<String> bookSchedule=new ConcurrentLinkedDeque<String>();
	public void run(){
		initail();
		while (true){
			if(bookSchedule.isEmpty())
				break;
			try {
				spiderTask();
				Thread.sleep(200000);
			}catch (Exception e){
				log.error(e.getLocalizedMessage());
			}

		}

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
		Set<String> detailurls=null;
		synchronized (DoubanBookTask.bookSchedule){
			detailurls=new HashSet<String>(bookSchedule.size());
			for (int i=0;i<bookSchedule.size();i++) {
				detailurls.add(bookSchedule.poll());
			}
			bookSchedule.clear();
		}
		int taskNum=5;
		Set taskSet=new HashSet(5);
		for (String url:detailurls) {
			if(taskNum-->0){
				taskSet.add(url);
			}else{
				DoubanBookDetail Detial=new DoubanBookDetail(taskSet);
				App.mainTaskThreadPool.execute(Detial);
				taskNum=5;
				taskSet=new HashSet(5);
			}

		}
		//Detial.run();
		System.out.println(Thread.currentThread().getName() + "当前任务结束");
	}
}