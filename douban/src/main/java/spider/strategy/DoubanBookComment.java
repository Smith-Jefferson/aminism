package spider.strategy;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.App;
import spider.database.DoubanDataRep;
import spider.model.DoubanbookCommentEntity;
import spider.model.LogLevel;
import spider.model.UserEntity;
import spider.pool.SessionPool;
import spider.service.LogManager;
import spider.tool.DateUtil;
import spider.tool.SpiderTool;

import java.sql.Date;
import java.util.List;

/**
 * Created by hello world on 2017/1/13.
 */
public class DoubanBookComment implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(DoubanBookComment.class);
    private String url;
    private long bookID;
    private int pageCount=1;
    private int currentPage=1;
    private static int count=0;
    private StringBuilder str=new StringBuilder();
    public DoubanBookComment(String url) {
        this.url = url;
    }
    DoubanbookCommentEntity comment;
    @Override
    public void run() {
        setBookid(url);
        try {
            task(url);
        }
        catch (Exception e) {
            log.error(e.getStackTrace().toString());
            LogManager.writeLog(e, LogLevel.FATAL,url);
        }
    }

    public void task(String url){
        Document doc= SpiderTool.Getdoc(url,3,false);
        Elements elements=doc.select("div#comments").select("li.comment-item");
        UserEntity user;
        Session session;
        for (Element el:elements) {
            comment=new DoubanbookCommentEntity();
            user=new UserEntity();
            comment.setId(++count);
            user.setDoubanuserid(getDoubanuserid(el));
            user.setUname(getUserName(el));
            user.setAvatar(getUserAvatar(el));
            user.setFlag(0);
            long userid=user.getUserID(user);
            try {
                session= SessionPool.getSession();
                comment.setUserid(userid);
                comment.setBookid(bookID);
                comment.setDoubanuserid(user.getDoubanuserid());
                comment.setRate(getRate(el));
                comment.setComment(getComment(el));
                comment.setRatedate(getRateDate(el));
                comment.setFollownum(getFollownum(el));
                if(!App.getBloomFilter().ContainedThenAdd(str.append(comment.getDoubanuserid()).append(comment.getBookid()).toString())){
                    Transaction transaction=session.beginTransaction();
                    session.save(comment);
                    transaction.commit();
                    SessionPool.freeSession(session);
                }
            }catch (Exception e){
                log.error(el.toString(),e);
                LogManager.writeLog(e, LogLevel.FATAL,el.toString());
            }

            str.delete(0,str.length());
        }
        log.info("记录短评条数："+count);
        pageCount=Integer.parseInt(SpiderTool.OnlyNo(SpiderTool.removeZh(doc.select("span#total-comments").text()).trim()));
        if(currentPage++<(int)Math.ceil((double)pageCount/(double) 20)){
            try {
                Thread.sleep(4000);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            task(url+"hot?p="+currentPage);
        }
    }


    private String getUserAvatar(Element element){
        return element.select("div.avatar").select("img").attr("src");
    }

    private String getUserName(Element element){
        return element.select("span.comment-info").select("a[href]").text();
    }

    public String getDoubanuserid(Element element) {
        return element.select("span.comment-info").select("a[href]").attr("abs:href").split("people/")[1].split("/")[0];
    }

    public void setBookid(String url) {
        String bookid = url.split("subject/")[1].split("/")[0];
        bookID=Long.valueOf(bookid);
    }

    public short getRate(Element element) {
        String evaluate=element.select("span.user-stars").attr("title");
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

    public String getComment(Element element) {
        return element.select("p.comment-content").text();
    }

    public Date getRateDate(Element element){
        String ratedate=null;
        try {
            ratedate= element.select("span.comment-info").select("span").last().text();
            return DateUtil.toSqlDate(DateUtil.parseDate(ratedate,"yyyy-MM-dd"));
        }catch (Exception e){
            log.error(ratedate+e.getMessage());
        }
        return new Date(System.currentTimeMillis());
    }
    public Integer getFollownum(Element element) {
        String follws=element.select("span.comment-vote").select("span.vote-count").text();
        return Integer.valueOf(follws);
    }
}
