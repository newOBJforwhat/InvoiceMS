package Enum;

public enum InvoiceStatus {
	ENTERING(1,"录入"),
	CHECKED(2,"业务审核"),
	FINANCE(3,"财务审核"),
	FINAL(4,"终了"),
	;
	
	private int code;
	private String name;
	private InvoiceStatus(int code,String name){
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
		for(InvoiceStatus is : InvoiceStatus.values()){
			if(is.code==code){
				return is.name;
			}
		}
		return null;
	}
}
