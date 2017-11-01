package spider;

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import spider.tool.OCRServer;
import spider.tool.SpiderTool;

/** 
* App Tester. 
* 
* @author <Authors name> 
* @since <pre>һ�� 10, 2017</pre> 
* @version 1.0 
*/ 
public class AppTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: main(String[] args) 
* 
*/ 
@Test
public void testMain() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getBloomFilter() 
* 
*/ 
@Test
public void testGetBloomFilter() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: setBloomFilter(BloomFilter bloomFilter) 
* 
*/ 
@Test
public void testSetBloomFilter() throws Exception { 
//TODO: Test goes here... 
}

@Test
public void testRecoginzeCaptcha(){
    String image="https://www.douban.com/misc/captcha?id=rR24CC3LLbAbIJ2nxtKBIeJp:en&size=s";
    String s= new OCRServer().RecognizeCaptcha(image);
    System.out.printf(s);
}


} 
