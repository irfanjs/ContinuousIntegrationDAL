package com.infosys.dcsc.so.platform.cidb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;


public class CIDBHelper {
	static private CIDBHelper cihelper;

	private final static Object cihelperLock = new Object();

	private DataSource dataSource;

	private CIDBHelper() {
		PoolProperties p = new PoolProperties();
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		String dbserver=prop.getProperty("db_server");
		String dbuser=prop.getProperty("db_user");
		String dbpasswd=prop.getProperty("db_password");
		
		p.setUrl("jdbc:mysql://" + dbserver + "/ci");
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername(dbuser);
		p.setPassword(dbpasswd);

		p.setJmxEnabled(true);
		p.setTestWhileIdle(true);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

		dataSource = new DataSource(p);
	}

	public static synchronized CIDBHelper getInstance() {
		if (cihelper == null) {
			cihelper = new CIDBHelper();
		}
		return cihelper;
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public List<Map<String, Object>> getEntitiesFromResultSet(
			ResultSet resultSet) throws SQLException {
		ArrayList<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
		while (resultSet.next()) {
			entities.add(getEntityFromResultSet(resultSet));
		}
		return entities;
	}

	protected Map<String, Object> getEntityFromResultSet(ResultSet resultSet)
			throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();
		Map<String, Object> resultsMap = new HashMap<String, Object>();
		for (int i = 1; i <= columnCount; ++i) {
			String columnName = metaData.getColumnName(i).toLowerCase();
			Object object = resultSet.getObject(i);
			resultsMap.put(columnName, object);
		}
		return resultsMap;
	}

	/*
	 * private Connection connect; private final Object connlock = new Object();
	 * public Connection getConnection() throws SQLException,
	 * ClassNotFoundException{ synchronized(connlock){ if(null == connect){ //
	 * Setup the connection with the DB Class.forName("com.mysql.jdbc.Driver");
	 * connect = DriverManager .getConnection("jdbc:mysql://10.211.64.231/ci?" +
	 * "user=ci&password=ci&noAccessToProcedureBodies=true&autoReconnect=true");
	 * } } return connect; }
	 */
	public Statement createExecuteStatement() throws SQLException,
			ClassNotFoundException {
		// Statements allow to issue SQL queries to the database
		// Connection connect = CIDBHelper.getInstance();
		Connection c = getConnection();
		return c.createStatement();
	}

	public PreparedStatement createInsertStatement(String sqlQuery)
			throws SQLException, ClassNotFoundException {
		// Statements allow to issue SQL queries to the database
		Connection c = getConnection();
		return c.prepareStatement(sqlQuery);
	}

	public static void close(Connection c, Statement s, ResultSet r) {
		try {
			if(r != null){
				r.close();
			}
			if(s != null){
				s.close();
			}
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			// ignore
		}
	}
}
