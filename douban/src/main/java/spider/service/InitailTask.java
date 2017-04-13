package spider.service;

import spider.App;
import spider.database.DoubanDataRep;
import spider.model.DoubanbookCommentEntity;
import spider.model.DoubanbookReviewCommentEntity;
import spider.model.DoubanbookReviewEntity;
import spider.model.UserEntity;
import spider.strategy.AgentFetcher;
import spider.strategy.DoubanBookIndex;
import spider.tool.SpiderTool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by hello world on 2017/1/10.
 */
public class InitailTask {
    public static void initailDoubanBook(){
        List<String> bookurls= DoubanDataRep.getDoubanBook();
        for(String url:bookurls){
            App.getBloomFilter().add(url);
        }
    }
    public static void initailDoubanBookComment(){
        List<DoubanbookCommentEntity> books=DoubanDataRep.getDoubanbookComment();
        StringBuilder str=new StringBuilder();
        for (DoubanbookCommentEntity comment:books) {
            App.getBloomFilter().add(str.append(comment.getDoubanuserid()).append(comment.getBookid()).toString());
            str.delete(0,str.length());
        }
    }
    public static void initialDoubanBookReview(){
        List<DoubanbookReviewEntity> books=DoubanDataRep.getDoubanbookReview();
        StringBuilder str=new StringBuilder();
        for (DoubanbookReviewEntity bookreview:books) {
            App.getBloomFilter().add(str.append(bookreview.getDoubanuserid()).append(bookreview.getBookid()).append("review").toString());
            str.delete(0,str.length());
        }
    }
    public static void initialDoubanBookReviewComment(){
        List<DoubanbookReviewCommentEntity> reviewComments=DoubanDataRep.getDoubanbookReviewCommet();
        StringBuilder str=new StringBuilder();
        for (DoubanbookReviewCommentEntity comment:reviewComments) {
            App.getBloomFilter().add(str.append(comment.getDoubanuserid()).append(comment.getBookid()).append(comment.getReviewid()).toString());
            str.delete(0,str.length());
        }
    }
    public static void initailDoubanUser(){
        List<UserEntity> userlist=DoubanDataRep.getDoubanUser();
        StringBuilder str=new StringBuilder();
        for (UserEntity user:userlist) {
            App.getBloomFilter().add(str.append(user.getDoubanuserid()).append(user.getUname()).toString());
            str.delete(0,str.length());
        }
    }
    public static void initailDoubanbookSchedule(){
        SpiderTool.initailAgent();
        DoubanBookIndex.getBookUrl();
    }
}
