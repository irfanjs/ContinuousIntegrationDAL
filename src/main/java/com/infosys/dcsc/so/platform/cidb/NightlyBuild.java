package com.infosys.dcsc.so.platform.cidb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class NightlyBuild {
	
	int projectid;
	
	public NightlyBuild(int projectid)
	{
		this.projectid = projectid;
	}
	public boolean insert(int buildId,int nightlyBuild, String moduleName,String result, String reason) throws SQLException, ClassNotFoundException{
		
		Connection conn = null;
		PreparedStatement prepStatement = null;
		
		try
		{
			conn = CIDBHelper.getInstance().getConnection();
			prepStatement = conn
					.prepareStatement("insert into nightlybuild(datetime) values(?);");
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		prepStatement.setTimestamp(6, new java.sql.Timestamp(cal.getTimeInMillis()));
		
		prepStatement.executeUpdate();
		}
		finally 
		{
			CIDBHelper.close(conn, prepStatement, null);
	}
		return true;
	}
	
	public int getRecordIdForBuildId(int buildId) throws SQLException, ClassNotFoundException{
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		String sql = "select id  from nightlybuild order by datetime desc limit 1;";
		try
		{
			conn = CIDBHelper.getInstance().getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);			
		if(resultSet.next()){
			return resultSet.getInt("id");			
		}
		return -1;
	}
		finally
		{
			CIDBHelper.close(conn, statement, resultSet);
		}
	}
	
	public List<Map<String, Object>> getSummaryDataLatestBuild() throws SQLException, ClassNotFoundException{

	//	String sql = "select nb.buildnumber,bi.result,bi.reason  from nightlybuild nb LEFT JOIN buildinfo bi on nb.id = bi.nightlybuild_id  where nb.datetime in (select max(datetime) from nightlybuild where status = 1) order by bi.datetime desc limit 1;";
	//	String sql = "select nb.id,nb.buildnumber,bi.result,bi.reason,sum(bi.loc) loc  from nightlybuild nb INNER JOIN buildinfo bi on nb.id = bi.nightlybuild_id  where nb.datetime in (select max(datetime) from nightlybuild where status = 1) order by bi.datetime desc;";
   //     String sql = "select nb.id,nb.buildnumber,bi.result,bi.reason,bi.datetime datetime from nightlybuild nb LEFT JOIN buildinfo bi on nb.id = bi.nightlybuild_id  where nb.datetime in (select max(datetime) from nightlybuild where status = 1) order by bi.datetime desc limit 1;";
		String sql = "select tempBI.buildnumber,BI.result,BI.reason,tempBI.datetime,tempBI.loc  from buildinfo BI INNER JOIN (select max(bi.datetime) dt,sum(LOC) loc,nb.buildnumber,nb.datetime  from buildinfo bi,nightlybuild nb where bi.nightlybuild_id = nb.id and bi.nightlybuild_id in (select id from nightlybuild where datetime in (select max(datetime) from nightlybuild))) tempBI ON tempBI.dt = BI.datetime;";
		
		return executeQuery(sql);
	}
	
public List<Map<String, Object>> getBuildArtifactsForLatestNightlyBuild() throws SQLException, ClassNotFoundException{

		
		
		//	String sql = "select sum(subut.total) total,sum(subut.pass) pass,sum(subut.fail) fail, sum(subut.skip) skip from (select modulename, datetime as dt,max(id) id from  buildinfo  subbi  where subbi.nightlybuild_id in (select id from nightlybuild where datetime in (select max(datetime) from nightlybuild where status = 1)) and project_id = " + this.projectid + " group by modulename) suborig LEFT JOIN unittest subut ON suborig.id = subut.buildinfo_id;";
			// query to get data from so e2e snapshot 
			String sql = "select nb.buildnumber,bi.loc,bi.result,bi.reason,bi.datetime,nb.reviewidcount from nightlybuild nb inner join buildinfo bi on bi.nightlybuild_id = nb.id where project_id = " + this.projectid + " order by nb.buildnumber desc limit 20;";	
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
