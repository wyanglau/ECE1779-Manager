package ece1779.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;

import org.apache.commons.dbcp.cpdsadapter.*;
import org.apache.commons.dbcp.datasources.*;

public class InitializationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitializationServlet() {
		super();
	}

	public void init() {
		initAWS(this.getServletConfig());
		initJDBC(this.getServletConfig());
	}

	private void initAWS(ServletConfig config) {

		String accessKey = config.getInitParameter("AWSaccessKey");
		String secretKey = config.getInitParameter("AWSsecretKey");
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,
				secretKey);
		config.getServletContext().setAttribute(GlobalValues.AWS_CREDENTIALS,
				awsCredentials);
	}

	private void initJDBC(ServletConfig config) {
		try {
			// Initialize connection pool

			getServletContext().log("SQLGatewayPool: Connecting to DB");

			DriverAdapterCPDS ds = new DriverAdapterCPDS();
			ds.setDriver(config.getInitParameter("dbDriver"));
			ds.setUrl(config.getInitParameter("dbURL"));

			ds.setUser(config.getInitParameter("dbUser"));
			ds.setPassword(config.getInitParameter("dbPassword"));

			SharedPoolDataSource dbcp = new SharedPoolDataSource();
			dbcp.setConnectionPoolDataSource(ds);

			this.getServletContext().setAttribute(GlobalValues.Connection_Tag,
					dbcp);
		} catch (Exception ex) {
			getServletContext().log("SQLGatewayPool Error: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
