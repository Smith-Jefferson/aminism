package com.ctrip.flight.backendservice.backofficetool.aminism.spider.service;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.App;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.strategy.DoubanBookTaskInitServer;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.SpiderTool;
import com.ctrip.flight.backendservice.backofficetool.spider.dao.DoubanDataRep;
import com.ctrip.flight.backendservice.backofficetool.spider.entity.DoubanbookCommentEntity;
import com.ctrip.flight.backendservice.backofficetool.spider.entity.DoubanbookReviewCommentEntity;
import com.ctrip.flight.backendservice.backofficetool.spider.entity.DoubanbookReviewEntity;
import com.ctrip.flight.backendservice.backofficetool.spider.entity.UserEntity;
import com.ctrip.flight.backendservice.backofficetool.spider.log.BloomFilterUtil;
import com.ctrip.flight.backendservice.backofficetool.spider.log.CLogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hello world on 2017/1/10.
 */
@Service
public class InitailTask {

    private DoubanDataRep dataRep;
    @Autowired
    public void setDataRep(DoubanDataRep dataRep) {
        this.dataRep = dataRep;
    }

    public void initailDoubanBook(){
        List<Long> bookIDs= dataRep.getDoubanBook();
        BloomFilterUtil.getBloomFilter().addAll(bookIDs);
    }
    public void initailDoubanBookComment(){
        List<DoubanbookCommentEntity> books=dataRep.getDoubanbookComment();
        StringBuilder str=new StringBuilder();
        for (DoubanbookCommentEntity comment:books) {
            BloomFilterUtil.getBloomFilter().add(str.append(comment.getDoubanuserid()).append(comment.getBookid()).toString());
            str.delete(0,str.length());
        }
    }
    public void initialDoubanBookReview(){
        List<DoubanbookReviewEntity> books=dataRep.getDoubanbookReview();
        StringBuilder str=new StringBuilder();
        for (DoubanbookReviewEntity bookreview:books) {
            BloomFilterUtil.getBloomFilter().add(str.append(bookreview.getDoubanuserid()).append(bookreview.getBookid()).append("review").toString());
            str.delete(0,str.length());
        }
    }
    public void initialDoubanBookReviewComment(){
        List<DoubanbookReviewCommentEntity> reviewComments=dataRep.getDoubanbookReviewCommet();
        StringBuilder str=new StringBuilder();
        for (DoubanbookReviewCommentEntity comment:reviewComments) {
            BloomFilterUtil.getBloomFilter().add(str.append(comment.getDoubanuserid()).append(comment.getBookid()).append(comment.getReviewid()).toString());
            str.delete(0,str.length());
        }
    }
    public void initailDoubanUser(){
        List<UserEntity> userlist=dataRep.getDoubanUser();
        StringBuilder str=new StringBuilder();
        for (UserEntity user:userlist) {
            BloomFilterUtil.getBloomFilter().add(str.append(user.getDoubanuserid()).append(user.getUname()).toString());
            str.delete(0,str.length());
        }
    }

    @Autowired
    private DoubanBookTaskInitServer taskInitServer;
    public void initailDoubanbookSchedule(){
        try {
            SpiderTool.initailAgent();
            taskInitServer.getBookUrl();
        }catch (Exception e){
            CLogManager.error(e);}

    }
}
