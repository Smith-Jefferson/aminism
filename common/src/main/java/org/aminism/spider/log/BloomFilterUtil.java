package org.aminism.spider.log;


/**
 * Created by xieyigang on 2019/1/17.
 */
public class BloomFilterUtil {
    private volatile static BloomFilter bloomFilter=new BloomFilter();
    public static BloomFilter getBloomFilter() {
        return bloomFilter;
    }

    public static void setBloomFilter(BloomFilter bloomFilter) {
        bloomFilter = bloomFilter;
    }
}
