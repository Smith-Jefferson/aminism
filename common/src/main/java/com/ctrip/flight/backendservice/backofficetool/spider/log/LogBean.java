package com.ctrip.flight.backendservice.backofficetool.spider.log;

import java.util.Map;

/**
 * Created by xieyigang on 2018/11/18.
 */
public class LogBean {
    private String title;
    /**
     * level=1:warning
     */
    private byte level;
    private int stage;
    private String msg;
    private Throwable exception;
    private Map<String, String> tag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Map<String, String> getTag() {
        return tag;
    }

    public void setTag(Map<String, String> tag) {
        this.tag = tag;
    }

    public LogBean() {
        //stage += 1;
    }

    public LogBean(String title, String msg) {
        this();
        this.title = title;
        this.msg = msg;
    }

    public LogBean(String title, String msg, Map<String, String> tag) {
        this(title, msg);
        this.tag = tag;
    }

    public LogBean(String title, String msg, Map<String, String> tag, byte level) {
        this(title, msg, tag);
        this.level = level;
    }

    public LogBean(String title, Throwable e) {
        this();
        this.title = title;
        this.exception = e;
    }

    public LogBean(String title, Throwable e, byte level) {
        this(title, e);
        this.level = level;
    }

    public LogBean(String title, byte level, int stage, String msg, Map<String, String> tag) {
        this.title = title;
        this.level = level;
        this.stage = stage;
        this.msg = msg;
        this.tag = tag;
    }
}
