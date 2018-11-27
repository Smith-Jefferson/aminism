package com.ctrip.flight.backendservice.backofficetool.spider.entity;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by hello world on 2017/1/10.
 */
@Entity
@Table(name = "doubanbook_review_comment", schema = "aminism", catalog = "")
public class DoubanbookReviewCommentEntity {
    private long id;
    private long userid;
    private String doubanuserid;
    private long bookid;
    private long reviewid;
    private String comment;
    private String replyid;
    private Timestamp inserttime;
    private Timestamp updatetime;
    private Timestamp ratedate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "userid", nullable = false)
    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "doubanuserid", nullable = false)
    public String getDoubanuserid() {
        return doubanuserid;
    }

    public void setDoubanuserid(String doubanuserid) {
        this.doubanuserid = doubanuserid;
    }

    @Basic
    @Column(name = "bookid", nullable = false)
    public long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    @Basic
    @Column(name = "reviewid", nullable = false)
    public long getReviewid() {
        return reviewid;
    }

    public void setReviewid(long reviewid) {
        this.reviewid = reviewid;
    }
    @Basic
    @Column(name = "comment", nullable = false, length = 255)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if(comment!=null && comment.trim()!="")
            comment=comment.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
        this.comment = comment;
    }

    @Basic
    @Column(name = "replyid", nullable = true)
    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
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
    @Basic
    @Column(name = "ratedate", nullable = true)
    public Timestamp getRatedate() {
        return ratedate;
    }

    public void setRatedate(Timestamp ratedate) {
        this.ratedate = ratedate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubanbookReviewCommentEntity that = (DoubanbookReviewCommentEntity) o;

        if (id != that.id) return false;
        if (userid != that.userid) return false;
        if (doubanuserid != null ? !doubanuserid.equals(that.doubanuserid) : that.doubanuserid != null) return false;
        if (bookid != that.bookid) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        result = 31 * result + (doubanuserid != null ? doubanuserid.hashCode() : 0);
        result = 31 * result + (int) (bookid ^ (bookid >>> 32));
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
