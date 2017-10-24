package ece1779.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;

import ece1779.GlobalValues;
import ece1779.loadBalance.CloudWatching;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String managerName;
	private String managerPassword;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	public void init() {
		managerName = this.getServletConfig().getInitParameter("Manager");
		managerPassword = this.getServletConfig().getInitParameter(
				"ManagerPassword");
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
		ServletContext context = this.getServletContext();
		BasicAWSCredentials credentials = (BasicAWSCredentials) context
				.getAttribute(GlobalValues.AWS_CREDENTIALS);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String user = (String) request.getParameter(GlobalValues.USERNAME);
		String pwd = (String) request.getParameter(GlobalValues.PASSWORD);
		// if password entered was blank, don't check anything; it was wrong
		if (pwd.length() > 0) {
			if (isManager(user, pwd)) {
				/** set cloud watching **/
				CloudWatching cw = new CloudWatching(credentials);
				request.getSession().setAttribute(GlobalValues.CLOUD_WATCHING,
						cw);
				String encodedURL = response
						.encodeRedirectURL("../pages/managerView.jsp");
				response.sendRedirect(encodedURL);
			} else {
				out.println("Invalid login information. <a href='../login.jsp'>Try again</a>.");
			}
		} else {
			out.println("Invalid login information. <a href='../login.jsp'>Try again</a>.");
		}

	}

	// checks if login information corresponds to manager
	private boolean isManager(String username, String password) {
		if (username.equals(managerName) && password.equals(managerPassword)) {
			return true;
		} else {
			return false;
		}
	}
}
