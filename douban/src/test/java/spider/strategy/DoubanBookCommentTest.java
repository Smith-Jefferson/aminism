package spider.strategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spider.pool.SessionPool;
import spider.service.InitailTask;

/**
 * Created by hello world on 2017/1/14.
 */
public class DoubanBookCommentTest {
    @Before
    public void before() throws Exception{
        InitailTask initailTask=new InitailTask();
        initailTask.initailDoubanBook();
        initailTask.initailDoubanBookComment();
        initailTask.initailDoubanUser();
        initailTask.initialDoubanBookReview();
        initailTask.initialDoubanBookReviewComment();
    }
    @After
    public void after() throws Exception {
        SessionPool.releaseDatabaseSource();
    }
    @Test
    public void testDoubanBookCommmentTest(){
        DoubanBookComment commenttask=new DoubanBookComment("https://book.douban.com/subject/26910673/comments/");
        commenttask.run();
    }

    @Test
    public void testDoubanBookReview(){
        DoubanBookReview reviewtask=new DoubanBookReview("https://book.douban.com/subject/26910673/reviews");
        reviewtask.run();
    }

    @Test
    public void testDoubanBookReviewComment(){
        DoubanBookReviewComment reviewtask=new DoubanBookReviewComment("https://book.douban.com/review/1393458/");
        reviewtask.setBookid(1080370);
        reviewtask.run();
    }
}
