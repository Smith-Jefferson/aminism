package org.aminism.spider;

import com.alibaba.fastjson.JSON;
import org.aminism.spider.log.CLogManager;
import org.aminism.spider.log.Lunar;
import org.junit.Test;

/**
 * @author ygxie
 * @date 2019/1/27.
 */
public class LunarTest {
    @Test
    public void test1(){
        Lunar lunar = new Lunar(2019,2,4);
        CLogManager.info(JSON.toJSONString(lunar));

        Lunar lunar1 = new Lunar(2019,3,6);
        CLogManager.info(JSON.toJSONString(lunar1));

        Lunar lunar2 = new Lunar(2019,3,8);
        CLogManager.info(JSON.toJSONString(lunar2));
    }
}
