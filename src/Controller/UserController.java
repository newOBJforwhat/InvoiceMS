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
import Enum.UserType;
import Form.ApplyUserForm;
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
	@RequestMapping(value="/noNeedLogin/login",produces="text/html;charset=UTF-8")
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
	@RequestMapping(value="/noNeedLogin/validateUser",produces="text/html;charset=UTF-8")
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
	/**
	 * 申请用户
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/applyUser",produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String applyUser(HttpSession session,ApplyUserForm form){
		if(getCurrentUser(session).getType() != UserType.SUPER.getCode())
			return failure("只有超级用户有权限申请");
		//后端校验
		if(form.getUsername() == null || form.getUsername().equals(""))
			return failure("请填写用户名");
		else if(form.getUsername().length() >30 || form.getUsername().length() < 6)
			return failure("用户名长度须在6-30之间");
		else if(form.getPassword() == null || form.getPassword().equals(""))
			return failure("必须填写密码");
		else if(form.getPassword().length() <6 || form.getPassword().length() > 30)
			return failure("密码位数须在6-30位");
		else if(form.getName() == null || form.getName().equals(""))
			return failure("填写用户名字");
		else if(form.getDepartmentId() == 0)
			return failure("选择部门");
		
		form.setCreateTime(System.currentTimeMillis());
		form.setType(UserType.NORMAL.getCode());
		try{
			if(uService.applyUser(form) != 0)
				return success("申请成功");
			else
				return failure("用户已存在");
		}catch (Exception e) {
			logger.error("生成用户失败:"+e.getMessage());
			return failure("申请用户失败");
		}
	}
	
	/**
	 * 请求更新用户信息页面
	 * @return
	 */
	@RequestMapping(value="/updateInfoPage",produces="text/html;charset=UTF-8")
	public String updateInfoPage(){
		return "updateUserInfoPage";
	}
	/**
	 * 更新用户信息接口
	 * @param session
	 * @param password
	 * @param name
	 * @param departmentId
	 * @return
	 */
	@RequestMapping(value="/updateInfo",produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String updateInfo(HttpSession session,String password,String name,long departmentId){
		User u = getCurrentUser(session);
		String tpassword = password;
		String tname = name;
		long tdepartmentId = departmentId;
		int control = 0;
		if(tpassword == null || tpassword.equals("")){
			tpassword = u.getPassword();
			control = control + 1;
		}
		else if(tname == null || tname.equals("")){
			tname = u.getName();
			control = control + 1;
		}
		else if(tdepartmentId == 0){
			tdepartmentId = u.getDepartmentId();
			control = control + 1;
		}
		System.out.println(u.getId()+" "+tpassword+" "+tname+" "+tdepartmentId);
		if(control != 3)
			uService.updateUserInfo(u.getId(), tpassword, tname, tdepartmentId);
		return success("更新成功");
	}
	/**
	 * 删除用户接口
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deleteUser",produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String deleteUser(HttpSession session,long id){
		User u = getCurrentUser(session);
		if(u.getType() != UserType.SUPER.getCode())
			return failure("没有权限更改");
		try{
			uService.deleteUser(id);
		}catch (NullPointerException e) {
			logger.debug("找不到此用户:"+e.getMessage());
			return failure("该用户不存在");
		}catch (Exception e) {
			logger.error("产生异常:"+e.getMessage());
			return failure("产生异常");
		}
		return success("删除成功");
	}
}
