package ece1779.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.loadBalance.WorkerPoolManagement;

/**
 * Servlet implementation class ManualGrowServlet
 */
public class ManualGrowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManualGrowServlet() {
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
		try {

			BasicAWSCredentials awsCredentials = (BasicAWSCredentials) this
					.getServletContext().getAttribute(
							GlobalValues.AWS_CREDENTIALS);
			int ratio = Integer.parseInt((String) request
					.getParameter("manualGrowRatio"));
			if ((ratio < 2)) {
				System.out
						.println("[ManualGrowServlet]Invalid Ratio Parameter");
				out.print(GlobalValues.INVALID_PARAMETER);
				return;
			}

			WorkerPoolManagement wpm = new WorkerPoolManagement(awsCredentials);

			System.out.println("[ManualGrowServlet]Expanding Worker Pool");
			wpm.growingByRatio(ratio);
			out.print(GlobalValues.SUCCESS);
		} catch (NumberFormatException nfe) {
			out.print(GlobalValues.INVALID_PARAMETER);
			nfe.printStackTrace();
		} catch (Exception e) {

			out.print(e.getMessage());
			e.printStackTrace();
		}

	}

}
