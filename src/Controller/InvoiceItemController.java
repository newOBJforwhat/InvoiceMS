package Controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Common.StringUtil;
import Common.Controller.OutputStringController;
import Dao.InvoiceDao;
import Dao.InvoiceItemDao;
import Dao.UserDao;
import Model.Invoice;
import Model.InvoiceItem;
import Model.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/invoiceItem")
public class InvoiceItemController extends OutputStringController{
	@Autowired
	private InvoiceItemDao itDao;
	@Autowired
	private InvoiceDao iDao;
	@Autowired
	private UserDao uDao;
	/**
	 * 获得发票操作记录
	 * @param invoiceNumber
	 * @return
	 */
	@RequestMapping(value="/super/invoiceLog",produces="text/html;charset=UTF-8",method = RequestMethod.POST)
	public String invoiceLog(String invoiceNumber){
		if(invoiceNumber == null || "".equals(invoiceNumber))
			return failure("发票编号为空");
		JSONObject resultInfo = new JSONObject();
		JSONObject invoiceInfo = new JSONObject();
		Invoice  invoice = iDao.findByNumber(invoiceNumber);
		if(invoice == null)
			return failure("该发票号不存在");
		User u = uDao.findById(invoice.getRegister());
		List<InvoiceItem> items = itDao.findByInvoiceNumber(invoiceNumber);
		JSONArray jarr = new JSONArray();
		invoiceInfo.put("invoiceNumber", invoice.getInvoiceNumber());
		invoiceInfo.put("money", invoice.getMoney());
		invoiceInfo.put("supplierName", invoice.getSupplierName());
		invoiceInfo.put("invoiceDate", invoice.getInvoiceDate());
		invoiceInfo.put("register", u.getName());
		invoiceInfo.put("registerDate", StringUtil.format2.format(new Date(invoice.getRegisterDate())));
		resultInfo.put("invoice", invoiceInfo);
		for(InvoiceItem it : items){
			JSONObject jobj = new JSONObject();
			jobj.put("solve", uDao.findNameById(it.getSolve()));
			jobj.put("advice", it.getAdvice());
			jarr.add(jobj);
		}
		resultInfo.put("items", jarr);
		return resultSuccess("查询成功", resultInfo.toString());
	}
}
