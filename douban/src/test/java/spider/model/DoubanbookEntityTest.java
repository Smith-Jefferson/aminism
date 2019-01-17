package spider.model;

import com.ctrip.flight.backendservice.backofficetool.spider.entity.DoubanbookEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/** 
* DoubanbookEntity Tester. 
* 
* @author <Authors name> 
* @since <pre>??? 10, 2017</pre> 
* @version 1.0 
*/ 
public class DoubanbookEntityTest {
    private static SessionFactory SessionFactory;
    //private static StandardServiceRegistry registry;
    private static Session session;
    //private static Transaction transaction;
@Before
public void before() throws Exception {
    Configuration configuration = new Configuration();
    configuration.configure();
   // registry=new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    SessionFactory = configuration.buildSessionFactory();
    session=SessionFactory.openSession();
    //transaction=session.beginTransaction();
} 

@After
public void after() throws Exception {
    //transaction.commit();
    session.close();
    SessionFactory.close();
} 

/** 
* 
* Method: getBookid() 
* 
*/
@Test
public void testSaveBook() throws Exception{
    DoubanbookEntity doubanbookEntity=new DoubanbookEntity();
    doubanbookEntity.setBookid(3);
    doubanbookEntity.setBookname("巨婴");
    session.save(doubanbookEntity);
}

@Test
public void testGetBookid() throws Exception { 
    String Hsql="select bookid from DoubanbookEntity";
    Query<Long> query=session.createQuery(Hsql,Long.class);
    List<Long> bookids=query.getResultList();
    for (long id:bookids) {
        System.out.println(id);
    }
} 

/** 
* 
* Method: setBookid(long bookid) 
* 
*/ 
@Test
public void testSetBookid() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getAuthor() 
* 
*/ 
@Test
public void testGetAuthor() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setAuthor(String author) 
* 
*/ 
@Test
public void testSetAuthor() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getPublisher() 
* 
*/ 
@Test
public void testGetPublisher() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setPublisher(String publisher) 
* 
*/ 
@Test
public void testSetPublisher() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getPublishdate() 
* 
*/ 
@Test
public void testGetPublishdate() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setPublishdate(Date publishdate) 
* 
*/ 
@Test
public void testSetPublishdate() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getPageno() 
* 
*/ 
@Test
public void testGetPageno() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setPageno(Integer pageno) 
* 
*/ 
@Test
public void testSetPageno() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getBinding() 
* 
*/ 
@Test
public void testGetBinding() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setBinding(String binding) 
* 
*/ 
@Test
public void testSetBinding() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getIsbn() 
* 
*/ 
@Test
public void testGetIsbn() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setIsbn(Long isbn) 
* 
*/ 
@Test
public void testSetIsbn() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getBookintro() 
* 
*/ 
@Test
public void testGetBookintro() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setBookintro(String bookintro) 
* 
*/ 
@Test
public void testSetBookintro() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getAuthorintro() 
* 
*/ 
@Test
public void testGetAuthorintro() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setAuthorintro(String authorintro) 
* 
*/ 
@Test
public void testSetAuthorintro() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getMenu() 
* 
*/ 
@Test
public void testGetMenu() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setMenu(String menu) 
* 
*/ 
@Test
public void testSetMenu() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getSample() 
* 
*/ 
@Test
public void testGetSample() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setSample(String sample) 
* 
*/ 
@Test
public void testSetSample() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getTagids() 
* 
*/ 
@Test
public void testGetTagids() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setTagids(String tagids) 
* 
*/ 
@Test
public void testSetTagids() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getDoubanIbcf() 
* 
*/ 
@Test
public void testGetDoubanIbcf() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setDoubanIbcf(String doubanIbcf) 
* 
*/ 
@Test
public void testSetDoubanIbcf() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getDoubanUbcf() 
* 
*/ 
@Test
public void testGetDoubanUbcf() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setDoubanUbcf(String doubanUbcf) 
* 
*/ 
@Test
public void testSetDoubanUbcf() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getUrl() 
* 
*/ 
@Test
public void testGetUrl() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setUrl(String url) 
* 
*/ 
@Test
public void testSetUrl() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getInserttime() 
* 
*/ 
@Test
public void testGetInserttime() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setInserttime(Timestamp inserttime) 
* 
*/ 
@Test
public void testSetInserttime() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getUpdatetime() 
* 
*/ 
@Test
public void testGetUpdatetime() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setUpdatetime(Timestamp updatetime) 
* 
*/ 
@Test
public void testSetUpdatetime() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: equals(Object o) 
* 
*/ 
@Test
public void testEquals() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: hashCode() 
* 
*/ 
@Test
public void testHashCode() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getFaceimg() 
* 
*/ 
@Test
public void testGetFaceimg() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setFaceimg(String faceimg) 
* 
*/ 
@Test
public void testSetFaceimg() throws Exception { 
//TODO: Test goes here... 
} 


} 
