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

public class FindBug {
	
	int projectid;
	
	public FindBug(int projectid)
	{
		this.projectid = projectid;
		
	}

	public boolean insert(int buildintoId,int highwarnings,int normalwarnings,int lowwarnings) throws SQLException, ClassNotFoundException{
		
		Connection conn = null;
		PreparedStatement prepStatement = null;
		
		try
		{
			conn = CIDBHelper.getInstance().getConnection();
			prepStatement = conn
					.prepareStatement("insert into findbug(buildinfo_id,highwarnings,normalwarnings,lowwarnings) values(?,?,?,?);");
		
		prepStatement.setInt(1, buildintoId);
		prepStatement.setInt(2, highwarnings);
		prepStatement.setInt(3, normalwarnings);
		prepStatement.setInt(4, lowwarnings);
		
		prepStatement.executeUpdate();
		}
		finally 
		{
			CIDBHelper.close(conn, prepStatement, null);
	}
		
		return true;
	}
	
	public List<Map<String, Object>> getFindBugDataForLatestBuildId() throws SQLException, ClassNotFoundException{
		
		String sql = "select fb.highwarnings,"
		          		+ "fb.normalwarnings,"
		          		+ "fb.lowwarnings,"
						+ "bi.id,"
						+ "bi.buildid "
						+ "from findbug fb, buildinfo bi "
						+ "where bi.id = fb.buildinfo_id "
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


	public List<Map<String, Object>> getFindBugForBuildId(int buildnumber) throws SQLException, ClassNotFoundException{
		
		String sql = "select fb.highwarnings,"
		          		+ "fb.normalwarnings,"
		          		+ "fb.lowwarnings,"
						+ "bi.id,"
						+ "bi.buildnumber "
						+ "from findbug fb, buildinfo bi "
						+ "where bi.id = fb.buildinfo_id "
						+ "and bi.buildnumber = "+ buildnumber;
		
		
		return executeQuery(sql);
	}	

	public List<Map<String, Object>> getFindBugDataForNightlyBuildId(int nightlybuildnumber) throws SQLException, ClassNotFoundException{
		
		String sql = "select fb.highwarnings,"
		          		+ "fb.normalwarnings,"
		          		+ "fb.lowwarnings,"
						+ "bi.id,"
						+ "bi.buildid "
						+ "from findbug fb, buildinfo bi, nightlybuild nb "
						+ "where nb.id = bi.nightlybuild_id "
						+ "and bi.id = fb.buildinfo_id "
						+ "and nb.nightlybuildnumber = "+ nightlybuildnumber;
		
		
		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAggregatedFindBugDataForNightlyBuildId(int nightlybuildnumber) throws SQLException, ClassNotFoundException{
		
		String sql = "select nb.id,"
						+ "sum(fb.highwarnings) highwarnings,"
						+ "sum(fb.normalwarnings) normalwarnings,"
						+ "sum(fb.lowwarnings) lowwarnings "
						+ "from findbug fb, buildinfo bi, nightlybuild nb "
						+ "where nb.id = bi.nightlybuild_id "
						+ "and bi.id = fb.buildinfo_id "
						+ "and nb.nightlybuildnumber = "+ nightlybuildnumber
						+ " group by nb.id;";

		
		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAllModulesFindBugForLatestNightlyBuild() throws SQLException, ClassNotFoundException{
		
		String sql = "select bi.modulename,bi.datetime,bi.id,bi.nightlybuild_id, subfb.totalwarnings,subfb.highwarnings,subfb.normalwarnings,subfb.lowwarnings from buildinfo bi LEFT JOIN findbug subfb ON bi.id = subfb.buildinfo_id where  bi.nightlybuild_id in (select id from nightlybuild where datetime in (select max(datetime) from nightlybuild where status = 1));";
		
		return executeQuery(sql);

	}

	public List<Map<String, Object>> getAggregatedFindBugDataForLatestNightlyBuild() throws SQLException, ClassNotFoundException{

	//	String sql = "select sum(subfb.totalwarnings) totalwarnings,sum(subfb.highwarnings) highwarnings,sum(subfb.normalwarnings) normalwarnings,sum(subfb.lowwarnings) lowwarnings from buildinfo bi LEFT JOIN findbug subfb ON bi.id = subfb.buildinfo_id where bi.nightlybuild_id in (select id from nightlybuild where datetime in (select max(datetime) from nightlybuild where status = 1));";
	String sql = "select totalwarnings,highwarnings,normalwarnings,lowwarnings from (select max(id) id from buildinfo where project_id = " + this.projectid + " and nightlybuild_id != 'NULL') tempbi inner join findbug fb on fb.buildinfo_id = tempbi.id;";	
		
		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAggregatedFindBugDataForLatestBuild() throws SQLException, ClassNotFoundException{
		
		String sql = "select sum(subfb.totalwarnings) totalwarnings,"
						+ "sum(subfb.highwarnings) highwarnings,"
						+ "sum(subfb.normalwarnings) normalwarnings,"
						+ "sum(subfb.lowwarnings) lowwarnings "
						+ "from "
						+ "(select modulename, "
						+ "datetime as dt,"
						+ "max(id) id "
						+ "from  buildinfo  subbi  group by modulename) suborig "
						+ "LEFT JOIN findbug subfb ON suborig.id = subfb.buildinfo_id;";


		return executeQuery(sql);
	}

	public List<Map<String, Object>> getAllModulesFindBugForLatestBuild() throws SQLException, ClassNotFoundException{
		
		String sql = "select subbi.modulename,"
						+ "bi.datetime,"
						+ "subbi.id,"
						+ "subfb.highwarnings,"
						+ "subfb.normalwarnings,"
						+ "subfb.lowwarnings "
						+ "from "
						+ "(select modulename, "
						+ "datetime as dt,"
						+ "max(id) id "
						+ "from  buildinfo  subbi  "
						+ "where project_id = " + this.projectid
						+ " and nightlybuild_id is NULL "
						+ "group by modulename) subbi "
						+ "INNER JOIN buildinfo bi "
						+ "ON subbi.id = bi.id "
						+ "LEFT JOIN findbug subfb ON subbi.id = subfb.buildinfo_id;";
		
		
		return executeQuery(sql);
	}
	
	public List<Map<String, Object>> getWeekFbAggregateDataNightlyBuild() throws SQLException, ClassNotFoundException{
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -7);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		
	//	String sql = "select tempnb.buildnumber,sum(highwarnings),sum(normalwarnings),sum(lowwarnings),sum(totalwarnings) from buildinfo bi inner join(select * from nightlybuild where datetime >" + " '" + dateFormat.format(cal.getTime())+ "'" + " and datetime < '" + dateFormat.format(date) + "'" + " and status =1) tempnb on bi.nightlybuild_id = tempnb.id inner join findbug fb on fb.buildinfo_id = bi.id group by tempnb.id;";
	        String sql = "select bi.buildnumber,fb.highwarnings,fb.normalwarnings,fb.lowwarnings,fb.totalwarnings from buildinfo bi inner join findbug fb on bi.id = fb.buildinfo_id where bi.datetime >" + " '" + dateFormat.format(cal.getTime())+ "'" + " and bi.datetime < '" + dateFormat.format(date) + "'" + " and bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL;";
		
		
		return executeQuery(sql);
	}
	
	public List<Map<String, Object>> getfbspecificbldno(int buildnumber) throws SQLException, ClassNotFoundException{
		//	String sql = "select bi.buildnumber,ut.total,ut.pass,ut.fail,ut.skip from buildinfo bi inner join codecoverage cc on bi.id = ut.buildinfo_id where bi.project_id = " + this.projectid + " and bi.buildnumber = " + buildnumber + " and bi.nightlybuild_id is not NULL;";
	//	String sql = "select totalwarnings,highwarnings,normalwarnings,lowwarnings from (select max(id) id from buildinfo where project_id = " + this.projectid + " and nightlybuild_id != 'NULL' and buildnumber = " + buildnumber + ") tempbi inner join findbug fb on fb.buildinfo_id = tempbi.id;";
		String sql = "select fb.totalwarnings,fb.highwarnings,fb.normalwarnings,fb.lowwarnings from nightlybuild nt inner join buildinfo bi on nt.id = bi.nightlybuild_id and nt.buildnumber= " + buildnumber + " inner join findbug fb on bi.id = fb.buildinfo_id where bi.project_id = " + this.projectid + ";";
			return executeQuery(sql);
		}
	
	public List<Map<String, Object>> getMonthFbAggregateDataNightlyBuild() throws SQLException, ClassNotFoundException{
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -30);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));
		
	//	String sql = "select tempnb.buildnumber,sum(highwarnings),sum(normalwarnings),sum(lowwarnings),sum(totalwarnings) from buildinfo bi inner join(select * from nightlybuild where datetime >" + " '" + dateFormat.format(cal.getTime())+ "'" + " and datetime < '" + dateFormat.format(date) + "'" + " and status =1) tempnb on bi.nightlybuild_id = tempnb.id inner join findbug fb on fb.buildinfo_id = bi.id group by tempnb.id;";
	        String sql = "select bi.buildnumber,fb.highwarnings,fb.normalwarnings,fb.lowwarnings,fb.totalwarnings from buildinfo bi inner join findbug fb on bi.id = fb.buildinfo_id where bi.datetime >" + " '" + dateFormat.format(cal.getTime())+ "'" + " and bi.datetime < '" + dateFormat.format(date) + "'" + " and bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL;";
		
		
		return executeQuery(sql);
	}
	
	public List<Map<String, Object>> getTrendCustomFbData(String todate,String fromdate) throws SQLException, ClassNotFoundException{
	
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

//			String sql = "select tempnb.buildnumber,sum(highwarnings),sum(normalwarnings),sum(lowwarnings),sum(totalwarnings) from buildinfo bi inner join(select * from nightlybuild where datetime >" + " '" + finalfromdate+ "'" + " and datetime < '" + finaltodate + "'" + " and status =1) tempnb on bi.nightlybuild_id = tempnb.id inner join findbug fb on fb.buildinfo_id = bi.id group by tempnb.id;";
		
			  String sql = "select bi.buildnumber,fb.highwarnings,fb.normalwarnings,fb.lowwarnings,fb.totalwarnings from buildinfo bi inner join findbug fb on bi.id = fb.buildinfo_id where bi.datetime >" + " '" + finalfromdate+ "'" + " and bi.datetime < '" + finaltodate + "'" + " and bi.project_id = " + this.projectid + " and bi.nightlybuild_id is not NULL;";		
			
			return executeQuery(sql);
			
	}
	
}
