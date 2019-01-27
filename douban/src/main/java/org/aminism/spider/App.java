package org.aminism.spider;

import org.aminism.spider.service.DoubanBookTask;
import org.aminism.spider.tool.WorkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * project start!
 */
@EnableJpaRepositories("org.aminism.spider.dao")
@SpringBootApplication(scanBasePackages = "org.aminism.spider")
@EnableCaching
public class App extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * Configure your application when it’s launched by the servlet container
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(App.class);
    }

    @Autowired
    private DoubanBookTask doubanBookTask;

    public static ExecutorService fixedThreadPool;
    public static ExecutorService mainTaskThreadPool;
    private static WorkContext ctx = new WorkContext();

    static {
        fixedThreadPool = Executors.newFixedThreadPool(3);
        mainTaskThreadPool = Executors.newFixedThreadPool(5);
    }

    public void run() throws Exception {

        ctx.info("程序开始获取数据...");
        doubanBookTask.run();
        fixedThreadPool.shutdown();
        mainTaskThreadPool.shutdown();
        while (true) {
            if (fixedThreadPool.isTerminated() && mainTaskThreadPool.isTerminated()) {
                ctx.info("豆瓣读书...");
                ctx.info("程序结束.");
                break;
            }
            Thread.sleep(20000);
        }
        ctx.flush();
    }

}
