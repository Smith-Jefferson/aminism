package com.ctrip.flight.backendservice.backofficetool.aminism.spider.tool;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.model.DoubanConnect;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.pool.DoubanConnectPool;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.strategy.AgentFetcher;
import com.ctrip.flight.backendservice.backofficetool.spider.log.CLogManager;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class SpiderTool {
	//private static final Logger log = LoggerFactory.getLogger(SpiderTool.class);
	private static volatile String[][] agent=new String[4][2];//代理服务器
	private static AtomicInteger up=new AtomicInteger(0);
	private static AtomicInteger down=new AtomicInteger(0);
	public static void initailAgent() {
		try {
			AgentFetcher agentFetcher=new AgentFetcher();
			agentFetcher.run();
           /* new Thread().start();
			Thread.sleep(5000);*/
		} catch (Exception e) {
			CLogManager.error(e);
		}
		CLogManager.info("aminism","当前获取代理ip条数："+Math.abs(up.get()-down.get()));
	}
	public static synchronized Map<String,String> getAgent() {
		Map<String,String> proxy=new HashMap<String,String>(2);
		if(down.get()>agent.length || down.get()>=up.get())
			down=new AtomicInteger(0);
		proxy.put("host",agent[down.get()][0]);
		proxy.put("port",agent[down.getAndIncrement()][1]);
		return proxy;
	}
	public static synchronized void setAgent(String host,String port) {
		if(up.get()>agent.length)
			up=new AtomicInteger(0);
		agent[up.get()][0]=host;
		agent[up.getAndIncrement()][1]=port;
	}

	private static void sleep(){
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			CLogManager.error("ThreadSleep",e);
		}
	}

	public static synchronized Document Getdoc(String oneListUrl, int tryTime,boolean isNeedLogin){
		Document doc=null;
        DoubanConnect doubanConnect=null;
		try {
            doubanConnect= DoubanConnectPool.getInstance().getConnection();
            if(!doubanConnect.isLogin && isNeedLogin){
				doubanConnect.isLogin=true;
			}
			Connection conn=doubanConnect.getConnection();
			conn.url(oneListUrl);
			Response rse=null;
			try{
                rse=conn.ignoreContentType(true).method(Method.GET).execute();//获取响应
				sleep();
            }catch (Exception ex){
				CLogManager.error(ex);
                rse=conn.ignoreContentType(true).method(Method.POST).execute();//获取响应
            }
			doc = Jsoup.parse(rse.body());//转换为Dom树
			if (doc == null && tryTime >= 0) {
				System.out.println("解析product：" + oneListUrl + "的 DOC 时出错！剩余尝试次数："
						+ tryTime);
				return Getdoc(oneListUrl, tryTime--,isNeedLogin);
			} else if (isLogin(doc)) {
				doubanConnect.setStatus(false);
				System.out.println("掉线重连...");
				return Getdoc(oneListUrl, tryTime--,isNeedLogin);
			}
		} catch (Exception e) {
		    try {
				sleep();
                doc=Jsoup.connect(oneListUrl).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12.4; U; fr) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(10000).get();
            }catch (Exception ex){
                CLogManager.error(ex);
            }
			CLogManager.error(e);
		}finally{
		    if(doubanConnect!=null)
			    DoubanConnectPool.freeConnection(doubanConnect);
		}
		return doc;
	}
	public static boolean isLogin(Document doc){
		Elements ele=doc.select("form_email");
		if(ele==null)
			return true;
		return false;
	}

	public static String removeZh(String str){
		if(str==null || str.trim()=="")
			return str;
		String reg="[\u4e00-\u9fa5]";
		Pattern pattern = Pattern.compile(reg);
		return pattern.matcher(str).replaceAll("");
	}

	public static String OnlyNo(String str){
		StringBuffer str2=new StringBuffer(str.length());
		for(int i=0;i<str.length();i++) {
			if ((str.charAt(i) >= 48 && str.charAt(i) <= 57)||str.charAt(i)==46 ) {
				str2.append(str.charAt(i));
			}
		}
		return str2.toString();
	}

	public static boolean isNo(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        if(pattern.matcher(str).matches())
            return true;
        return false;
    }



	/*public  Document GetdocByExplore(String url){
		WebElement doc =null;
		try {
			WebDriver driver = new ChromeDriver();
			driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
			driver.get(url);
			WebDriverWait w = new WebDriverWait(driver, 10);
			doc = driver.findElement(By.tagName("body"));
			driver.close();
			if(doc!=null){
                Document webpage=null;
                webpage = Jsoup.parse(doc.toString());
                return  webpage;
            }
		} catch (Exception e) {
			log.error(e.toString());
		}
		return null;
	}*/

}