package com.luochen.web;

import com.luochen.recommend.ContentBaseRecommender.quartz.calculateKeyWordsTriggerRunner;
import com.luochen.web.Dao.interf.NewsKeywordsRepository;
import com.luochen.web.Dao.interf.NewsRepository;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SuppressWarnings("ALL")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.luochen.web.controllers")
@ComponentScan("com.luochen.web.conf")
@ComponentScan("com.luochen.web")
@EnableAutoConfiguration
public class DemoApplication extends SpringBootServletInitializer {
    @Autowired
    private NewsKeywordsRepository newsKeywordsRepository;
    @Autowired
    private NewsRepository newsRepository;

    private static NewsRepository newsRepo;
    private static NewsKeywordsRepository newsKeywordsRepo;
    @PostConstruct
    public void init() {
        DemoApplication.newsRepo=newsRepository;
        DemoApplication.newsKeywordsRepo=newsKeywordsRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
  //     new Thread(new MailUtils("1329127421@qq.com",CodeUtils.generateUniqueCode()+CodeUtils.generateUniqueCode())).start();
        try {
            new calculateKeyWordsTriggerRunner().task(newsRepo,newsKeywordsRepo);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
    @Override//为了打包springboot项目
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

}
