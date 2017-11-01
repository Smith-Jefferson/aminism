package spider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spider.App;
import spider.database.DoubanDataRep;
import spider.strategy.DoubanBookDetail;
import spider.tool.CLogManager;
import spider.tool.WorkContext;

import java.util.HashSet;
import java.util.List;
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
			ctx.info("新的任务清单长度："+detailurls.size());
			for (int i=0;i<bookSchedule.size();i++) {
				detailurls.add(bookSchedule.poll());
			}
			bookSchedule.clear();
		}
		int taskNum=1;
		Set<String> taskSet=null;
		for (String url:detailurls) {
			if(--taskNum>0){
				taskSet.add(url);
			}else{
				taskNum=5;
				ctx.info("开启新线程,线程内任务数："+taskNum);
				taskSet=new HashSet<String>(taskNum);
				DoubanBookDetail doubanBookDetailetial=new DoubanBookDetail(taskSet);
				App.mainTaskThreadPool.execute(doubanBookDetailetial);
			}
		}
		ctx.info(Thread.currentThread().getName() + "当前任务结束");
		ctx.flush();
	}
}