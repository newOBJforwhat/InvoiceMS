package Model;

import Common.Annotation.ATable;
import Common.Annotation.AutoIncrement;
import Common.Annotation.Column;
import Common.Annotation.PrimaryKey;

/**
 * 发票处理表
 * @author ctk
 *
 */
@ATable(name = "Solve")
public class Solve {
	@PrimaryKey
	@AutoIncrement
	private long id;
	@Column("Invoice_number")
	private String invoiceNumber;
	private long auditing;
	private long finance;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public long getAuditing() {
		return auditing;
	}
	public void setAuditing(long auditing) {
		this.auditing = auditing;
	}
	public long getFinance() {
		return finance;
	}
	public void setFinance(long finance) {
		this.finance = finance;
	}
}
