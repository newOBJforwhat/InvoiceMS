package Common.Interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import Common.CommonInfo;
import Model.User;
import net.sf.json.JSONObject;

/**
 * 登录过滤器
 * @author ctk
 *
 */
public class UserLoginInterceptor implements HandlerInterceptor{
	private static Logger logger = Logger.getLogger(UserLoginInterceptor.class);
	//后置执行
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}
			
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	//前置执行
	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		logger.debug("用户登录拦截器的前置方法.....");
		
		HttpSession session = arg0.getSession();
		User u = (User)session.getAttribute(CommonInfo.userInfo);
		if(u == null)
		{
			logger.debug("用户未登录");
			JSONObject info = new JSONObject();
			info.put("status", 3);
			info.put("info","未登录");
			arg1.setStatus(200);
			String req = new String(info.toString().getBytes(),"utf-8");
			arg1.setContentType("text/html; charset=utf-8");
			arg1.getWriter().println(req);
			return false;
		}
		else
			logger.debug("用户已登录:"+u.getUsername());
		
		return true;
	}
}
