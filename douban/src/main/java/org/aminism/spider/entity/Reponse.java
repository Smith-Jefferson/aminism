package org.aminism.spider.entity;

/**
 * @author ygxie
 * @date 2019/1/25.
 */
public class Reponse<T> {
    private String offerUrl;
    private T data;

    public String getOfferUrl() {
        return offerUrl;
    }

    public void setOfferUrl(String offerUrl) {
        this.offerUrl = offerUrl;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Reponse(T data) {
        this.data = data;
    }
}
