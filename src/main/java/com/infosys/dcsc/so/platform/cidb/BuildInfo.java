package com.infosys.dcsc.so.platform.cidb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildInfo {

	private static final Logger LOGGER = LoggerFactory.getLogger(BuildInfo.class);
	public boolean insert(int buildId,int nightlyBuild, String moduleName,String result, String reason) throws SQLException, ClassNotFoundException{
		Connection conn = null;
		PreparedStatement prepStatement = null;
		try
		{
		LOGGER.debug("Build Info data inserting");
		conn = CIDBHelper.getInstance().getConnection();
		
		prepStatement = conn
				.prepareStatement("insert into buildinfo(nightlybuild_id,buildnumber,modulename,result,reason,datetime) values(?,?,?,?,?,?);");
		
		if(nightlyBuild > 0){
			prepStatement.setInt(1, nightlyBuild);
		}
		else{
			prepStatement.setNull(1,Types.INTEGER);
		}
		prepStatement.setInt(2, buildId);
		prepStatement.setString(3, moduleName);
		prepStatement.setString(4, result);
		prepStatement.setString(5, reason);
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		prepStatement.setTimestamp(6, new java.sql.Timestamp(cal.getTimeInMillis()));
		
		prepStatement.executeUpdate();
		LOGGER.debug("Build Info insert complete");
		
	}
		finally 
		{
			CIDBHelper.close(conn, prepStatement, null);
		}
		return true;
	}
	public int getRecordIdForBuildId(int buildnumber) throws SQLException, ClassNotFoundException{
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		String sql = "select id from buildinfo where buildnumber = " + buildnumber + " order by datetime desc Limit 1;";
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
	
	public int getBuildInfoForBuildId(int buildnumber) throws SQLException, ClassNotFoundException{
		
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		String sql = "select id,nightlybuild_id, modulename, result, reason, datetime from buildinfo where buildnumber =" + buildnumber;
		
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
		
	//	Statement statement = CIDBHelper.getConnection().createExecuteStatement();
		
	}
	
	}
