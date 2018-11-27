package com.ctrip.flight.backendservice.backofficetool.spider.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by hello world on 2017/1/20.
 */
@Entity
@Table(name = "doubanbook_offer", schema = "aminism", catalog = "")
public class DoubanbookOfferEntity {
    private long id;
    private long userid;
    private long bookid;
    private String state;
    private String mark;
    private Timestamp offerdate;
    private Timestamp inserttime;
    private Timestamp updatetime;
    private double price;
    private String city;
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
    @Column(name = "bookid", nullable = false)
    public long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    @Basic
    @Column(name = "state", nullable = true, length = 255)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name = "mark", nullable = true, length = 255)
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Basic
    @Column(name = "offerdate", nullable = true)
    public Timestamp getOfferdate() {
        return offerdate;
    }

    public void setOfferdate(Timestamp offerdate) {
        this.offerdate = offerdate;
    }
    @Basic
    @Column(name = "city", nullable = true)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "price", nullable = true)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "inserttime", nullable = true,updatable = false,columnDefinition = "timestamp default current_timestamp")
    public Timestamp getInserttime() {
        return inserttime;
    }

    public void setInserttime(Timestamp inserttime) {
        this.inserttime = inserttime;
    }

    @Basic
    @Column(name = "updatetime", nullable = true,columnDefinition = "timestamp default current_timestamp")
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

        DoubanbookOfferEntity that = (DoubanbookOfferEntity) o;

        if (id != that.id) return false;
        if (userid != that.userid) return false;
        if (bookid != that.bookid) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (mark != null ? !mark.equals(that.mark) : that.mark != null) return false;
        if (offerdate != null ? !offerdate.equals(that.offerdate) : that.offerdate != null) return false;
        if (inserttime != null ? !inserttime.equals(that.inserttime) : that.inserttime != null) return false;
        if (updatetime != null ? !updatetime.equals(that.updatetime) : that.updatetime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        result = 31 * result + (int) (bookid ^ (bookid >>> 32));
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.hashCode() : 0);
        result = 31 * result + (offerdate != null ? offerdate.hashCode() : 0);
        result = 31 * result + (inserttime != null ? inserttime.hashCode() : 0);
        result = 31 * result + (updatetime != null ? updatetime.hashCode() : 0);
        return result;
    }
}
