import java.sql.ResultSet;
import java.sql.SQLException;

public class SpecialDealModel
{
	String code,flightID,dep_city,arr_city;
	double deal_cost; String expiry_time;
	public String returnCode()
	{
		return code;
	}
	public String returnID()
	{
		return flightID;
	}
	public double returnDealCost()
	{
		return deal_cost;
	}
	public String returnExpiryTime()
	{
		return expiry_time;
	}
	public void setDepCity(String d)
	{
		dep_city=d;
	}
	public void setArrCity(String a)
	{
		arr_city=a;
	}
	public String returnDepCity()
	{
		return dep_city;
	}
	public String returnArrCity()
	{
		return arr_city;
	}
	public void initialize(ResultSet rs)
	{
		try
		{
			code=rs.getString("Code");
			flightID=rs.getString("Flight_ID");
			deal_cost=Double.parseDouble(rs.getString("Deal_Cost"));
			expiry_time=rs.getString("Expiry_time");
		}catch(SQLException e) { System.out.println(e.getMessage()); }
		catch(Exception e) { System.out.println(e.getMessage()); }
	}
}