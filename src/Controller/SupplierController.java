package Controller;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import Common.BusinessErrorException;
import Common.CommonInfo;
import Common.Controller.OutputStringController;
import Model.Supplier;
import Service.SupplierService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/supplier")
public class SupplierController extends OutputStringController{
	@Autowired
	private SupplierService sService;
	
	private static Logger logger = Logger.getLogger(SupplierController.class);
	
	/**
	 * 添加供应商接口
	 * @param number
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/super/addSupplier",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String addSupplier(String number,String name){
		if(number == null || "".equals(number))
			return failure("输入参数为空");
		else if(name == null || "".equals(name))
			return failure("输入参数为空");
		
		try {
			sService.addSupplier(number, name);
			return success("添加成功");
		} catch (BusinessErrorException e) {
			return failure(e.getMessage());
		} catch (Exception e) {
			logger.error("添加失败:"+e.getMessage());
			return exception("添加供应商失败");
		}
	}
	/**
	 * 删除接口
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/super/deleteSupplier",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String deleteSupplier(long sid){
		if(sid == 0)
			return failure("参数为空");
		try {
			sService.deleteSupplier(sid);
			return success("删除成功");
		} catch (Exception e) {
			logger.error("删除:"+e.getMessage());
			return exception("删除异常");
		}
	}
	
	/**
	 * 更新信息接口
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/super/updateInfo",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String updateInfo(long sid,String name){
		if(sid == 0)
			return failure("参数为空");
		else if(name == null || "".equals(name))
			return failure("参数为空");
		
		try {
			sService.alterInfo(sid, name);
			return success("修改成功");
		} catch (Exception e) {
			logger.error("信息更新失败:"+e.getMessage());
			return exception("信息更新异常");
		}
	}
	
	/**
	 * 模糊查询接口
	 * @param number
	 * @param name
	 * @return
	 */
	@RequestMapping(value="/findLike/{number}/{name}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String findLike(@PathVariable("number")String number,@PathVariable("name")String name){
		if(number == null || "".equals(number))
			return failure("参数为空");
		else if(name == null || "".equals(name))
			return failure("参数为空");
		
		try{
			List<Supplier> result = sService.findLikeNumberOrName(name, number);
			JSONArray arrResult = new JSONArray();
			for(Supplier su : result){
				JSONObject objResult = new JSONObject();
				objResult.put("id", su.getId());
				objResult.put("number", su.getSupplierName());
				objResult.put("name", su.getSupplierName());
				arrResult.add(objResult);
			}
			return resultSuccess("查询成功", arrResult.toString());
		}catch (Exception e) {
			logger.error("查询异常:"+e.getMessage());
			return exception("查询异常");
		}
		
	}
	
	/**
	 * 超级用户查看供应商列表
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping(value="/super/findList/{page}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String findListByPage(@PathVariable("page")int pageIndex){
		if(pageIndex == 0)
			return failure("参数为空");

		try{
			List<Supplier> result = sService.getListByPage(pageIndex, CommonInfo.pageSize);
			JSONObject data = new JSONObject();
			JSONArray arrResult = new JSONArray();
			for(Supplier su : result){
				JSONObject objResult = new JSONObject();
				objResult.put("id", su.getId());
				objResult.put("number", su.getSupplierName());
				objResult.put("name", su.getSupplierName());
				arrResult.add(objResult);
			}
			int total = sService.findCount();
			data.put("data", arrResult);
			data.put("total",total);
			data.put("page", pageIndex);
			data.put("totalPage",total%CommonInfo.pageSize == 0?(total/CommonInfo.pageSize):((total/CommonInfo.pageSize) + 1));
			return resultSuccess("查询成功", arrResult.toString());
		}catch (Exception e) {
			logger.error("查询异常:"+e.getMessage());
			return exception("查询异常");
		}
	}
	/**
	 * 按照编号查询接口
	 * @param number
	 * @return
	 */
	@RequestMapping(value="/getByNumber/{number}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String getByNumber(@PathVariable("number")String number){
		if(number == null || "".equals(number))
			return failure("参数为空");
		try{
			Supplier su = sService.getByNumber(number);
			if(su == null)
				return success("查询成功");
			JSONObject jsonSupplier = new JSONObject();
			jsonSupplier.put("id", su.getId());
			jsonSupplier.put("number", su.getSupplierNumber());
			jsonSupplier.put("name", su.getSupplierName());
			return resultSuccess("查询成功", jsonSupplier.toString());
		}catch (Exception e) {
			logger.error("查询异常:"+e.getMessage());
			return exception("查询异常");
		}
	}
}
