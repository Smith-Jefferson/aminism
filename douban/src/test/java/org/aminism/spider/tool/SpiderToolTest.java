package org.aminism.spider.tool;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import static org.junit.Assert.*;
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
    Document doc= SpiderTool.Getdoc("https://book.douban.com/",3,false);
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

@Test
public void testOnlyNo(){
    String s="() 3.9";
    s=SpiderTool.OnlyNo(s);
    assertTrue("3.9".equals(s));
}

@Test
public void testSubUrl(){
    String url="https://book.douban.com/subject/3920144/";
    if(url.endsWith("/")){
        url=url.substring(0,url.length()-1);
    }
    System.out.print(url);
}


} 
