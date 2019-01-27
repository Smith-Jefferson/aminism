package org.aminism.spider.strategy;

import org.aminism.spider.tool.DateUtil;
import org.aminism.spider.tool.SpiderTool;
import org.aminism.spider.dao.DoubanDataRep;
import org.aminism.spider.entity.DoubanbookCommentEntity;
import org.aminism.spider.entity.UserEntity;
import org.aminism.spider.log.BloomFilterUtil;
import org.aminism.spider.log.CLogManager;
import org.hibernate.Session;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

/**
 * Created by hello world on 2017/1/13.
 */
@Component
public class DoubanBookComment{
    private static final Logger log = LoggerFactory.getLogger(DoubanBookComment.class);
    @Autowired
    private DoubanDataRep doubanDataRep;
    @Autowired
    private UserElementAnalysis userElementAnalysis;
    private String url;
    private long bookID;
    private int pageCount=1;
    private int currentPage=1;
    private static int count=0;


    public DoubanBookComment() {
    }


    public void run(String url) {
        setBookid(url);
        try {
            task(url);
        }
        catch (Exception e) {
            CLogManager.error(e);
        }
    }

    public void task(String url){
        Document doc= SpiderTool.Getdoc(url,3,false);
        ++count;
        save(doc,this.bookID);
        log.info("记录短评条数："+count);
        pageCount=Integer.parseInt(SpiderTool.OnlyNo(SpiderTool.removeZh(doc.select("span#total-comments").text()).trim()));
        if(currentPage++<(int)Math.ceil((double)pageCount/(double) 20)){
            try {
                Thread.sleep(4000);
            }catch (Exception e){
                CLogManager.error(e);
            }
            task(url+"hot?p="+currentPage);
        }
    }

    public void save(Document doc,Long bookID) {
        Elements elements=doc.select("div#comments").select("li.comment-item");
        for (Element el:elements) {
            DoubanbookCommentEntity comment=new DoubanbookCommentEntity();
            try {
                UserEntity user=doubanDataRep.getUser(
                        getDoubanuserid(el),getUserName(el),getUserAvatar(el));
                comment.setUserid(user.getUserid());
                comment.setBookid(bookID);
                comment.setDoubanuserid(user.getDoubanuserid());
                comment.setRate(getRate(el));
                comment.setComment(getComment(el));
                comment.setRatedate(getRateDate(el));
                comment.setFollownum(getFollownum(el));
                doubanDataRep.saveComment(comment);
            }catch (Exception e){
                CLogManager.error(el.text(),e);
            }
        }
    }

    private String getDoubanuserid(Element element) {
        return userElementAnalysis.getDoubanuserid(element.select("span.comment-info"));
    }

    private String getUserName(Element element){
        return userElementAnalysis.getUserName(element.select("span.comment-info"));
    }

    public String getUserAvatar(Element element){
        return userElementAnalysis.getUserAvatar(element.select("div.avatar"));
    }

    public void setBookid(String url) {
        String bookid = url.split("subject/")[1].split("/")[0];
        bookID=Long.valueOf(bookid);
    }

    @Autowired
    RateStartConvert rateStartConvert;
    public short getRate(Element element) {
        String evaluate=element.select("span.user-stars").attr("title");
        return rateStartConvert.convert(evaluate);
    }

    public String getComment(Element element) {
        return element.select("p.comment-content").text();
    }

    public Date getRateDate(Element element){
        String ratedate=null;
        try {
            ratedate= element.select("span.comment-info").select("span").last().text();
            return DateUtil.toSqlDate(DateUtil.parseDate(ratedate,"yyyy-MM-dd"));
        }catch (Exception e){
            CLogManager.error(e);
        }
        return new Date(System.currentTimeMillis());
    }
    public Integer getFollownum(Element element) {
        String follws=element.select("span.comment-vote").select("span.vote-count").text();
        return Integer.valueOf(follws);
    }
}
