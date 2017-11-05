package com.ctrip.flight.backendservice.backofficetool.aminism.spider.model;

public class Item {
	private String Iname;
	private String Iimg_url;
	private String Iurl;
	public String getIname() {
		return Iname;
	}
	public void setIname(String iname) {
		Iname = iname;
	}
	public String getIimg_url() {
		return Iimg_url;
	}
	public void setIimg_url(String iimg_url) {
		Iimg_url = iimg_url;
	}
	public String getIurl() {
		return Iurl;
	}
	public void setIurl(String iurl) {
		Iurl = iurl;
	}
	public Item(String iname, String iimg_url, String iurl) {
		super();
		Iname = iname;
		Iimg_url = iimg_url;
		Iurl = iurl;
	}
	
	public String toString() {
		return "Item [Iname=" + Iname + ", Iurl=" + Iurl + "]";
	}
	public Item(){};
}
