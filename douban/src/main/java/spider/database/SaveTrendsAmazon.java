package spider.database;

import spider.model.Item;

import java.util.List;

public class SaveTrendsAmazon implements Runnable {
	private List<Item> items;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	
	public void run() {
		TrendAmazonDataOp save=new TrendAmazonDataOp();
		save.saveAll(items);
	}

}
