package Dao;

import Model.Invoice;

public interface InvoiceDao {
	//添加
	public void addInvoice(Invoice invoice);
	//删除
	//查找
	public Invoice findByNumber(String invoiceNumber);
	//修改
}
