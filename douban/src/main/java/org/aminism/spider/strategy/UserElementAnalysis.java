package org.aminism.spider.strategy;

import org.aminism.spider.log.CLogManager;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * @author ygxie
 * @date 2019/1/25.
 */
@Component
public class UserElementAnalysis {

    public String getDoubanuserid(Elements element) {
        return element.select("a[href]").attr("abs:href").split("people/")[1].split("/")[0];
    }

    public String getUserName(Elements element){
        return element.select("a[href]").text();
    }


    public String getUserAvatar(Elements element){
        if(element.size()==0)return null;
        return element.select("img").attr("src");
    }
}
