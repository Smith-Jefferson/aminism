package com.ctrip.flight.backendservice.backofficetool.aminism.spider.pool;

import com.ctrip.flight.backendservice.backofficetool.aminism.spider.model.DoubanConnect;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class DoubanConnectPool {
	public static Map<String,String> data;
	public static Map<String,String> cookies;
	// 连接池队列
	private static Queue<DoubanConnect> pool = new ConcurrentLinkedQueue<>();
	private static DoubanConnectPool instance = new DoubanConnectPool();

	/**
	 * 获取一个数据库连接
	 */
	public static DoubanConnectPool getInstance(){
		return instance;
	}
	public DoubanConnect getConnection(){
		if (pool.size() > 0) {
			return pool.poll();
		} else {
			return new DoubanConnect();
		}
	}
	/**
	 * 连接归池，这里的实现思想是使用过的线程入池以备下次使用
	 */
	public static void freeConnection(DoubanConnect conn) {
		if(conn!=null && conn.isStatus()==true)
			pool.add(conn);
	}

	public static void addConnection(DoubanConnect conn){
		if(conn!=null)
			pool.add(conn);
	}
}
