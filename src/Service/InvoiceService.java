package Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import Common.BusinessErrorException;
import Dao.InvoiceDao;
import Dao.InvoiceItemDao;
import Dao.SupplierDao;
import Enum.InvoiceStatus;
import Form.InvoiceForm;
import Model.Invoice;
import Model.Supplier;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceDao iDao;
	@Autowired
	private InvoiceItemDao itDao;
	@Autowired
	private SupplierDao sDao;
	
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void enteringInvoice(long userid,InvoiceForm form) throws BusinessErrorException{
		if(iDao.findByNumber(form.getInvoiceNumber()) != null)
		{
			throw new BusinessErrorException("发票编号已存在："+form.getInvoiceNumber());
		}

		Supplier supplier = sDao.findById(form.getSupplierId());
		//查询供应商id并加入到form里
		if(supplier == null)	{
			throw new BusinessErrorException("供应商不存在："+form.getSupplierName());
		}
		
		Invoice invoice = new Invoice();
		invoice.setInvoiceNumber(form.getInvoiceNumber());
		invoice.setInvoiceDate(form.getInvoiceDate());
		invoice.setMoney(form.getMoney());
		invoice.setStatus(InvoiceStatus.ENTERING.getCode());
		invoice.setSupplierId(supplier.getId());
		invoice.setSupplierName(supplier.getSupplierName());
		
		//系统设置
		invoice.setRegister(userid);
		invoice.setRegisterDate(System.currentTimeMillis());
		
		iDao.addInvoice(invoice);
		itDao.addItem(form.getInvoiceNumber(), userid, "发票录入");
	}
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Invoice> getByUserid(long userid,int pageIndex,int pageSize){
		List<Invoice> results = iDao.findByUserId(userid,(pageIndex - 1)*pageSize,pageSize);
		return results;
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Invoice> getByStatus(long userid,int status){
		List<Invoice> results = iDao.findByStatus(userid,status);
		return results;
	}
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public Invoice getByNumber(String number){
		Invoice invoice = iDao.findByNumber(number);
		return invoice;
	}
	@Transactional(isolation=Isolation.REPEATABLE_READ,rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void setInvoiceStatus(String invoiceNumber,int status,int before,long userid){
		iDao.invoiceMove(invoiceNumber, status , before);
		itDao.addItem(invoiceNumber, userid, "发票状态修改："+InvoiceStatus.getNameByCode(before)+" -> "+InvoiceStatus.getNameByCode(status));
	}

	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Invoice> findByInvoiceNumberOrSupplier(String invoiceNumber,String supplier,int pageIndex,int pageSize){
		return iDao.findLikeNumbersorSupplierName(invoiceNumber, supplier,(pageIndex - 1)*pageSize,pageSize);
	}
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Invoice> findByInvoiceNumberOrSupplier(long userid,String invoiceNumber,String supplier,int pageIndex,int pageSize){
		return iDao.findLikeNumbersorSupplierNameandUid(userid,invoiceNumber, supplier,(pageIndex - 1)*pageSize,pageSize);
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Invoice> findByStatus(int status,int pageIndex,int pageSize){
		return iDao.findByStatusnoUserId(status,( pageIndex - 1 )*pageSize,pageSize);
	}
	@Transactional(isolation=Isolation.REPEATABLE_READ,rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void softDelete(long id,long userid){
		Invoice invoice = iDao.findById(id);
		if(invoice == null)
			return;
		iDao.softDelete(id,userid);
		itDao.addItem(invoice.getInvoiceNumber(), userid, "删除发票");
	}
	@Transactional(isolation=Isolation.REPEATABLE_READ,rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void deleteById(long id){
		Invoice invoice = iDao.findById(id);
		if(invoice == null)
			return;
		iDao.deleteById(id);
		itDao.addItem(invoice.getInvoiceNumber(), 0, "超级用户删除发票");
	}
	//获得总数 按用户id查询
	public int findCountByUserId(long userid){
		return iDao.findCountByUserId(userid);
	}
	//获得总数 按状态查询
	public int findCountByStatusnoUserId(int status){
		return iDao.findCountByStatusnoUserId(status);
	}
	//获得总数 按状态和用户id查询
	public int findCountByStatus(long userid,int status){
		return iDao.findCountByStatus(userid, status);
	}
	//获得总数 按照发票号或者供应商名字模糊查询
	public int findCountLikeNumbersorSupplierName(String number,String supplierName){
		return iDao.findCountLikeNumbersorSupplierName(number, supplierName);
	}
	//获得总数 按照编号或供应商名字和用户id模糊查询
	public int findCountLikeNumbersorSupplierNameandUid(String number,String supplierName,long userid){
		return iDao.findCountLikeNumbersorSupplierNameandUid(number,supplierName,userid);
	}
}
