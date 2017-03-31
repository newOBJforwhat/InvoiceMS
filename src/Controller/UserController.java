package Controller;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import Common.CommonInfo;
import Common.Controller.OutputStringController;
import Model.User;
import Service.UserService;

@Controller
public class UserController extends OutputStringController{
	@Autowired
	private UserService uService;
	private static Logger logger = Logger.getLogger(UserController.class);
	/**
	 * 用户登录
	 * @param session
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/Login",produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String login(HttpSession session,String username,String password){
		if(getCurrentUser(session) != null)
			return success("用户已登录");
		User user = null;
		try {
			user = uService.login(username, password);
		} catch (Exception e) {
			logger.error("登录发生异常:" + e.getMessage());
		}
		if (user != null) {
			session.setAttribute(CommonInfo.userInfo, user);
			return success("登录成功");
		}
		return failure("不存在此用户");
	}
}
