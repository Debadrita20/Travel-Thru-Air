

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 * Servlet implementation class SearchFlights
 */
@WebServlet("/SearchFlights")
public class SearchFlights extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchFlights() {
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
		engine.process("searchFlights", ctx,response.getWriter());
		response.setContentType("text/html;charset=UTF-8");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String dcity=request.getParameter("depcity");
		String acity=request.getParameter("arrcity");
		String dtime=request.getParameter("deptime");
		String atime=request.getParameter("arrtime");
		ArrayList<FlightModel> afm=new ArrayList<>();
		ServletContext sc=request.getServletContext();
		TemplateEngine engine=TemplateEngineUtil.getTemplateEngine(sc);
		try
		{
			Connection conn=(Connection)request.getServletContext().getAttribute("DBConn");
			if(conn!=null) {
			String q="select * from flights where";
			if(dcity!=null&&dcity!="") q=q+" DEPCITY='"+dcity+"' and";
			if(acity!=null&&acity!="") q=q+" ARRCITY='"+acity+"' and";
			if(dtime!=null&&dtime!="") q=q+" DEPTIME='"+dtime+"' and";
			if(atime!=null&&atime!="") q=q+" ARRTIME='"+atime+"' and";
			if(q.endsWith("where")) q=q.substring(0,q.length()-6);
			else if(q.endsWith("and")) q=q.substring(0,q.length()-4);
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(q);
			while(rs.next())
			{
				FlightModel f=new FlightModel();
				f.initialize(rs);
				String q2="select * from intermediatecities where Flight_ID='"+f.returnID()+"'";
				Statement st2=conn.createStatement();
				ResultSet rs2=st2.executeQuery(q2);
				f.initializeIntCities(rs2,0);
				String q3="select * from specialdeals where Flight_ID='"+f.returnID()+"' and Expiry_Time>NOW()";
				Statement st3=conn.createStatement();
				ResultSet rs3=st3.executeQuery(q3);
				while(rs3.next())
				{
					if(Double.parseDouble(rs3.getString("Deal_Cost"))<f.returnCost())
						f.setCost(Double.parseDouble(rs3.getString("Deal_Cost")));
				}
				afm.add(f);
			}
			//for intermediate cities
			if(!q.endsWith("flights"))
			{
				//departure from intermediate city
				q="select * from intermediatecities where";
				if(dcity!=null&&dcity!="") q=q+" City='"+dcity+"' and";
				if(dtime!=null&&dtime!="") q=q+" Time='"+dtime+"' and";
				if(q.endsWith("where")) q=q.substring(0,q.length()-6);
				else if(q.endsWith("and")) q=q.substring(0,q.length()-4);
				Statement st3=conn.createStatement();
				ResultSet rs3=st3.executeQuery(q);
				while(rs3.next())
				{
					String id=rs3.getString("Flight_ID");
					//arrival at final destination
					String qq="select * from flights where ID='"+id+"'";
					Statement st4=conn.createStatement();
					ResultSet rs4=st4.executeQuery(qq);
					rs4.next();
					if((acity==""&&atime=="")||(acity==""&&atime.equalsIgnoreCase(rs4.getString("ARRTIME")))||(atime==""&&acity.equalsIgnoreCase(rs4.getString("ARRCITY")))||(atime.equalsIgnoreCase(rs4.getString("ARRTIME"))&&acity.equalsIgnoreCase(rs4.getString("ARRCITY"))))
					{
						FlightModel f=new FlightModel();
						f.initialize(rs4);
						f.setDCity(rs3.getString("City"));
						f.setDTime(rs3.getString("Time"));
						f.setCost(Double.parseDouble(rs4.getString("TOTCOST"))-Double.parseDouble(rs3.getString("Cost"))+500);
						f.setNICities(Integer.parseInt(rs4.getString("INT_CITIES"))-Integer.parseInt(rs3.getString("StopNo")));
						String qqq="select * from intermediatecities where Flight_ID='"+id+"' and StopNo>'"+Integer.parseInt(rs3.getString("StopNo"))+"'";
						Statement st5=conn.createStatement();
						ResultSet rs5=st5.executeQuery(qqq);
						f.initializeIntCities(rs5, Integer.parseInt(rs3.getString("StopNo")));
						afm.add(f);
					}
					else  //arrival at intermediate city
					{
						String qqq="select * from intermediatecities where Flight_ID='"+id+"' and StopNo>'"+Integer.parseInt(rs3.getString("StopNo"))+"'";
						if(acity!="") qqq=qqq+" and City='"+acity+"'";
						if(atime!="") qqq=qqq+" and Time='"+atime+"'";
						Statement st5=conn.createStatement();
						ResultSet rs5=st5.executeQuery(qqq);
						while(rs5.next())
						{
							FlightModel f=new FlightModel();
							f.initialize(rs4);
							f.setDCity(rs3.getString("City"));
							f.setDTime(rs3.getString("Time"));
							f.setACity(rs5.getString("City"));
							f.setATime(rs5.getString("Time"));
							f.setCost(Double.parseDouble(rs5.getString("Cost"))-Double.parseDouble(rs3.getString("Cost"))+500);
							f.setNICities(Integer.parseInt(rs5.getString("StopNo"))-Integer.parseInt(rs3.getString("StopNo"))-1);
							String qqqq="select * from intermediatecities where Flight_ID='"+id+"' and StopNo>'"+Integer.parseInt(rs3.getString("StopNo"))+"' and StopNo<'"+Integer.parseInt(rs5.getString("StopNo"))+"'";
							Statement st6=conn.createStatement();
							ResultSet rs6=st6.executeQuery(qqqq);
							f.initializeIntCities(rs6, Integer.parseInt(rs3.getString("StopNo")));
							afm.add(f);
						}
					}
				}
				//departure from initial starting point and arrival to intermediate city
				String qd="select * from flights where";
				if(dcity!="") qd=qd+" DEPCITY='"+dcity+"' and";
				if(dtime!="") qd=qd+" DEPTIME='"+dtime+"'";
				if(qd.endsWith("and")) qd=qd.substring(0,qd.length()-4);
				if(qd.endsWith("where")) qd=qd.substring(0,qd.length()-6);
				//System.out.println(qd);
				Statement std=conn.createStatement();
				ResultSet rsd=std.executeQuery(qd);
				while(rsd.next())
				{
					String id=rsd.getString("ID");
					
					String qqq="select * from intermediatecities where Flight_ID='"+id+"'";
					if(acity!="") qqq=qqq+" and City='"+acity+"'";
					if(atime!="") qqq=qqq+" and Time='"+atime+"'";
					Statement st5=conn.createStatement();
					ResultSet rs5=st5.executeQuery(qqq);
					while(rs5.next())
					{
						FlightModel f=new FlightModel();
						f.initialize(rsd);
						f.setACity(rs5.getString("City"));
						f.setATime(rs5.getString("Time"));
						f.setCost(Double.parseDouble(rs5.getString("Cost")));
						f.setNICities(Integer.parseInt(rs5.getString("StopNo"))-1);
						String qqqq="select * from intermediatecities where Flight_ID='"+id+"' and StopNo<'"+Integer.parseInt(rs5.getString("StopNo"))+"'";
						Statement st6=conn.createStatement();
						ResultSet rs6=st6.executeQuery(qqqq);
						f.initializeIntCities(rs6, 0);
						afm.add(f);
					}
				}
			}
			WebContext ctx=new WebContext(request,response,sc);
			ctx.setVariable("existFlights",!afm.isEmpty());
			ctx.setVariable("Flights", afm);
			ctx.setVariable("number", afm.size());
			engine.process("searchResults", ctx,response.getWriter());
			response.setContentType("text/html;charset=UTF-8");
			}
		}catch(Exception e) { System.out.println(e.getMessage()); e.printStackTrace();}
	}

}
