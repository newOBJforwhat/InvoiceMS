package Controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import Common.BusinessErrorException;
import Common.StringUtil;
import Common.Controller.OutputStringController;
import Enum.InvoiceStatus;
import Form.InvoiceForm;
import Model.Invoice;
import Model.User;
import Service.InvoiceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@Scope("singleton")
@RequestMapping(value="/invoice")
public class InvoiceController extends OutputStringController{
	@Autowired
	private InvoiceService iService;
	private static Logger logger = Logger.getLogger(InvoiceController.class);
	/**
	 *	录入发票的接口
	 * @param session
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/staff/enterInvoice",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String enterInvoice(HttpSession session,InvoiceForm form){
		if(form.getInvoiceNumber() == null || "".equals(form.getInvoiceNumber())){
			return failure("发票号为空");
		}else if(form.getMoney() == 0){
			return failure("发票金额为空");
		}else if(form.getSupplierName() == null || "".equals(form.getInvoiceNumber())){
			return failure("供应商为空");
		}else if(form.getInvoiceDate() == null || "".equals(form.getInvoiceDate()))
			return failure("发票日期为空");
		
		try{
			iService.enteringInvoice(getCurrentUser(session).getId(), form);
			return success("录入成功!");
		}catch (BusinessErrorException e) {
			logger.debug("已存在发票编号，请勿重复录入");
			return failure("发票已存在!");
		}catch (Exception e) {
			logger.error("录入错误:"+e.getMessage());
			return exception("录入出现异常!");
		}
	}
	/**
	 * 按发票号查询
	 * @param invoiceNumber
	 * @return
	 */
	@RequestMapping(value="/super/findInvoiceByNumber",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String existInvoice(String invoiceNumber){
		Invoice invoice;
		try{
			invoice = iService.getByNumber(invoiceNumber);
		}catch (Exception e) {
			logger.error("验证出错："+e.getMessage());
			return exception("验证发票号出错");
		}
		if(invoice != null){
			JSONObject iJson = new JSONObject();
			iJson.put("id",invoice.getId());
			iJson.put("invoiceNumber",invoice.getInvoiceNumber());
			iJson.put("invoiceDate",invoice.getInvoiceDate());
			iJson.put("money",invoice.getMoney());
			iJson.put("registerDate",invoice.getRegisterDate());
			iJson.put("status",InvoiceStatus.getNameByCode(invoice.getStatus()));
			iJson.put("supplierName",invoice.getSupplierName());
			return resultSuccess("查询成功", iJson.toString());
		}
		return resultSuccess("查询成功", "");
	}
	/**
	 * 获取自身提交的发票
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/myInvoice",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String getInvoiceMine(HttpSession session){
		try{
			User loginUser = getCurrentUser(session);
			List<Invoice> invoices = iService.getByUserid(loginUser.getId());
			JSONArray iarr = new JSONArray();
			for(Invoice in : invoices){
				JSONObject iJson = new JSONObject();
				iJson.put("id", in.getId());
				iJson.put("invoiceNumber",in.getInvoiceNumber());
				iJson.put("invoiceDate",in.getInvoiceDate());
				iJson.put("money",in.getMoney());
				iJson.put("registerDate",StringUtil.millsToString(in.getRegisterDate()));
				iJson.put("status",in.getStatus());
				iJson.put("supplierName",in.getSupplierName());
				iarr.add(iJson);
			}
			return resultFailure("查询成功", iarr.toString());
		}catch (Exception e) {
			logger.error("查询错误:"+e.getMessage());
			return exception("查询失败");
		}
	}
	/**
	 * 获取发票状态
	 * @return
	 */
	@RequestMapping(value="/invoiceStatus",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String getStatus(){
		JSONObject jsonResult  = new JSONObject();
		jsonResult.put(InvoiceStatus.ENTERING.getCode(), InvoiceStatus.ENTERING.getName());
		jsonResult.put(InvoiceStatus.CHECKED.getCode(), InvoiceStatus.CHECKED.getName());
		jsonResult.put(InvoiceStatus.FINANCE.getCode(), InvoiceStatus.FINANCE.getName());
		return resultSuccess("success", jsonResult.toString());
	}
	/**
	 * 录入员提交发票到业务审核
	 * @return
	 */
	@RequestMapping(value="/staff/forward",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String staffForword(String invoiceNumber){
		try{
			iService.setInvoiceStatus(invoiceNumber, InvoiceStatus.CHECKED.getCode(),InvoiceStatus.ENTERING.getCode());
		}catch (Exception e) {
			return exception("转移到业务接口异常");
		}

		return success("修改成功");
	}
	/**
	 * 业务员提交发票到财务
	 * @return
	 */
	@RequestMapping(value="/auditing/forward",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String auditingForward(String invoiceNumber){
		try {
			iService.setInvoiceStatus(invoiceNumber, InvoiceStatus.FINANCE.getCode(),InvoiceStatus.CHECKED.getCode());
		} catch (Exception e) {
			return exception("转移到财务接口异常");
		}
		return success("修改成功");
	}
	
	/**
	 * 业务员回退
	 * @return
	 */
	@RequestMapping(value="/auditing/back",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String back(String invoiceNumber){
		try{
			iService.setInvoiceStatus(invoiceNumber, InvoiceStatus.ENTERING.getCode(),InvoiceStatus.CHECKED.getCode());
		}catch (Exception e) {
			return exception("回退到录入异常");
		}
		return success("修改成功");
	}

	/**
	 * 财务终结发票
	 * @return
	 */
	@RequestMapping(value="/finance/final",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String financeFinal(String invoiceNumber){
		try{
			iService.setInvoiceStatus(invoiceNumber, InvoiceStatus.FINAL.getCode(),InvoiceStatus.FINANCE.getCode());
		}catch (Exception e) {
			return exception("终结财务发票异常");
		}
		return success("修改成功");
	}
	/**
	 * 财务回退
	 * @return
	 */
	@RequestMapping(value="/finance/back",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String financeBack(String invoiceNumber){
		try{
			iService.setInvoiceStatus(invoiceNumber, InvoiceStatus.CHECKED.getCode(),InvoiceStatus.FINANCE.getCode());
		}catch (Exception e) {
			return exception("回退到业务部门异常");
		}
		return success("修改成功");
	}
	/**
	 * 根据发票号和供应商名字模糊查询
	 * 只能查询与自己有关的
	 * @param session
	 * @param invoiceNumber
	 * @param supplier
	 * @return
	 */
	@RequestMapping(value="/staff/findByNumberorSupplier/{invoiceNumber}/{supplier}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String findByNumberorSupplier(HttpSession session,@PathVariable("invoiceNumber")String invoiceNumber,@PathVariable("supplier")String supplier){
		if(invoiceNumber == null || supplier == null)
			return failure("参数为空");
		User u = (User)getCurrentUser(session);
		try{
			List<Invoice> results = iService.findByInvoiceNumberOrSupplier(u.getId(),invoiceNumber,supplier);
			JSONArray jarray = new JSONArray();
			for (Invoice in : results) {
				JSONObject jobj = new JSONObject();
				jobj.put("id", in.getId());
				jobj.put("invoiceNumber", in.getInvoiceNumber());
				jobj.put("invoiceDate", in.getInvoiceDate());
				jobj.put("status", in.getStatus());
				jobj.put("money", in.getMoney());
				jobj.put("supplier", in.getSupplierName());
				jobj.put("registerDate", StringUtil.millsToString(in.getRegisterDate()));
				jarray.add(jobj);
			}
			return resultSuccess("查询成功", jarray.toString());
		}catch (Exception e) {
			logger.error("模糊查询出错："+e.getMessage());
			return exception("查询异常");
		}

	}
	/**
	 * 根据发票号和供应商名字模糊查询
	 * 超级用户使用
	 * @param session
	 * @param invoiceNumber
	 * @param supplier
	 * @return
	 */
	@RequestMapping(value="/super/findByNumberorSupplier/{invoiceNumber}/{supplier}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String findByNumberorSupplierForSuper(HttpSession session,@PathVariable("invoiceNumber")String invoiceNumber,@PathVariable("supplier")String supplier){
		if(invoiceNumber == null || supplier == null)
			return failure("参数为空");
		try{
			List<Invoice> results = iService.findByInvoiceNumberOrSupplier(invoiceNumber,supplier);
			JSONArray jarray = new JSONArray();
			for (Invoice in : results) {
				JSONObject jobj = new JSONObject();
				jobj.put("id", in.getId());
				jobj.put("invoiceNumber", in.getInvoiceNumber());
				jobj.put("invoiceDate", in.getInvoiceDate());
				jobj.put("money", in.getMoney());
				jobj.put("supplier", in.getSupplierName());
				jobj.put("registerDate", StringUtil.millsToString(in.getRegisterDate()));
				jarray.add(jobj);
			}
			return resultSuccess("查询成功", jarray.toString());
		}catch (Exception e) {
			logger.error("模糊查询出错："+e.getMessage());
			return exception("查询异常");
		}

	}
	/**
	 * 超级用户按状态查询
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/super/findByStatus/{status}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String findByStatus(@PathVariable("status")int status){
		if(status != InvoiceStatus.ENTERING.getCode() && status != InvoiceStatus.CHECKED.getCode() && status != InvoiceStatus.FINANCE.getCode() && status != InvoiceStatus.FINAL.getCode())
			return failure("参数错误");
		try{
			List<Invoice> results = iService.findByStatus(status);
			JSONArray jarray = new JSONArray();
			for (Invoice in : results) {
				JSONObject jobj = new JSONObject();
				jobj.put("id", in.getId());
				jobj.put("invoiceNumber", in.getInvoiceNumber());
				jobj.put("invoiceDate", in.getInvoiceDate());
				jobj.put("money", in.getMoney());
				jobj.put("supplier", in.getSupplierName());
				jobj.put("registerDate", StringUtil.millsToString(in.getRegisterDate()));
				jarray.add(jobj);
			}
			return resultSuccess("查询成功", jarray.toString());
		}catch (Exception e) {
			logger.error("查询异常:"+e.getMessage());
			return exception("查询异常:"+e.getMessage());
		}
		
	}

}
