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
import Common.BusinessErrorException;
import Common.Controller.OutputStringController;
import Enum.InvoiceStatus;
import Form.InvoiceForm;
import Model.Invoice;
import Service.InvoiceService;

@Controller
@Scope("request")
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
	@RequestMapping(value="/enterInvoice",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String enterInvoice(HttpSession session,InvoiceForm form){
		try{
			iService.enteringInvoice(getCurrentUser(session).getId(), form);
			return success("录入成功!");
		}catch (BusinessErrorException e) {
			logger.debug("已存在发票编号，请勿重复录入");
			return failure("发票已存在!");
		}catch (Exception e) {
			logger.error("录入错误:"+e.getMessage());
			return failure("录入出现异常!");
		}
	}
	/**
	 * 打开本人发票
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/myInvoicePage",produces="text/html;charset=UTF-8")
	public String myInvoice(HttpSession session,HttpServletRequest request){
		long userid = getCurrentUser(session).getId();
		try{
			List<Invoice> invoices = iService.getByUserid(userid);
			request.setAttribute("invoices", invoices);
		}catch (Exception e) {
			logger.error("获取本人发票失败："+e.getMessage());
		}
		return "myInvoicePage";
	}
	/**
	 * 审核部门页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checked/checkedPage",produces="text/html;charset=UTF-8")
	public String checkedPage(HttpServletRequest request){
		List<Invoice> checkedData = null;
		try{
			checkedData = iService.getByStatus((InvoiceStatus.CHECKED.getCode()));
		}catch (Exception e) {
			logger.error("加载审核部门页面出错:"+e.getMessage());
		}
		request.setAttribute("checkedData", checkedData);
		return "checkedPage";
	}
	/**
	 * 财务部页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/finance/financePage",produces="text/html;charset=UTF-8")
	public String financePage(HttpServletRequest request){
		List<Invoice> financeData = null;
		try{
			financeData = iService.getByStatus((InvoiceStatus.FINANCE.getCode()));
		}catch (Exception e) {
			logger.error("加载财务部页面页面出错:"+e.getMessage());
		}
		request.setAttribute("financeData", financeData);
		return "financePage";
	}
}
