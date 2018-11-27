package com.ctrip.flight.backendservice.backofficetool.spider.connect;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by hello world on 2017/1/11.
 */
public class SessionPool {
    public SessionPool(){
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    private static  SessionFactory sessionFactory;
    public static final ThreadLocal<Session> session=new ThreadLocal<>();
    //private static Transaction transaction;
    //private  static Queue<Session> pool = new ConcurrentLinkedQueue<>();
/*    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }*/

    public static synchronized Session getSession() throws HibernateException {
        Session s=session.get();
        if(s==null){
            s=sessionFactory.openSession();
            session.set(s);
        }
        return s;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void freeSession(Session session){
        session.clear();
        //pool.add(session);
    }

    public static void releaseDatabaseSource(){
        Session s = session.get();
        if(s != null) {
            s.close();
        }
        session.set(null);
    }
}
