
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


@WebServlet("/Home")
public class SpecialDeals extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public SpecialDeals() {
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
		
		try
		{
			Connection conn=(Connection)getServletContext().getAttribute("DBConn");
			if(conn!=null)
			{
				String q1="select * from specialdeals where Expiry_time>=NOW() order by Expiry_time";
				Statement st1=conn.createStatement();
				ArrayList<SpecialDealModel> asm=new ArrayList<>();
				ResultSet rs1=st1.executeQuery(q1);
				while(rs1.next())
				{
					SpecialDealModel sd=new SpecialDealModel();
					sd.initialize(rs1);
					String q2="select * from flights where ID='"+sd.returnID()+"'";
					Statement st2=conn.createStatement();
					ResultSet rs2=st2.executeQuery(q2);
					rs2.next();
					sd.setDepCity(rs2.getString("DEPCITY"));
					sd.setArrCity(rs2.getString("ARRCITY"));
					asm.add(sd);
				}
				//request.setAttribute("existDeals", "yes");
				//request.setAttribute("Deals", asm);
				WebContext ctx=new WebContext(request,response,sc);
				ctx.setVariable("existDeals",!asm.isEmpty());
				ctx.setVariable("Deals", asm);
				//System.out.println(ctx.getVariableNames());
				engine.process("homepage", ctx,response.getWriter());
				response.setContentType("text/html;charset=UTF-8");
				//RequestDispatcher rd=request.getRequestDispatcher("homepage.html");
				//rd.forward(request, response);
			}
		}catch(SQLException se) { System.out.println("in sqlexception catch"); System.out.println(se.getMessage()); se.printStackTrace();}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
