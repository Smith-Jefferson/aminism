package spider.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by hello world on 2017/1/11.
 */
@Entity
@Table(name = "doubanrate", schema = "aminism", catalog = "")
@IdClass(DoubanrateEntityPK.class)
public class DoubanrateEntity {
    private long doubanbookid;
    private long userid;
    private long doubanuserid;
    private short rate;
    private Timestamp inserttime;
    private Timestamp updatetime;

    @Id
    @Column(name = "doubanbookid", nullable = false)
    public long getDoubanbookid() {
        return doubanbookid;
    }

    public void setDoubanbookid(long doubanbookid) {
        this.doubanbookid = doubanbookid;
    }

    @Id
    @Column(name = "userid", nullable = false)
    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "doubanuserid", nullable = false)
    public long getDoubanuserid() {
        return doubanuserid;
    }

    public void setDoubanuserid(long doubanuserid) {
        this.doubanuserid = doubanuserid;
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
    @Column(name = "inserttime", nullable = true)
    public Timestamp getInserttime() {
        return inserttime;
    }

    public void setInserttime(Timestamp inserttime) {
        this.inserttime = inserttime;
    }

    @Basic
    @Column(name = "updatetime", nullable = true)
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

        DoubanrateEntity that = (DoubanrateEntity) o;

        if (doubanbookid != that.doubanbookid) return false;
        if (userid != that.userid) return false;
        if (doubanuserid != that.doubanuserid) return false;
        if (rate != that.rate) return false;
        if (inserttime != null ? !inserttime.equals(that.inserttime) : that.inserttime != null) return false;
        if (updatetime != null ? !updatetime.equals(that.updatetime) : that.updatetime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (doubanbookid ^ (doubanbookid >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        result = 31 * result + (int) (doubanuserid ^ (doubanuserid >>> 32));
        result = 31 * result + (int) rate;
        result = 31 * result + (inserttime != null ? inserttime.hashCode() : 0);
        result = 31 * result + (updatetime != null ? updatetime.hashCode() : 0);
        return result;
    }
}
