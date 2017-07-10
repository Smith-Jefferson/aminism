package spider.model;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by hello world on 2017/1/10.
 */
@Entity
@Table(name = "doubanbook_comment", schema = "aminism", catalog = "")
public class DoubanbookCommentEntity {
    private long id;
    private long userid;
    private String doubanuserid;
    private long bookid;
    private short rate;
    private String comment;
    private Integer follownum;
    private Timestamp inserttime;
    private Timestamp updatetime;
    private Date ratedate;
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
    @Column(name = "follownum", nullable = true)
    public Integer getFollownum() {
        return follownum;
    }

    public void setFollownum(Integer follownum) {
        this.follownum = follownum;
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
    @Basic
    @Column(name = "ratedate", nullable = true)
    public Date getRatedate() {
        return ratedate;
    }

    public void setRatedate(Date ratedate) {
        this.ratedate = ratedate;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubanbookCommentEntity that = (DoubanbookCommentEntity) o;

        if (id != that.id) return false;
        if (userid != that.userid) return false;
        if (doubanuserid != that.doubanuserid) return false;
        if (bookid != that.bookid) return false;
        if (rate != that.rate) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (follownum != null ? !follownum.equals(that.follownum) : that.follownum != null) return false;
        if (inserttime != null ? !inserttime.equals(that.inserttime) : that.inserttime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        result = 31 * result + (doubanuserid != null ?  doubanuserid.hashCode() : 0 );
        result = 31 * result + (int) (bookid ^ (bookid >>> 32));
        result = 31 * result + (int) rate;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (follownum != null ? follownum.hashCode() : 0);
        return result;
    }
}
