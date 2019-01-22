package org.aminism.spider.entity;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by hello world on 2017/1/10.
 */
@Entity
@Table(name = "doubanbook", schema = "aminism", catalog = "")
public class DoubanbookEntity {
    private long bookid;
    private String author;
    private String publisher;
    private Date publishdate;
    private Integer pageno;
    private String binding;
    private Double price;
    private Long isbn;
    private String bookintro;
    private String authorintro;
    private String menu;
    private String sample;
    private String tagids;
    private String doubanIbcf;
    private String doubanUbcf;
    private String url;
    private Timestamp inserttime;
    private Timestamp updatetime;
    private String faceimg;
    private String bookname;
    private String rate;
    private String extention;
    public DoubanbookEntity(){}
    public DoubanbookEntity(long bookid,String rate, String author, String publisher, Date publishdate, Integer pageno, String binding, Long isbn, String bookintro, String authorintro, String menu, String sample, String tagids, String doubanIbcf, String doubanUbcf, String url, Timestamp inserttime, Timestamp updatetime, String faceimg) {
        this.bookid = bookid;
        this.author = author;
        this.publisher = publisher;
        this.publishdate = publishdate;
        this.pageno = pageno;
        this.binding = binding;
        this.isbn = isbn;
        this.bookintro = bookintro;
        this.authorintro = authorintro;
        this.menu = menu;
        this.sample = sample;
        this.tagids = tagids;
        this.doubanIbcf = doubanIbcf;
        this.doubanUbcf = doubanUbcf;
        this.url = url;
        this.inserttime = inserttime;
        this.updatetime = updatetime;
        this.faceimg = faceimg;
        this.rate=rate;
    }

    @Id
    @Column(name = "bookid", nullable = false)
    public long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    @Basic
    @Column(name = "author", nullable = true, length = 255)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Basic
    @Column(name = "publisher", nullable = true, length = 255)
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    @Basic
    @Column(name = "rate", nullable = true, length = 255)
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    @Basic
    @Column(name = "extention", nullable = true)
    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

    @Basic
    @Column(name = "publishdate", nullable = true)
    public Date getPublishdate() {
        return publishdate;
    }

    public void setPublishdate(Date publishdate) {
        this.publishdate = publishdate;
    }

    @Basic
    @Column(name = "pageno", nullable = true)
    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    @Basic
    @Column(name = "binding", nullable = true, length = 255)
    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }
    @Basic
    @Column(name = "price", nullable = true)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "ISBN", nullable = true)
    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "bookintro", nullable = true, length = -1)
    public String getBookintro() {
        return bookintro;
    }

    public void setBookintro(String bookintro) {
        this.bookintro = bookintro;
    }

    @Basic
    @Column(name = "authorintro", nullable = true, length = -1)
    public String getAuthorintro() {
        return authorintro;
    }

    public void setAuthorintro(String authorintro) {
        this.authorintro = authorintro;
    }

    @Basic
    @Column(name = "menu", nullable = true, length = -1)
    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    @Basic
    @Column(name = "sample", nullable = true, length = -1)
    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    @Basic
    @Column(name = "tagids", nullable = true, length = 255)
    public String getTagids() {
        return tagids;
    }

    public void setTagids(String tagids) {
        this.tagids = tagids;
    }

    @Basic
    @Column(name = "douban_IBCF", nullable = true, length = -1)
    public String getDoubanIbcf() {
        return doubanIbcf;
    }

    public void setDoubanIbcf(String doubanIbcf) {
        this.doubanIbcf = doubanIbcf;
    }

    @Basic
    @Column(name = "douban_UBCF", nullable = true, length = -1)
    public String getDoubanUbcf() {
        return doubanUbcf;
    }

    public void setDoubanUbcf(String doubanUbcf) {
        this.doubanUbcf = doubanUbcf;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "inserttime", nullable = true,updatable = false,columnDefinition = "timestamp default current_timestamp")
    @Generated(GenerationTime.INSERT)
    public Timestamp getInserttime() {
        return inserttime;
    }

    public void setInserttime(Timestamp inserttime) {
        this.inserttime = inserttime;
    }

    @Basic
    @Column(name = "updatetime", nullable = true,columnDefinition = "timestamp default current_timestamp")
    @Generated(GenerationTime.ALWAYS)
    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubanbookEntity that = (DoubanbookEntity) o;

        if (bookid != that.bookid) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (publisher != null ? !publisher.equals(that.publisher) : that.publisher != null) return false;
        if (publishdate != null ? !publishdate.equals(that.publishdate) : that.publishdate != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (bookid ^ (bookid >>> 32));
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (publishdate != null ? publishdate.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "faceimg", nullable = true, length = 255)
    public String getFaceimg() {
        return faceimg;
    }

    public void setFaceimg(String faceimg) {
        this.faceimg = faceimg;
    }

    @Basic
    @Column(name = "bookname", nullable = false, length = 255)
    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
