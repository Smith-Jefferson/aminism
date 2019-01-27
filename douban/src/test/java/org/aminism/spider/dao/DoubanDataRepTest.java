package org.aminism.spider.dao;

import org.aminism.spider.AbstractSpringContextTest;
import org.aminism.spider.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ygxie
 * @date 2019/1/26.
 */
public class DoubanDataRepTest extends AbstractSpringContextTest {
    @Autowired
    DoubanDataRep dataRep;

    @Test
    public void  testGetUser(){
        UserEntity userEntity = dataRep.getUser("fishniao","恶鸟",null);
    }
}
