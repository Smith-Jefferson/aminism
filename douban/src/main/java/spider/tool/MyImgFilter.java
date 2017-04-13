package spider.tool;

/**
 * Created by hello world on 2017/1/18.
 */

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class MyImgFilter {
    BufferedImage image;
    private int iw, ih;
    private int[] pixels;

    public MyImgFilter(BufferedImage image) {
        this.image = image;
        iw = image.getWidth();
        ih = image.getHeight();
        pixels = new int[iw * ih];
    }

    public BufferedImage changeGrey() {
        BufferedImage binaryBufferedImage = new BufferedImage(iw, ih,
                BufferedImage.TYPE_BYTE_BINARY);
        int[][] gray = new int[iw][ih];
        for (int x = 0; x < iw; x++)
        {
            for (int y = 0; y < ih; y++)
            {
                int argb = image.getRGB(x, y);
                // 图像加亮（调整亮度识别率非常高）
                int r = (int) (argb >> 16) & 0xFF;
                int g = (int) (argb >> 8) & 0xFF;
                int b = (int) (argb >> 0) & 0xFF;
                if (r >= 15 || g>15 || b>15)
                {
                    gray[x][y] |= 0x00FFFF;
                }
                else
                    gray[x][y] &= 0xFF0000;
            }
        }
        binaryBufferedImage=medieanfilter(gray,2);
        return binaryBufferedImage;

    }
    public BufferedImage medieanfilter(int[][] image,int window){
        BufferedImage binaryBufferedImage = new BufferedImage(iw, ih,
                BufferedImage.TYPE_BYTE_BINARY);
        int[] window_x=new int[]{1, 0, 0, -1, 0};
        int[] window_y= new int[]{0, 1, 0, 0, -1};
        if(window==2){
            window_x = new int[]{-1,  0,  1, -1, 0, 1, 1, -1, 0};
            window_y= new int[]{-1, -1, -1,  1, 1, 1, 0,  0, 0};
        }
        for(int i=0;i<iw;i++){
            for(int j=0;j<ih;j++){
                ArrayList<Integer> box=new ArrayList<>();
                int black_count=0,white_count=0;
                for (int k = 0; k < window_x.length; k++) {
                    int d_x=i + window_x[k];
                    int d_y=j + window_y[k];
                    try{
                        if(image[d_x][d_y] == (image[d_x][d_y] | 0x00FFFF)){
                            box.add(0);
                        }else{
                            box.add(1);
                        }
                    }catch (Exception e){
                        image[i][j] |= 0x00FFFF;
                        continue;
                    }
                }
                Collections.sort(box);
                if(box.size()==window_x.length){
                    int mid=box.get((int)box.size()/2);
                    if(mid==0){
                        image[i][j] |= 0x00FFFF;
                    }else{
                        image[i][j] &= 0xFF0000;
                    }
                }
                binaryBufferedImage.setRGB(i, j, image[i][j]);
            }
        }
        return binaryBufferedImage;
    }


}
/*运行java myfilter.MyImgFilter t6.bmp，请确认图片t6.bmp与myfilter目录在同一目录下。
顺便说一下，在JDK1.5下，ImageIO可以输出JPG，BMP，PNG三种格式图片，但不支持GIF图片输出。*/

