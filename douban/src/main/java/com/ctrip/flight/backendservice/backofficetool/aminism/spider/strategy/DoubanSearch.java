package com.ctrip.flight.backendservice.backofficetool.aminism.spider.strategy;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool.SpiderTool;
import com.ctrip.flight.backendservice.backofficetool.spider.dao.DoubanDataRep;
import com.ctrip.flight.backendservice.backofficetool.spider.log.CLogManager;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Created by hello world on 2017/1/11.
 */
@Service
public class DoubanSearch implements Runnable{
    private String base;
    private String searchName;
    private String searchType;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getSearchName() {
        try {
            return URLDecoder.decode(searchName,"utf-8");
        }catch (Exception e){
            CLogManager.error(e);
            return searchName;
        }
    }

    public void setSearchName(String searchName) {
        try {
            this.searchName = URLEncoder.encode(searchName,"utf-8");
        }catch (Exception e){
            CLogManager.error(e);
            this.searchName =searchName;
        }
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void init(String base, String searchName, String searchType){
        this.base = base;
        this.searchName = searchName;
        this.searchType = searchType;
    }

    @Autowired
    private DoubanDataRep taskInit;
    private void sleep(){
        try {
            Thread.sleep(2000);
        }catch (Exception e){
            CLogManager.error(e);
        }
    }

    private  int errorallowed=2;
    @Override
    public void run() {
        String url=base+searchType+"/"+searchName;
        sleep();

        Document doc= SpiderTool.Getdoc(url,3,false);
        if(doc==null) return;
        Elements books=doc.select("a[href]");
        taskInit.addToSchedule(books);
        int maxstep=getMaxNum(doc);
        //maxstep=maxstep>3?3:maxstep;
        for(int i=2;i<maxstep;i++){
            sleep();

            url=getNextPage(base,i);
            if(url!=null){
                try {
                    taskInit.addToSchedule(SpiderTool.Getdoc(url,3,false).select("a[href]"));
                }
                catch (Exception e){
                    CLogManager.error(e);
                    if(errorallowed--<=0)
                        break;
                }
            }
            else
                break;
        }
    }

    public String getNextPage(String baseUrl,int no){
        if(no>50)
            return null;
        return baseUrl+searchType+"/"+searchName+"?start="+no*20+"&type=T";
    }

    public int getMaxNum(Document doc){
        Elements elements=doc.select("div.paginator").select("a[href]");
        Pattern pattern = Pattern.compile("[0-9]*");
        for (int i = elements.size()-1; i >=0 ; i--) {
            String content=elements.get(i).text();
            if(pattern.matcher(content).matches()){
                return Integer.parseInt(content);
            }
        }
        return 0;
    }
}
