package org.aminism.spider.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aminism.spider.App;
import org.aminism.spider.model.LogLevel;
import org.aminism.spider.service.LogManager;
import org.aminism.spider.tool.DateUtil;
import org.aminism.spider.tool.SpiderTool;
import org.aminism.spider.tool.WorkContext;
import org.aminism.spider.dao.DoubanDataRep;
import org.aminism.spider.entity.DoubanbookEntity;
import org.aminism.spider.log.CLogManager;
import com.google.common.base.Joiner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hello world on 2017/1/12.
 */
public class DoubanBookDetail implements Runnable {
    private WorkContext ctx;
    private Set<String> books;
    private DoubanbookEntity doubanbook;
    public DoubanBookDetail(Set<String> books) {
        this.books = books;
    }

    @Autowired
    private DoubanDataRep doubanDataRep;
    @Override
    public void run() {
        for (String url:books) {
            ctx=new WorkContext(url);
            try{
                task(url);
            }catch (Exception e){
                CLogManager.error("DoubanBookDetail",e,url);
            }
            ctx.flush();
        }
    }

    public void task(String url) throws Exception{
        doubanbook=new DoubanbookEntity();
        Document doc= SpiderTool.Getdoc(url,3,false);
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
        doubanDataRep.saveOrUpdateBookDerail(doubanbook);
        doubanDataRep.deleteTaskUrl(url);
        ctx.info("获取该书的短评");
        if(url.endsWith("/")){
            url=url.substring(0,url.length()-1);
        }
        DoubanBookComment comment=new DoubanBookComment(url+"/comments/");
        try {
            App.fixedThreadPool.execute(comment);
            //comment.run();
        }catch (Exception e){
            CLogManager.error("task",e,url);
        }

        ctx.info("获取该书的书评");
        DoubanBookReview review=new DoubanBookReview(url+"/reviews");
        try {
            App.fixedThreadPool.execute(review);
            //review.run();
        } catch (Exception e) {
            CLogManager.error("task",e,url);
        }
        Elements ems=doc.getElementsContainingText("二手书欲转让");
        if(ems!=null && ems.size()>0){
            String offerurl=ems.select("a[href]").last().attr("abs:href");
            DoubanbookOffer offer=new DoubanbookOffer(offerurl);
            //offer.run();
            try{
                App.fixedThreadPool.execute(offer);
                //bookoffer.join();
            }catch (Exception e)
            {
                CLogManager.error("task",e,url);
            }
        }
    }

    public void setBooks(Set<String> books) {
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
        String author=null;
        try {
            author=doc.select("div.indent").select("div#info").select("a[href]").get(0).text();
        }catch (Exception e){
            CLogManager.error("setAuthor",e,doc.select("div.indent").select("div#info").select("a[href]").html());
        }
        doubanbook.setAuthor(author);
    }

    public void setPublisher(Document doc) {
        String publisher =null;
        Elements els=doc.select("div.indent").select("div#info").select("span");
        for (Element el:els){
            if(el.text().equals("出版社:")){
                publisher = el.nextSibling().outerHtml();
                break;
            }
        }
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
                doubanbook.setPageno(Integer.valueOf(SpiderTool.OnlyNo(pageno)));
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
                String price = SpiderTool.OnlyNo(el.nextSibling().outerHtml().replace("元","").replace("CNY","").replace("RMB","")).trim();
                if (price!=""){
                    doubanbook.setPrice(Double.valueOf(price));
                    break;
                }

            }
        }
    }

    public void setIsbn(Document doc) {
        Elements els=doc.select("div#info").select("span");
        for (Element el:els){
            if("ISBN:".equals(el.text())) {
                String isbn = el.nextSibling().outerHtml().trim();
                doubanbook.setIsbn(Long.valueOf(SpiderTool.OnlyNo(isbn)));
                break;
            }
        }
    }

    public void setBookintro(Document doc) {
        String intro=doc.select("div.related_info").select("div#link-report").select("span.all").select("div.intro").html();
        if("".equals(intro))
            intro=doc.select("div.related_info").select("div#link-report").select("div.intro").html();
        doubanbook.setBookintro(intro);
    }

    public void setAuthorintro(Document doc) {
        String authorintro = null;
        try{
            authorintro=doc.select("div.related_info").select("div.indent").select("div.intro").last().text();
        }catch (Exception e){
            LogManager.writeLog("没有找到作者简介"+e.getMessage(), LogLevel.WARM);
        }

        doubanbook.setAuthorintro(authorintro);
    }

    public void setMenu(Document doc) {
        String menu=doc.select("div#dir_"+doubanbook.getBookid()+"_full").html();
        if(!"".equals(menu)) {
            menu=menu.substring(0,menu.lastIndexOf("<a")-14);
        }
        doubanbook.setMenu(menu);
    }

    public void setSample(Document doc) {
        Elements els=doc.select("div.related_info").select("div.indent").select("a[href]");
        for(Element el:els){
            String url=el.absUrl("href");
            if(url!=null && url.contains("book")&&url.contains("reading")){
                url="https://book.douban.com/reading/"+doubanbook.getBookid()+"/";
                Document sample= SpiderTool.Getdoc(url,3,false);
                try{
                    doubanbook.setSample(sample.select("div#content").select("div.book-info").text());
                }catch (Exception e){
                    CLogManager.error("setSample",e);
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
        if("".equals(ratenum)){
            doubanbook.setRate(null);
        }
        else {
            jsonObject.put("ratenum",Float.valueOf(ratenum));
            Elements rating_per=rating_wrap.select(".rating_per");
            Elements rating_label=rating_wrap.select(".starstop");
            for (int i = 0; i <rating_per.size() ; i++) {
                jsonObject.put(rating_label.get(i).text(),rating_per.get(i).text());
            }
            doubanbook.setRate(JSON.toJSONString(jsonObject));
        }

    }

    public void setExtention(Document doc){
        //从哪里可以借到书
        JSONObject jsonObject = new JSONObject();
        Elements borrowinfo= doc.select("div#borrowinfo");
        if(borrowinfo.size()>0){
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
                    CLogManager.error("setExtention:liburl",e,liburl);
                }
                lib.put("url",liburl);
                libs.add(lib);
            }
            jsonObject.put("borrow",libs);
        }
        Elements buyinfo=doc.select("div#buyinfo");
        if(buyinfo.size()==0)
            buyinfo=doc.select("#buyinfo-printed");
        if(buyinfo.size()>0){
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
                    CLogManager.error("setExtention:sellerurl",e,sellerurl);
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
