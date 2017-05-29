package Model;

import Common.Annotation.ATable;
import Common.Annotation.AutoIncrement;
import Common.Annotation.Column;
import Common.Annotation.PrimaryKey;

/**
 * 供应商
 * @author ctk
 *
 */
@ATable(name = "Supplier")
public class Supplier {
	@PrimaryKey
	@AutoIncrement
	private long id;
	@Column("supplier_name")
	private String supplierName;
	@Column("supplier_number")
	private String supplierNumber;
	@Column("register_date")
	private long registerDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierNumber() {
		return supplierNumber;
	}
	public void setSupplierNumber(String supplierNumber) {
		this.supplierNumber = supplierNumber;
	}
	public long getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(long registerDate) {
		this.registerDate = registerDate;
	}
}
