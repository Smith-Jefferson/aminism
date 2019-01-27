package org.aminism.spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("org.aminism.spider.dao")
@SpringBootApplication(scanBasePackages = "org.aminism.spider")
@EnableCaching
public class SpringBootTestInitializer extends SpringBootServletInitializer
{

    public static void main(String[] args) {
        //System.setProperty("env", "fat");
        SpringApplication.run(SpringBootTestInitializer.class, args);
    }

    /**
     * Configure your application when it’s launched by the servlet container
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootTestInitializer.class);
    }

}
