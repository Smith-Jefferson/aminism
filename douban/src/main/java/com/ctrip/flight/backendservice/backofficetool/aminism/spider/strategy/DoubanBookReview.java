package com.ctrip.flight.backendservice.backofficetool.aminism.spider.strategy;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.App;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.DateUtil;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.SpiderTool;
import com.ctrip.flight.backendservice.backofficetool.spider.dao.DoubanDataRep;
import com.ctrip.flight.backendservice.backofficetool.spider.entity.DoubanbookReviewEntity;
import com.ctrip.flight.backendservice.backofficetool.spider.entity.UserEntity;
import com.ctrip.flight.backendservice.backofficetool.spider.log.BloomFilterUtil;
import com.ctrip.flight.backendservice.backofficetool.spider.log.CLogManager;
import com.google.common.base.Joiner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hello world on 2017/1/16.
 */
public class DoubanBookReview implements Runnable {
    @Autowired
    private DoubanDataRep doubanDataRep;
    private static final Logger log = LoggerFactory.getLogger(DoubanBookReview.class);
    private int start=0;
    private String baseurl;
    private List<String> reviewsUrl=null;
    private long bookid;
    private String url;
    public DoubanBookReview(String url) {
        this.baseurl = url;
    }

    @Override
    public void run() {
        setBookid(baseurl);
        task(baseurl);
    }
    public void task(String baseurl){
        String url="";
        if(start>0)
            url=baseurl+"?start="+start;
        else
            url=baseurl;
        start+=20;
        try {
            Thread.sleep(4000);
        }catch (Exception e){
            CLogManager.error(e);
        }
        Document doc= SpiderTool.Getdoc(url,3,false);
        int counts=Integer.parseInt(SpiderTool.OnlyNo(SpiderTool.removeZh(doc.select("div#content").select("h1").text().replace("(","").replace(")","")).replace(",","").trim()));
        if(counts<=0)
            return;
        if(reviewsUrl==null)
            reviewsUrl=new ArrayList<>(counts);
        Elements elements=doc.select("div.review-list").select("div.review-item").select("header");
        for (Element el:elements) {
            reviewsUrl.add(el.select(".title").select("a[href]").attr("abs:href"));
        }

        UserEntity user;
        StringBuilder str=new StringBuilder();
        for (String reviewUrl:reviewsUrl) {
            try{
                str.delete(0,str.length());

                Document review= SpiderTool.Getdoc(reviewUrl,3,false);
                DoubanbookReviewEntity bookreview=new DoubanbookReviewEntity();
                user=new UserEntity();
                user.setDoubanuserid(getDoubanuserid(review));
                user.setUname(getUserName(review));
                user.setAvatar(getUserAvatar(review));
                user.setFlag(0);
                long userid=doubanDataRep.getUserID(user);
                bookreview.setUserid(userid);
                bookreview.setDoubanuserid(user.getDoubanuserid());
                bookreview.setUrl(reviewUrl);
                bookreview.setBookid(bookid);
                bookreview.setDisfollownum(getDisfollownum(review));
                bookreview.setFollownum(getFollownum(review));
                bookreview.setRate(getRate(review));
                bookreview.setReview(getContent(review));
                bookreview.setRatedate(getRatedate(review));
                bookreview.setReviewRecusers(getReviewRecusers(reviewUrl));
                bookreview.setReviewLikeuser(getReviewLikeuser(reviewUrl));
                if(!BloomFilterUtil.getBloomFilter().ContainedThenAdd(str.append(bookreview.getDoubanuserid()).append(bookreview.getBookid()).append("review").toString())){
                    doubanDataRep.saveReview(bookreview);
                }
                try{
                    DoubanBookReviewComment reviewComment=new DoubanBookReviewComment(review);
                    reviewComment.setBookid(bookid);
                    reviewComment.setBaseUrl(reviewUrl);
                    App.fixedThreadPool.execute(reviewComment);
                    //reviewComment.run();
                }catch(Exception e){
                    CLogManager.error(e);
                }

            }
            catch (Exception e){
                CLogManager.error(e);
            }
        }
        if(start<counts)
            task(baseurl);
        log.info("记录书评条数："+counts);
    }

    public String getTitle(Document doc){
        return doc.select("div#content").select("h1").select("span").text();
    }

    public String getContent(Document doc){
        return doc.select("div#link-report").select("div.review-content").text();
    }

    public String getDoubanuserid(Document doc) {
        String id=doc.select("div.article").select("a.avatar").attr("abs:href").split("people/")[1].replace("/","");
        return id;
    }

    public String getUserName(Document doc){
        String uname=doc.select("div.article").select("header").select("a>span").text();
        return uname;
    }

    public String getUserAvatar(Document doc){
        return doc.select("div.article").select("a.avatar").select("img").attr("src");
    }

    public void setBookid(String url) {
        bookid=Long.valueOf(baseurl.split("review")[0].replace("/","").split("subject")[1].trim());
    }

    public short getRate(Document doc) {
        String evaluate=doc.select("div.article").select("header").select("div.header-more").select("span.main-title-rating").attr("title");
        short rate=0;
        switch (evaluate){
            case "力荐" : rate=5;break;
            case "推荐" : rate=4;break;
            case "还行" : rate=3;break;
            case "较差" : rate=2;break;
            case "很差" : rate=1;break;
        }
        return rate;
    }

    public Integer getFollownum(Document doc) {
        String userful=doc.select("div.main-panel-useful").select("button.useful_count").text();
        return Integer.valueOf(SpiderTool.removeZh(userful).trim());
    }

    public Integer getDisfollownum(Document doc) {
        String unuserful=doc.select("div.main-panel-useful").select("button.useless_count").text();
        return Integer.valueOf(SpiderTool.removeZh(unuserful).trim());
    }

    public String getReviewRecusers(String url) {
        String reviewRecusers=null;
        try{
            Document doc=SpiderTool.Getdoc(url+"?tab=recommendations",3,true);
            Elements recUs=doc.select("div#recommendations").select("div.review-rec-list").select("li");
            UserEntity user;
            int recN=0;
            Long[] userids=new Long[recUs.size()];
            for (Element el:recUs) {
                user=new UserEntity();
                String userid=el.select("div.content").select("a[href]").attr("abs:href").split("people/")[1].replace("/","");
                user.setDoubanuserid(userid);
                String uname=el.select("div.pic").select("img").attr("alt");
                //user.setInserttime(DateUtil.string2Time(el.select("div.content").select("a[href]").first().child(0).text(),"yyyy-MM-dd"));
                user.setUname(uname);
                String avatar=el.select("div.pic").select("img").attr("src");
                user.setAvatar(avatar);
                user.setFlag(0);
                userids[recN++]=doubanDataRep.getUserID(user);
            }
            reviewRecusers=Joiner.on(",").join(userids).toString();
        }catch (Exception e){
            CLogManager.error(e);
        }
        return reviewRecusers;
    }

    public String getReviewLikeuser(String url) {
        String reviewLikeuser=null;
        try{
            Document doc=SpiderTool.Getdoc(url+"?tab=likes#likes",3,true);
            Elements likeUs=doc.select("div#likes").select("div.review-fav-list").select("li");
            UserEntity user;
            int recN=0;
            Long[] userids=new Long[likeUs.size()];
            for (Element el:likeUs) {
                user=new UserEntity();
                String userid=el.select("a[href]").attr("abs:href").split("people/")[1].replace("/","");
                user.setDoubanuserid(userid);
                String uname= el.select("div.pic").select("img").attr("alt");
                //user.setInserttime(DateUtil.string2Time(el.select("div.content").select("a[href]").first().child(0).text(),"yyyy-MM-dd"));
                user.setUname(uname);
                String avatar=el.select("div.pic").select("img").attr("src");
                user.setAvatar(avatar);
                user.setFlag(0);
                userids[recN++]=doubanDataRep.getUserID(user);
            }
            reviewLikeuser=Joiner.on(",").join(userids).toString();
        }catch (Exception e){
            CLogManager.error(e);
        }
        return reviewLikeuser;
    }

    private Timestamp getRatedate(Document doc){
        String date=doc.select("header").select("span").last().text();
        Timestamp time= null;
        try {
            time = DateUtil.string2Time(date,"yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            CLogManager.error(e);
        }
        return time;
    }


}
