package Common.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import Common.CommonInfo;
import Enum.UserType;
import Model.User;

/**
 * 录入角色拦截器
 * @author MacBook
 *
 */
public class StaffInterceptor implements HandlerInterceptor{
	private static Logger logger = Logger.getLogger(StaffInterceptor.class);
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
		logger.debug("录入拦截器.....");
		User u = (User)arg0.getSession().getAttribute(CommonInfo.userInfo);
		if(u.getType() != UserType.STAFF.getCode()){
			arg1.setStatus(401);
			return false;
		}
		else{
			return true;
		}
	}

}
