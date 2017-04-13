package test.spider.tool; 

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import spider.tool.SpiderTool;

/** 
* SpiderTool Tester. 
* 
* @author <Authors name> 
* @since <pre>һ�� 11, 2017</pre> 
* @version 1.0 
*/ 
public class SpiderToolTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: Getdoc(String oneListUrl, int tryTime) 
* 
*/ 
@Test
public void testGetdoc() throws Exception {
    Document doc= SpiderTool.Getdoc("https://book.douban.com/",3);
    System.out.print(doc.toString());
} 

/** 
* 
* Method: isLogin(Document doc) 
* 
*/ 
@Test
public void testIsLogin() throws Exception { 
//TODO: Test goes here... 
} 


} 
