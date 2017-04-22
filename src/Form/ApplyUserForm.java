package Form;

public class ApplyUserForm {
	/**
	 * 填入 username，password，name
	 */
//	private long departmentId;
	private String username;
	private String password;
	private String name;
	private int type;
	/**
	 * 系统生成
	 */
	private long createTime;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public long getDepartmentId() {
//		return departmentId;
//	}
//	public void setDepartmentId(long departmentId) {
//		this.departmentId = departmentId;
//	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
