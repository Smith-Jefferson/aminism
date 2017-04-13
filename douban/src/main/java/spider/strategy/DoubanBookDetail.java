package spider.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.App;
import spider.model.DoubanbookEntity;
import spider.pool.SessionPool;
import spider.tool.DateUtil;
import spider.tool.SpiderTool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by hello world on 2017/1/12.
 */
public class DoubanBookDetail implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(DoubanBookDetail.class);
    private String[] books;
    private DoubanbookEntity  doubanbook;
    public DoubanBookDetail(String[] books) {
        this.books = books;
    }

    @Override
    public void run() {
        for (String url:books) {
            try{
                task(url);
            }catch (Exception e){
                log.error(e.getMessage());
            }

        }
        log.info(Thread.currentThread().getName() + ":"  + "结束");
    }

    public void task(String url) throws Exception{
        doubanbook=new DoubanbookEntity();
        Document doc= SpiderTool.Getdoc(url,3);
        setRate(doc);
        setExtention(doc);
        setBookid(url);
        setAuthor(doc);
        setPublisher(doc);
        setPublishdate(doc);
        setPageno(doc);
        setPrice(doc);
        setBinding(doc);
        setIsbn(doc);
        setBookintro(doc);
        setMenu(doc);
        setSample(doc);
        setTagids(doc);
        setDoubanIbcf(doc);
        setDoubanUbcf(doc);
        setUrl(url);
        setFaceimg(doc);
        setBookname(doc);
        setAuthorintro(doc);
        Session session= SessionPool.getSession();
        Transaction transaction=session.beginTransaction();
        if(App.getBloomFilter().contains(doubanbook.getUrl())){
            session.update(doubanbook);
        }else{
            session.save(doubanbook);
        }
        transaction.commit();
        SessionPool.freeSession(session);
        log.info("获取该书的短评");
        DoubanBookComment comment=new DoubanBookComment(url+"/comments/");
        comment.run();
        ///
        Thread bookcomment=new Thread(comment);
        bookcomment.start();
        log.info("获取该书的书评");
        DoubanBookReview review=new DoubanBookReview(url+"reviews");
        Thread bookview=new Thread(review);
        bookview.start();
        Elements ems=doc.getElementsContainingText("二手书欲转让");
        if(ems!=null && ems.size()>0){
            String offerurl=ems.select("a[href]").last().attr("abs:href");
            DoubanbookOffer offer=new DoubanbookOffer(offerurl);
            Thread bookoffer=new Thread(offer);
            bookoffer.start();
            bookoffer.join();
        }
        bookview.join();
        bookcomment.join();
    }

    public void setBooks(String[] books) {
        this.books = books;
    }

    public void setDoubanbookEntity(DoubanbookEntity doubanbookEntity) {
        this. doubanbook = doubanbookEntity;
    }

    public void setBookid(String url) {
        String id=url.split("subject/")[1].replace("/","");
        doubanbook.setBookid(Long.valueOf(id));
    }

    public void setAuthor(Document doc) {
        String author=doc.select("div.indent").select("div#info").select("a[href]").get(0).text();
        doubanbook.setAuthor(author);
    }

    public void setPublisher(Document doc) {
        String publisher = doc.select("div.indent").select("div#info").select("span").get(2).nextSibling().outerHtml();
        doubanbook.setPublisher(publisher);
    }

    public void setPublishdate(Document doc) {
        Elements els=doc.select("div#info").select("span");
        for (Element el:els){
            if(el.text().equals("出版年:")) {
                String publishdate = el.nextSibling().outerHtml();
                doubanbook.setPublishdate(DateUtil.toSqlDate(DateUtil.parseDate(publishdate,"yyyy-MM")));
                break;
            }
        }
    }

    public void setPageno(Document doc) {
        Elements els=doc.select("div#info").select("span");
        for (Element el:els){
            if("页数:".equals(el.text())) {
                String pageno = el.nextSibling().outerHtml().trim();
                doubanbook.setPageno(Integer.valueOf(pageno));
                break;
            }
        }
    }

    public void setBinding(Document doc) {
        Elements els=doc.select("div#info").select("span");
        for (Element el:els){
            if("装帧:".equals(el.text())) {
                String binding = el.nextSibling().outerHtml().trim();
                doubanbook.setBinding(binding);
                break;
            }
        }
    }

    public void setPrice(Document doc){
        Elements els=doc.select("div#info").select("span");
        for (Element el:els){
            if("定价:".equals(el.text())) {
                String price = el.nextSibling().outerHtml().replace("元","").trim();
                doubanbook.setPrice(Double.valueOf(price));
                break;
            }
        }
    }

    public void setIsbn(Document doc) {
        Elements els=doc.select("div#info").select("span");
        for (Element el:els){
            if("ISBN:".equals(el.text())) {
                String isbn = el.nextSibling().outerHtml().trim();
                doubanbook.setIsbn(Long.valueOf(isbn));
                break;
            }
        }
    }

    public void setBookintro(Document doc) {
        String intro=doc.select("div.related_info").select("div#link-report").select("span.all").select("div.intro").html();
        doubanbook.setBookintro(intro);
    }

    public void setAuthorintro(Document doc) {
        String authorintro = doc.select("div.related_info").select("div.indent").select("div.intro").last().text();
        doubanbook.setAuthorintro(authorintro);
    }

    public void setMenu(Document doc) {
        String menu=doc.select("div#dir_"+doubanbook.getBookid()+"_full").html();
        doubanbook.setMenu(menu);
    }

    public void setSample(Document doc) {
        Elements els=doc.select("div.related_info").select("div.indent").select("a[href]");
        for(Element el:els){
            String url=el.absUrl("href");
            if(url!=null && url.contains("book")&&url.contains("reading")){
                url="https://book.douban.com/reading/"+doubanbook.getBookid()+"/";
                Document sample= SpiderTool.Getdoc(url,3);
                try{
                    doubanbook.setSample(sample.select("div#content").select("div.book-info").text());
                }catch (Exception e){
                    log.warn(e.getMessage());
                }
            }
        }
    }

    public void setTagids(Document doc) {
        Elements elements=doc.select("div#db-tags-section").select("span a[href]");
        ArrayList<String> taglist=new ArrayList(elements.size());
        for (Element el:elements) {
            taglist.add(el.text());
        }
        doubanbook.setTagids(Joiner.on(",").join(taglist.toArray()));
    }

    public void setDoubanIbcf(Document doc) {
        Elements elements=doc.select("div#rec-ebook-section").select("dl");
        ArrayList<JSONObject> list=new ArrayList<>(elements.size());
        JSONObject jsonObject;
        for (Element el:elements) {
            jsonObject=new JSONObject();
            jsonObject.put("cover",el.select("img").attr("src"));
            jsonObject.put("title",el.select("div.title").select("a[href]").text());
            jsonObject.put("url",el.select("div.title").select("a[href]").attr("abs:href"));
            jsonObject.put("price",el.select("div.price").text());
            list.add(jsonObject);
        }
        String recEbook=JSON.toJSONString(list);
        doubanbook.setDoubanIbcf(recEbook);
    }

    public void setDoubanUbcf(Document doc) {
        Elements elements=doc.select("div#db-rec-section").select("dl");
        ArrayList<JSONObject> list=new ArrayList<>(elements.size());
        JSONObject jsonObject;
        for (Element el:elements) {
            jsonObject = new JSONObject();
            jsonObject.put("cover",el.select("img").attr("src"));
            jsonObject.put("title",el.select("dd").select("a[href]").text());
            jsonObject.put("url",el.select("dd").select("a[href]").attr("abs:href"));
            list.add(jsonObject);
        }
        String recbook=JSON.toJSONString(list);
        doubanbook.setDoubanUbcf(recbook);
    }

    public void setUrl(String url) {
        doubanbook.setUrl(url);
    }

    public void setFaceimg(Document doc) {
        doubanbook.setFaceimg(doc.select("div#mainpic").select("img").attr("src"));
    }

    public void setBookname(Document doc) {
        String title=doc.select("div#wrapper").select("h1").get(0).select("span").text();
        doubanbook.setBookname(title);
    }

    public void setRate(Document doc){
        Elements rating_wrap=doc.select("div.rating_wrap");
        JSONObject jsonObject = new JSONObject();
        String ratenum=rating_wrap.select("div.rating_self").select("strong.rating_num").text();
        jsonObject.put("ratenum",Float.valueOf(ratenum));
        Elements rating_per=rating_wrap.select("span.rating_per");
        jsonObject.put("stars5",rating_per.get(0).text());
        jsonObject.put("stars4",rating_per.get(1).text());
        jsonObject.put("stars3",rating_per.get(2).text());
        jsonObject.put("stars2",rating_per.get(3).text());
        jsonObject.put("stars1",rating_per.get(4).text());
        doubanbook.setRate(JSON.toJSONString(jsonObject));
    }

    public void setExtention(Document doc){
        //从哪里可以借到书
        JSONObject jsonObject = new JSONObject();
        Elements borrowinfo= doc.select("div#borrowinfo");
        if(borrowinfo!=null){
            borrowinfo=borrowinfo.select("ul>li");
            List<JSONObject> libs=new ArrayList<>(borrowinfo.size());
            for (Element info:borrowinfo) {
                JSONObject lib=new JSONObject();
                lib.put("name",info.select("a[href]").text());
                String liburl=info.select("a[href]").attr("abs:href");
                liburl=liburl.split("url=")[1];
                try{
                    liburl=java.net.URLDecoder.decode(liburl,"utf-8");
                }catch (Exception e){
                    log.error(e.getMessage());
                }
                lib.put("url",liburl);
                libs.add(lib);
            }
            jsonObject.put("borrow",libs);
        }
        Elements buyinfo=doc.select("div#buyinfo");
        if(buyinfo!=null){
            buyinfo=buyinfo.select("ul>li").select("a[href]");
            List<JSONObject> editions=new ArrayList<>(borrowinfo.size());
            for (Element info:buyinfo){
                JSONObject edition=new JSONObject();
                String seller=info.getAllElements().get(0).text();
                edition.put("seller",seller);
                String sellerurl=info.select("a[href]").attr("abs:href");
                try{
                    sellerurl=java.net.URLDecoder.decode(sellerurl,"utf-8");
                }catch (Exception e){
                    log.error(e.getMessage());
                }
                edition.put("url",sellerurl);
                if(info.select("span.buylink-price")!=null)
                    edition.put("price",info.select("span.buylink-price").text());
                editions.add(edition);
            }
            jsonObject.put("edition",editions);
        }
        doubanbook.setExtention(JSON.toJSONString(jsonObject));
    }
}
