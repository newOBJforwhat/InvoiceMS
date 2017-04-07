package Common;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import Common.Annotation.ATable;
import Common.Annotation.AutoIncrement;
import Common.Annotation.Column;
import Common.Annotation.PrimaryKey;

/**
 * 初始化数据库 按照Model包下的类及字段创建
 * @author ctk
 *
 */


public class InitDataBases {
	
	private static Logger logger = Logger.getLogger(InitDataBases.class);
	private Connection conn = null;
	private Set<String> tables;
	
	//单例
	private static InitDataBases instance = new InitDataBases();
	private InitDataBases(){
		conn = getConnection();
		tables = new HashSet<>();
		searchTables();
	}
	/**
	 * 读取数据库资源文件
	 * 获得数据库链接
	 * @return
	 */
	private Connection getConnection(){
		logger.debug("建立数据库连接");
		String driver = "";
		String url = "";
		String username = "";
		String password = "";
		
    	File f = new File(this.getClass().getClassLoader().getResource("/").getPath()+"jdbc.properties");
    	
		Properties pro = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(f);
			pro.load(in);
			driver = pro.getProperty("jdbc.driverClass");
			url = pro.getProperty("jdbc.url");
			username = pro.getProperty("jdbc.username");
			password = pro.getProperty("jdbc.password");
		} catch (FileNotFoundException e) {
			logger.error("资源文件未找到，请命名为jdbc.properties，并置于src下");
			System.err.println("资源文件未找到，请命名为jdbc.properties，并置于src下");
		} catch (IOException e) {
			logger.error("资源文件读写异常");
			System.err.println("资源文件读写异常");
		}
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException e) {
			logger.error("加载驱动不成功，请检查是否添加了jdbc的必要包");
			System.err.println("加载驱动不成功，请检查是否添加了jdbc的必要包");
		} catch (SQLException e) {
			logger.error("数据库连接错误，检查账号密码和数据库地址");
			System.err.println("数据库连接错误，检查账号密码和数据库地址");
		}
		return conn;
	}
	/**
	 * 自动建表
	 * @param clazz
	 */
	public void checkAndCreate(Class<?> clazz){
		String tableName = getTableName(clazz);
		if(tableExist(tableName))
		{
			logger.debug(tableName+"表已存在");
			return;
		}
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sb = new StringBuilder("create table ");
		sb.append(tableName);
		sb.append(" (");
		for(int i=0;i<fields.length;i++){
			PrimaryKey pk = fields[i].getAnnotation(PrimaryKey.class);
			sb.append(getColumnName(fields[i]));
			sb.append(" ");
			Class<?> type = fields[i].getType();
			if(type == String.class)
				sb.append("VARCHAR(255)");
			else if(type == int.class)
				sb.append("INT(50)");
			else if(type == long.class)
				sb.append("BIGINT(20)");
			else if(type == double.class || type == float.class)
				sb.append("DOUBLE");
			//如果是主键字段
			if(pk != null){
				sb.append(" primary key");
				AutoIncrement ai = fields[i].getAnnotation(AutoIncrement.class);
				//判断是否自增
				if(ai != null){
					sb.append(" AUTO_INCREMENT");
				}
			}
			if(i != (fields.length-1))
				sb.append(",");
		}
		sb.append(")DEFAULT CHARSET=utf8");
		logger.debug("sql:"+sb.toString());
		try {
			PreparedStatement pst = conn.prepareStatement(sb.toString());
			pst.execute();
		} catch (SQLException e) {
			logger.error("建表错误："+e.getMessage());
		}
		
	}
	/**
	 * 获得表名字
	 * @param clazz
	 * @return
	 */
	public String getTableName(Class<?> clazz){
		//获得表别名
		ATable table = clazz.getAnnotation(ATable.class);
		if(table != null && "".equals(table.name()))
			return table.name();
		else
		{
			return clazz.getSimpleName();
		}
	}
	/**
	 * 获取列名称
	 * @param field
	 * @return
	 */
	public String getColumnName(Field field){
		Column column = field.getAnnotation(Column.class);
		if(column != null){
			return column.value();
		}else{
			return field.getName();
		}
	}
	/**
	 * 查询表是否存在
	 * @return
	 */
	private void searchTables(){
		String sql = "show tables";
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			logger.debug("sql执行："+sql);
			ResultSet rset = pst.executeQuery();
			while(rset.next()){
				String tname = rset.getString(1);
				tables.add(tname);
			}
		} catch (SQLException e) {
			logger.error("sql错误:"+e.getMessage());
		}
	}
	/**
	 * 判断是否存在某数据
	 * @param sql
	 * @return
	 */
	public boolean dataExist(String sql){
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			logger.debug("sql执行："+sql);
			ResultSet rset = pst.executeQuery();
			long id = 0;
			while(rset.next()){
				id = rset.getLong("id");
			}
			if(id == 0)
				return false;
			else
				return true;
		} catch (SQLException e) {
			logger.error("sql错误:"+e.getMessage());
			return false;
		}
	}
	/**
	 * 插入数据sql
	 * @param sql
	 */
	public void insertSql(String sql){
		try {
			PreparedStatement pst = conn.prepareStatement(sql);
			logger.debug("sql执行："+sql);
			pst.execute();
		} catch (SQLException e) {
			logger.error("sql错误:"+e.getMessage());
		}
	}
	//获得单例
	public static InitDataBases getInstance(){
		return instance;
	}
	public boolean tableExist(String table) {
		return tables.contains(table);
	}
	//关闭链接
	public void closeConn(){
		logger.debug("关闭数据库连接...");
		try {
			conn.close();
		} catch (SQLException e) {
			logger.error("关闭数据库连接失败:"+e.getMessage());
		}
	}
}
