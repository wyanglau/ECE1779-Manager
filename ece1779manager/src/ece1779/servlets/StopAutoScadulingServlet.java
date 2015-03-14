package ece1779.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ece1779.GlobalValues;

/**
 * Servlet implementation class StopAutoScadulingServlet
 */
public class StopAutoScadulingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StopAutoScadulingServlet() {
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
		System.out
				.println("[StopAutoScadulingServlet] Stopping auto scaling timer.");
		PrintWriter out = response.getWriter();
		try {
			Timer timer = (Timer) request.getSession().getAttribute("Timer");
			timer.cancel();
			out.print(GlobalValues.SUCCESS);
			request.getSession().setAttribute("Timer", null);
		} catch (Exception e) {
			out.print(e.getMessage());
			e.printStackTrace();
		}

	}

}
