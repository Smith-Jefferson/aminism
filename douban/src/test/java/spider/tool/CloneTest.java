package spider.tool;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import com.ctrip.flight.backendservice.backofficetool.aminism.spider.model.DoubanConnect;

/**
 * Created by xieyigang on 2017/7/21.
 */
public class CloneTest {
    @Test
    public void copytest(){
        DoubanConnect con=new DoubanConnect();
//        DoubanConnect con1=(DoubanConnect)con.clone();
        con.setStatus(false);
 //       Assert.assertNotEquals(con.isStatus(),con1.isStatus());
 //       DoubanConnect con2=con;
 //       Assert.assertEquals(con.isStatus(),con2.isStatus());
        try {
            con.getConnection().timeout(80);
            String doc=con.getConnection().url("https://list.lu.com/list/transfer-p2p?minMoney=&maxMoney=&minDays=&maxDays=&minRate=&maxRate=&mode=&tradingMode=&isOverdueTransfer=&isCx=&currentPage=1&orderCondition=&isShared=&canRealized=&productCategoryEnum=&notHasBuyFeeRate=&riskLevel=").method(Connection.Method.GET).execute().body().toString();
            System.out.println(doc);
            doc=Jsoup.connect("https://list.lu.com/list/transfer-p2p?minMoney=&maxMoney=&minDays=&maxDays=&minRate=&maxRate=&mode=&tradingMode=&isOverdueTransfer=&isCx=&currentPage=2&orderCondition=&isShared=&canRealized=&productCategoryEnum=&notHasBuyFeeRate=&riskLevel=").timeout(80).method(Connection.Method.GET).execute().body().toString();

            System.out.println(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
