package spider.tool;

import com.alibaba.fastjson.JSONArray;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * Created by ygxie on 2017/10/31.
 */
public class OCRServer {
    private static String datapath = System.getProperty("user.dir")+"/src/main/resources/";
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
        }catch (Exception e){
            CLogManager.error("OCRServer",e);
        }
        return result;
    }
}
