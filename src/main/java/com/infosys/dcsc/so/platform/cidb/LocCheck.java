package com.infosys.dcsc.so.platform.cidb;
/*package com.symantec.dcsc.so.platform.cidb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;




public class LocCheck {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		
		LocCheck l = new LocCheck();
		l.result();
		

	}
	
	
	public List<Map<String, Object>> getSummaryDataLatestBuild() throws SQLException, ClassNotFoundException{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		  Date date = new Date();
		  System.out.println("current date is :" + dateFormat.format(date) );
	        Calendar cal = Calendar.getInstance();
	        
	        cal.add(Calendar.DATE, -7);
	        System.out.println("last week dats is :" + dateFormat.format(cal.getTime()));

		//	String sql = "select nb.buildnumber,bi.result,bi.reason  from nightlybuild nb LEFT JOIN buildinfo bi on nb.id = bi.nightlybuild_id  where nb.datetime in (select max(datetime) from nightlybuild where status = 1) order by bi.datetime desc limit 1;";
		//	String sql = "select nb.id,nb.buildnumber,bi.result,bi.reason,sum(bi.loc) loc  from nightlybuild nb INNER JOIN buildinfo bi on nb.id = bi.nightlybuild_id  where nb.datetime in (select max(datetime) from nightlybuild where status = 1) order by bi.datetime desc;";
	//String sql = "select tempnb.buildnumber,sum(pass),sum(fail),sum(total),sum(skip) from buildinfo bi inner join(select * from nightlybuild where datetime >" + " '" + dateFormat.format(cal.getTime())+ "'" + " and datetime < '" + dateFormat.format(date) + "'" + " and status =1) tempnb on bi.nightlybuild_id = tempnb.id inner join unittest ut on ut.buildinfo_id = bi.id group by tempnb.id;";
	        String sql = "select ni.buildnumber,round(avg(linesofcode)) linesCoverage from codecoverage cc inner join (select * from buildinfo where nightlybuild_id in (select id from nightlybuild where datetime >" + " '" +dateFormat.format(cal.getTime())+ "'" + " and datetime < '" + dateFormat.format(date)+ "'" + " and status =1 )) tempBI on cc.buildinfo_id = tempBI.id inner join nightlybuild ni on tempBI.nightlybuild_id = ni.id group by nightlybuild_id;";	        
			
			return executeQuery(sql);
		}
	
	
	public List<Map<String, Object>> executeQuery(String sql)
			throws SQLException {
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<Map<String, Object>> data;
		
		

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
	
	public String result() throws ClassNotFoundException, SQLException
	{
		
		ChartData d = new ChartData();
		Gson gson = new Gson();
		
		LocCheck l = new LocCheck();
		ObjectClass coverageTotal = new ObjectClass("Total");
		
		List<ObjectClass> result = new ArrayList<ObjectClass>();
		List<Integer> arrayList = new ArrayList<Integer>();
		String json;
		
		
		List<Map<String, Object>> data;
		data = l.getSummaryDataLatestBuild();
		for (Map<String, Object> data1 : data) {
		    for (Map.Entry<String, Object> entry : data1.entrySet()) {
		       System.out.println(entry.getKey() + ": " + entry.getValue());
		       
		       if (entry.getKey().equals("buildnumber"))
		       {
		    	   arrayList.add(Integer.parseInt(entry.getValue().toString()));
		       }
		       
		       else if (entry.getKey().equals("linescoverage"))
		       {
		    	   coverageTotal.data.add((int) Float.parseFloat(entry.getValue().toString()));
		       }
		    }
		}
		
		result.add(coverageTotal);
		d.setCategories(arrayList);
		d.setData(result);
		json = gson.toJson(d);
		System.out.println(json);
		return json;
		
		
		
	}
}
*/