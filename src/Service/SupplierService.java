package Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import Common.BusinessErrorException;
import Dao.SupplierDao;
import Model.Supplier;

@Service
public class SupplierService {
	@Autowired
	private SupplierDao sDao;
	
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=30)
	public void addSupplier(String supplierNumber,String supplierName) throws Exception{
		if(sDao.findByNumber(supplierNumber) != null)
		{
			throw new BusinessErrorException("供应商已存在："+supplierNumber);
		}
		sDao.addSupplier(supplierNumber, supplierName);
	}
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void deleteSupplier(long id){
		sDao.deleteByNumber(id);
	}
	@Transactional(rollbackFor=Exception.class,readOnly = false, propagation = Propagation.REQUIRED,timeout=15)
	public void alterInfo(long id,String supplierName){
		sDao.alterById(id, supplierName);
	}
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public Supplier getById(long id){
		return sDao.findById(id);
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public Supplier getByNumber(String supplierNumber){
		return sDao.findByNumber(supplierNumber);
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Supplier> findLikeNumberOrName(String supplierNumber,String supplierName){
		return sDao.findLikeNumberorName(supplierNumber, supplierName);
	}
	
	@Transactional(rollbackFor=Exception.class,readOnly = true, propagation = Propagation.REQUIRED,timeout=15)
	public List<Supplier> getListByPage(int pageIndex,int pageSize){
		return sDao.findListByPage((pageIndex - 1)*pageSize,pageSize);
	}
	//获取总数
	public int findCount(){
		return sDao.findCountListByPage();
	}
}
