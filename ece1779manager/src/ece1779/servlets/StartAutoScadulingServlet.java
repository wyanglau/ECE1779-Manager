package ece1779.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.loadBalance.AutoScaling;

/**
 * Servlet implementation class AutoScaduling
 */
public class StartAutoScadulingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartAutoScadulingServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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

			Double expandThreshlod = Double.parseDouble((String) request
					.getParameter("expandThreshlod"));
			Double shrinkThreshlod = Double.parseDouble((String) request
					.getParameter("shrinkThreshlod"));

			int growRatio = Integer.parseInt((String) request
					.getParameter("growRatio"));
			int shrinkRatio = Integer.parseInt((String) request
					.getParameter("shrinkRatio"));

			if ((expandThreshlod <= 0) || (expandThreshlod > 100)
					|| (shrinkThreshlod <= 0) || (shrinkThreshlod > 100)
					|| (expandThreshlod <= shrinkThreshlod) || (growRatio < 2)
					|| (shrinkRatio < 2)) {
				System.out
						.println("[StartAutoScadulingServlet]Invalid Ratio Parameter");
				out.print(GlobalValues.INVALID_PARAMETER);
				return;

			}
			System.out
					.println("[StartAutoScadulingServlet] Get parameters from request : [expandThreshlod:"
							+ expandThreshlod
							+ "][shrinkThreshlod:"
							+ shrinkThreshlod
							+ "][growRatio:"
							+ growRatio
							+ "][shrinkRatio:" + shrinkRatio + "]");
			Timer timer = new Timer();
			long start = System.currentTimeMillis();
			System.out
					.println("[StartAutoScadulingServlet] scheduling new timer, current time is "
							+ start);

			timer.scheduleAtFixedRate(new AutoScaling(awsCredentials,
					expandThreshlod, shrinkThreshlod, growRatio, shrinkRatio),
					5 * 1000, 5 * 60 * 1000); // 5mins
			// 5
			// *
			// 60
			// *
			// 1000
			request.getSession().setAttribute("Timer", timer);
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
