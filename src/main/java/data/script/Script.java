package data.script;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Script {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/support";
	private static String user = "root";
	private static String password = "root";
	
	public static void main(String[] args) {
		createTable();
		insertData();
	}
	
	private static void createTable() {
		StringBuilder query = new StringBuilder("DROP TABLE IF EXISTS bigtable; CREATE TABLE bigtable (id int,");
		for (int i = 0; i< 100; i++)
			query.append("column" + (i+1) + " varchar(45), ");
		query.append("PRIMARY KEY(id));");
		
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(query.toString());
			query.delete(0, query.length());
			pstmt.execute();
			conn.commit();
			conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insertData() {
		StringBuilder query = new StringBuilder("INSERT INTO bigtable VALUES (");
		for (int i = 0; i< 100; i++)
			query.append("?,");
		query.append("?);");
		
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(query.toString());
			for(int i = 0; i< 22000; i++) {
				pstmt.setInt(1, i+1);
				for(int j = 0; j < 100; j++)
					pstmt.setString(j + 2, "x = " + (i+1) + ", y = " + (j+1));
				pstmt.addBatch();
			}
			
			int[] updateCounts = pstmt.executeBatch();
			for (int i = 0; i < updateCounts.length; i++) {
				if (updateCounts[i] >= 0) {
					System.out.println("Successfully executed; updateCount=" + updateCounts[i]);
				} else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
					System.out.println("Successfully executed; updateCount=Statement.SUCCESS_NO_INFO");
				} else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
					System.out.println("Failed to execute; updateCount=Statement.EXECUTE_FAILED");
				}
			}
			conn.commit();
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
