package Common;

import java.util.UUID;

/**
 * 生成验证码
 * @author ctk
 *
 */
public class GereratorOfUUID {
	//获取一个uuid
	public static String generator(){
		return UUID.randomUUID().toString();
	}
}
