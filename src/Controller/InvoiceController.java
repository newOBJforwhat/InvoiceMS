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
	@RequestMapping(value="/enterInvoice",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String enterInvoice(HttpSession session,InvoiceForm form){
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
	@RequestMapping(value="/existInvoice",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
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
			iJson.put("invoiceNumber",invoice.getId());
			iJson.put("invoiceNumber",invoice.getInvoiceNumber());
			iJson.put("invoiceDate",invoice.getInvoiceDate());
			iJson.put("money",invoice.getMoney());
			iJson.put("registerDate",invoice.getRegisterDate());
			iJson.put("status",invoice.getStatus());
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
				/*
				 * 需要vo
				 */
				JSONObject iJson = new JSONObject();
				iJson.put("id", in.getId());
				iJson.put("invoiceNumber",in.getInvoiceNumber());
				iJson.put("invoiceDate",in.getInvoiceDate());
				iJson.put("money",in.getMoney());
				iJson.put("registerDate",in.getRegisterDate());
				iJson.put("status",in.getStatus());
				iJson.put("supplierName",in.getSupplierName());
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
	@RequestMapping(value="/staff/Forward/invoiceNumber",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String staffForword(@PathVariable("invoiceNumber") String invoiceNumber){
		return null;
	}
	/**
	 * 业务员提交发票到财务
	 * @return
	 */
	@RequestMapping(value="/auditing/Forward/{invoiceNumber}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String auditingForward(@PathVariable("invoiceNumber") String invoiceNumber){
		return null;
	}
	
	/**
	 * 业务员回退
	 * @return
	 */
	@RequestMapping(value="/auditing/back/{invoiceNumber}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String back(@PathVariable("invoiceNumber") String invoiceNumber){
		return null;
	}

	/**
	 * 财务终结发票
	 * @return
	 */
	@RequestMapping(value="/finance/final/{invoiceNumber}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String financeFinal(@PathVariable("invoiceNumber") String invoiceNumber){
		return null;
	}
	/**
	 * 财务回退
	 * @return
	 */
	@RequestMapping(value="/finance/back/{invoiceNumber}",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public String financeBack(@PathVariable("invoiceNumber") String invoiceNumber){
		return null;
	}
	/**
	 * 根据发票号和供应商名字模糊查询
	 * @param invoiceNumber
	 * @param supplier
	 * @return
	 */
	@RequestMapping(value="/findByNumberorSupplier",produces="text/html;charset=UTF-8",method = RequestMethod.GET)
	public List<Invoice> findByNumberorSupplier(String invoiceNumber,String supplier){
		
		return null;
	}
	

}
