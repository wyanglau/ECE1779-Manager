package ece1779.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.DAO.MngrDBOperations;
import ece1779.DAO.MngrS3Operations;

/**
 * Servlet implementation class DeleteServlet
 */
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteServlet() {
		super();
		// TODO Auto-generated constructor stub
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

		PrintWriter out = response.getWriter();
		BasicAWSCredentials credentials = (BasicAWSCredentials) this
				.getServletContext().getAttribute(GlobalValues.AWS_CREDENTIALS);

		try {

			MngrS3Operations s3 = new MngrS3Operations(credentials);
			s3.deleteAllS3();

			SharedPoolDataSource dbcp = (SharedPoolDataSource) this
					.getServletContext().getAttribute(
							GlobalValues.Connection_Tag);
			MngrDBOperations db = new MngrDBOperations(dbcp);

			db.deleteAllDB();

			out.write(GlobalValues.SUCCESS);
		} catch (Exception e) {

			out.write(e.getMessage());
			e.printStackTrace();
		}

	}

}
