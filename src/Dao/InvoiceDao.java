package Dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import Model.Invoice;

public interface InvoiceDao {
	//添加
	public void addInvoice(Invoice invoice); 
	//删除
	//查找
	public Invoice findById(long id);
	public Invoice findByNumber(String invoiceNumber);
	public List<Invoice> findByUserId(long userid);
	public List<Invoice> findByStatusnoUserId(int status);
	public List<Invoice> findByStatus(@Param("userid") long userid,@Param("status") int status);
	public List<Invoice> findLikeNumbersorSupplierName(@Param("invoiceNumber") String invoiceNumber,@Param("supplierName") String supplierName);
	public List<Invoice> findLikeNumbersorSupplierNameandUid(@Param("userid")long userid, @Param("invoiceNumber") String invoiceNumber,@Param("supplierName") String supplierName);
	//修改
	public void invoiceMove(@Param("invoiceNumber")String invoiceNumber ,@Param("status") int status,@Param("before") int before);//状态转移
	public void updateInvoiceInfo(@Param("id") int id, @Param("money") String money,@Param("supplierId") String supplierId,@Param("supplierName") String supplierName,@Param("invoiceDate")  String invoiceDate);
	public void softDelete(long id);
	
}
