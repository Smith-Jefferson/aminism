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
		Queue<String> temp;
		synchronized (bookSchedule){
			temp=bookSchedule;
			bookSchedule.clear();
		}
		Object[] bookurls=temp.toArray();
		int threadnum=3;
		int left=bookurls.length;
		int booksnumInTheard=(int)Math.ceil((double)left/(double) threadnum);
		List<Thread> threads=new ArrayList<>(threadnum);
	    for (int i=0;i<bookurls.length;i+=booksnumInTheard-1){
	    	if(left<=0)
	    		break;
			if(left<booksnumInTheard)
				booksnumInTheard=left;
	    	String[] detailurls=new String[booksnumInTheard];
	    	for(int j=0;j<booksnumInTheard;j++){
				detailurls[j]=bookurls[i].toString();
			}
			DoubanBookDetail Detial=new DoubanBookDetail(detailurls);
			try {
				Detial.run();
//				Thread detailThread=new Thread(Detial);
//				detailThread.start();
//				threads.add(detailThread);
			} catch (Exception e) {
				log.error(e.toString());
			}
			left-=booksnumInTheard;
		}
		for (Thread tr:threads) {
			try {
				tr.join();
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}
		}
		System.out.println(Thread.currentThread().getName() + "当前任务结束");
	}
}