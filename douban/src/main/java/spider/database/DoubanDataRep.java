package spider.database;

import org.hibernate.Session;
import org.hibernate.query.Query;
import spider.model.DoubanbookCommentEntity;
import spider.model.DoubanbookReviewCommentEntity;
import spider.model.DoubanbookReviewEntity;
import spider.model.UserEntity;
import spider.pool.SessionPool;

import java.util.List;

/**
 * Created by hello world on 2017/1/11.
 */
public class DoubanDataRep {
    public static List<String> getDoubanBook(){
        String Hsql="select url from DoubanbookEntity";
        Session session= SessionPool.getSession();
        Query<String> query = session.createQuery(Hsql,String.class);
        SessionPool.freeSession(session);
        return query.getResultList();
    }

    public static List<DoubanbookCommentEntity> getDoubanbookComment(){
        String Hsql="from DoubanbookCommentEntity";
        Session session= SessionPool.getSession();
        try {
            return session.createQuery(Hsql,DoubanbookCommentEntity.class).list();
        }finally {
            SessionPool.freeSession(session);
        }
    }

    public static List<DoubanbookReviewEntity> getDoubanbookReview(){
        String Hsql="from DoubanbookReviewEntity";
        Session session= SessionPool.getSession();
        try {
            return session.createQuery(Hsql,DoubanbookReviewEntity.class).list();
        }finally {
            SessionPool.freeSession(session);
        }
    }

    public static List<DoubanbookReviewCommentEntity> getDoubanbookReviewCommet(){
        String Hsql="from DoubanbookReviewCommentEntity";
        Session session= SessionPool.getSession();
        try{
            return session.createQuery(Hsql,DoubanbookReviewCommentEntity.class).list();
        }finally {
            SessionPool.freeSession(session);
        }
    }

    public static List<UserEntity> getDoubanUser(){
        String Hsql="from UserEntity";
        Session session= SessionPool.getSession();
        try {
            return session.createQuery(Hsql,UserEntity.class).list();
        }finally {
            SessionPool.freeSession(session);
        }
    }

    public static List<UserEntity> getUsersByName(String username,String doubanid){
        String Hsql ="from UserEntity where uname=:username and doubanuserid=:doubanid";
        Session session= SessionPool.getSession();
        Query<UserEntity> query = session.createQuery(Hsql,UserEntity.class);
        query.setParameter("username",username);
        query.setParameter("doubanid",doubanid);
        SessionPool.freeSession(session);
        return query.getResultList();
    }
}
