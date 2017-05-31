package Model;
import Common.Annotation.ATable;
import Common.Annotation.AutoIncrement;
import Common.Annotation.Column;
import Common.Annotation.PrimaryKey;

/**
 * 发票
 * @author ctk
 *
 */
@ATable(name = "Invoice")
public class Invoice {
	@PrimaryKey
	@AutoIncrement
	private long id;
	@Column("invoice_number")
	private String invoiceNumber;
	private double money;
	@Column("supplier_id")
	private long supplierId;
	@Column("supplier_name")
	private String supplierName;
	private int status;
	@Column("invoice_date")
	private String invoiceDate;
	private long register;//是谁录入
	@Column("register_date")
	private long registerDate;//录入时间
	@Column("is_deleted")
	private int isDeleted;
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
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public long getRegister() {
		return register;
	}
	public void setRegister(long register) {
		this.register = register;
	}
	public long getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(long registerDate) {
		this.registerDate = registerDate;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}
