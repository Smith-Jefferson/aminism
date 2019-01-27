package org.aminism.spider.aminism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author ygxie
 * @date 2019/1/27.
 */
@SpringBootApplication(scanBasePackages = {"org.aminism.spider"})
@ServletComponentScan
@EnableJpaRepositories("org.aminism.spider.dao")
public class Starter extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Starter.class);
    }

    /**
     * 启动主方法
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
