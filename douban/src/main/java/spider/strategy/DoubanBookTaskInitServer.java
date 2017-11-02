package spider.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spider.App;
import spider.database.DoubanDataRep;
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
    //private static final Logger log = LoggerFactory.getLogger(DoubanBookTaskInitServer.class);
    public void getBookUrl(){
        Document doc= SpiderTool.Getdoc("https://book.douban.com/",3,false);
        //首页直接获取书籍地址
        Elements books=doc.select("a[href]");
        doubanDataRep.addToSchedule(books);
        //通过tag搜索获得
        Elements tags=doc.select("div.aside ul.hot-tags-col5").select("a[href]");
        int count=3;
        for (Element tag:tags){
            String herf=tag.attr("href");
            if(herf.contains("?"))continue;
            String[] attr=herf.split("/");
            try {
                doubanSearch.init("https://book.douban.com/",attr[2],attr[1]);
                //if(count-->0)
                //    doubanSearch.run();
                //else
                    App.fixedThreadPool.execute(doubanSearch);
            }catch (Exception e){
                CLogManager.error(e);
            }
        }
    }


}
