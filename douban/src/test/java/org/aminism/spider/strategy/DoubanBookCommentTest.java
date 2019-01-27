package org.aminism.spider.strategy;

import org.aminism.spider.service.InitailTask;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by hello world on 2017/1/14.
 */
public class DoubanBookCommentTest {
    @Before
    public void before() throws Exception {
        InitailTask initailTask = new InitailTask();
        initailTask.initailDoubanBook();
        initailTask.initailDoubanBookComment();
        initailTask.initailDoubanUser();
        initailTask.initialDoubanBookReview();
        initailTask.initialDoubanBookReviewComment();
    }

    @Test
    public void testDoubanBookCommmentTest() {
        DoubanBookComment commenttask = new DoubanBookComment();
        commenttask.run("https://book.douban.com/subject/26910673/comments/");
    }

    @Test
    public void testDoubanBookReview() {
        DoubanBookReview reviewtask = new DoubanBookReview();
        reviewtask.run("https://book.douban.com/subject/26910673/reviews");
    }

    @Test
    public void testDoubanBookReviewComment() {
        DoubanBookReviewComment reviewtask = new DoubanBookReviewComment();
        reviewtask.setBookid(1080370);
        reviewtask.run("https://book.douban.com/review/1393458/");
    }
}
