package spider.pool;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by hello world on 2017/1/11.
 */
public class SessionPool {
    private static final SessionFactory sessionFactory;
    private static Session session;
    private static Transaction transaction;
    private  static Queue<Session> pool = new ConcurrentLinkedQueue<>();
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        if (pool!=null &&pool.size() > 0) {
            return pool.poll();
        } else {
            return sessionFactory.openSession();
        }
    }

    public static void freeSession(Session session){
        session.clear();
        pool.add(session);
    }

    public static void releaseDatabaseSource(){
        for (Session session:pool) {
            session.close();
        }
        sessionFactory.close();
    }
}
