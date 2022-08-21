

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class Initializing
 *
 */
@WebListener
public class Initializing implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public Initializing() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	ServletContext sc = sce.getServletContext();
    	sc.removeAttribute("connString");
    	sc.removeAttribute("connUser");
    	sc.removeAttribute("connPwd");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	ServletContext sc = sce.getServletContext();
    	sc.setAttribute("connString", "jdbc:mysql://localhost:3306/assg3db");
    	sc.setAttribute("connUser", "user");
    	sc.setAttribute("connPwd", "1234");
    }
	
}
