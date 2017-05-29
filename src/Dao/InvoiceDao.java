package Dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import Model.Invoice;

public interface InvoiceDao {
	//添加
	public void addInvoice(Invoice invoice); 
	//删除
	public void deleteById(long id);
	//查找
	public Invoice findById(long id);
	public Invoice findByNumber(String invoiceNumber);
	public List<Invoice> findByUserId(@Param("userid") long userid,@Param("pageIndex") int pageIndex,@Param("pageSize") int pageSize);
	public int findCountByUserId(@Param("userid") long userid);
	public List<Invoice> findByStatusnoUserId(@Param("status") int status,@Param("pageIndex") int pageIndex,@Param("pageSize") int pageSize);
	public int findCountByStatusnoUserId(@Param("status") int status);
	public List<Invoice> findByStatus(@Param("userid") long userid,@Param("status") int status);
	public int findCountByStatus(@Param("userid") long userid,@Param("status") int status);
	public List<Invoice> findLikeNumbersorSupplierName(@Param("invoiceNumber") String invoiceNumber,@Param("supplierName") String supplierName,@Param("pageIndex") int pageIndex,@Param("pageSize") int pageSize);
	public int findCountLikeNumbersorSupplierName(@Param("invoiceNumber") String invoiceNumber,@Param("supplierName") String supplierName);
	public List<Invoice> findLikeNumbersorSupplierNameandUid(@Param("userid")long userid, @Param("invoiceNumber") String invoiceNumber,@Param("supplierName") String supplierName,@Param("pageIndex") int pageIndex,@Param("pageSize") int pageSize);
	public int findCountLikeNumbersorSupplierNameandUid(@Param("invoiceNumber") String invoiceNumber,@Param("supplierName") String supplierName,@Param("userid") long userid);
	
	public List<Invoice> bySupplier(String supplier);
	public List<Invoice> byTime(@Param("start")long start,@Param("end")long end);
	//修改
	public void invoiceMove(@Param("invoiceNumber")String invoiceNumber ,@Param("status") int status,@Param("before") int before);//状态转移
	public void updateInvoiceInfo(@Param("id") int id, @Param("money") String money,@Param("supplierId") String supplierId,@Param("supplierName") String supplierName,@Param("invoiceDate")  String invoiceDate);
	public void softDelete(@Param("id") long id,@Param("userid") long userid);
	
}
