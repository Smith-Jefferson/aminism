package org.aminism.spider;

import org.aminism.spider.dao.DoubanbookDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static org.aminism.spider.log.CLogManager.info;

/**
 * Created by xieyigang on 2019/1/19.
 */
public class JpaTest extends AbstractSpringContextTest {
    @Autowired
    DoubanbookDao dao;

    @Test
    public void test1() {
        Long r = dao.count();
        info(Objects.equals(r,null )? "null" : r.toString());
    }
}
