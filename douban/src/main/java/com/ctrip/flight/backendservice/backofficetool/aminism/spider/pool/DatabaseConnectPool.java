package com.ctrip.flight.backendservice.backofficetool.aminism.spider.pool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import javax.activation.DataSource;

/**
 * 数据库连接池
 */
public class DatabaseConnectPool implements DataSource {
	//数据库连接信息
	private static final String url = "jdbc:mysql://localhost:3306/aminism";
	private static final String user = "root";
	private static final String pswd = "root";
 
	// 初始化
	private  static Queue<Connection> pool = new ConcurrentLinkedQueue<Connection>();
	private static DatabaseConnectPool instance = new DatabaseConnectPool();

	public static DatabaseConnectPool getInstance() {
		return instance;
	}

	public static Connection getConnection() throws SQLException {
		if (pool!=null &&pool.size() > 0) {
			return pool.poll();
		} else {
			return DriverManager.getConnection(url, user, pswd);
		}
	}
	public static void freeConnection(Connection conn) {
		pool.add(conn);
	}
 

 
	public PrintWriter getLogWriter() throws SQLException {
		
		return null;
	}
 
	public void setLogWriter(PrintWriter out) throws SQLException {
		
 
	}
 
	public void setLoginTimeout(int seconds) throws SQLException {
		
 
	}
 
	public int getLoginTimeout() throws SQLException {
		
		return 0;
	}
 
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		
		return null;
	}
 
	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return null;
	}
 
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		
		return false;
	}

	@Override
	public String getContentType() {
		
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		
		return null;
	}

	@Override
	public String getName() {
		
		return null;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		
		return null;
	}
}