

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 * Servlet implementation class LoginMarketing
 */
@WebServlet({ "/LoginMarketing", "/login" })
public class LoginMarketing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginMarketing() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher rd=request.getRequestDispatcher("login.html");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out=response.getWriter(); //.append("Served at: ").append(request.getContextPath());
		try
		{
			String username=request.getParameter("username");
			String password=request.getParameter("password");
			Connection conn=(Connection)getServletContext().getAttribute("DBConn");
			String q="select * from marketing where username='"+username+"'";
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(q);
			if(rs.next())
			{
				if(rs.getString("password").equals(password))
				{
					HttpSession session=request.getSession();
					session.setAttribute("username",username);
					ServletContext sc=request.getServletContext();
					TemplateEngine engine=TemplateEngineUtil.getTemplateEngine(sc);
					WebContext ctx=new WebContext(request,response,sc);
					engine.process("setupdeals", ctx,response.getWriter());
					response.setContentType("text/html;charset=UTF-8");
					//response.sendRedirect("setupdeals.html");
				}
				else
				{
					out.println("<h1>Invalid Password</h1>");
					out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
				}
			}
			else
			{
				out.println("<h1>Username entered does not exist</h1>");
				out.println("Are you new to the marketing department?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/register\">Register</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
			}
		}catch(Exception e) { System.out.println(e.getMessage());}
	}

}
