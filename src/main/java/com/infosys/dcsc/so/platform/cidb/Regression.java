package com.infosys.dcsc.so.platform.cidb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Regression {

	int projectid;
	
	public Regression(int projectid)
	{
		this.projectid = projectid;
	}
	
	
	public List<Map<String, Object>> getWeekRegAggregateDataNightlyBuild() throws SQLException
	{
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -7);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		String sql = "select bi.buildnumber,rg.total,rg.pass,rg.fail from buildinfo bi inner join regression rg on bi.nightlybuild_id = rg.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL and bi.datetime > '" + dateFormat.format(cal.getTime()) + "'" + " and bi.datetime < '" + dateFormat.format(date) + "'" + ";";
		return executeQuery(sql);
		
	}
	
	public List<Map<String, Object>> getMonthRegAggregateDataNightlyBuild() throws SQLException
	{
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -30);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		String sql = "select bi.buildnumber,rg.total,rg.pass,rg.fail from buildinfo bi inner join regression rg on bi.nightlybuild_id = rg.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL and bi.datetime > '" + dateFormat.format(cal.getTime()) + "'" + " and bi.datetime < '" + dateFormat.format(date) + "'" + ";";
		return executeQuery(sql);
		
	}
	
	public List<Map<String, Object>> getCustomRegAggregateDataNightlyBuild(String todate,String fromdate) throws SQLException
	{
		String dateString1 = new String(todate);
		String dateString2 = new String(fromdate);
		
		String finalfromdate = null;
		String finaltodate = null;
		
		    java.util.Date dtDate = new Date();
		//	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			SimpleDateFormat sdfAct = new SimpleDateFormat("dd/MM/yyyy");
			try
			{
			dtDate = sdfAct.parse(dateString1);
			System.out.println("Date After parsing in required format:"+(sdf.format(dtDate)));
			finaltodate = (sdf.format(dtDate));
			}
			catch (ParseException e)
			{
			System.out.println("Unable to parse the date string");
			e.printStackTrace();
			}
			
			try
			{
			dtDate = sdfAct.parse(dateString2);
			System.out.println("Date After parsing in required format:"+(sdf.format(dtDate)));
			finalfromdate = (sdf.format(dtDate));
			}
			catch (ParseException e)
			{
			System.out.println("Unable to parse the date string");
			e.printStackTrace();
			}
			
	String sql = "select bi.buildnumber,rg.total,rg.pass,rg.fail from buildinfo bi inner join regression rg on bi.nightlybuild_id = rg.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL and bi.datetime > '" + finalfromdate + "'" + " and bi.datetime < '" + finaltodate + "'" + ";";
		return executeQuery(sql);
		
	}
	
	
	
	public List<Map<String, Object>> getWeekRegcoverageAggregateDataNightlyBuild() throws SQLException
	{
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -7);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		String sql = "select bi.buildnumber,reg.coverage from regression reg inner join buildinfo bi on bi.nightlybuild_id = reg.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.datetime >  '" + dateFormat.format(cal.getTime()) + "'" + " and bi.datetime < '" + dateFormat.format(date) + "'" + " and bi.nightlybuild_id is not NULL;";
		return executeQuery(sql);
		
	}
	
	public List<Map<String, Object>> getMonthRegcoverageAggregateDataNightlyBuild() throws SQLException
	{
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -30);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		String sql = "select bi.buildnumber,reg.coverage from regression reg inner join buildinfo bi on bi.nightlybuild_id = reg.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.datetime >  '" + dateFormat.format(cal.getTime()) + "'" + " and bi.datetime < '" + dateFormat.format(date) + "'" + " and bi.nightlybuild_id is not NULL;";
		return executeQuery(sql);
		
	}
	
	
	
	public List<Map<String, Object>> executeQuery(String sql)
			throws SQLException {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			conn = CIDBHelper.getInstance().getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);

			return CIDBHelper.getInstance().getEntitiesFromResultSet(resultSet);
		}

		finally {
			CIDBHelper.close(conn, statement, resultSet);
		}

	}
	
		
	

	
	

	

		}

