package Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import Common.InitDataBases;
import Common.Controller.OutputStringController;
import Dao.UserDao;
import Model.User;
import org.apache.log4j.Logger;

@Controller
public class Hello extends OutputStringController{
	@Autowired
	private UserDao uDao;
	private static Logger logger = Logger.getLogger(Hello.class);
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
	public String start2(long id) {
		if(id != 0)
		{
			User u = uDao.findById(id);
			System.out.println(u == null);
			return success(u.getUsername());
		}
		return failure("传入参数");
	}
}
