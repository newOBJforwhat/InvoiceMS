package Controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import Common.Controller.OutputStringController;
import Model.Department;
import Service.DepartmentService;

@Controller
@RequestMapping(value="/department")
public class DepartmentController extends OutputStringController{
	@Autowired
	private DepartmentService dService;
	private static Logger logger = Logger.getLogger(DepartmentController.class);
	/**
	 * 超级用户查询所有部门
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryDepartments",produces="text/html;charset=UTF-8")
	public String queryAllDepartment(HttpServletRequest request){
		List<Department> departments =  dService.getAll();
		request.setAttribute("departments", departments);
		return "queryDeparment";
	}
	/**
	 * 超级用户修改部门信息
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/alterDepartmentInfo",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String alterDepartment(long id,String name){
		try{
			dService.alterInfo(id, name);
			return success("修改成功");
		}catch (NullPointerException e) {
			logger.debug("查询不到部门"+e.getMessage());
			return failure("查询不到部门");
		}catch (Exception e) {
			logger.error("查询发生异常："+e.getMessage());
			return failure("查询发生异常");
		}
		
	}
	/**
	 * 添加新部门
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/addDepartment",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addDepartment(String name){
		try{
			long id = dService.addDepartment(name);
			logger.debug("添加成功:"+id);
			return success("添加成功");
		}catch (Exception e) {
			logger.error("添加出现错误:"+e.getMessage());
			return failure("添加出现错误");
		}
		
	}
	/**
	 * 部门管理页面
	 * @return
	 */
	@RequestMapping(value="/departmentPage",produces="text/html;charset=UTF-8")
	public String departmentPage(){
		return "departmentPage";
	}
}
