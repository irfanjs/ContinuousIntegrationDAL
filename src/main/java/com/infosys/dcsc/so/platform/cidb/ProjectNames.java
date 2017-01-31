package com.infosys.dcsc.so.platform.cidb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class ProjectNames {
	
	
public List<Map<String, Object>> getProjectNamesId() throws SQLException, ClassNotFoundException{
		
		String sql = "select id,name from projects;";
		
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
