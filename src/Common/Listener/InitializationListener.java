package Common.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.log4j.Logger;

import Common.Encoder;
import Common.InitDataBases;
import Enum.UserType;
import Model.User;

/**
 * Application Lifecycle Listener implementation class InitializationListener
 * 初始化加载监听器
 */
@WebListener
public class InitializationListener implements ServletContextListener {
	private static Logger logger = Logger.getLogger(InitializationListener.class);
    /**
     * Default constructor. 
     */
    public InitializationListener() {
    	logger.debug("初始化监听器启动.....");
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
    	File f = new File(this.getClass().getClassLoader().getResource("/").getPath()+"/Model");
    	File[] fs = f.listFiles();
    	List<String> tables = new ArrayList<>();
    	for(File fl:fs){
    		String fname = fl.getName();
    		fname = fname.substring(0,fname.length()-6);
    		tables.add(fname);
    	}
    	InitDataBases initDB = InitDataBases.getInstance();
		try {
			for (String table : tables) {
				Class<?> clazz = Class.forName("Model." + table);
				initDB.checkAndCreate(clazz);
			}
		} catch (ClassNotFoundException e) {
			logger.error("找不到bean："+e.getMessage());;
		}
		//添加超级用户
		String querySql = "select * from User where username = 'admin'";
		StringBuilder insertSql = new StringBuilder("insert into User(department_id,username,password,name,type,is_deleted) values(");
		insertSql.append("0,'admin',");
		insertSql.append(Encoder.string2MD5("123456")+",'超级用户',");
		insertSql.append(UserType.SUPER.getCode());
		insertSql.append(",0)");
		if(!initDB.dataExist(querySql)){
			initDB.insertSql(insertSql.toString());
		}
		initDB.closeConn();
		
		//录入超级用户
		User root = new User();
		root.setUsername("admin");
		root.setPassword("123456");
		root.setType(UserType.SUPER.getCode());
		root.setName("超级用户");
//		root.setDepartmentId(0);
		root.setIsDeleted(0);
    	/*
    	 * 读取email账号、密码、email地址
    	 */
//    	File f = new File(arg0.getServletContext().getRealPath("/")+"WEB-INF/classes/emailAccount.properties");
//		Properties pro = new Properties();
//		InputStream in = null;
//		try {
//			in = new FileInputStream(f);
//			pro.load(in);
//			CommonInfo.emailAccount = pro.getProperty("emailAccount");
//			CommonInfo.emailPassword = pro.getProperty("emailPassword");
//			CommonInfo.emailAddress = pro.getProperty("emailAddress");
//			in.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e){
//			e.printStackTrace();
//		}
    	
    }
	
}
