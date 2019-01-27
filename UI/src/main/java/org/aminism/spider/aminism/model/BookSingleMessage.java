package org.aminism.spider.aminism.model;

import org.aminism.spider.log.Lunar;

import java.sql.Date;

/**
 * @author ygxie
 * @date 2019/1/27.
 */
public class BookSingleMessage {
    // 书的信息
    private long bookid;
    private String bookname;
    private String bookface;
    private String author;
    private Date publishDate;
    private float ratescore;
    // 用户信息
    private long userid;
    private String username;
    private String useravator;

    // 笔记的信息
    private String content;

    // 笔记评价的信息
    private int follows;
    // 农历时间
    private Lunar date;

    public Lunar getDate() {
        return date;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public void setDate(Lunar date) {
        this.date = date;
    }

    public long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public String getBookface() {
        return bookface;
    }

    public void setBookface(String bookface) {
        this.bookface = bookface;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public float getRatescore() {
        return ratescore;
    }

    public void setRatescore(float ratescore) {
        this.ratescore = ratescore;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseravator() {
        return useravator;
    }

    public void setUseravator(String useravator) {
        this.useravator = useravator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }
}
