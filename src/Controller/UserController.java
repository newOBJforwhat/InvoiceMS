package Controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import Common.CommonInfo;
import Common.Encoder;
import Common.Controller.OutputStringController;
import Enum.DeleteCode;
import Enum.UserType;
import Form.ApplyUserForm;
import Model.User;
import Service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope("singleton")
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
	@RequestMapping(value="/noNeedLogin/login",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody 
	public String login(HttpSession session,String username,String password){
		if(username == null)
			return failure("请填写用户名");
		if(password == null)
			return failure("请填写密码");
		User user = getCurrentUser(session); 
		if(user != null){
			//返回用户信息
			JSONObject userinfo = new JSONObject();
			userinfo.put("username", user.getUsername());
			userinfo.put("name", user.getName() == null?"":user.getName());
			userinfo.put("type", user.getType());
			return resultSuccess("用户已登录", userinfo.toString());
		}
		//登录
		User loginUser = null;
		try {
			loginUser = uService.login(username, Encoder.string2MD5(password));
		} catch (Exception e) {
			logger.error("登录发生异常:" + e.getMessage());
			return exception("登录发生异常");
		}
		if (loginUser != null) {
			if(loginUser.getIsDeleted() == DeleteCode.DELETED.getCode())
				return failure("用户名或者密码错误");
			session.setAttribute(CommonInfo.userInfo, loginUser);
			//返回用户信息
			JSONObject userinfo = new JSONObject();
			userinfo.put("username", loginUser.getUsername());
			userinfo.put("name", loginUser.getName() == null?"":loginUser.getName());
			userinfo.put("type", loginUser.getType());
			return resultSuccess("登录成功", userinfo.toString());
		} 
		return failure("用户名或者密码错误");
	}
	/**
	 * 登出方法
	 * @param session
	 */
	@RequestMapping(value="/logout",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody
	public String logout(HttpSession session){
		session.setAttribute(CommonInfo.userInfo, null);
		return success("登出成功");
	}
	/**
	 * ajax接口验证用户
	 * @param username
	 * @return
	 */
	@RequestMapping(value="/noNeedLogin/validateUser/{username}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody 
	public String validateUser(@PathVariable("username")String username){
		User u = null;
		try{
			u = uService.validateUser(username);
		}catch (Exception e) {
			logger.debug("验证用户出错 : "+e.getMessage());
			return exception("验证方法出现异常");
		}
		if(u == null)
			return failure("不存在此用户");
		else
			return success("用户已存在");
	}
	
	/**
	 * 申请用户
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/super/applyUser",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody 
	public String applyUser(ApplyUserForm form){
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
		else if(form.getType() != UserType.STAFF.getCode() && form.getType() != UserType.AUDITING.getCode() && form.getType() != UserType.FINANCE.getCode())
			return failure("非法用户类型");
		form.setCreateTime(System.currentTimeMillis());
		try{
			if(uService.applyUser(form) != 0)
				return success("申请成功");
			else
				return failure("用户已存在");
		}catch (Exception e) {
			logger.error("生成用户失败:"+e.getMessage());
			return exception("申请用户失败");
		}
	}
	/**
	 * 测试接口
	 * @param str
	 * @return
	 */
//	@CrossOrigin(origins = "*", maxAge = 3600)
//	@RequestMapping(value="/noNeedLogin/acceptStr",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
//	@ResponseBody
//	public String acceptStr(String str){
//		System.out.println("获得字符串："+str);
//		return success("成功");
//	}
	/**
	 * 修改密码接口
	 * @param session
	 * @param password
	 * @param passwordAgain
	 * @return
	 */
	@RequestMapping(value="/alterPassword",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String alterPassword(HttpSession session,String beforePassword,String password,String passwordAgain){
		User user = getCurrentUser(session);
		if(!user.getPassword().equals(beforePassword))
			return failure("原密码输入不正确");
		if (!password.equals(passwordAgain))
			return failure("两次密码输入不正确");
		try {
			uService.alterPassword(user.getId(), Encoder.string2MD5(password));
			return success("密码修改成功");
		} catch (NullPointerException e) {
			logger.debug("查询不到用户:" + e.getMessage());
			return exception("查询不到用户");
		} catch (Exception e) {
			logger.error("修改密码产生异常:" + e.getMessage());
			return exception("修改密码异常");
		}
	}
	/**
	 * 重置密码接口
	 * 超级用户
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/super/resetPassword/{id}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody 
	public String resetUserPassword(@PathVariable("id") long id){
		try{
			uService.alterPassword(id, Encoder.string2MD5("123456"));
		}catch (NullPointerException e) {
			logger.debug("重置密码出现异常:"+e.getMessage());
			return failure("找不到用户");
		}
		catch (Exception e) {
			logger.error("重置密码出现异常:"+e.getMessage());
			return exception("重置密码出现异常");
		}
		return success("重置成功");
	}
	/**
	 * 更改姓名
	 * 
	 * @param session
	 * @param name
	 * @param departmentId
	 * @return
	 */
	@RequestMapping(value="/updateInfo",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody 
	public String updateInfo(HttpSession session,String name){
		User u = getCurrentUser(session);
		String tname = name;
		int control = 0;
		if(tname == null || tname.equals("")){
			tname = u.getName();
			control = control + 1;
		}
		if(control != 1)
			uService.updateUserInfo(u.getId(), tname);
		return success("更新成功");
	}
	/**
	 * 删除用户接口
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/super/deleteUser/{id}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody 
	public String deleteUser(@PathVariable("id")long id){
		try{
			uService.deleteUser(id);
		}catch (NullPointerException e) {
			logger.debug("找不到此用户:"+e.getMessage());
			return exception("该用户不存在");
		}catch (Exception e) {
			logger.error("产生异常:"+e.getMessage());
			return exception("产生异常");
		}
		return success("删除成功");
	}
	/**
	 * 超级用户查询所有接口
	 * @return
	 */
	@CrossOrigin(origins = "*", maxAge = 3600)
	@RequestMapping(value="/super/queryUser",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody
	public String queryUser(HttpSession session){
		try{
			List<User> userData = uService.findAll();
			JSONArray userArr = new JSONArray();
			for(User u : userData){
				JSONObject userjson = new JSONObject();
				userjson.put("id", u.getId());
				userjson.put("username", u.getUsername());
				userjson.put("name", u.getName());
				userjson.put("type", u.getType());
				userArr.add(userjson);
			}
			return resultSuccess("返回成功", userArr.toString());
		}catch (Exception e) {
			logger.error("查询错误:"+e.getMessage());
			return exception("查询错误");
		}
		
	}
	/**
	 * 软删除，把状态改为1
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/super/softDelete/{id}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	@ResponseBody 
	public String softDelete(@PathVariable("id")long id){
		try{
			uService.softDelete(id);
		}catch (Exception e) {
			logger.error("软删除失败："+e.getMessage());
			return exception("软删除失败");
		}
		return success("软删除成功");
	}

	/**
	 * 返回所有用户类型
	 * @return
	 */
	@RequestMapping(value="/getType",produces="text/html;charset=UTF-8",method=RequestMethod.GET)
	@ResponseBody 
	public String getUserType(){
		JSONObject jsonResult  = new JSONObject();
		jsonResult.put(UserType.SUPER.getCode(), UserType.SUPER.getName());
		jsonResult.put(UserType.STAFF.getCode(), UserType.STAFF.getName());
		jsonResult.put(UserType.AUDITING.getCode(), UserType.AUDITING.getName());
		jsonResult.put(UserType.FINANCE.getCode(), UserType.FINANCE.getName());
		return resultSuccess("success", jsonResult.toString());
	}
	
	/**
	 * 下载模板
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/noNeedLogin/downLoad",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String download(HttpServletRequest request,HttpServletResponse response){
		File download = new File(CommonInfo.FilePackage+"/hello.txt");
		try {
			if(!download.exists())
				download.createNewFile();
			FileOutputStream fous = new FileOutputStream(download);
			fous.write("hello this is a test".getBytes());
			fous.flush();
			fous.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=" + download.getName());// 设置文件名
		byte[] buffer = new byte[1024];
		try {
			BufferedInputStream bins = new BufferedInputStream(new FileInputStream(download));
			OutputStream ous = response.getOutputStream();
			int len = bins.read(buffer);
			while(len != -1){
				ous.write(buffer,0,len);
				len = bins.read(buffer);
			}
			bins.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
