package spider.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.App;
import spider.service.DoubanBookTask;
import spider.tool.SpiderTool;

/**
 * Created by hello world on 2017/1/11.
 */
public class DoubanBookIndex {
    private static final Logger log = LoggerFactory.getLogger(DoubanBookIndex.class);
    public static void getBookUrl(){
        Document doc= SpiderTool.Getdoc("https://book.douban.com/",3,false);
        if(doc==null){
            SpiderTool tool=new SpiderTool();
            doc=tool.GetdocByExplore("https://book.douban.com/");
        }
        //首页直接获取书籍地址
        Elements books=doc.select("a[href]");
        addToSchedule(books);
        //通过tag搜索获得
        Elements tags=doc.select("div.aside ul.hot-tags-col5").select("a[href]");
        int count=5;
        for (Element tag:tags){
            String[] attr=tag.attr("href").split("/");
            try {
                DoubanSearch doubanSearch=new DoubanSearch("https://book.douban.com/",attr[2],attr[1]);

                if(--count>0)
                    App.fixedThreadPool.execute(doubanSearch);
                else
                    doubanSearch.run();
                //new Thread(doubanSearch).start();
            }catch (Exception e){
                log.info(e.getMessage());
            }
        }
    }

    public static void addToSchedule(Elements books){
        for (Element book:books) {
            addToSchedule(book);
        }
    }
    public static void addToSchedule(Element book){
        String url=book.attr("abs:href");
        addToSchedule(url);
    }

    public static void addToSchedule(String url){
        if(isBookUrl(url)){
            try {
                url="https://book.douban.com/subject/"+url.split("subject/")[1].split("/")[0];
                if(!App.getBloomFilter().contains(url)){
                    synchronized (DoubanBookTask.bookSchedule){
                        DoubanBookTask.bookSchedule.add(url);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static boolean isBookUrl(String url){
        if (url!=null&&url.contains("book") && url.contains("com/subject"))
            return true;
        return false;
    }
}
