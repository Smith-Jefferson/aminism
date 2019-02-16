package org.aminism.spider.entity;

import javax.persistence.*;

/**
 * Created by ygxie on 2017/10/31.
 */
@Entity
@Table(name = "TaskUrl", schema = "aminism", catalog = "")
public class TaskUrlEntity {
    @Id
    @Column(name = "url")
    private String url;

    // tag:book,review,comment,
    @Basic
    @Column(name = "tag")
    private String tag;

    @Basic
    @Column(name = "source")
    private String source;

    @Basic
    @Column(name="isdel")
    private int isdel;

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public TaskUrlEntity(){}

    public TaskUrlEntity(String url) {
        this.url = url;
    }

    public TaskUrlEntity(String url, String tag) {
        this.url = url;
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "TaskUrl{" +
                "url='" + url + '\'' +
                '}';
    }
}
