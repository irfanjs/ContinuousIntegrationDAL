package com.infosys.dcsc.so.platform.cidb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class CodeComplex {
	
	
	int projectid;
	public CodeComplex(int projectid)
	{
		this.projectid = projectid;
	}
	
	public List<Map<String, Object>> getAllModulesCodeComplexForLatestBuild() throws SQLException, ClassNotFoundException{
	//	LOGGER.debug("called getAllModulesUnitTestForLatestNightlyBuild");
		
		String sql = "select subbi.modulename,subfn.function from (select modulename, max(id) id from  buildinfo  subbi  where project_id = " + this.projectid + " and nightlybuild_id is NULL group by modulename) subbi INNER JOIN buildinfo bi ON subbi.id = bi.id LEFT JOIN codecomplexity subfn ON subbi.id = subfn.buildinfo_id where function is not null;";
		
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
