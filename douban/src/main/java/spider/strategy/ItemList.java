package spider.strategy;

import spider.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemList {
			List<Item> getItemList(String url, String query, Map<String,String> data);
}
