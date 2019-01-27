package org.aminism.spider.strategy;

import org.aminism.spider.dao.DoubanDataRep;
import org.aminism.spider.entity.DoubanbookOfferEntity;
import org.aminism.spider.entity.UserEntity;
import org.aminism.spider.log.CLogManager;
import org.aminism.spider.tool.DateUtil;
import org.aminism.spider.tool.SpiderTool;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Created by hello world on 2017/1/20.
 */
@Component
public class DoubanbookOffer {
    @Autowired
    private DoubanDataRep doubanDataRep;
    private String url;
    private static final Logger log = LoggerFactory.getLogger(DoubanbookOffer.class);
    private int max;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void run(String url) {
        Document doc = SpiderTool.Getdoc(url, 3, false);
        int current = 1;
        getMaxpage(doc);
        task(doc);
        while (max > current) {
            doc = SpiderTool.Getdoc(url + "?start=" + 15 * current++, 3, false);
            task(doc);
        }
    }

    public void task(Document doc) {
        Elements content = doc.select("div#content");
        if (content != null) {
            content = content.select("table");
            if (!content.isEmpty()) {
                content.remove(0);
                for (Element el : content) {
                    try {
                        DoubanbookOfferEntity offer = new DoubanbookOfferEntity();
                        UserEntity user = new UserEntity();
                        user.setFlag(0);
                        user.setAvatar(getAvatar(el));
                        user.setDoubanuserid(getDoubanuserid(el));
                        user.setUname(getUname(el));
                        long userid = doubanDataRep.getUser(user).getUserid();
                        offer.setUserid(userid);
                        offer.setBookid(getBookid(url));
                        offer.setPrice(getPrice(el));
                        offer.setOfferdate(getDate(el));
                        offer.setMark(getRemark(el));
                        doubanDataRep.saveBookOffer(offer);
                    } catch (Exception e) {
                        CLogManager.error(e);
                    }
                }
            }

        }
    }

    private void getMaxpage(Document doc) {
        Elements pages = doc.select("div.paginator").select("a[href]");
        if (pages != null && pages.size() > 0) {
            pages = pages.select("a[href]");
            for (Element p : pages) {
                String temp = p.select("a[href]").text();
                if (SpiderTool.isNo(temp)) {
                    int tmp = Integer.parseInt(SpiderTool.OnlyNo(temp));
                    if (max < tmp)
                        max = tmp;
                }
            }
        }
    }

    public String getAvatar(Element el) {
        return el.select("td").get(0).select("img").attr("src");
    }

    public String getDoubanuserid(Element el) {
        return el.select("td").get(0).select("a[href]").attr("abs:href").split("people")[1].replace("/", "");
    }


    public double getPrice(Element el) {
        String price = el.select("td").get(2).text().replace("元", "");
        return Double.parseDouble(price);
    }

    public String getCity(Element el) {
        String city = el.select("td").get(3).getAllElements().get(0).text();
        return city;
    }

    public Timestamp getDate(Element el) {
        Elements els = el.select("td").get(4).getAllElements();
        String date = els.get(0).childNode(2).toString().replace("&nbsp;", "").trim();
        Timestamp time = null;
        try {
            time = DateUtil.string2Time(date, "yyyy-MM-dd");
        } catch (Exception e) {
            CLogManager.error(e);
        }
        return time;
    }

    public String getUname(Element el) {
        return el.select("td").get(4).select("a[href]").text();
    }

    public String getRemark(Element el) {
        Elements els = el.select("td").get(4).select("p");
        String remark = els.text();
        return remark;
    }

    public long getBookid(String url) {
        String bookid = url.split("subject/")[1].split("/")[0];
        return Long.parseLong(bookid);
    }
}
