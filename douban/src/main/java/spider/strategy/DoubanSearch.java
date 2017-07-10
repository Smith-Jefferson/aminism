package spider.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.tool.SpiderTool;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Created by hello world on 2017/1/11.
 */
public class DoubanSearch implements Runnable{
    private String base;
    private String searchName;
    private String searchType;
    private static final Logger log = LoggerFactory.getLogger(DoubanSearch.class);

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
            log.error(e.getMessage());
            return searchName;
        }
    }

    public void setSearchName(String searchName) {
        try {
            this.searchName = URLEncoder.encode(searchName,"utf-8");
        }catch (Exception e){
            log.error(e.getMessage());
            this.searchName =searchName;
        }
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public DoubanSearch(String base, String searchName, String searchType) {
        this.base = base;
        this.searchName = searchName;
        this.searchType = searchType;
    }

    @Override
    public void run() {
        String url=base+searchType+"/"+searchName;
        try {
            Thread.sleep(2000);
        }catch (Exception e){}

        Document doc= SpiderTool.Getdoc(url,3,false);
        Elements books=doc.select("a[href]");
        DoubanBookIndex.addToSchedule(books);
        int maxstep=getMaxNum(doc);
        maxstep=maxstep>3?3:maxstep;
        for(int i=2;i<maxstep;i++){
            try {
                Thread.sleep(2000);
            }catch (Exception e){}

            url=getNextPage(base,i);
            if(url!=null){
                DoubanBookIndex.addToSchedule(SpiderTool.Getdoc(url,3,false).select("a[href]"));
            }else
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
