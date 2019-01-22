package org.aminism.spider;

import org.aminism.spider.dao.DoubanbookDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by xieyigang on 2019/1/19.
 */
public class JpaTest extends AbstractSpringContextTest {
    private static Logger logger = LogManager.getLogManager().getLogger("JpaTest");
    @Autowired
    DoubanbookDao dao;

    @Test
    public void test1(){
        long r =dao.count();
        logger.info(""+r);
    }
}
