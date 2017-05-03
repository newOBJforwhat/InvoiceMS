package Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import Common.BusinessErrorException;
import Dao.InvoiceDao;
import Enum.InvoiceStatus;
import Form.InvoiceForm;
import Model.Invoice;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceDao iDao;
	
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void enteringInvoice(long userid,InvoiceForm form) throws BusinessErrorException{
		if(iDao.findByNumber(form.getInvoiceNumber()) != null)
		{
			throw new BusinessErrorException("发票编号已存在："+form.getInvoiceNumber());
		}
		Invoice invoice = new Invoice();
		invoice.setInvoiceNumber(form.getInvoiceNumber());
		invoice.setInvoiceDate(form.getInvoiceDate());
		invoice.setMoney(form.getMoney());
		invoice.setStatus(InvoiceStatus.ENTERING.getCode());
		invoice.setSupplierId(form.getSupplierId());
		invoice.setSupplierName(form.getSupplierName());
		
		//系统设置
		invoice.setRegister(userid);
		invoice.setRegisterDate(System.currentTimeMillis());
		
		iDao.addInvoice(invoice);
	}
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Invoice> getByUserid(long userid){
		List<Invoice> results = iDao.findByUserId(userid);
		return results;
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Invoice> getByStatus(int status){
		List<Invoice> results = iDao.findByStatus(status);
		return results;
	}
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public Invoice getByNumber(String number){
		Invoice invoice = iDao.findByNumber(number);
		return invoice;
	}
}
