package spider.strategy;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** 
* DoubanBookDetail Tester. 
* 
* @author <Authors name> 
* @since <pre>һ�� 12, 2017</pre> 
* @version 1.0 
*/ 
public class DoubanBookDetailTest {
    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {

    }
    @Test
    public void taskTest(){
        String[] books={"https://book.douban.com/subject/1080370/"};
        DoubanBookDetail bookDetail=new DoubanBookDetail(books);
        bookDetail.run();
    }

    @Test
    public void testOffer(){
        DoubanbookOffer offer=new DoubanbookOffer("https://book.douban.com/subject/1080370/offers");
        offer.run();
    }

} 
