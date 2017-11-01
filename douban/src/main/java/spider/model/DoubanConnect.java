package spider.model;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.service.DoubanBookTask;
import spider.tool.CLogManager;
import spider.tool.SpiderTool;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hello world on 2017/1/10.
 */
public class DoubanConnect extends CloneableBase{
    private static final Logger log = LoggerFactory.getLogger(DoubanBookTask.class);
    private volatile static Map cookies;
    private volatile boolean status=true;
    private Connection con;
    public static boolean isLogin=false;
    public Connection getConnection() throws Exception{
        if(con==null)
            setCon();
        if(cookies==null){
            log.info("建立新的联接...");
            //if(isLogin)
                //login("zhishanghan@qq.com","xieyigang123");
        }else {
            setCon();
            con.cookies(cookies);
        }
        return con;
    }

    public Map getCookies() {
        return cookies;
    }

    public void setCon() throws Exception {
        System.setProperty("http.maxRedirects", "50");
        System.getProperties().setProperty("proxySet", "true");
        // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
        Map<String,String> proxy= SpiderTool.getAgent();
        System.getProperties().setProperty("http.proxyHost", proxy.get("host")!=null?proxy.get("host"): InetAddress.getLocalHost().getHostAddress());
        System.getProperties().setProperty("http.proxyPort", proxy.get("port")!=null?proxy.get("port"):"80");
        this.con= Jsoup.connect("https://www.douban.com");
        this.con.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12.4; U; fr) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31");
        this.con.timeout(300 * 1000);
    }
    public void login(String userName,String pwd) {
        try{
            setCon();
            Connection.Response rs= con.execute();//获取响应
            Document d1=Jsoup.parse(rs.body());//转换为Dom树
            List<Element> et= d1.select("form#lzform");//获取form表单，可以通过查看页面源码代码得知
            //获取，cooking和表单属性，下面map存放post时的数据
            Map<String, String> datas=new HashMap<>();
            for(Element e:et.get(0).getAllElements()){
                if(e.attr("name").equals("form_email")){
                    e.attr("value", userName);//设置用户名
                }

                if(e.attr("name").equals("form_password")){
                    e.attr("value",pwd); //设置用户密码
                }

                if(e.attr("name").equals("remember")){
                    e.attr("checked",true);
                }
                if(e.attr("name").equals("source")){
                    e.attr("value","book");
                }
                /*
                if(e.attr("name").equals("captcha-solution")){
                    String imgurl=d1.select("img#captcha_image").attr("src");
                    String captcha=SpiderTool.RecognizeCaptcha(imgurl);
                    e.attr("value",captcha);
                }
                */
                if(e.attr("name").length()>0){//排除空值表单属性
                    datas.put(e.attr("name"), e.attr("value"));
                }
            }
            Connection.Response login=con.ignoreContentType(true).method(Connection.Method.POST).data(datas).cookies(rs.cookies()).execute();
            if(login.cookies()==null || login.cookies().size()==0)
                login(userName,pwd);
            else{
                con.cookies(login.cookies());
                cookies=login.cookies();
            }
        }
        catch (Exception e){
            try {
                setCon();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            CLogManager.error(e);
        }

    }
    public boolean isStatus(){
        return status;
    }

    public synchronized void setStatus(boolean status){
        this.status=status;
    }
}