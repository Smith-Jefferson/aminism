package com.ctrip.flight.backendservice.backofficetool.aminism.spider.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by hello world on 2017/1/10.
 */
@Entity
@Table(name = "doubanbook_review", schema = "aminism", catalog = "")
public class DoubanbookReviewEntity {
    private long id;
    private long userid;
    private String doubanuserid;
    private long bookid;
    private short rate;
    private String review;
    private Integer follownum;
    private Integer disfollownum;
    private String reviewRecusers;
    private String reviewLikeuser;
    private String url;
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
    @Column(name = "doubanuserid", nullable = false,length = 50)
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
    @Column(name = "rate", nullable = false)
    public short getRate() {
        return rate;
    }

    public void setRate(short rate) {
        this.rate = rate;
    }

    @Basic
    @Column(name = "review", nullable = true, length = -1)
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        if(review!=null && review.trim()!="")
            review=review.replaceAll("[\ud800\udc00-\udbff\udfff\ud800-\udfff]", "");
        this.review = review;
    }

    @Basic
    @Column(name = "follownum", nullable = true)
    public Integer getFollownum() {
        return follownum;
    }

    public void setFollownum(Integer follownum) {
        this.follownum = follownum;
    }

    @Basic
    @Column(name = "disfollownum", nullable = true)
    public Integer getDisfollownum() {
        return disfollownum;
    }

    public void setDisfollownum(Integer disfollownum) {
        this.disfollownum = disfollownum;
    }

    @Basic
    @Column(name = "review_recusers", nullable = true, length = 255)
    public String getReviewRecusers() {
        return reviewRecusers;
    }

    public void setReviewRecusers(String reviewRecusers) {
        this.reviewRecusers = reviewRecusers;
    }

    @Basic
    @Column(name = "review_likeuser", nullable = true, length = 255)
    public String getReviewLikeuser() {
        return reviewLikeuser;
    }

    public void setReviewLikeuser(String reviewLikeuser) {
        this.reviewLikeuser = reviewLikeuser;
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

        DoubanbookReviewEntity that = (DoubanbookReviewEntity) o;

        if (id != that.id) return false;
        if (userid != that.userid) return false;
        if (doubanuserid != that.doubanuserid) return false;
        if (bookid != that.bookid) return false;
        if (rate != that.rate) return false;
        if (review != null ? !review.equals(that.review) : that.review != null) return false;
        if (follownum != null ? !follownum.equals(that.follownum) : that.follownum != null) return false;
        if (disfollownum != null ? !disfollownum.equals(that.disfollownum) : that.disfollownum != null) return false;
        if (reviewRecusers != null ? !reviewRecusers.equals(that.reviewRecusers) : that.reviewRecusers != null)
            return false;
        if (reviewLikeuser != null ? !reviewLikeuser.equals(that.reviewLikeuser) : that.reviewLikeuser != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        result = 31 * result + (doubanuserid != null ? doubanuserid.hashCode() : 0);
        result = 31 * result + (int) (bookid ^ (bookid >>> 32));
        result = 31 * result + (int) rate;
        return result;
    }
}
