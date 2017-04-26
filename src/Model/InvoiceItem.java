package Model;
/**
 * 记录发票的操作结果
 * @author ctk
 *
 */

import Common.Annotation.ATable;
import Common.Annotation.AutoIncrement;
import Common.Annotation.Column;
import Common.Annotation.PrimaryKey;

@ATable(name = "InvoiceItem")
public class InvoiceItem {
	@AutoIncrement
	@PrimaryKey
	private long id;
	@Column("invoice_number")
	private String invoiceNumber;
	private long solve;//处理人
	private String advice;//处理意见
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public long getSolve() {
		return solve;
	}
	public void setSolve(long solve) {
		this.solve = solve;
	}
}
