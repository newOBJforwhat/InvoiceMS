package Controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import Common.CommonInfo;
import Common.Controller.OutputStringController;
import Model.User;
import Service.UserService;

@Controller
@Scope("request")
@RequestMapping(value="/user")
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
	@RequestMapping(value="/login",produces="text/html;charset=UTF-8")
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
	/**
	 * ajax接口验证用户
	 * @param username
	 * @return
	 */
	@RequestMapping(value="/validateUser",produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String validateUser(String username){
		User u = null;
		try{
			u = uService.validateUser(username);
		}catch (Exception e) {
			logger.debug("验证用户出错 : "+e.getMessage());
			return failure("验证方法出现异常");
		}
		if(u == null)
			return failure("不存在此用户");
		else
			return success("用户已存在");
	}
	/**
	 * 查询部门成员
	 * @param request
	 * @param departmentId
	 * @return
	 */
	@RequestMapping(value="/deparmentMember",produces="text/html;charset=UTF-8")
	public String departmentMember(HttpServletRequest request,long departmentId){
		List<User> members = null;
		try{
			members = uService.departmentMembers(departmentId);
		}catch (Exception e) {
			logger.error("查询部门成员出错:"+e.getMessage());
		}
		request.setAttribute("departmentMembers", members);
		return "deparmentMember";
	}
}
