package Common;
/**
 * 自定义异常类
 * @author MacBook
 *
 */
public class BusinessErrorException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String info = "";
	public BusinessErrorException(){
		
	}
	public BusinessErrorException(String info){
		this.info = info;
	}
}
