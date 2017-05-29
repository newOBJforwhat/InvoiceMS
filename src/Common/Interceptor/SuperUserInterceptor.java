package Common.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import Common.CommonInfo;
import Enum.UserType;
import Model.User;
import net.sf.json.JSONObject;

public class SuperUserInterceptor implements HandlerInterceptor{
	private static Logger logger = Logger.getLogger(SuperUserInterceptor.class);
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		logger.debug("超级用户拦截器启动...");
		HttpSession session = arg0.getSession();
		User u = (User)session.getAttribute(CommonInfo.userInfo);
		if(u.getType() != UserType.SUPER.getCode())
		{
			logger.debug("非超级用户无法访问");
			JSONObject info = new JSONObject();
			info.put("status", 3);
			info.put("info","无权访问");
			arg1.setStatus(200);
			String req = new String(info.toString().getBytes(),"utf-8");
			arg1.setContentType("text/html; charset=utf-8");
			arg1.getWriter().println(req);
			return false;
		}
		else
			return true;
	}

}
