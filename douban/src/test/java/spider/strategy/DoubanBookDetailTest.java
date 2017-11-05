package spider.strategy;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.strategy.DoubanBookDetail;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.strategy.DoubanbookOffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/** 
* DoubanBookDetail Tester. 
* 
* @author <Authors name> 
* @since <pre>һ�� 12, 2017</pre> 
* @version 1.0 
*/ 
public class DoubanBookDetailTest {
    private static String url="https://book.douban.com/subject/5337243";
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {

    }
    @Test
    public void taskTest(){
        Set<String> books=new HashSet<>();
        books.add(url);
        DoubanBookDetail bookDetail=new DoubanBookDetail(books);
        bookDetail.run();
    }

    @Test
    public void testOffer(){
        DoubanbookOffer offer=new DoubanbookOffer("https://book.douban.com/subject/1080370/offers");
        offer.run();
    }

} 
