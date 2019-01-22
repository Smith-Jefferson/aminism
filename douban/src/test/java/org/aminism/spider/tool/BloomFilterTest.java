package org.aminism.spider.tool;

import org.aminism.spider.log.BloomFilter;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by xieyigang on 2017/7/14.
 */
public class BloomFilterTest {

    private volatile static BloomFilter bloomFilter=new BloomFilter();

    @Test
    public void TestContains(){
        bloomFilter.add("2012");
        bloomFilter.add("2013");
        bloomFilter.add("2014");
        bloomFilter.add("2015");
        bloomFilter.add(2017);
        bloomFilter.add(2018);
        assertTrue(bloomFilter.contains(2017));
        assertTrue(bloomFilter.contains(2012+""));
        assertTrue(bloomFilter.contains(2013));
    }

    @Test
    public void TestIsNo(){
        boolean f= SpiderTool.isNo("3.1");
        assertTrue(f);
    }
}
