package com.ctrip.flight.backendservice.backofficetool.aminism.spider.model;

public class searchItemDetail extends searchItem {
	private double best_sellers_rank;
	private double sellers_count;
	private double  review_count;
	private double best_review;
	private double rank_up_down;
	private double review_up_down;
	private double good_review_up_down;
	private double price;
	private double star4;
	private double star5;
	private int sellersTr;
	private int review;
	private int ranks;
	private long inserttime;
	
	public long getInserttime() {
		return inserttime;
	}
	public void setInserttime(long inserttime) {
		this.inserttime = inserttime;
	}
	public int getRanks() {
		return ranks;
	}
	public void setRanks(int rank) {
		this.ranks = rank;
	}
	public double getBest_sellers_rank() {
		return best_sellers_rank;
	}
	public void setBest_sellers_rank(double best_sellers_rank) {
		this.best_sellers_rank = best_sellers_rank;
	}
	public double getReview_count() {
		return review_count;
	}
	public void setReview_count(double review_count) {
		this.review_count = review_count;
	}
	public double getBest_review() {
		return best_review;
	}
	public void setBest_review(double best_review) {
		this.best_review = best_review;
	}
	public double getRank_up_down() {
		return rank_up_down;
	}
	public void setRank_up_down(double rank_up_down) {
		this.rank_up_down = rank_up_down;
	}
	public double getReview_up_down() {
		return review_up_down;
	}
	public void setReview_up_down(double review_up_down) {
		this.review_up_down = review_up_down;
	}
	public double getGood_review_up_down() {
		return good_review_up_down;
	}
	public void setGood_review_up_down(double good_review_up_down) {
		this.good_review_up_down = good_review_up_down;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getStar4() {
		return star4;
	}
	public void setStar4(double star4) {
		this.star4 = star4;
	}
	public double getStar5() {
		return star5;
	}
	public void setStar5(double star5) {
		this.star5 = star5;
	}
	public int getSellers() {
		return sellersTr;
	}
	public void setSellers(int sellersTr) {
		this.sellersTr = sellersTr;
	}
	public int getReview() {
		return review;
	}
	public void setReview(int review) {
		this.review = review;
	}
	
	public double getSellers_count() {
		return sellers_count;
	}
	public void setSellers_count(double sellers_count) {
		this.sellers_count = sellers_count;
	}
	public searchItemDetail(String asin, double best_sellers_rank, double sellers_count, double review_count, double best_review,
                            double rank_up_down, double review_up_down, double good_review_up_down){
		super();
		setAsin(asin);
		this.best_sellers_rank = best_sellers_rank;
		this.sellers_count=sellers_count;
		this.review_count = review_count;
		this.best_review = best_review;
		this.rank_up_down = rank_up_down;
		this.review_up_down = review_up_down;
		this.good_review_up_down = good_review_up_down;
	}
	@Override
	public String toString() {
		return "Item [详情：asin=" + getAsin() +"]";
	}
	
}