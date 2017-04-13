package spider.strategy;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.model.DoubanbookOfferEntity;
import spider.model.UserEntity;
import spider.pool.SessionPool;
import spider.tool.DateUtil;
import spider.tool.SpiderTool;

import java.sql.Timestamp;

/**
 * Created by hello world on 2017/1/20.
 */
public class DoubanbookOffer implements Runnable{
    private String url;
    private static final Logger log = LoggerFactory.getLogger(DoubanbookOffer.class);
    private int max;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public DoubanbookOffer(){}

    public DoubanbookOffer(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        Document doc= SpiderTool.Getdoc(url,3);
        int current=1;
        getMaxpage(doc);
        task(doc);
        while(max>current){
            doc= SpiderTool.Getdoc(url+"?start="+15*current++,3);
            task(doc);
        }
    }
    public void task(Document doc){
        Session session= SessionPool.getSession();
        Transaction transaction=session.beginTransaction();
        Elements content=doc.select("div#content");
        if(content!=null){
            content=content.select("table");
            content.remove(0);
            for (Element el:content) {
                DoubanbookOfferEntity offer=new DoubanbookOfferEntity();
                UserEntity user=new UserEntity();
                user.setFlag(0);
                user.setAvatar(getAvatar(el));
                user.setDoubanuserid(getDoubanuserid(el));
                user.setUname(getUname(el));
                long userid=user.getUserID(user);
                offer.setUserid(userid);
                offer.setBookid(getBookid(url));
                offer.setPrice(getPrice(el));
                offer.setOfferdate(getDate(el));
                offer.setMark(getRemark(el));
                session.save(offer);
            }
            transaction.commit();
            SessionPool.freeSession(session);
        }
    }

    private void getMaxpage(Document doc){
        Elements pages=doc.select("div.paginator").select("a[href]");
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
    }
    public String getAvatar(Element el){
        return el.select("td").get(0).select("img").attr("src");
    }

    public String getDoubanuserid(Element el){
        return el.select("td").get(0).select("a[href]").attr("abs:href").split("people")[1].replace("/","");
    }


    public double getPrice(Element el){
        String price=el.select("td").get(2).text().replace("元","");
        return Double.parseDouble(price);
    }

    public String getCity(Element el){
        String city=el.select("td").get(3).getAllElements().get(0).text();
        return city;
    }

    public Timestamp getDate(Element el){
        Elements els=el.select("td").get(4).getAllElements();
        String date=els.get(0).childNode(2).toString().replace("&nbsp;","").trim();
        Timestamp time=null;
        try {
            time=DateUtil.string2Time(date,"yyyy-MM-dd");
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return time;
    }

    public String getUname(Element el){
        return el.select("td").get(4).select("a[href]").text();
    }

    public String getRemark(Element el){
        Elements els=el.select("td").get(4).select("p");
        String remark=els.text();
        return remark;
    }

    public long getBookid(String url){
        String bookid=url.split("subject/")[1].split("/")[0];
        return Long.parseLong(bookid);
    }
}
