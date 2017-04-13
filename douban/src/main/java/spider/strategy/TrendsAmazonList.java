package spider.strategy;

import spider.model.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrendsAmazonList implements ItemList {

	@Override
	public List<Item> getItemList(String url, String keyword, Map<String,String> data) {
		
		String max=data.get("MAX_NUM");
		List<Item> totolItem =new ArrayList<>();
		int max_num=1;
		if(!max.isEmpty()){
			 max_num=Integer.parseInt(max);
		}
		url+="?keyword="+keyword;
		String url1;
		for(int i=1;i<=max_num;i++){
			url1=url+"&page="+i;
			try {
				int size= totolItem.size();
				totolItem.addAll(getItemList(url1));
				if(size== totolItem.size())
					break;
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return totolItem;
	}
	public static List<Item> getItemList(String url) throws IOException{

		return null;
	}
}