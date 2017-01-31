package com.infosys.dcsc.so.platform.cidb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;



public class check {
	int projectid = 1;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		Gson gson = new Gson();
		ChartData cd = new ChartData();
		HashMap<String, String> month = new HashMap();
		HashMap<String, String> week = new HashMap();
		
	
		String monthstart = "1/1/1090";
		String monthend = "1/1/1091";
		
		month.put("start", monthstart);
		month.put("end", monthend);
		
		week.put("start", monthstart);
		week.put("end", monthend);
		
		
		cd.setMonth(month);
		cd.setWeek(week);
		
		String json;
		json = gson.toJson(cd);
		System.out.println(json);
		
      
	}
}
	
	