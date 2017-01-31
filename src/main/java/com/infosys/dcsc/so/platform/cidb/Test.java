package com.infosys.dcsc.so.platform.cidb;
/*package com.symantec.dcsc.so.platform.cidb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Test {

	public static void main(String[] args) throws MalformedURLException, IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
		Gson gson = new Gson();
		
		String json;
		ProductDesc p = new ProductDesc();
		ProductDesc p1 = new ProductDesc();
		
		
		p.dn = "so";
		p.id = 1;
		
		p1.dn = "umc";
		p1.id = 2;
		
		
		List<ProductDesc> arrayList = new ArrayList<ProductDesc>();
		
		arrayList.add(p);
		arrayList.add(p1);
		
	    json = gson.toJson(arrayList);
	    System.out.println(json);
	
	//	List<Map<String, Object>> data;
		
		String displaynanme = "security orchestration";
		String id = "1";
		
		String displaynanme1 = "umc";
		String id1 = "2";
		
		
		
		x.put(displaynanme, id);
		x.put(displaynanme1, id1);
		arrayList.add(x);
		
		
		
	//	arrayList.add(displaynanme);
	//	arrayList.add(id);
		
		ChartData d = new ChartData();
		
		
		CodeComplex cc = new CodeComplex();
		data = cc.getAllModulesCodeComplexForLatestBuild();
		String modulename = null;
		Float function;
		List<String> arrayList = new ArrayList<String>();
		ArrayList<Float> singleList = new ArrayList<Float>();
		
		for (Map<String, Object> data1 : data) {
		    for (Map.Entry<String, Object> entry : data1.entrySet()) {
		       System.out.println(entry.getKey() + ": " + entry.getValue());
		       if (entry.getKey().equals("modulename"))
		       {
		    	    
		    	    arrayList.add(entry.getValue().toString());
		    	    
		       }
		       
		       else if (entry.getKey().equals("function"))
		       {
		    	   function = Float.parseFloat(entry.getValue().toString());
		    	    singleList.add(function);
		       }
		       
		       
		    }
		}
		
		// d.setCategories(arrayList);
		// d.setData(id);
		   // d.setData(singleList);
		
		
		    
		    
		    
		
		
		
		//CalculateCodeComplexity cc = new CalculateCodeComplexity();
		//cc.getData(null);

	}

}
*/