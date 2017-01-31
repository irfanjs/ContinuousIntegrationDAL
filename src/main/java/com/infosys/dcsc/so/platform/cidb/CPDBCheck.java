package com.infosys.dcsc.so.platform.cidb;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.commons.dbcp.*;

public class CPDBCheck {
	
	 public static void main(String[] args) throws Exception {
	
		PoolProperties p = new PoolProperties();
		  p.setUrl("jdbc:mysql://10.211.64.231/ci");
          p.setDriverClassName("com.mysql.jdbc.Driver");
          p.setUsername("ci");
          p.setPassword("ci");
          
          p.setJmxEnabled(true);
          p.setTestWhileIdle(false);
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
          p.setJdbcInterceptors(
            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
          
          DataSource datasource = new DataSource();
          datasource.setPoolProperties(p);
  
          Connection con = null;
          Statement st = null;
          ResultSet rs = null;
          try {
            con = datasource.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("select 'localhost' as host, 'Irfan' as user, 2+2 as password");
            int cnt = 1;
            while (rs.next()) {
                System.out.println((cnt++)+". Host:" +rs.getString("Host")+
                  " User:"+rs.getString("User")+" Password:"+rs.getString("Password"));
            }
            
          } finally {
            if (con!=null) try {con.close();}catch (Exception ignore) {}
            rs.close();
            st.close();
          }
		
	}

	 }  


