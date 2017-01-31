package com.infosys.dcsc.so.platform.cidb;

import java.sql.Connection;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocCount {
	
	int projectid;
	public LocCount(int projectid)
	{
		this.projectid = projectid;
	}
	
	public List<Map<String, Object>> getTotalLOCCountForNightlyBuildId() throws SQLException, ClassNotFoundException{
		
		String sql = "select modulename,LOC from buildinfo "
						+ "where nightlybuild_id in (select id from nightlybuild nb where nb.datetime in "
						+ "(select max(datetime) from nightlybuild)) and LOC >0;";
		

		return executeQuery(sql); 
	}
	
public List<Map<String, Object>> getLocCountforLatestCIbuild() throws SQLException, ClassNotFoundException{
		
		String sql = "select tempBI.modulename,LOC from (select modulename,datetime as dt,max(id) id from  buildinfo  subbi  where nightlybuild_id is NULL and LOC > 0  and project_id = " + this.projectid + " group by modulename) tempBI inner join buildinfo bi on tempBI.id = bi.id;";
		return executeQuery(sql); 
	}

public List<Map<String, Object>> getLocCountforweekTrend() throws SQLException, ClassNotFoundException{
	
	 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  Date date = new Date();
	  System.out.println("current date is :" + dateFormat.format(date) );
       Calendar cal = Calendar.getInstance();
       
       cal.add(Calendar.DATE, -7);
       System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
	
//	String sql = "select nightlybuild.buildnumber,sum(buildinfo.LOC) loc from buildinfo inner join nightlybuild on nightlybuild.id = buildinfo.nightlybuild_id where nightlybuild.datetime > '" + dateFormat.format(cal.getTime()) + "' and nightlybuild.datetime < '" + dateFormat.format(date) + "' group by nightlybuild.buildnumber;";
    String sql = "select buildnumber,loc from buildinfo where project_id = " + this.projectid + " and nightlybuild_id is NOT NULL and datetime > '" + dateFormat.format(cal.getTime())+ "' and datetime < '" + dateFormat.format(date) + "';"; 
	return executeQuery(sql); 
}


public List<Map<String, Object>> getLocCountformonthTrend() throws SQLException, ClassNotFoundException{
	 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  Date date = new Date();
	  System.out.println("current date is :" + dateFormat.format(date) );
      Calendar cal = Calendar.getInstance();
      
      cal.add(Calendar.DATE, -30);
      System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
	
	//String sql = "select nightlybuild.buildnumber,sum(buildinfo.LOC) loc from buildinfo inner join nightlybuild on nightlybuild.id = buildinfo.nightlybuild_id where nightlybuild.datetime > '" + dateFormat.format(cal.getTime()) + "' and nightlybuild.datetime < '" + dateFormat.format(date) + "' group by nightlybuild.buildnumber;";
      String sql = "select buildnumber,loc from buildinfo where project_id = " + this.projectid + " and nightlybuild_id is NOT NULL and datetime > '" + dateFormat.format(cal.getTime())+ "' and datetime < '" + dateFormat.format(date) + "';";
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
	
	public List<Map<String, Object>> getLocCountforCustomTrend(String todate, String fromdate) throws SQLException, ClassNotFoundException{
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
   //String sql = "select nightlybuild.buildnumber,sum(buildinfo.LOC) loc from buildinfo inner join nightlybuild on nightlybuild.id = buildinfo.nightlybuild_id where nightlybuild.datetime > '" + finalfromdate + "' and nightlybuild.datetime < '" + finaltodate + "' group by nightlybuild.buildnumber;";
	String sql = "select buildnumber,loc from buildinfo where project_id = " + this.projectid + " and nightlybuild_id is NOT NULL and datetime > '" + finalfromdate + "' and datetime < '" + finaltodate + "';";
	return executeQuery(sql);
 
	}
	
	
}
