package com.ctrip.flight.backendservice.backofficetool.aminism.spider.database;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.App;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.CLogManager;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by hello world on 2017/1/11.
 */
@Repository
public class DoubanDataRep {
    @Autowired
    private SessionFactory sessionFactory;
    private Session getSession(){
        return sessionFactory.getCurrentSession();
    }
    public List<Long> getDoubanBook(){
        String HSQL="select bookid from DoubanbookEntity";
        Query<Long> query = getSession().createQuery(HSQL,Long.class);
        return query.getResultList();
    }

    public List<DoubanbookCommentEntity> getDoubanbookComment(){
        String HSQL="from DoubanbookCommentEntity";
        return getSession().createQuery(HSQL,DoubanbookCommentEntity.class).list();
    }

    public  List<DoubanbookReviewEntity> getDoubanbookReview(){
        String HSQL="from DoubanbookReviewEntity";
        return getSession().createQuery(HSQL,DoubanbookReviewEntity.class).list();
    }

    public  List<DoubanbookReviewCommentEntity> getDoubanbookReviewCommet(){
        String HSQL="from DoubanbookReviewCommentEntity";
        return getSession().createQuery(HSQL,DoubanbookReviewCommentEntity.class).list();
    }

    public List<UserEntity> getDoubanUser(){
        String HSQL="from UserEntity";
        return getSession().createQuery(HSQL,UserEntity.class).list();
    }

    public List<UserEntity> getUsersByName(String username,String doubanid){
        String HSQL ="from UserEntity where uname=:username and doubanuserid=:doubanid";
        Query<UserEntity> query =  getSession().createQuery(HSQL,UserEntity.class);
        query.setParameter("username",username);
        query.setParameter("doubanid",doubanid);
        return query.getResultList();
    }

    public long getUserID(UserEntity user){
        StringBuilder str=new StringBuilder();
        long userid=0;
        if(App.getBloomFilter().ContainedThenAdd(str.append(user.getDoubanuserid()).append(user.getUname()).toString())){
            List<UserEntity> tmp= getUsersByName(user.getUname(),user.getDoubanuserid());
            if(tmp!=null && !tmp.isEmpty())
                userid= tmp.get(0).getUserid();
        }
        if(userid==0)
        {
            userid=saveUser(user);
        }
        str.delete(0,str.length());
        return userid;
    }

    @Transactional
    public long saveUser(UserEntity user){
        getSession().save(user);
        return user.getUserid();
    }

    @Transactional
    public void saveComment(DoubanbookCommentEntity comment){
        getSession().save(comment);
    }

    @Transactional
    public void saveOrUpdateBookDerail(DoubanbookEntity detail){
        if(App.getBloomFilter().ContainedThenAdd(detail.getBookid())){
            getSession().update(detail);
        }else{
            getSession().save(detail);
        }
    }

    @Transactional
    public void saveBookOffer(DoubanbookOfferEntity offer){
        getSession().save(offer);
    }

    @Transactional
    public void saveReview(DoubanbookReviewEntity bookreview){
        getSession().save(bookreview);
    }

    @Transactional
    public void saveReviewComment(DoubanbookReviewCommentEntity commet){
        getSession().save(commet);
    }

    @Transactional
    public void saveTaskUrl(TaskUrlEntity url){
        if(getSession().find(url.getClass(),url.getUrl())!=null)
            return;
        getSession().save(url);
    }

    public List<String> listTaskUrl(){
        String HSQL="select url from TaskUrlEntity";
        return getSession().createQuery(HSQL,String.class).list();
    }

    public void deleteTaskUrl(String url){
        getSession().delete(new TaskUrlEntity(url));
    }

    public void addToSchedule(Elements books){
        if(books==null)return;
        for (Element book:books) {
            if(book==null)return;
            addToSchedule(book);
        }
    }
    public void addToSchedule(Element book){
        String url=book.attr("abs:href");
        addToSchedule(url);
    }

    public void addToSchedule(String url){
        if(isBookUrl(url)){
            try {
                String bookid=url.split("subject/")[1].split("/")[0];
                url="https://book.douban.com/subject/"+bookid;
                if(!App.getBloomFilter().contains(bookid)){
                    saveTaskUrl(new TaskUrlEntity(url));
                }
            } catch (Exception e) {
                CLogManager.error(e);
            }
        }
    }

    public static boolean isBookUrl(String url){
        if (url!=null&&url.contains("book") && url.contains("com/subject"))
            return true;
        return false;
    }
}
