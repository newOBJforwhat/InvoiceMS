package Model;

import Common.Annotation.ATable;
import Common.Annotation.AutoIncrement;
import Common.Annotation.Column;
import Common.Annotation.PrimaryKey;

/**
 * 用户类
 * @author ctk
 *
 */
@ATable(name = "User")
public class User {
	@PrimaryKey
	@AutoIncrement
	private long id;
	@Column("department_id")
	private long departmentId;
	private String username;
	private String password;
	private String name;
	private int type;
	@Column("is_deleted")
	private int isDeleted;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}
