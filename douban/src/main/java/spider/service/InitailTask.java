package spider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spider.App;
import spider.database.DoubanDataRep;
import spider.model.DoubanbookCommentEntity;
import spider.model.DoubanbookReviewCommentEntity;
import spider.model.DoubanbookReviewEntity;
import spider.model.UserEntity;
import spider.strategy.DoubanBookTaskInitServer;
import spider.tool.SpiderTool;

import java.util.List;

/**
 * Created by hello world on 2017/1/10.
 */
@Service
public class InitailTask {
    @Autowired
    private DoubanDataRep dataRep;

    public void initailDoubanBook(){
        List<Long> bookIDs= dataRep.getDoubanBook();
        App.getBloomFilter().addAll(bookIDs);
    }
    public void initailDoubanBookComment(){
        List<DoubanbookCommentEntity> books=dataRep.getDoubanbookComment();
        StringBuilder str=new StringBuilder();
        for (DoubanbookCommentEntity comment:books) {
            App.getBloomFilter().add(str.append(comment.getDoubanuserid()).append(comment.getBookid()).toString());
            str.delete(0,str.length());
        }
    }
    public void initialDoubanBookReview(){
        List<DoubanbookReviewEntity> books=dataRep.getDoubanbookReview();
        StringBuilder str=new StringBuilder();
        for (DoubanbookReviewEntity bookreview:books) {
            App.getBloomFilter().add(str.append(bookreview.getDoubanuserid()).append(bookreview.getBookid()).append("review").toString());
            str.delete(0,str.length());
        }
    }
    public void initialDoubanBookReviewComment(){
        List<DoubanbookReviewCommentEntity> reviewComments=dataRep.getDoubanbookReviewCommet();
        StringBuilder str=new StringBuilder();
        for (DoubanbookReviewCommentEntity comment:reviewComments) {
            App.getBloomFilter().add(str.append(comment.getDoubanuserid()).append(comment.getBookid()).append(comment.getReviewid()).toString());
            str.delete(0,str.length());
        }
    }
    public void initailDoubanUser(){
        List<UserEntity> userlist=dataRep.getDoubanUser();
        StringBuilder str=new StringBuilder();
        for (UserEntity user:userlist) {
            App.getBloomFilter().add(str.append(user.getDoubanuserid()).append(user.getUname()).toString());
            str.delete(0,str.length());
        }
    }

    @Autowired
    private DoubanBookTaskInitServer taskInitServer;
    public void initailDoubanbookSchedule(){
        SpiderTool.initailAgent();
        taskInitServer.getBookUrl();
    }
}
