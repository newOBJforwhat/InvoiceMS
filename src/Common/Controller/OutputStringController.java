package Common.Controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import Common.CommonInfo;
import Model.User;
import net.sf.json.JSONObject;
/**
 * 接口输出JSON形式
 * restful
 * {"status":"0|1|2","info":message,"result":result}
 * status：0.失败；1.成功；2.（未定）；
 * info：返回服务器信息
 * result：返回查询结果
 * @author ctk
 *
 */
@Controller
public class OutputStringController {
	//返回成功信息
	public String success(String info){
		JSONObject result = new JSONObject();
		result.put("status", 1);
		if(info == null)
			result.put("info", "");
		else
			result.put("info", info);
		result.put("result", "");
		return result.toString();
	}
	//返回失败信息
	public String failure(String info){
		JSONObject result = new JSONObject();
		result.put("status",0);
		if(info == null)
			result.put("info", "");
		else
			result.put("info", info);
		result.put("result", "");
		return result.toString();
	}
	//返回供前端使用的result-成功
	public String resultSuccess(String info,String resultStr){
		JSONObject result = new JSONObject();
		result.put("status",1);
		if(info == null)
			result.put("info", "");
		else
			result.put("info", info);
		if(resultStr == null)
			result.put("result", "");
		else
			result.put("result", resultStr);
		return result.toString();
	}
	//返回供前端使用的result-失败
	public String resultFailure(String info,String resultStr){
		JSONObject result = new JSONObject();
		result.put("status",0);
		if(info == null)
			result.put("info", "");
		else
			result.put("info", info);
		if(resultStr == null)
			result.put("result", "");
		else
			result.put("result", resultStr);
		return result.toString();
	}
	//获取当前用户
	public User getCurrentUser(HttpSession session){
		User u = (User) session.getAttribute(CommonInfo.userInfo);
		return u;
	}
}
