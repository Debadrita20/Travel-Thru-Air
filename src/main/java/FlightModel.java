import java.sql.ResultSet;
import java.sql.SQLException;

public class FlightModel
{
	String arrival_city,departure_city,arrival_time,departure_time,id;
	String intermediate_cities[]=new String[10];
	double tot_cost;
	double costs[]=new double[10];
	int no_of_intermediate_cities;
	public String returnID()
	{
		return id;
	}
	public String returnACity()
	{
		return arrival_city;
	}
	public String returnDCity()
	{
		return departure_city;
	}
	public String returnATime()
	{
		return arrival_time;
	}
	public String returnDTime()
	{
		return departure_time;
	}
	public double returnCost()
	{
		return tot_cost;
	}
	public int returnNLegs()
	{
		return no_of_intermediate_cities+1;
	}
	public void setCost(double c)
	{
		tot_cost=c;
	}
	public void setID(String ii)
	{
		id=ii;
	}
	public void setACity(String a)
	{
		arrival_city=a;
	}
	public void setDCity(String d)
	{
		departure_city=d;
	}
	public void setATime(String at)
	{
		arrival_time=at;
	}
	public void setDTime(String dt)
	{
		departure_time=dt;
	}
	public void setNICities(int n)
	{
		no_of_intermediate_cities=n;
	}
	public void initialize(ResultSet rs)
	{
		try
		{
			id=rs.getString("ID");
			arrival_city=rs.getString("ARRCITY");
			departure_city=rs.getString("DEPCITY");
			arrival_time=rs.getString("ARRTIME");
			departure_time=rs.getString("DEPTIME");
			tot_cost=Double.parseDouble(rs.getString("TOTCOST"));
			no_of_intermediate_cities=Integer.parseInt(rs.getString("INT_CITIES"));
		}catch(SQLException e) { System.out.println(e.getMessage()); }
		catch(Exception e) { System.out.println(e.getMessage()); }
	}
	public String returnRoute()
	{
		if(no_of_intermediate_cities==0) return "NIL";
		else
		{
			String route="";
			for(int i=0;i<no_of_intermediate_cities;i++)
				route=route+intermediate_cities[i]+"-";
			route=route.substring(0,route.length()-1);
			return route;
		}
	}
	public void initializeIntCities(ResultSet rs,int start)
	{
		//System.out.println("Called for "+id);
		try
		{
			while(rs.next()) {
				//System.out.println("here");
			String city=rs.getString("City");
		    int sn=Integer.parseInt(rs.getString("StopNo"));
			double cost=Double.parseDouble(rs.getString("Cost"));
			intermediate_cities[sn-start-1]=city;
			costs[sn-start-1]=cost;
			}
		}catch(SQLException e) { System.out.println(e.getMessage()); }
		catch(Exception e) { System.out.println(e.getMessage()); }
	}
}