package org.aminism.spider;

import org.aminism.spider.service.DoubanBookTask;
import org.aminism.spider.tool.WorkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * project start!
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories("org.aminism.spider.dao")
public class App 
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private DoubanBookTask doubanBookTask;

    public static ExecutorService fixedThreadPool;
    public static ExecutorService mainTaskThreadPool;
    private static WorkContext ctx=new WorkContext();
    static {
        fixedThreadPool= Executors.newFixedThreadPool(3);
        mainTaskThreadPool= Executors.newFixedThreadPool(5);
    }

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

}
