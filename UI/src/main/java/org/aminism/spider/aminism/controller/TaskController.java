package org.aminism.spider.aminism.controller;

import com.alibaba.fastjson.JSON;
import org.aminism.spider.aminism.model.Result;
import org.aminism.spider.aminism.model.TaskReslut;
import org.aminism.spider.aminism.model.TaskUrl;
import org.aminism.spider.dao.TaskUrlDao;
import org.aminism.spider.entity.TaskUrlEntity;
import org.aminism.spider.strategy.DoubanBookDetail;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by xieyigang on 2018/11/18.
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskUrlDao taskUrlDao;
    @Autowired
    DoubanBookDetail bookDetail;

    @GetMapping("/getTask")
    public TaskUrl getTask() {
        TaskUrlEntity entity = taskUrlDao.queryTop1ByTagNotLikeAndAndIsdel("lock", 0);
        if (entity == null) {
            return new TaskUrl();
        }
        entity.setTag("lock_" + entity.getTag());
        taskUrlDao.save(entity);
        TaskUrl taskUrl = new TaskUrl();
        taskUrl.setUrl(entity.getUrl());
        return taskUrl;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/saveResult")
    public Result doTask(@RequestBody String json) {
        //json = StringEscapeUtils.unescapeHtml4(json);

        TaskReslut taskReslut = null;
        try {
            taskReslut = JSON.parseObject(json,TaskReslut.class);
            if(StringUtils.isNotBlank(taskReslut.getUrl()) && StringUtils.isNotBlank(taskReslut.getHtml())){
                bookDetail.runHtml(StringEscapeUtils.unescapeHtml4(taskReslut.getHtml()), taskReslut.getUrl());
                TaskUrlEntity entity = new TaskUrlEntity();
                entity.setUrl(taskReslut.getUrl());
                entity.setIsdel(1);
                taskUrlDao.save(entity);
                return new Result();
            }else {
                throw new RuntimeException("miss info");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(taskReslut != null && StringUtils.isNotBlank(taskReslut.getUrl())){
                TaskUrlEntity entity = taskUrlDao.queryTop1ByUrl(taskReslut.getUrl());
                String tag = entity.getTag().split("_")[1];
                entity.setTag(tag);
                taskUrlDao.save(entity);
            }
            Result r = new Result();
            r.setStatus(1);
            return r;
        }
    }
}
