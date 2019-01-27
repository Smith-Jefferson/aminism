package org.aminism.spider.entity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author ygxie
 * @date 2019/1/25.
 */
@Entity
@Table(name = "doubanbook_readnote", schema = "aminism", catalog = "")
public class DouBanBookReadNoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Long userid;
    @Column
    private Long bookid;
    @Column
    private Short rate;
    @Column
    private String pictrues;
    @Column
    private Integer follows;
    @Column
    private Calendar weitedate;
    @Column
    private Calendar datachange_lasttime;
    @Column
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getBookid() {
        return bookid;
    }

    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }

    public Short getRate() {
        return rate;
    }

    public void setRate(Short rate) {
        this.rate = rate;
    }

    public String getPictrues() {
        return pictrues;
    }

    public void setPictrues(String pictrues) {
        this.pictrues = pictrues;
    }

    public Integer getFollows() {
        return follows;
    }

    public void setFollows(Integer follows) {
        this.follows = follows;
    }

    public Calendar getWeitedate() {
        return weitedate;
    }

    public void setWeitedate(Calendar weitedate) {
        this.weitedate = weitedate;
    }


    public Calendar getDatachange_lasttime() {
        return datachange_lasttime;
    }

    public void setDatachange_lasttime(Calendar datachange_lasttime) {
        datachange_lasttime = datachange_lasttime;
    }
}
