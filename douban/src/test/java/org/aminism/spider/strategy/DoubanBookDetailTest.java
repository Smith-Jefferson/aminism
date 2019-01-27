package org.aminism.spider.strategy;

import org.aminism.spider.AbstractSpringContextTest;
import org.aminism.spider.FileReader;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
* DoubanBookDetail Tester.
*
* @author <Authors name>
* @since <pre>һ�� 12, 2017</pre>
* @version 1.0
*/
public class DoubanBookDetailTest extends AbstractSpringContextTest {
    private static String url="https://book.douban.com/subject/5337243";

    @Autowired
    DoubanBookDetail bookDetail;
    @Autowired
    DoubanbookOffer offer;

    FileReader fileReader = new FileReader();

    @Test
    public void taskTest() throws Exception{
        Document doc = fileReader.getDoc("test-data/test1.html");
        bookDetail.task(url,doc );
    }

    @Autowired
    DoubanBookComment bookComment;

    @Test
    public void testComment(){
        bookComment.save(fileReader.getDoc("test-data/test1.html"), 5337243L);
    }

    @Autowired
    DoubanBookReview doubanBookReview;

    @Test
    public void testReview(){
        doubanBookReview.save(fileReader.getDoc("test-data/test1.html"), 5337243L);
    }

    @Autowired
    DouBanBookReadNoteServer readNoteServer;

    @Test
    public void testReadNote(){
        readNoteServer.save(fileReader.getDoc("test-data/test1.html"), 5337243L);
    }
    @Test
    public void testOffer(){
        offer.run("https://book.douban.com/subject/1080370/offers");
    }

}
