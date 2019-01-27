package org.aminism.spider.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aminism.spider.constant.Urltag;
import org.aminism.spider.dao.DoubanDataRep;
import org.aminism.spider.dao.TaskUrlDao;
import org.aminism.spider.entity.DoubanbookEntity;
import org.aminism.spider.entity.Reponse;
import org.aminism.spider.entity.TaskUrlEntity;
import org.aminism.spider.log.CLogManager;
import org.aminism.spider.service.LogManager;
import org.aminism.spider.tool.DateUtil;
import org.aminism.spider.tool.SpiderTool;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hello world on 2017/1/12.
 */
@Component
public class DoubanBookDetail {
    @Autowired
    LogManager logManager;

    @Autowired
    private DoubanDataRep doubanDataRep;
    @Autowired
    DouBanBookReadNoteServer readNoteServer;

    public void run(String url) {
        try {
            Document doc = SpiderTool.Getdoc(url, 3, false);
            task(url,doc);
        } catch (Exception e) {
            CLogManager.error("DoubanBookDetail", e, url);
        }

    }

    public void task(String url,Document doc) throws Exception {
        DoubanbookEntity doubanbook = new DoubanbookEntity();
        fillData(url, doc, doubanbook);
        doubanDataRep.saveOrUpdateBookDerail(doubanbook);
        doubanDataRep.deleteTaskUrl(url);
        saveMoreInfo(doc,doubanbook.getBookid());
        Elements ems = doc.getElementsContainingText("二手书欲转让");
        if (ems != null && ems.size() > 0) {
            String offerurl = ems.select("a[href]").last().attr("abs:href");
            taskUrlDao.save(new TaskUrlEntity(offerurl, Urltag.bookoff));
        }
    }

    private void fillData(String url, Document doc, DoubanbookEntity doubanbook) {
        doubanbook.setRate(getRate(doc));
        doubanbook.setExtention(getExtention(doc));
        doubanbook.setBookid(getBookid(url));
        doubanbook.setAuthor(getAuthor(doc));
        doubanbook.setPublisher(getPublisher(doc));
        doubanbook.setPublishdate(getPublishdate(doc));
        doubanbook.setPageno(getPageno(doc));
        doubanbook.setPrice(getPrice(doc));
        doubanbook.setBinding(getBinding(doc));
        doubanbook.setIsbn(getIsbn(doc));
        doubanbook.setBookintro(getBookintro(doc));
        doubanbook.setMenu(getMenu(doc,doubanbook.getBookid()));
        doubanbook.setSample(getSample(doc));
        doubanbook.setTagids(getTagids(doc));
        doubanbook.setDoubanIbcf(getDoubanIbcf(doc));
        doubanbook.setDoubanUbcf(getDoubanUbcf(doc));
        doubanbook.setUrl(url);
        doubanbook.setFaceimg(getFaceimg(doc));
        doubanbook.setBookname(getBookname(doc));
        doubanbook.setAuthorintro(getAuthorintro(doc));
    }

    @Autowired
    DoubanBookComment bookComment;
    @Autowired
    DoubanBookReview doubanBookReview;

    private void saveMoreInfo(Document doc, Long bookid) {
        CLogManager.info("获取该书的短评");
        bookComment.save(doc, bookid);
        CLogManager.info("获取该书的书评");
        doubanBookReview.save(doc, bookid);
        CLogManager.info("获取该书的笔记");
        readNoteServer.save(doc, bookid);
    }

    public Long getBookid(String url) {
        String id = url.split("subject/")[1].replace("/", "");
        return Long.valueOf(id);
    }

    public String getAuthor(Document doc) {
        String author = null;
        try {
            author = doc.select("div.indent").select("div#info").select("a[href]").get(0).text();
        } catch (Exception e) {
            CLogManager.error("setAuthor", e, doc.select("div.indent").select("div#info").select("a[href]").html());
        }
        return author;
    }

    public String getPublisher(Document doc) {
        String publisher = null;
        Elements els = doc.select("div.indent").select("div#info").select("span");
        for (Element el : els) {
            if (el.text().equals("出版社:")) {
                publisher = el.nextSibling().outerHtml();
                break;
            }
        }
        return publisher;

    }

    public Date getPublishdate(Document doc) {
        Elements els = doc.select("div#info").select("span");
        for (Element el : els) {
            if (el.text().equals("出版年:")) {
                String publishdate = el.nextSibling().outerHtml();
                return DateUtil.toSqlDate(DateUtil.parseDate(publishdate, "yyyy-MM"));
            }
        }
        return null;
    }

    public Integer getPageno(Document doc) {
        Elements els = doc.select("div#info").select("span");
        for (Element el : els) {
            if ("页数:".equals(el.text())) {
                String pageno = el.nextSibling().outerHtml().trim();
                return Integer.valueOf(SpiderTool.OnlyNo(pageno));
            }
        }
        return null;
    }

    public String getBinding(Document doc) {
        Elements els = doc.select("div#info").select("span");
        for (Element el : els) {
            if ("装帧:".equals(el.text())) {
                String binding = el.nextSibling().outerHtml().trim();
                return binding;
            }
        }
        return null;
    }

    public Double getPrice(Document doc) {
        Elements els = doc.select("div#info").select("span");
        for (Element el : els) {
            if ("定价:".equals(el.text())) {
                String price = SpiderTool.OnlyNo(el.nextSibling().outerHtml().replace("元", "").replace("CNY", "").replace("RMB", "")).trim();
                if (price != "") {
                    return Double.valueOf(price);
                }

            }
        }
        return null;
    }

    public Long getIsbn(Document doc) {
        Elements els = doc.select("div#info").select("span");
        for (Element el : els) {
            if ("ISBN:".equals(el.text())) {
                String isbn = el.nextSibling().outerHtml().trim();
                return Long.valueOf(SpiderTool.OnlyNo(isbn));
            }
        }
        return null;
    }

    public String getBookintro(Document doc) {
        String intro = doc.select("div.related_info").select("div#link-report").select("span.all").select("div.intro").html();
        if ("".equals(intro))
            intro = doc.select("div.related_info").select("div#link-report").select("div.intro").html();
        return intro;
    }

    public String getAuthorintro(Document doc) {
        String authorintro = null;
        try {
            authorintro = doc.select("div.related_info").select("div.indent").select("div.intro").last().text();
        } catch (Exception e) {
            CLogManager.error("没有找到作者简介" + e.getMessage(), e);
        }

        return authorintro;
    }

    public String getMenu(Document doc,long bookid) {
        String menu = doc.select("div#dir_" + bookid + "_full").html();
        if (!"".equals(menu)) {
            menu = menu.substring(0, menu.lastIndexOf("<a") - 14);
        }
        return menu;
    }

    public String getSample(Document doc) {
        Elements els = doc.select("div.related_info").select("div.indent").select("a[href]");
        for (Element el : els) {
            String url = el.absUrl("href");
            if (url != null && url.contains("book") && url.contains("reading")) {
//                url = "https://book.douban.com/reading/" + getBookid() + "/";
//                Document sample = SpiderTool.Getdoc(url, 3, false);
//                try {
//                    doubanbook.setSample(sample.select("div#content").select("div.book-info").text());
//                } catch (Exception e) {
//                    CLogManager.error("setSample", e);
//                }
            }
        }
        return null;
    }

    public String getTagids(Document doc) {
        Elements elements = doc.select("div#db-tags-section").select("span a[href]");
        return elements.stream().map(el -> el.text()).collect(Collectors.joining(","));
    }

    @Autowired
    TaskUrlDao taskUrlDao;

    public String getDoubanIbcf(Document doc) {
        Elements elements = doc.select("div#rec-ebook-section").select("dl");
        ArrayList<JSONObject> list = new ArrayList<>(elements.size());
        List<String> urls = new ArrayList<>();
        JSONObject jsonObject;
        for (Element el : elements) {
            jsonObject = new JSONObject();
            jsonObject.put("cover", el.select("img").attr("src"));
            jsonObject.put("title", el.select("div.title").select("a[href]").text());
            String url = el.select("div.title").select("a[href]").attr("abs:href");
            jsonObject.put("url", url);
            jsonObject.put("price", el.select("div.price").text());
            list.add(jsonObject);
            urls.add(url);
        }
        if (CollectionUtils.isNotEmpty(urls)) {
            taskUrlDao.save(urls.stream().map(x -> new TaskUrlEntity(x, Urltag.book)).collect(Collectors.toList()));
        }
        String recEbook = JSON.toJSONString(list);
        return recEbook;
    }

    public String getDoubanUbcf(Document doc) {
        Elements elements = doc.select("div#db-rec-section").select("dl");
        ArrayList<JSONObject> list = new ArrayList<>(elements.size());
        List<String> urls = new ArrayList<>();
        JSONObject jsonObject;
        for (Element el : elements) {
            jsonObject = new JSONObject();
            jsonObject.put("cover", el.select("img").attr("src"));
            jsonObject.put("title", el.select("dd").select("a[href]").text());
            String url = el.select("dd").select("a[href]").attr("abs:href");
            jsonObject.put("url", url);
            list.add(jsonObject);
            urls.add(url);
        }
        if (CollectionUtils.isNotEmpty(urls)) {
            taskUrlDao.save(urls.stream().map(x -> new TaskUrlEntity(x, Urltag.book)).collect(Collectors.toList()));
        }
        String recbook = JSON.toJSONString(list);
        return recbook;
    }


    public String getFaceimg(Document doc) {
        return doc.select("div#mainpic").select("img").attr("src");
    }

    public String getBookname(Document doc) {
        String title = doc.select("div#wrapper").select("h1").get(0).select("span").text();
        return title;
    }

    public String getRate(Document doc) {
        Elements rating_wrap = doc.select("div.rating_wrap");
        JSONObject jsonObject = new JSONObject();
        String ratenum = rating_wrap.select("div.rating_self").select("strong.rating_num").text();
        if ("".equals(ratenum)) {
            return null;
        } else {
            jsonObject.put("ratenum", Float.valueOf(ratenum));
            Elements rating_per = rating_wrap.select(".rating_per");
            Elements rating_label = rating_wrap.select(".starstop");
            for (int i = 0; i < rating_per.size(); i++) {
                jsonObject.put(rating_label.get(i).text(), rating_per.get(i).text());
            }
            return JSON.toJSONString(jsonObject);
        }

    }

    public String getExtention(Document doc) {
        //从哪里可以借到书
        JSONObject jsonObject = new JSONObject();
        Elements borrowinfo = doc.select("div#borrowinfo");
        if (borrowinfo.size() > 0) {
            borrowinfo = borrowinfo.select("ul>li");
            List<JSONObject> libs = new ArrayList<>(borrowinfo.size());
            for (Element info : borrowinfo) {
                JSONObject lib = new JSONObject();
                lib.put("name", info.select("a[href]").text());
                String liburl = info.select("a[href]").attr("abs:href");
                liburl = liburl.split("url=")[1];
                try {
                    liburl = java.net.URLDecoder.decode(liburl, "utf-8");
                } catch (Exception e) {
                    CLogManager.error("setExtention:liburl", e, liburl);
                }
                lib.put("url", liburl);
                libs.add(lib);
            }
            jsonObject.put("borrow", libs);
        }
        Elements buyinfo = doc.select("div#buyinfo");
        if (buyinfo.size() == 0)
            buyinfo = doc.select("#buyinfo-printed");
        if (buyinfo.size() > 0) {
            buyinfo = buyinfo.select("ul>li").select("a[href]");
            List<JSONObject> editions = new ArrayList<>(borrowinfo.size());
            for (Element info : buyinfo) {
                JSONObject edition = new JSONObject();
                String seller = info.getAllElements().get(0).text();
                edition.put("seller", seller);
                String sellerurl = info.select("a[href]").attr("abs:href");
                try {
                    sellerurl = java.net.URLDecoder.decode(sellerurl, "utf-8");
                } catch (Exception e) {
                    CLogManager.error("setExtention:sellerurl", e, sellerurl);
                }
                edition.put("url", sellerurl);
                if (info.select("span.buylink-price") != null)
                    edition.put("price", info.select("span.buylink-price").text());
                editions.add(edition);
            }
            jsonObject.put("edition", editions);
        }
        return JSON.toJSONString(jsonObject);
    }


}
