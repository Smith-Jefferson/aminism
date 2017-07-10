package spider.strategy;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.App;
import spider.model.DoubanbookReviewCommentEntity;
import spider.model.LogLevel;
import spider.model.UserEntity;
import spider.pool.SessionPool;
import spider.service.LogManager;
import spider.tool.DateUtil;
import spider.tool.SpiderTool;

import java.sql.Timestamp;
import java.text.ParseException;

/**
 * Created by hello world on 2017/1/16.
 */
public class DoubanBookReviewComment implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(DoubanBookReviewComment.class);
    private Document doc;
    private String baseUrl;
    private long bookid;
    private int current=1;
    public DoubanBookReviewComment(String url) {
        baseUrl=url;
        this.doc= SpiderTool.Getdoc(url,3,false);
    }

    public DoubanBookReviewComment(Document doc) {
        this.doc = doc;
    }

    public long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(4000);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        Elements comments=doc.select("div#comments").select("div.comment-item");
        task(comments);
        Elements pages=doc.select("div.paginator");
        int max=1;
        if(pages!=null && pages.size()>0){
            pages=pages.select("a[href]");
            for (Element p:pages) {
                String temp=p.select("a[href]").text();
                if(SpiderTool.isNo(temp)){
                    int tmp=Integer.parseInt(temp);
                    if(max<tmp)
                        max=tmp;
                }
            }
        }
        while(max>current){
            String url=baseUrl+"?start="+current*100;
            current++;
            try {
                try {
                    Thread.sleep(4000);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
                doc=SpiderTool.Getdoc(url,3,false);
                comments=doc.select("div#comments").select("div.comment-item");
                task(comments);
            } catch (Exception e) {
                log.error(e.getStackTrace().toString());
                LogManager.writeLog(e, LogLevel.FATAL,url);
            }
        }
    }

    public void task(Elements comments){
        Session session=null;
        StringBuilder str=new StringBuilder();
        for (Element el:comments) {
            try {
                session= SessionPool.getSession();
                DoubanbookReviewCommentEntity commet=new DoubanbookReviewCommentEntity();
                String doubanuserid= getDoubanUserid(el);
                UserEntity user=new UserEntity();
                user.setDoubanuserid(doubanuserid);
                user.setAvatar(getAvatar(el));
                user.setFlag(0);
                user.setUname(getUserName(el));
                long userid=user.getUserID(user);
                commet.setBookid(getBookid());
                commet.setReviewid(getReviewid());
                commet.setUserid(userid);
                commet.setDoubanuserid(doubanuserid);
                commet.setComment(getComment(el));
                commet.setRatedate(getRatedate(el));
                if(!App.getBloomFilter().ContainedThenAdd(str.append(commet.getDoubanuserid()).append(commet.getBookid()).append(commet.getReviewid()).toString())){
                    Transaction transaction=session.beginTransaction();
                    session.save(commet);
                    transaction.commit();
                }
                str.delete(0,str.length());
            }catch (Exception e){
                log.error(el.toString()+e.getMessage(),e);
            }finally {
                SessionPool.freeSession(session);
            }
        }
    }

    public String getDoubanUserid(Element el){
       return el.select("div.report-comment").select("div.header").select("a[href]").attr("abs:href").split("people")[1].replace("/","");
    }

    public long getReviewid() {
        return Long.valueOf(baseUrl.split("review")[1].replace("/","").trim());
    }

    public String getComment(Element el) {
        return el.select("p.comment-text").text();
    }

    public String getReplyid(Element el) {
        Elements quote= el.select("reply-quote");
        if(quote==null)
            return null;
        return quote.select("span.all").text();
    }
    private Timestamp getRatedate(Element el){
        String date=el.select("div.report-comment").select("div.header").select("span").text();
        Timestamp time= null;
        try {
            time = DateUtil.string2Time(date,"yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return time;
    }

    private String getAvatar(Element el){
        String avatar=el.select("div.avatar").select("img").attr("src");
        return avatar;
    }
    private String getUserName(Element element){
        return element.select("div.report-comment").select("header").select("a[href]").text();
    }
}
