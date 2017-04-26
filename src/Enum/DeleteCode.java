package Enum;

public enum DeleteCode {
	UNDELETED(0,"未删除"),
	DELETED(1,"已删除"),
	;
	private int code;
	private String name;
	private DeleteCode(int code,String name){
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
		for(DeleteCode dc : DeleteCode.values()){
			if(dc.code==code){
				return dc.name;
			}
		}
		return null;
	}
}
