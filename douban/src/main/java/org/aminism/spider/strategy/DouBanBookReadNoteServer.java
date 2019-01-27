package org.aminism.spider.strategy;

import org.aminism.spider.dao.DouBanBookReadNoteDao;
import org.aminism.spider.dao.DoubanDataRep;
import org.aminism.spider.entity.DouBanBookReadNoteEntity;
import org.aminism.spider.entity.UserEntity;
import org.aminism.spider.log.BloomFilterUtil;
import org.aminism.spider.log.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ygxie
 * @date 2019/1/25.
 */
@Component
public class DouBanBookReadNoteServer {
    @Autowired
    UserElementAnalysis userElementAnalysis;
    @Autowired
    private DoubanDataRep doubanDataRep;
    @Autowired
    DouBanBookReadNoteDao douBanBookReadNoteDao;

    public void save(Document doc,Long bookid){
        Elements elements = doc.select("div.reading-notes").select("ul.comments").select("li");
        List<DouBanBookReadNoteEntity> list = new ArrayList<>();
        for(Element element:elements){
            DouBanBookReadNoteEntity readnote = new DouBanBookReadNoteEntity();
            UserEntity user = doubanDataRep.getUser(getDoubanUserid(element),
                    getUsername(element),getUserAvatar(element));
            readnote.setUserid(user.getUserid());
            readnote.setBookid(bookid);
            readnote.setFollows(getFollows(element));
            readnote.setWeitedate(getWriteDate(element));
            readnote.setContent(getContent(element));
            readnote.setRate(getVote(element));
            readnote.setPictrues(getPictures(element));
            if(!BloomFilterUtil.getBloomFilter().containedThenAdd(
                    "readnote"+readnote.getBookid()+readnote.getUserid()+readnote.getWeitedate().getTimeInMillis())){
                list.add(readnote);
            }
        }
        douBanBookReadNoteDao.save(list);
    }

    private String getDoubanUserid(Element element){
        return userElementAnalysis.getDoubanuserid(element.select("div.ilst"));
    }

    private String getUsername(Element element){
        return userElementAnalysis.getUserName(element.select("p.user"));
    }

    private String getUserAvatar(Element element){
        return userElementAnalysis.getUserAvatar(element.select("div.ilst"));
    }

    private Integer getFollows(Element element){
        Elements els =  element.select("div.reading-note").select("p.pl").select("span");
        if(els.size()<2){
            return 0;
        }
        String v = els.get(1).text().replace("人喜欢", "");
        return Integer.parseInt(v);
    }

    private Calendar getWriteDate(Element element){
        String v = element.select("div.reading-note").select("div.short").select("p.pl").select("span").get(0).text();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date t= TimeUtil.tryParsetime(v,format );
        Calendar c = Calendar.getInstance();
        c.setTime(t);
        return c;
    }

    private String getContent(Element element){
        try {
            String vv = element.select("div.all").text().split("推荐")[0];
            if(StringUtils.isNotBlank(vv)){
                return vv;
            }
        }catch (Exception e){

        }

        String v = element.select("div.reading-note").select("div.short").select("span").get(0)
                .text();
        return v;
    }

    @Autowired
    RateStartConvert rateStartConvert;

    private Short getVote(Element element){
        String v = element.select("span.allstar40").attr("title");
        return rateStartConvert.convert(v);
    }

    private String getPictures(Element element){
        String v = element.select("div.reading-note").select("div.short").select("img")
                .stream().map(x->x.attr("src")).collect(Collectors.joining(";"));           return v;
    }



}
