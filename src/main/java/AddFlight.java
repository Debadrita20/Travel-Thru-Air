

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
 * Servlet implementation class AddFlight
 */
@WebServlet("/AddFlight")
public class AddFlight extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddFlight() {
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
		engine.process("addFlights", ctx,response.getWriter());
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
		String cost=request.getParameter("cost");
		String ac=request.getParameter("ac");
		String dc=request.getParameter("dc");
		String at=request.getParameter("at");
		String dt=request.getParameter("dt");
		String n_ic=request.getParameter("n_ic");
		String int_cities[]=new String[Integer.parseInt(n_ic)];
		double costs[]=new double[Integer.parseInt(n_ic)];
		String times[]=new String[Integer.parseInt(n_ic)];
		for(int i=0;i<Integer.parseInt(n_ic);i++)
		{
			int_cities[i]=request.getParameter("ic"+(i+1));
			costs[i]=Double.parseDouble(request.getParameter("ic_c"+(i+1)));
			times[i]=request.getParameter("ic_time"+(i+1));
		}
		try
		{
			String q="select * from flights where ID='"+fid+"'";
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(q);
			if(!rs.next())
			{
				q="insert into flights(ID,ARRCITY,DEPCITY,ARRTIME,DEPTIME,TOTCOST,INT_CITIES) values('"+fid+"','"+ac+"','"+dc+"','"+at+"','"+dt+"','"+cost+"','"+n_ic+"')";
				PreparedStatement ps=conn.prepareStatement(q);
				ps.execute();
				for(int i=0;i<Integer.parseInt(n_ic);i++)
				{
					String q1="insert into intermediatecities(Flight_ID,StopNo,City,Cost,Time) values('"+fid+"','"+(i+1)+"','"+int_cities[i]+"','"+costs[i]+"','"+times[i]+"')";
					PreparedStatement ps1=conn.prepareStatement(q1);
					ps1.execute();
				}
				out.println("<h1>Flight Successfully added!</h1>");
				out.println("Do you want to add another flight?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/AddFlight\">Add Flight</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
			}
			else
			{
				out.println("<h1>Flight ID entered already exists</h1>");
				out.println("Do you want to try again?");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/AddFlight\">Add Flight</a>");
				out.println("<br>");
				out.println("<br>");
				out.println("<a href=\"http://localhost:8080/Travel-Thru-Air/Home\">Customer Homepage</a>");
			}
		}catch(Exception e) { System.out.println(e.getMessage()); e.printStackTrace();}
	}

}
