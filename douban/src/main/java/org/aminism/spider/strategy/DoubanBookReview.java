package org.aminism.spider.strategy;

import com.google.common.base.Joiner;
import org.aminism.spider.dao.DoubanDataRep;
import org.aminism.spider.dao.DoubanbookReviewDao;
import org.aminism.spider.entity.DoubanbookReviewEntity;
import org.aminism.spider.entity.UserEntity;
import org.aminism.spider.log.BloomFilterUtil;
import org.aminism.spider.log.CLogManager;
import org.aminism.spider.log.TimeUtil;
import org.aminism.spider.tool.DateUtil;
import org.aminism.spider.tool.SpiderTool;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hello world on 2017/1/16.
 */
@Component
public class DoubanBookReview {
    @Autowired
    private DoubanDataRep doubanDataRep;
    private static final Logger log = LoggerFactory.getLogger(DoubanBookReview.class);
    private int start = 0;
    private String baseurl;
    private List<String> reviewsUrl = null;
    private long bookid;
    private String url;

    public void run(String url) {
        setBookid(baseurl);
        task(baseurl);
    }

    public void task(String baseurl) {
        String url = "";
        if (start > 0)
            url = baseurl + "?start=" + start;
        else
            url = baseurl;
        start += 20;
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            CLogManager.error(e);
        }
        Document doc = SpiderTool.Getdoc(url, 3, false);
        int counts = Integer.parseInt(SpiderTool.OnlyNo(SpiderTool.removeZh(doc.select("div#content").select("h1").text().replace("(", "").replace(")", "")).replace(",", "").trim()));
        if (counts <= 0)
            return;
        if (reviewsUrl == null)
            reviewsUrl = new ArrayList<>(counts);
        Elements elements = doc.select("div.review-list").select("div.review-item").select("header");
        for (Element el : elements) {
            reviewsUrl.add(el.select(".title").select("a[href]").attr("abs:href"));
        }

        for (String reviewUrl : reviewsUrl) {
            try {
                StringBuilder str = new StringBuilder();

                Document review = SpiderTool.Getdoc(reviewUrl, 3, false);
                DoubanbookReviewEntity bookreview = new DoubanbookReviewEntity();
                UserEntity user = doubanDataRep.getUser(getDoubanuserid(review), getUserName(review), getUserAvatar(review));
                bookreview.setUserid(user.getUserid());
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
                if (!BloomFilterUtil.getBloomFilter().containedThenAdd(str.append(bookreview.getDoubanuserid()).append(bookreview.getBookid()).append("review").toString())) {
                    doubanDataRep.saveReview(bookreview);
                }
                try {
                    DoubanBookReviewComment reviewComment = new DoubanBookReviewComment(review);
                    reviewComment.setBookid(bookid);
                    reviewComment.run(reviewUrl);
                } catch (Exception e) {
                    CLogManager.error(e);
                }

            } catch (Exception e) {
                CLogManager.error(e);
            }
        }
        if (start < counts)
            task(baseurl);
        log.info("记录书评条数：" + counts);
    }

    @Autowired
    DoubanbookReviewDao doubanbookReviewDao;

    public void save(Document doc, long bookid) {
        Elements elements = doc.select("div.review-list").select("div.review-item");
        List<DoubanbookReviewEntity> list = new ArrayList<>();
        for (Element e : elements) {
            try {
                UserEntity user = doubanDataRep.getUser(getDoubanuserid(e), getUserName(e), getUserAvatar(e));
                DoubanbookReviewEntity bookreview = new DoubanbookReviewEntity();
                bookreview.setBookid(bookid);
                bookreview.setUserid(user.getUserid());
                bookreview.setDoubanuserid(user.getDoubanuserid());
                bookreview.setUrl(getUrl(e));
                bookreview.setDisfollownum(getDisfollownum(e));
                bookreview.setFollownum(getFollownum(e));
                bookreview.setRate(getRate(e));
                bookreview.setReview(getContent(e));
                bookreview.setRatedate(getRatedate(e));
                list.add(bookreview);
            } catch (Exception ex) {
                CLogManager.error(e.toString(), ex);
            }

        }
        doubanbookReviewDao.save(list);
    }

    private String getUrl(Element e) {
        return e.select("div.main-bd").select("h2").select("a[href]").attr("abs:href");
    }

    public String getTitle(Document doc) {
        return doc.select("div#content").select("h1").select("span").text();
    }

    public String getContent(Document doc) {
        return doc.select("div#link-report").select("div.review-content").text();
    }

    public String getContent(Element e) {
        return e.select("div.main-bd").select("div.short-content").text().replace("(展开)", "");
    }

    @Autowired
    UserElementAnalysis userElementAnalysis;

    private String getDoubanuserid(Element element) {
        return userElementAnalysis.getDoubanuserid(element.select("header").select("a.avator"));
    }

    private String getUserName(Element element) {
        return userElementAnalysis.getUserName(element.select("header").select("a.name"));
    }

    private String getUserAvatar(Element element) {
        return userElementAnalysis.getUserAvatar(element.select("header").select("a.avatar"));
    }

    public String getDoubanuserid(Document doc) {
        String id = doc.select("div.article").select("a.avatar").attr("abs:href").split("people/")[1].replace("/", "");
        return id;
    }

    public String getUserName(Document doc) {
        String uname = doc.select("div.article").select("header").select("a>span").text();
        return uname;
    }

    public String getUserAvatar(Document doc) {
        return doc.select("div.article").select("a.avatar").select("img").attr("src");
    }

    public void setBookid(String url) {
        bookid = Long.valueOf(baseurl.split("review")[0].replace("/", "").split("subject")[1].trim());
    }

    @Autowired
    RateStartConvert rateStartConvert;

    public short getRate(Document doc) {
        String evaluate = doc.select("div.article").select("header").select("div.header-more").select("span.main-title-rating").attr("title");
        return rateStartConvert.convert(evaluate);
    }

    public short getRate(Element e) {
        String v = e.select("span.main-title-rating").attr("title");
        return rateStartConvert.convert(v);
    }

    public Integer getFollownum(Document doc) {
        String userful = doc.select("div.main-panel-useful").select("button.useful_count").text();
        return StringUtils.isBlank(userful) ? 0 : Integer.valueOf(SpiderTool.removeZh(userful).trim());
    }

    public Integer getFollownum(Element e) {
        String v = e.select("div.action").select("a.up").select("span").text();
        return StringUtils.isBlank(v) ? 0 :  Integer.parseInt(v);
    }

    public Integer getDisfollownum(Document doc) {
        String unuserful = doc.select("div.main-panel-useful").select("button.useless_count").text();
        return StringUtils.isBlank(unuserful) ? 0 : Integer.valueOf(SpiderTool.removeZh(unuserful).trim());
    }

    public Integer getDisfollownum(Element e) {
        String v = e.select("div.action").select("a.down").select("span").text();
        return StringUtils.isBlank(v) ? 0 : Integer.parseInt(v);
    }

    public String getReviewRecusers(String url) {
        String reviewRecusers = null;
        try {
            Document doc = SpiderTool.Getdoc(url + "?tab=recommendations", 3, true);
            Elements recUs = doc.select("div#recommendations").select("div.review-rec-list").select("li");
            UserEntity user;
            int recN = 0;
            Long[] userids = new Long[recUs.size()];
            for (Element el : recUs) {
                user = new UserEntity();
                String userid = el.select("div.content").select("a[href]").attr("abs:href").split("people/")[1].replace("/", "");
                user.setDoubanuserid(userid);
                String uname = el.select("div.pic").select("img").attr("alt");
                //user.setInserttime(DateUtil.string2Time(el.select("div.content").select("a[href]").first().child(0).text(),"yyyy-MM-dd"));
                user.setUname(uname);
                String avatar = el.select("div.pic").select("img").attr("src");
                user.setAvatar(avatar);
                user.setFlag(0);
                userids[recN++] = doubanDataRep.getUser(user).getUserid();
            }
            reviewRecusers = Joiner.on(",").join(userids).toString();
        } catch (Exception e) {
            CLogManager.error(e);
        }
        return reviewRecusers;
    }

    public String getReviewLikeuser(String url) {
        String reviewLikeuser = null;
        try {
            Document doc = SpiderTool.Getdoc(url + "?tab=likes#likes", 3, true);
            Elements likeUs = doc.select("div#likes").select("div.review-fav-list").select("li");
            UserEntity user;
            int recN = 0;
            Long[] userids = new Long[likeUs.size()];
            for (Element el : likeUs) {
                user = new UserEntity();
                String userid = el.select("a[href]").attr("abs:href").split("people/")[1].replace("/", "");
                user.setDoubanuserid(userid);
                String uname = el.select("div.pic").select("img").attr("alt");
                //user.setInserttime(DateUtil.string2Time(el.select("div.content").select("a[href]").first().child(0).text(),"yyyy-MM-dd"));
                user.setUname(uname);
                String avatar = el.select("div.pic").select("img").attr("src");
                user.setAvatar(avatar);
                user.setFlag(0);
                userids[recN++] = doubanDataRep.getUser(user).getUserid();
            }
            reviewLikeuser = Joiner.on(",").join(userids).toString();
        } catch (Exception e) {
            CLogManager.error(e);
        }
        return reviewLikeuser;
    }

    private Timestamp getRatedate(Document doc) {
        String date = doc.select("header").select("span").last().text();
        Timestamp time = null;
        try {
            time = DateUtil.string2Time(date, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            CLogManager.error(e);
        }
        return time;
    }

    private Timestamp getRatedate(Element e) {
        String date = e.select("header.main-hd").select("span").text();
        return new Timestamp(TimeUtil.tryParsetime(date).getTime());
    }


}
