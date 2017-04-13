package spider.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by hello world on 2017/1/11.
 */
public class DoubanrateEntityPK implements Serializable {
    private long doubanbookid;
    private long userid;

    @Column(name = "doubanbookid", nullable = false)
    @Id
    public long getDoubanbookid() {
        return doubanbookid;
    }

    public void setDoubanbookid(long doubanbookid) {
        this.doubanbookid = doubanbookid;
    }

    @Column(name = "userid", nullable = false)
    @Id
    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoubanrateEntityPK that = (DoubanrateEntityPK) o;

        if (doubanbookid != that.doubanbookid) return false;
        if (userid != that.userid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (doubanbookid ^ (doubanbookid >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        return result;
    }
}
