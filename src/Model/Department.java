package Model;

import Common.Annotation.ATable;
import Common.Annotation.AutoIncrement;
import Common.Annotation.PrimaryKey;

/**
 * 部门
 * @author ctk
 *
 */
@ATable(name = "Department")
public class Department {
	@PrimaryKey
	@AutoIncrement
	private long id;
	private String name;
	private String power;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
}
