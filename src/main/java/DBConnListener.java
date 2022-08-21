

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class DBConnListener
 *
 */
@WebListener
public class DBConnListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public DBConnListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	ServletContext sc = sce.getServletContext();
		Connection conn=(Connection)sc.getAttribute("DBConn");
		if(conn!=null)
		{
			try {
				conn.close();
				sc.removeAttribute("DBConn");
			}catch(Exception e){ System.out.println(e);}
		}
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	ServletContext sc = sce.getServletContext();
		Connection conn=null;
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/assg3db","user","1234");
			//conn=DriverManager.getConnection((String)sc.getAttribute("connString"), (String)sc.getAttribute("connUser"), (String)sc.getAttribute("connPwd"));)
			sc.setAttribute("DBConn",conn);
           }catch(Exception e){ System.out.println(e);}
    }
	
}
