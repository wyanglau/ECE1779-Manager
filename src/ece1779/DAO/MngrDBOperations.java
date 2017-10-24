package ece1779.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import ece1779.GlobalValues;

public class MngrDBOperations {

	private SharedPoolDataSource dbcp;

	public MngrDBOperations(SharedPoolDataSource dbcp) throws SQLException {
		this.dbcp = dbcp;

	}

	/**
	 * clean up MySQL
	 * 
	 * @throws SQLException
	 */
	public void deleteAllDB() throws SQLException {
		Connection con = this.dbcp.getConnection();
		Statement statement = con.createStatement();
		try {
			System.out
					.println("[MngrDBOperations] Delete all data from database.");
			// delete everything from both the users and images table in the
			// database
			statement
					.executeUpdate("DELETE FROM " + GlobalValues.dbTable_Users);
			statement.executeUpdate("DELETE FROM "
					+ GlobalValues.dbTable_Images);

		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {

			}

		}
	}
}