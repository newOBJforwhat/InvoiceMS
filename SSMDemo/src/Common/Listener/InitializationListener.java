package Common.Listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import Common.CommonInfo;

/**
 * Application Lifecycle Listener implementation class InitializationListener
 * 初始化加载监听器
 */
@WebListener
public class InitializationListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public InitializationListener() {
    	System.out.println("初始化监听器启动.....");
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
    	
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
    	/*
    	 * 读取email账号、密码、email地址
    	 */
    	File f = new File(arg0.getServletContext().getRealPath("/")+"WEB-INF/classes/emailAccount.properties");
		Properties pro = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(f);
			pro.load(in);
			CommonInfo.emailAccount = pro.getProperty("emailAccount");
			CommonInfo.emailPassword = pro.getProperty("emailPassword");
			CommonInfo.emailAddress = pro.getProperty("emailAddress");
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

    }
	
}
