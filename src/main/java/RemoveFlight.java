

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 * Servlet implementation class RemoveFlight
 */
@WebServlet("/RemoveFlight")
public class RemoveFlight extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveFlight() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		ServletContext sc=request.getServletContext();
		TemplateEngine engine=TemplateEngineUtil.getTemplateEngine(sc);
		WebContext ctx=new WebContext(request,response,sc);
		engine.process("removeFlights", ctx,response.getWriter());
		response.setContentType("text/html;charset=UTF-8");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		PrintWriter out=response.getWriter();
		Connection conn=(Connection)request.getServletContext().getAttribute("DBConn");
		String fid=request.getParameter("flightid");
		try
		{
			String q="select * from flights where ID='"+fid+"'";
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(q);
			if(!rs.next())
			{
				out.println("<h1>Flight ID entered does not exist</h1>");
				out.println("Do you want to try again?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/RemoveFlight\">Remove Flight</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("Do you want to add another flight?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/AddFlight\">Add Flight</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
			}
			else
			{
				String q2="delete from flights where ID='"+fid+"'";
				PreparedStatement ps=conn.prepareStatement(q2);
				ps.execute();
				String q3="delete from specialdeals where Flight_ID='"+fid+"'";
				PreparedStatement ps2=conn.prepareStatement(q3);
				ps2.execute();
				String q4="delete from intermediatecities where Flight_ID='"+fid+"'";
				PreparedStatement ps3=conn.prepareStatement(q4);
				ps3.execute();
				out.println("<h1>Flight Successfully deleted!</h1>");
				out.println("Do you want to remove another flight?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/RemoveFlight\">Remove Flight</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("Do you want to add another flight?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/AddFlight\">Add Flight</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");				
			}
		}catch(Exception e) { System.out.println(e.getMessage()); e.printStackTrace();}
	}
	}

