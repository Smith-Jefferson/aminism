package com.ctrip.flight.backendservice.backofficetool.aminism.spider.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by xieyigang on 2017/6/14.
 */
@Entity
@Table(name = "Log", schema = "aminism", catalog = "")
public class LogEntity {
    private int idLog;
    private String message;
    private int level;
    private String tag;
    private Timestamp insertime;

    @Id
    @Column(name = "idLog")
    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    @Basic
    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "tag")
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Basic
    @Column(name = "insertime", updatable = false,columnDefinition = "timestamp default current_timestamp")
    public Timestamp getInsertime() {
        return insertime;
    }

    public void setInsertime(Timestamp insertime) {
        this.insertime = insertime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntity logEntity = (LogEntity) o;

        if (idLog != logEntity.idLog) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = idLog;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
