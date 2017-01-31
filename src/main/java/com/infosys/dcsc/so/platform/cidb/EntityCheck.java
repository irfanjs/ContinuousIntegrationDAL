package com.infosys.dcsc.so.platform.cidb;
/*package com.symantec.dcsc.so.platform.cidb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EntityCheck {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		List<Map<String, Object>> data;
		
		ArrayList<String> singleListcat = new ArrayList<String>();
		ArrayList<String> singleListdat = new ArrayList<String>();
		
		UnitTest ut = new UnitTest();
		data = ut.getAllModulesUnitTestForLatestBuild();
		for (Map<String, Object> data1 : data) {
		    for (Map.Entry<String, Object> entry : data1.entrySet()) {
		    //   System.out.println(entry.getKey() + ": " + entry.getValue());
		       if (entry.getKey().equals("modulename"))
		       {
		    	   singleListcat.add(entry.getValue().toString());
		       }
		       else if (entry.getKey().equals("pass")) {
		    	   singleListdat.add(entry.getValue().toString());
			}
		       else if (entry.getKey().equals("fail")) {
		    	   singleListdat.add(entry.getValue().toString());
			}
		       else if (entry.getKey().equals("skip")) {
		    	   singleListdat.add(entry.getValue().toString());
			}
		       
		       else if (entry.getKey().equals("total")) {
		    	   singleListdat.add(entry.getValue().toString());
			}
		       
		       else if (entry.getKey().equals("datetime")) {
		    	  // singleListdat.add(entry.getValue().toString());
			}
		       else if (entry.getKey().equals("id")) {
		    	   //singleListdat.add(entry.getValue().toString());
			}
		       {
		    	   
		       }
		       
		    }
		}
		
		Iterator<String> it = singleListcat.iterator();
		while(it.hasNext())
		{
		    Object obj = it.next();
		    System.out.println("the value of obj is :" + obj);
		    //Do something with obj
		}
	}
}		
	*/