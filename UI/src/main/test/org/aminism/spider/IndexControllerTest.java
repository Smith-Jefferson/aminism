package org.aminism.spider;

import org.aminism.spider.aminism.controller.IndexController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by xieyigang on 2017/11/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IndexController.class)
public class IndexControllerTest {


    @Test
    public void TestIndex(){
        IndexController controller=new IndexController();
        controller.index();
    }
}
