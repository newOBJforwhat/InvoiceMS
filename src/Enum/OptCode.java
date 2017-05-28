package Enum;

/**
 * 操作代码
 * @author ctk
 *
 */
public enum OptCode {
	MOVE(0,"移动"),
	DELETE(1,"删除"),
	ALTER(2,"修改信息"),
	;
	private int code;
	private String name;
	private OptCode(int code,String name){
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
		for(OptCode ut : OptCode.values()){
			if(ut.code==code){
				return ut.name;
			}
		}
		return null;
	}
}
