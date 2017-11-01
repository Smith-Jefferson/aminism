package spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spider.service.DoubanBookTask;
import spider.tool.BloomFilter;
import spider.tool.WorkContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * project start!
 *
 */
@Component
public class App 
{
    @Autowired
    private DoubanBookTask doubanBookTask;
    private volatile static BloomFilter bloomFilter=new BloomFilter();
    public static ExecutorService fixedThreadPool;
    public static ExecutorService mainTaskThreadPool;
    private static WorkContext ctx=new WorkContext();
    static {
        fixedThreadPool= Executors.newFixedThreadPool(5);
        mainTaskThreadPool= Executors.newFixedThreadPool(30);
    }
//    public static void main( String[] args ) throws Exception
//    {
//        new App().run();
//    }

    public void run() throws Exception{

        ctx.info("程序开始获取数据...");
        doubanBookTask.run();
        fixedThreadPool.shutdown();
        mainTaskThreadPool.shutdown();
        while (true){
            if(fixedThreadPool.isTerminated() && mainTaskThreadPool.isTerminated()){
                ctx.info("豆瓣读书...");
                ctx.info("程序结束.");
                break;
            }
            Thread.sleep(20000);
        }
        ctx.flush();
    }

    public static BloomFilter getBloomFilter() {
        return bloomFilter;
    }

    public static void setBloomFilter(BloomFilter bloomFilter) {
        App.bloomFilter = bloomFilter;
    }
}
