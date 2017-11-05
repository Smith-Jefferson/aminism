package com.ctrip.flight.backendservice.backofficetool.aminism.spider.model;

public class searchItem extends Item {
	private String asin;
	private double Iprice;
	private int sellers;
	private String Icategory;
	private int rank=0;
	
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getAsin() {
		return asin;
	}
	public void setAsin(String asin) {
		this.asin = asin;
	}
	public double getIprice() {
		return Iprice;
	}
	public void setIprice(double iprice) {
		Iprice = iprice;
	}
	public int getSellers() {
		return sellers;
	}
	public void setSellers(int sellers) {
		this.sellers = sellers;
	}
	public String getIcategory() {
		return Icategory;
	}
	public void setIcategory(String icategory) {
		Icategory = icategory;
	}
	public searchItem(){}
	public searchItem(String iname, String iimage, String asin, double iprice, int sellers, String iurl,
					  String icategory) {
		super(iname,iimage,iurl);
		this.asin = asin;
		Iprice = iprice;
		this.sellers = sellers;
		Icategory = icategory;
	}
}
