package spider.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import spider.App;
import spider.database.DoubanDataRep;
import spider.model.TaskUrlEntity;
import spider.tool.CLogManager;
import spider.tool.SpiderTool;

/**
 * Created by hello world on 2017/1/11.
 */
@Service
public class DoubanBookTaskInitServer {
    @Autowired
    private DoubanDataRep doubanDataRep;
    @Autowired
    private DoubanSearch doubanSearch;
    private static final Logger log = LoggerFactory.getLogger(DoubanBookTaskInitServer.class);
    public void getBookUrl(){
        Document doc= SpiderTool.Getdoc("https://book.douban.com/",3,false);
        //首页直接获取书籍地址
        Elements books=doc.select("a[href]");
        addToSchedule(books);
        //通过tag搜索获得
        Elements tags=doc.select("div.aside ul.hot-tags-col5").select("a[href]");
        int count=3;
        for (Element tag:tags){
            String[] attr=tag.attr("href").split("/");
            try {
                doubanSearch.init("https://book.douban.com/",attr[2],attr[1]);
                if(count-->0)
                    doubanSearch.run();
                else
                    App.fixedThreadPool.execute(doubanSearch);
            }catch (Exception e){
                CLogManager.error(e);
            }
        }
    }

    public void addToSchedule(Elements books){
        for (Element book:books) {
            addToSchedule(book);
        }
    }
    public void addToSchedule(Element book){
        String url=book.attr("abs:href");
        addToSchedule(url);
    }

    public void addToSchedule(String url){
        if(isBookUrl(url)){
            try {
                String bookid=url.split("subject/")[1].split("/")[0];
                url="https://book.douban.com/subject/"+bookid;
                if(!App.getBloomFilter().contains(bookid)){
                    doubanDataRep.saveTaskUrl(new TaskUrlEntity(url));
                }
            } catch (Exception e) {
                CLogManager.error(e);
            }
        }
    }

    public static boolean isBookUrl(String url){
        if (url!=null&&url.contains("book") && url.contains("com/subject"))
            return true;
        return false;
    }
}
