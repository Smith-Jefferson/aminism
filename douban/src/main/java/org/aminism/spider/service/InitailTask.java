package org.aminism.spider.service;

import org.aminism.spider.dao.DoubanDataRep;
import org.aminism.spider.entity.DoubanbookCommentEntity;
import org.aminism.spider.entity.DoubanbookReviewCommentEntity;
import org.aminism.spider.entity.DoubanbookReviewEntity;
import org.aminism.spider.entity.UserEntity;
import org.aminism.spider.log.BloomFilterUtil;
import org.aminism.spider.log.CLogManager;
import org.aminism.spider.strategy.DoubanBookTaskInitServer;
import org.aminism.spider.tool.SpiderTool;
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

    public void initailDoubanBook() {
        List<Long> bookIDs = dataRep.getDoubanBook();
        BloomFilterUtil.getBloomFilter().addAll(bookIDs);
    }

    public void initailDoubanBookComment() {
        List<DoubanbookCommentEntity> books = dataRep.getDoubanbookComment();
        StringBuilder str = new StringBuilder();
        for (DoubanbookCommentEntity comment : books) {
            BloomFilterUtil.getBloomFilter().add(str.append(comment.getDoubanuserid()).append(comment.getBookid()).toString());
            str.delete(0, str.length());
        }
    }

    public void initialDoubanBookReview() {
        List<DoubanbookReviewEntity> books = dataRep.getDoubanbookReview();
        StringBuilder str = new StringBuilder();
        for (DoubanbookReviewEntity bookreview : books) {
            BloomFilterUtil.getBloomFilter().add(str.append(bookreview.getDoubanuserid()).append(bookreview.getBookid()).append("review").toString());
            str.delete(0, str.length());
        }
    }

    public void initialDoubanBookReviewComment() {
        List<DoubanbookReviewCommentEntity> reviewComments = dataRep.getDoubanbookReviewCommet();
        StringBuilder str = new StringBuilder();
        for (DoubanbookReviewCommentEntity comment : reviewComments) {
            BloomFilterUtil.getBloomFilter().add(str.append(comment.getDoubanuserid()).append(comment.getBookid()).append(comment.getReviewid()).toString());
            str.delete(0, str.length());
        }
    }

    public void initailDoubanUser() {
        List<UserEntity> userlist = dataRep.getDoubanUser();
        StringBuilder str = new StringBuilder();
        for (UserEntity user : userlist) {
            BloomFilterUtil.getBloomFilter().add(str.append(user.getDoubanuserid()).append(user.getUname()).toString());
            str.delete(0, str.length());
        }
    }

    @Autowired
    private DoubanBookTaskInitServer taskInitServer;

    public void initailDoubanbookSchedule() {
        try {
            SpiderTool.initailAgent();
            taskInitServer.getBookUrl();
        } catch (Exception e) {
            CLogManager.error(e);
        }

    }
}
