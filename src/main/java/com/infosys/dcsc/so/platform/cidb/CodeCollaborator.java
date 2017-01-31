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

public class CodeCollaborator {
	
	int projectid;
	public CodeCollaborator(int projectid)
	
	{
		this.projectid = projectid;
	}
	
	public List<Map<String, Object>> getAggregatedCodeCollabDataForLatestNightlyBuild() throws SQLException, ClassNotFoundException{
		
		String sql = "select count(*) reviewidcount from (select distinct reviewid from buildinfo bi INNER JOIN " 
				+ "codecollaborator cc on bi.id = cc.buildinfo_id where datetime > (select bi.datetime from buildinfo bi inner join nightlybuild " 
				+ "nt on nt.id = bi.nightlybuild_id where bi.datetime < (select datetime from nightlybuild order by datetime "
				+ "desc limit 1) order by datetime desc limit 1) and datetime <= ( select bi.datetime from buildinfo bi inner join nightlybuild "
				+ "on nightlybuild.id = bi.nightlybuild_id order by bi.datetime desc limit 1)) s;";
		
		return executeQuery(sql);
		}
	
public List<Map<String, Object>> getWeekCodeCollabTrendData() throws SQLException, ClassNotFoundException{
	
	 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 Date date = new Date();
	  System.out.println("current date is :" + dateFormat.format(date) );
        Calendar cal = Calendar.getInstance();
        
        cal.add(Calendar.DATE, -7);
        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
    
		
  //String sql = "select buildnumber,reviewidcount from nightlybuild where datetime >" + " '" + dateFormat.format(cal.getTime()) + "'" + " and datetime <= '" + dateFormat.format(date) + "';";
  
 //String sql = "select nb.buildnumber,nb.reviewidcount,nb.reviewbugcount from nightlybuild nb inner join buildinfo bi on bi.nightlybuild_id = nb.id where nb.datetime >" + " '" + dateFormat.format(cal.getTime()) + "'" + " and nb.datetime <= '" + dateFormat.format(date) + "' and bi.project_id = " + this.projectid + ";";
 String sql = "select dt.buildnumber,dt.reviewidcount,dt.reviewbugcount,sum(dt.timespent) timespent from (select distinct nb.buildnumber,nb.reviewidcount,nb.reviewbugcount,cc.timespent from nightlybuild nb inner join buildinfo bi on bi.nightlybuild_id = nb.id inner join codecollaborator cc on cc.buildinfo_id = bi.id where nb.datetime >" + " '" + dateFormat.format(cal.getTime()) + "'" + " and nb.datetime <= '" + dateFormat.format(date) + "' and bi.project_id = 1) dt group by dt.buildnumber;";    
		
		return executeQuery(sql);
		}

public List<Map<String, Object>> getMonthCodeCollabTrendData() throws SQLException, ClassNotFoundException{
	
	 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 Date date = new Date();
	  System.out.println("current date is :" + dateFormat.format(date) );
       Calendar cal = Calendar.getInstance();
       
       cal.add(Calendar.DATE, -30);
       System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
   
		
 //String sql = "select buildnumber,reviewidcount from nightlybuild where datetime >" + " '" + dateFormat.format(cal.getTime()) + "'" + " and datetime <= '" + dateFormat.format(date) + "';";
       String sql = "select nb.buildnumber,nb.reviewidcount,nb.reviewbugcount from nightlybuild nb inner join buildinfo bi on bi.nightlybuild_id = nb.id where nb.datetime >" + " '" + dateFormat.format(cal.getTime()) + "'" + " and nb.datetime <= '" + dateFormat.format(date) + "' and bi.project_id = " + this.projectid + ";";
		
		return executeQuery(sql);
		}

public List <Map<String, Object>> getCustomCodeCollabTrenddata(String todate, String fromdate) throws SQLException, ClassNotFoundException {

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
//String sql = "select buildnumber,reviewidcount from nightlybuild where datetime >" + " '" + finalfromdate + "'" + " and datetime <= '" + finaltodate + "';";
		String sql = "select nb.buildnumber,nb.reviewidcount,nb.reviewbugcount from nightlybuild nb inner join buildinfo bi on bi.nightlybuild_id = nb.id where nb.datetime >" + " '" + finalfromdate + "'" + " and nb.datetime <= '" + finaltodate + "' and bi.project_id = " + this.projectid + ";";		
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
