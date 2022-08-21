

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Servlet implementation class SetUpDeals
 */
@WebServlet({ "/SetUpDeals", "/setup" })
public class SetUpDeals extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetUpDeals() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*ServletContextTemplateResolver templateResolver=new ServletContextTemplateResolver(getServletContext());
		templateResolver.setTemplateMode("HTML");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs(3600000L);
		TemplateEngine templateEngine=new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);*/
		PrintWriter out=response.getWriter();
		Connection conn=(Connection)request.getServletContext().getAttribute("DBConn");
		String fid=request.getParameter("flightid");
		String cost=request.getParameter("cost");
		String validity=request.getParameter("valid_time");
		try
		{
			String q="select * from flights where ID='"+fid+"'";
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(q);
			if(rs.next())
			{
				String c="C"+(int)(100*Math.random());
				q="select * from specialdeals";
				Statement stmt1=conn.createStatement();
				ResultSet rs1=stmt1.executeQuery(q);
				int x=0;
				while(rs1.next()) x++;
				if(c.length()==2) c=c+"0"+x;
				else c=c+x;
				q="insert into specialdeals values('"+c+"','"+fid+"','"+cost+"','"+validity+"')";
				PreparedStatement ps=conn.prepareStatement(q);
				ps.execute();
				out.println("<h1>Deal Successfully Set up!</h1>");
				out.println("Do you want to set up another deal?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/setupdeals.html\">Set Up</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
			}
			else
			{
				out.println("<h1>Flight ID entered does not exist</h1>");
				out.println("Do you want to try again?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/setupdeals.html\">Set Up</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
			}
		}catch(Exception e) { System.out.println(e.getMessage()); }
	}

}
