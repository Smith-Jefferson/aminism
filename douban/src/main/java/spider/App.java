package spider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.service.DoubanBookTask;
import spider.tool.BloomFilter;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * project start!
 *
 */
public class App 
{
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private volatile static BloomFilter bloomFilter=new BloomFilter();
    public static ExecutorService fixedThreadPool;
    static {
        fixedThreadPool= Executors.newFixedThreadPool(50);
    }
    public static void main( String[] args )
    {
        log.info("程序开始获取数据...");
        DoubanBookTask doubanBookTask=new DoubanBookTask();
        doubanBookTask.run();
//        Thread thread=new Thread(doubanBookTask);
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            log.error(e.getMessage());
//        }
        log.info("豆瓣读书...");
        log.info("程序结束.");
    }

    public static BloomFilter getBloomFilter() {
        return bloomFilter;
    }

    public static void setBloomFilter(BloomFilter bloomFilter) {
        App.bloomFilter = bloomFilter;
    }
}
