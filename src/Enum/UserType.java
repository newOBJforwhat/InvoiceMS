package Enum;

/**
 * 用户种类
 * @author ctk
 *
 */
public enum UserType {
	SUPER(0,"超级用户"),
	NORMAL(1,"普通用户"),
	;
	private int code;
	private String name;
	private UserType(int code,String name){
		this.code = code;
		this.name = name;
	}
	public int getCode(){
		return code;
	}
	public String getName(){
		return name;
	}
	public static String getNameByCode(int code){
		for(UserType ut : UserType.values()){
			if(ut.code==code){
				return ut.name;
			}
		}
		return null;
	}
}
