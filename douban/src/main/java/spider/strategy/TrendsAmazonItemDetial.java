package spider.strategy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import spider.database.TrendAmazonDataOp;
import spider.model.searchItem;
import spider.model.searchItemDetail;
import spider.tool.BloomFilter;
import spider.tool.SpiderTool;
public class TrendsAmazonItemDetial implements Runnable {
	private CountDownLatch threadsSignal; 
	public CountDownLatch getThreadsSignal() {
		return threadsSignal;
	}

	public void setThreadsSignal(CountDownLatch threadsSignal) {
		this.threadsSignal = threadsSignal;
	}

	@Override
	public void run() {
		List<String> detailList=new Vector<>();
		detailList=getDetailList(getStart(),getEnd());
		for(String url:detailList){
			getDetail(url);
		}
		threadsSignal.countDown();//绾跨▼缁撴潫鏃惰鏁板櫒鍑�1  
		System.out.println(Thread.currentThread().getName() + "缁撴潫. 杩樻湁" + threadsSignal.getCount() + " 涓嚎绋�"); 
	}

	//private static List<String> detailList;

	public static List<String> getDetailList(int start,int end) {
		List<String> detailList=new ArrayList<>();
		detailList.clear();
		for(int i=start;i<=end;i++){
			String url="http://us.trendsamazon.com/products/database/"+i;
			detailList.add(url);
		}
		return detailList;
	}
	private int start;
	private int end;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public  void getDetail(String url){
		BloomFilter bloomFilter=new BloomFilter();
		 if(bloomFilter.contains(url))
			 return;
		SpiderTool tool=new SpiderTool();
		TrendAmazonDataOp dataOp=new TrendAmazonDataOp();
		try {
			Document doc=tool.Getdoc(url,2,false);
			TrendsAmazonItemDetial detail=new TrendsAmazonItemDetial();
			 String asin=detail.getAsin(doc);
			 if(asin==null||asin.isEmpty())
				 return;
			 double best_sellers_rank=detail.getBest_sellers_rank(doc);
			 double sellers_count=detail.getSellers_count(doc);
			 double  review_count=detail.getReview_count(doc);
			 double best_review=detail.getBest_review(doc);
			 double rank_up_down=detail.getRank_up_down(doc);
			 double review_up_down=detail.getReview_up_down(doc);
			 double good_review_up_down=detail.getReview_up_down(doc);
			 List<Integer> rank=detail.getRank(doc);
			 List<Double> price=detail.getPrice(doc);
			 List<Float> star4=detail.getStar4(doc);
			 List<Float> star5=detail.getStar5(doc);
			 List<Integer> sellers=detail.getSellers(doc);
			 List<Integer> review=detail.getReview(doc);
			 List<Long> times=detail.getTime(doc);
			 if(times!=null){
				 for(int i=0;i<times.size();i++){
					 searchItemDetail pdetail=new searchItemDetail(
							  asin,  best_sellers_rank, sellers_count,  review_count,  best_review,
								 rank_up_down,  review_up_down,  good_review_up_down
							 );
					 if(price!=null&&i<price.size())
						 pdetail.setPrice(price.get(i));
					 if(star4!=null&&i<star4.size())
						 pdetail.setStar4(star4.get(i));
					 if(star5!=null&&i<star5.size())
						 pdetail.setStar5(star5.get(i));
					 if(sellers!=null&&i<sellers.size())
						 pdetail.setSellers(sellers.get(i));
					 if(review!=null&&i<review.size())
						 pdetail.setReview(review.get(i));
					 if(rank!=null&&i<rank.size())
						 pdetail.setRanks(rank.get(i));
					 if(times!=null&&i<times.size())
						 pdetail.setInserttime(times.get(i));
					 dataOp.saveDetail(pdetail);
				 }
			 }
			
			 if(!bloomFilter.contains(url)){
				 String iname=getName(doc);
				 String iimage=getIimage(doc);
				 double iprice=getIprice(doc);
				 int seller=getSeller(doc);
				 String iurl=getIurl(doc);
				 String icategory=getIcategory(doc);
				 searchItem product=new searchItem(iname,  iimage,  asin,  iprice,  seller,  iurl,icategory);
				 if(rank!=null)
					 product.setRank(rank.get(rank.size()-1));	
				 dataOp.save(product, "trendsamazon");
			 }else{
				 if(rank!=null&&price!=null)
					 dataOp.updateRankByAsin(price.get(price.size()-1),rank.get(rank.size()-1),asin);
			 }
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	public static String getIcategory(Document ele) {
		Elements eles=ele.select("tbody").select("td");
		try{
			String name=eles.eq(6).text();
			name=name.trim();
			return name;
			
		}catch(Exception e){
			return null;
		}
	}
	public static String getName(Document doc){
		String name=doc.select(".view-content").text();
		String[] temp=name.split("best");
		name=temp[0];
		return name.replace("'", "");
	}
	public static String getIimage(Document ele) {
		String name=ele.select("div.image").select("img[src]").attr("src");
		if(name!=null&&!name.isEmpty())
			return name;
		return null;
	}
	public static double getIprice(Document ele) {
		Elements eles=ele.select("tbody").select("td");
		String tems=eles.eq(2).text();
		try{
			return Double.parseDouble(tems);
			
		}catch(Exception e){
			return 0;
		}
	
	}
public static String getIurl(Document ele) {
		
		String url=ele.select("div.view-header>h3>a").attr("href");
		url="http://us.trendsamazon.com"+url;
		if(url!=null&&!url.isEmpty())
			return url;
		return null;
	}
	public static int getSeller(Document ele) {
		Elements eles=ele.select("tbody").select("td");
		String tems=eles.eq(3).text();
		try{
			return Integer.parseInt(SpiderTool.OnlyNo(tems));
			
		}catch(Exception e){
			return 0;
		}
	}
	public String getAsin(Document doc){
		String asin=doc.select(".view-header").text();
		if(asin!=null){
			asin=asin.replace("See product details", "");
		}
		return asin;
	}
	public double getBest_sellers_rank(Document doc){
		Elements eles=doc.select("div.tab-content>div.row>div.row>div.span2").select("img[src]");
		String name=eles.eq(0).attr("src");
		if(name!=null&&!name.isEmpty())
			return img2Star(name);
		return 0;
		
	}
	public double getSellers_count(Document doc){
		Elements eles=doc.select("div.tab-content>div.row>div.row>div.span2").select("img[src]");
		if(eles.size()==6){
			return 0;
		}
		String name=eles.eq(1).attr("src");
		if(name!=null&&!name.isEmpty())
			return img2Star(name);
		return 0;
		
	}
	public double getReview_count(Document doc){
		Elements eles=doc.select("div.tab-content>div.row>div.row>div.span2").select("img[src]");
		String name=null;
		if(eles.size()==6){
			name=eles.eq(1).attr("src");
		}else
			name=eles.eq(2).attr("src");
		if(name!=null&&!name.isEmpty())
			return img2Star(name);
		return 0;
		
	}
	public double getBest_review(Document doc){
		Elements eles=doc.select("div.tab-content>div.row>div.row>div.span2").select("img[src]");
		String name;
		if(eles.size()==6){
			name=eles.eq(2).attr("src");
		}else
			name=eles.eq(3).attr("src");
		
		if(name!=null&&!name.isEmpty())
			return img2Star(name);
		return 0;
		
	}
	public double getRank_up_down(Document doc){
		Elements eles=doc.select("div.tab-content>div.row>div.row>div.span2").select("img[src]");
		String name;
		if(eles.size()==6){
			name=eles.eq(3).attr("src");
		}else
			name=eles.eq(4).attr("src");
	
		if(name!=null&&!name.isEmpty())
			return img2Star(name);
		return 0;
		
	}
	public double getReview_up_down(Document doc){
		Elements eles=doc.select("div.tab-content>div.row>div.row>div.span2").select("img[src]");
		String name;
		if(eles.size()==6){
			name=eles.eq(4).attr("src");
		}else
			name=eles.eq(5).attr("src");
		if(name!=null&&!name.isEmpty())
			return img2Star(name);
		return 0;
		
	}
	public double getGood_review_up_down(Document doc){
		Elements eles=doc.select("div.tab-content>div.row>div.row>div.span2").select("img[src]");
		String name;
		if(eles.size()==6){
			name=eles.eq(5).attr("src");
		}else
			name=eles.eq(6).attr("src");
		if(name!=null&&!name.isEmpty())
			return img2Star(name);
		return 0;
		
	}
	
	public static double img2Star(String img){
		img=img.replace("/star/s", "");
		img=img.replace(".gif", "");
		double num=Double.parseDouble(img);
		double star=num;
		if(num>5){
			star=num/10;
		}
		return star;
	}
	public List<Integer> getRank(Document doc){
		List<Integer> ranklist=null;
		try{
			String name=doc.data();
			String ranks=getAttrValInLine(name,"rank");
			if(ranks==null ||ranks.isEmpty())
				return null;
			String[] ranktr=ranks.split(",");
			ranklist=new ArrayList<>(ranktr.length);
			for(String R:ranktr){
				R=R.trim();
				if(R.isEmpty())
					continue;
				ranklist.add(Integer.parseInt(R));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ranklist;
	}
	
		public static String getAttrValInLine(String line,String attr) {
			String tmpAttr = "name: \'"+attr+"\',";
		
			int start = line.indexOf(tmpAttr);
			if(start==-1){
				tmpAttr=attr;
				start = line.indexOf(attr);
			}
				
			if(start==-1){
					return null;
			}
			start+=tmpAttr.length();
			int end = line.indexOf("]",start);
			String temp=line.substring(start, end);
			temp=temp.replace("database: [", "");
			temp=temp.trim();
			return temp;
	}
	public List<Double> getPrice(Document doc){
		List<Double> ranklist=null;
		try{
			String name=doc.data();
			String ranks=getAttrValInLine(name,"Price");
			if(ranks==null)
				return null;
			String[] ranktr=ranks.split(",");
			 ranklist=new ArrayList<>(ranktr.length);
			for(String R:ranktr){
				R=R.trim();
				if(R.isEmpty())
					continue;
				ranklist.add(Double.parseDouble(R));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ranklist;
	}
	public  List<Long> getTime(Document doc){
		List<Long> ranklist=null;
		try{
			String name=doc.data();
			String ranks=getAttrValInLine(name,"categories: [");
			if(ranks==null)
				return null;
			String[] ranktr=ranks.split(",");
			ranklist=new ArrayList<>(ranktr.length);
			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");  
			Date date;
			for(String R:ranktr){
				R=R.trim();
				if(R.isEmpty())
					continue;
				String time="2016-"+R.replace("'", "");  
				 date = format.parse(time);  
				ranklist.add(date.getTime());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ranklist;
	}
	public List<Integer> getSellers(Document doc){
		List<Integer> ranklist=null;
		try{
			String name=doc.data();
			String ranks=getAttrValInLine(name,"Sellers");
			if(ranks==null)
				return null;
			String[] ranktr=ranks.split(",");
			ranklist=new ArrayList<>(ranktr.length);
			for(String R:ranktr){
				R=R.trim();
				if(R.isEmpty())
					continue;
				ranklist.add(Integer.parseInt(R));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ranklist;
	}
	public List<Integer> getReview(Document doc){
		List<Integer> ranklist=null;
		try{
			String name=doc.data();
			String ranks=getAttrValInLine(name,"Review");
			if(ranks==null)
				return null;
			String[] ranktr=ranks.split(",");
			ranklist=new ArrayList<>(ranktr.length);
			for(String R:ranktr){
				R=R.trim();
				if(R.isEmpty())
					continue;
				ranklist.add(Integer.parseInt(R));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ranklist;
	}
	public List<Float> getStar4(Document doc){
		List<Float> ranklist=null;
		try{
			String name=doc.data();
			String ranks=getAttrValInLine(name,"4star");
			String[] ranktr=ranks.split(",");
			ranklist=new ArrayList<>(ranktr.length);
			for(String R:ranktr){
				R=R.trim();
				if(R.isEmpty())
					continue;
				ranklist.add(Float.parseFloat(R));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ranklist;
	}
	public List<Float> getStar5(Document doc){
		List<Float> ranklist=null;
		try{
			String name=doc.data();
			String ranks=getAttrValInLine(name,"5star");
			String[] ranktr=ranks.split(",");
			ranklist=new ArrayList<>(ranktr.length);
			for(String R:ranktr){
				R=R.trim();
				if(R.isEmpty())
					continue;
				ranklist.add(Float.parseFloat(R));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ranklist;
	}
}