package spider.tool;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spider.model.DoubanConnect;
import spider.pool.DoubanConnectPool;
import spider.strategy.AgentFetcher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SpiderTool {
	private static final Logger log = LoggerFactory.getLogger(SpiderTool.class);
	private static volatile String[][] agent=new String[4][2];//代理服务器
	private static AtomicInteger up=new AtomicInteger(0);
	private static AtomicInteger down=new AtomicInteger(0);
	private final static String datapath = System.getProperty("user.dir")+"/src/main/resources/";
	public static void initailAgent() {
		try {
			AgentFetcher agentFetcher=new AgentFetcher();
			agentFetcher.run();
           /* new Thread().start();
			Thread.sleep(5000);*/
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.info("当前获取代理ip条数："+Math.abs(up.get()-down.get()));
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

	public static Document Getdoc(String oneListUrl, int tryTime,boolean isNeedLogin){
		Document doc=null;
        DoubanConnect doubanConnect=null;
		try {
			Thread.sleep(4000);
            doubanConnect= DoubanConnectPool.getInstance().getConnection();
            if(!doubanConnect.isLogin && isNeedLogin){
				doubanConnect.isLogin=true;
			}
			Connection conn=doubanConnect.getConnection();
			conn.url(oneListUrl);
			Response rse=null;
			try{
                rse=conn.ignoreContentType(true).method(Method.GET).execute();//获取响应
            }catch (Exception ex){
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
                doc=Jsoup.connect(oneListUrl).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12.4; U; fr) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").timeout(10000).get();
            }catch (Exception ex){
                log.error(ex.getMessage());
            }
			log.error(e.getMessage());
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

	public static boolean isNo(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        if(pattern.matcher(str).matches())
            return true;
        return false;
    }

	public static String RecognizeCaptcha(String imgsrc){
		String result=null;
		try{
			URL url=new URL(imgsrc);
			BufferedImage image = ImageIO.read(url);
			MyImgFilter filter=new MyImgFilter(image);
			image=filter.changeGrey();
			ITesseract instance= Tesseract.getInstance();
			instance.setDatapath(new File(datapath).getPath());
			result=instance.doOCR(image);
            result=result.replaceAll("[^a-zA-z]+","");
		}catch (Exception | Error e){
			log.error(e.getMessage());
		}
		return result;
	}

	public  WebDriver driver = new ChromeDriver();
	public  Document GetdocByExplore(String url){
		WebElement doc =null;
		try {
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
	}

}