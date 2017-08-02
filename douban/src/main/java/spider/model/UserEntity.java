package spider.model;

import net.sf.cglib.beans.BeanCopier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import spider.App;
import spider.database.DoubanDataRep;
import spider.pool.SessionPool;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by hello world on 2017/1/10.
 */
@Entity
@Table(name = "user", schema = "aminism", catalog = "")
public class UserEntity {
    private long userid;
    private String doubanuserid;
    private String uname;
    private String avatar;
    private String sentence;
    private String city;
    private String groups;
    private String attentionTo;
    private String ToAttention;
    private String bookRead;
    private String bookPlan;
    private String musicListen;
    private String musicPlan;
    private String movieWatch;
    private String moviePlan;
    private String activityOnline;
    private String activityOffline;
    private Date resgistdate;
    private Integer flag;
    private Timestamp inserttime;
    private Timestamp updatetime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid", nullable = false)
    public long getUserid() {
        return userid;
    }
    public long getUserID(UserEntity user){
        StringBuilder str=new StringBuilder();
        long userid=0;
        if(App.getBloomFilter().ContainedThenAdd(str.append(user.getDoubanuserid()).append(user.getUname()).toString())){
            List<UserEntity> tmp= DoubanDataRep.getUsersByName(user.getUname(),user.getDoubanuserid());
            if(tmp!=null && tmp.size()>0)
                userid= tmp.get(0).getUserid();
        }
        if(userid==0)
        {
            Session session= SessionPool.getSession();
            Transaction transaction=session.beginTransaction();
            session.save(user);
            transaction.commit();
            userid=user.getUserid();
            SessionPool.freeSession(session);
        }
        str.delete(0,str.length());
        return userid;
    }
    @Basic
    @Column(name = "flag", nullable = true)
    public Integer getFlag() {
        return flag;
    }
    public void setFlag(Integer flag) {
        this.flag = flag;
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
    @Column(name = "uname", nullable = false, length = 255)
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        if(uname!=null && uname.trim()!="")
            uname=uname.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
        this.uname = uname;
    }

    @Basic
    @Column(name = "avatar", nullable = false, length = 255)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Basic
    @Column(name = "sentence", nullable = true, length = 255)
    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    @Basic
    @Column(name = "city", nullable = true, length = 255)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "groups", nullable = true, length = 255)
    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    @Basic
    @Column(name = "attentionTo", nullable = true, length = 255)
    public String getAttentionTo() {
        return attentionTo;
    }

    public void setAttentionTo(String attentionTo) {
        this.attentionTo = attentionTo;
    }

    @Basic
    @Column(name = "Toattention", nullable = true, length = 255)
    public String getToAttention() {
        return ToAttention;
    }

    public void setToAttention(String toAttention) {
        ToAttention = toAttention;
    }

    @Basic
    @Column(name = "book_read", nullable = true, length = 255)
    public String getBookRead() {
        return bookRead;
    }

    public void setBookRead(String bookRead) {
        this.bookRead = bookRead;
    }

    @Basic
    @Column(name = "book_plan", nullable = true, length = 255)
    public String getBookPlan() {
        return bookPlan;
    }

    public void setBookPlan(String bookPlan) {
        this.bookPlan = bookPlan;
    }

    @Basic
    @Column(name = "music_listen", nullable = true, length = 255)
    public String getMusicListen() {
        return musicListen;
    }

    public void setMusicListen(String musicListen) {
        this.musicListen = musicListen;
    }

    @Basic
    @Column(name = "music_plan", nullable = true, length = 255)
    public String getMusicPlan() {
        return musicPlan;
    }

    public void setMusicPlan(String musicPlan) {
        this.musicPlan = musicPlan;
    }

    @Basic
    @Column(name = "movie_watch", nullable = true, length = 255)
    public String getMovieWatch() {
        return movieWatch;
    }

    public void setMovieWatch(String movieWatch) {
        this.movieWatch = movieWatch;
    }

    @Basic
    @Column(name = "movie_plan", nullable = true, length = 255)
    public String getMoviePlan() {
        return moviePlan;
    }

    public void setMoviePlan(String moviePlan) {
        this.moviePlan = moviePlan;
    }

    @Basic
    @Column(name = "activity_online", nullable = true, length = 255)
    public String getActivityOnline() {
        return activityOnline;
    }

    public void setActivityOnline(String activityOnline) {
        this.activityOnline = activityOnline;
    }

    @Basic
    @Column(name = "activity_offline", nullable = true, length = 255)
    public String getActivityOffline() {
        return activityOffline;
    }

    public void setActivityOffline(String activityOffline) {
        this.activityOffline = activityOffline;
    }

    @Basic
    @Column(name = "resgistdate", nullable = true)
    public Date getResgistdate() {
        return resgistdate;
    }

    public void setResgistdate(Date resgistdate) {
        this.resgistdate = resgistdate;
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

        UserEntity that = (UserEntity) o;

        if (userid != that.userid) return false;
        if (doubanuserid != that.doubanuserid) return false;
        if (uname != null ? !uname.equals(that.uname) : that.uname != null) return false;
        if (avatar != null ? !avatar.equals(that.avatar) : that.avatar != null) return false;
        if (sentence != null ? !sentence.equals(that.sentence) : that.sentence != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (groups != null ? !groups.equals(that.groups) : that.groups != null) return false;
        if (attentionTo != null ? !attentionTo.equals(that.attentionTo) : that.attentionTo != null) return false;
        if (ToAttention != null ? !ToAttention.equals(that.ToAttention) : that.ToAttention != null) return false;
        if (bookRead != null ? !bookRead.equals(that.bookRead) : that.bookRead != null) return false;
        if (bookPlan != null ? !bookPlan.equals(that.bookPlan) : that.bookPlan != null) return false;
        if (musicListen != null ? !musicListen.equals(that.musicListen) : that.musicListen != null) return false;
        if (musicPlan != null ? !musicPlan.equals(that.musicPlan) : that.musicPlan != null) return false;
        if (movieWatch != null ? !movieWatch.equals(that.movieWatch) : that.movieWatch != null) return false;
        if (moviePlan != null ? !moviePlan.equals(that.moviePlan) : that.moviePlan != null) return false;
        if (activityOnline != null ? !activityOnline.equals(that.activityOnline) : that.activityOnline != null)
            return false;
        if (activityOffline != null ? !activityOffline.equals(that.activityOffline) : that.activityOffline != null)
            return false;
        if (resgistdate != null ? !resgistdate.equals(that.resgistdate) : that.resgistdate != null) return false;
        if (inserttime != null ? !inserttime.equals(that.inserttime) : that.inserttime != null) return false;
        if (updatetime != null ? !updatetime.equals(that.updatetime) : that.updatetime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userid ^ (userid >>> 32));
        result =  31 * result + (doubanuserid != null ? doubanuserid.hashCode() : 0);
        result = 31 * result + (uname != null ? uname.hashCode() : 0);
        return result;
    }

}
