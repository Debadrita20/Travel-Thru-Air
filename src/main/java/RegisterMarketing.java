

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterMarketing
 */
@WebServlet({ "/RegisterMarketing", "/register" })
public class RegisterMarketing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterMarketing() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher rd=request.getRequestDispatcher("register.html");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			PrintWriter out=response.getWriter(); //.append("Served at: ").append(request.getContextPath());
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			String validation=request.getParameter("validation");
			if(validation.equals("QAZXSWEDC"))
			{
				String q="select * from marketing where username='"+username+"'";
				Connection conn=(Connection)getServletContext().getAttribute("DBConn");
				Statement stmt=conn.createStatement();
				ResultSet rs=stmt.executeQuery(q);
				if(rs.next())
				{
					out.println("<h1>Username already exists</h1>");
					out.println("Do you want to go to the Login page?");
					out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/login\">Login</a>");
					out.println("<br>");
					out.println("<br>");
					out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
				}
				else
				{
					q="insert into marketing values('"+username+"','"+password+"')";
					PreparedStatement ps=conn.prepareStatement(q);
					ps.execute();
					out.println("<h1>Successfully Registered!</h1>");
					out.println("Do you want to go to the Login page?");
					out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/login\">Login</a>");
					out.println("<br>");
					out.println("<br>");
					out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
				}
			}
			else
			{
				out.println("<h1>Validation String does not Match!! Unable to Register..</h1>");
				out.println("<br>");
				out.println("<br>");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
			}
			}catch(Exception e) { System.out.println(e.getMessage()); }
	}

}
