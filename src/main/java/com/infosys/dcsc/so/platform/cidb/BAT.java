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

public class BAT {

	int projectid;
	
	public BAT(int projectid)
	{
		this.projectid = projectid;
	}
	
	public boolean insert(int buildintoId,int total,int passed,int failed) throws SQLException, ClassNotFoundException{
		
		
		Connection conn = null;
		PreparedStatement prepStatement = null;
		try
		{
			conn = CIDBHelper.getInstance().getConnection();
			prepStatement = conn
					.prepareStatement("insert into bat(buildinfo_id,total,pass,fail) values(?,?,?,?);");
		
		prepStatement.setInt(1, buildintoId);
		prepStatement.setInt(2, total);
		prepStatement.setInt(3, passed);
		prepStatement.setInt(4, failed);
		
		prepStatement.executeUpdate();
		
		}
		finally 
		{
			CIDBHelper.close(conn, prepStatement, null);
	}
		return true;
	}
	
	public List<Map<String, Object>> getBATDataForLatestBuildId() throws SQLException, ClassNotFoundException{
		
		String sql = "select bt.total,"
						+ "bt.pass,"
						+ "bt.fail,"
						+ "bi.id,"
						+ "bi.buildnumber "
						+ "from bat bt, buildinfo bi "
						+ "where bi.id = bt.buildinfo_id "
						+ "order by datetime desc "
						+ "limit 1;";
		
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
	
	public List<Map<String, Object>> getbatspecificbldno(int buildnumber) throws SQLException, ClassNotFoundException{
		//	String sql = "select bi.buildnumber,ut.total,ut.pass,ut.fail,ut.skip from buildinfo bi inner join unittest ut on bi.id = ut.buildinfo_id where bi.project_id = " + this.projectid + " and bi.buildnumber = " + buildnumber + " and bi.nightlybuild_id is not NULL;";
	String sql = "select bi.buildnumber,bt.total,bt.pass,bt.fail from bat bt inner join buildinfo bi on bt.nightlybuild_id = bi.nightlybuild_id where bi.buildnumber = " + buildnumber + " and bi.project_id = " + this.projectid + ";";
			return executeQuery(sql);
		}


	public List<Map<String, Object>> getBATForBuildId(int buildnumber) throws SQLException, ClassNotFoundException{
	 String sql = "select bt.total,"
						+ "bt.pass,"
						+ "bt.fail,"
						+ "bi.id,"
						+ "bi.buildnumber "
						+ "from bat bt, buildinfo bi "
						+ "where bi.id = bt.buildinfo_id "
						+ "and bi.buildnumber = "+ buildnumber;
	
	 return executeQuery(sql);
	}	

	public List<Map<String, Object>> getBATDataForNightlyBuildId(int nightlybuildnumber) throws SQLException, ClassNotFoundException{
		String sql = "select bt.total,"
						+ "bt.pass,"
						+ "bt.fail,"
						+ "bi.id,"
						+ "bi.buildnumber "
						+ "from bat bt, buildinfo bi, nightlybuild nb "
						+ "where nb.id = bi.nightlybuild_id "
						+ "and bi.id = bt.buildinfo_id "
						+ "and nb.nightlybuildnumber = "+ nightlybuildnumber;
		
		
		return executeQuery(sql);
	}
	
	public List<Map<String, Object>> getWeekBtAggregateDataNightlyBuild() throws SQLException
	{
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -7);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		String sql = "select bi.buildnumber,bt.total,bt.pass,bt.fail from buildinfo bi inner join bat bt on bi.nightlybuild_id = bt.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL and bi.datetime > '" + dateFormat.format(cal.getTime()) + "'" + " and bi.datetime < '" + dateFormat.format(date) + "'" + ";";
		return executeQuery(sql);
	}
	
	
	public List<Map<String, Object>> getMonthBtAggregateDataNightlyBuild() throws SQLException
	{
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -30);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		String sql = "select bi.buildnumber,bt.total,bt.pass,bt.fail from buildinfo bi inner join bat bt on bi.nightlybuild_id = bt.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL and bi.datetime > '" + dateFormat.format(cal.getTime()) + "'" + " and bi.datetime < '" + dateFormat.format(date) + "'" + ";";
		return executeQuery(sql);
	}
	
	public List<Map<String, Object>> getCustomBtAggregateDataNightlyBuild(String todate,String fromdate) throws SQLException, ClassNotFoundException{
	
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

//		String sql = "select tempnb.buildnumber,sum(highwarnings),sum(normalwarnings),sum(lowwarnings),sum(totalwarnings) from buildinfo bi inner join(select * from nightlybuild where datetime >" + " '" + finalfromdate+ "'" + " and datetime < '" + finaltodate + "'" + " and status =1) tempnb on bi.nightlybuild_id = tempnb.id inner join findbug fb on fb.buildinfo_id = bi.id group by tempnb.id;";
	
		  String sql = "select bi.buildnumber,bt.total,bt.pass,bt.fail from buildinfo bi inner join bat bt on bi.nightlybuild_id = bt.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL and bi.datetime > '" + finalfromdate + "'" + " and bi.datetime < '" + finaltodate + "'" + ";";	
		
		return executeQuery(sql);

}
	public List<Map<String, Object>> getAggregatedBATDataForNightlyBuildId(int nightlybuildnumber) throws SQLException, ClassNotFoundException{

		String sql = "select nb.id,"
						+ "sum(bt.total) total,"
						+ "sum(bt.pass) pass,"
						+ "sum(bt.fail) fail "
						+ "from bat bt, buildinfo bi, nightlybuild nb "
						+ "where nb.id = bi.nightlybuild_id "
						+ "and bi.id = bt.buildinfo_id "
						+ "and nb.nightlybuildnumber = "+ nightlybuildnumber
						+ " group by nb.id;";

		
		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAllModulesBATForLatestNightlyBuild() throws SQLException, ClassNotFoundException{
		
		String sql = "select subbi.modulename,"
						+ "subbi.dt,"
						+ "subbi.id,"
						+ "subbt.total,"
						+ "subbt.pass,"
						+ "subbt.fail "
						+ "from "
						+ "(select modulename, "
						+ "bi.datetime as dt,"
						+ "bi.id from  buildinfo  bi, "
						+ "nightlybuild nb "
						+ "where bi.nightlybuild_id = nb.id "
						+ "and nb.id in (select id "
						+ "from nightlybuild "
						+ "where datetime in (select max(datetime) from nightlybuild))) subbi "
						+ "LEFT JOIN bat subbt ON subbi.id = subbt.buildinfo_id;";
		
		return executeQuery(sql);

	}

	public List<Map<String, Object>> getAggregatedBATDataForLatestNightlyBuild() throws SQLException, ClassNotFoundException{
		
   String sql = "select nb.nightlybuild_id,nb.total,nb.pass,nb.fail from bat nb inner join buildinfo bi on bi.nightlybuild_id = nb.nightlybuild_id where bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL order by nb.nightlybuild_id desc limit 1;";
		 return executeQuery(sql);
	}

	public List<Map<String, Object>> getAggregatedBATDataForLatestBuild() throws SQLException, ClassNotFoundException{

		String sql = "select sum(subbt.total) total,"
						+ "sum(subbt.pass) pass,"
						+ "sum(subbt.fail) fail "
						+ "from "
						+ "(select modulename, "
						+ "datetime as dt,"
						+ "max(id) id "
						+ "from  buildinfo  subbi  group by modulename) suborig "
						+ "LEFT JOIN bat subbt ON suborig.id = subbt.buildinfo_id;";
		 return executeQuery(sql);
	}

	public List<Map<String, Object>> getAllModulesBATForLatestBuild() throws SQLException, ClassNotFoundException{
		
		String sql = "select subbi.modulename,"
						+ "bi.datetime,"
						+ "subbi.id,"
						+ "subbt.total,"
						+ "subbt.pass,"
						+ "subbt.fail "
						+ "from "
						+ "(select modulename, "
						+ "max(id) id "
						+ "from  buildinfo  subbi  "						
						+ "group by modulename) subbi "
						+ "INNER JOIN buildinfo bi "
						+ "ON subbi.id = bi.id "
						+ "LEFT JOIN bat subbt "
						+ "ON subbi.id = subbt.buildinfo_id;";
		
		 return executeQuery(sql);
	}
	public List<Map<String, Object>> getBATCCLatestNightlyBuild() throws SQLException, ClassNotFoundException{
		String sql = "SELECT coverage FROM bat ORDER BY nightlybuild_id DESC LIMIT 1;";
		return executeQuery(sql);
			
		}
	}

