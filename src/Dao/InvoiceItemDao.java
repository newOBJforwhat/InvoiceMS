package Dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import Model.InvoiceItem;

public interface InvoiceItemDao {
	//添加
	public void addItem(@Param("invoiceNumber")String invoiceNumber,@Param("solve")long solve,@Param("advice")String advice);
	//查询
	public List<InvoiceItem> findByInvoiceNumber(String invoiceNumber);
	
}
