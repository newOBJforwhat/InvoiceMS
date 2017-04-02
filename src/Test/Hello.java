package Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import Common.InitDataBases;
import Common.Controller.OutputStringController;
import Service.UserService;
import org.apache.log4j.Logger;

@Controller
@Scope("request")
public class Hello extends OutputStringController{
	@Autowired
	private	UserService uService;
	private static Logger logger = Logger.getLogger(Hello.class);
	public Hello(){
		System.err.println("Hello bean contructed!");
		System.out.println("bean id : "+this.toString());
	}
	
	//接口形式
	@RequestMapping(value="/Hello",produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String sayHello(){
		logger.debug("测试开始....");
		try {
			Class<?> clazz = Class.forName("Model.Invoice");
			InitDataBases ib = InitDataBases.getInstance();
			System.out.println(ib.tableExist(clazz.getSimpleName()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return success("成功");
	}
	//restful接口
	@RequestMapping(value = "/start/{name}", produces = "text/html;charset=UTF-8")
	@ResponseBody 
	public String start2(@PathVariable("name") String name1) {
		System.out.println(name1);
		return "start";
	}
	//数据库测试
	@RequestMapping(value = "/getUser", produces = "text/html;charset=UTF-8")
	@ResponseBody 
	public String dbTest(String username) {
		System.err.println(uService);
		System.out.println(uService.validateUser(username).getUsername());
		//会返回一个长度为0的list
//		List<User> us = uDao.findByDepartment(department);
//		if(us != null){
//			for(User u : us){
//				System.out.println(u.getUsername());
//			}
//			return success("");
//		}
		//主键自动填充到user对象中
//		User u = new User();
//		u.setUsername("xxssd");
//		u.setPassword("654321");
//		u.setDepartmentId(1);
//		u.setName("jkl");
//		u.setType(1);
//		uDao.applyUser(u);
//		System.out.println(u.getId());
		
		return success("");
	}
	//restful接口
	@RequestMapping(value = "/say", produces = "text/html;charset=UTF-8")
	public String say() {
		return "index";
	}
}
