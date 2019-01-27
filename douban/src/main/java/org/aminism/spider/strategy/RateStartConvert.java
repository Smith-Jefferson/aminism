package org.aminism.spider.strategy;

import org.springframework.stereotype.Component;

/**
 * @author ygxie
 * @date 2019/1/25.
 */
@Component
public class RateStartConvert {
    public Short convert(String evaluate){
        short rate=0;
        switch (evaluate){
            case "力荐" : rate=5;break;
            case "推荐" : rate=4;break;
            case "还行" : rate=3;break;
            case "较差" : rate=2;break;
            case "很差" : rate=1;break;
        }
        return rate;
    }
}
