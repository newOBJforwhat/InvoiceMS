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
 * 业务审核部门角色拦截器
 * @author ctk
 *
 */
public class AuditingInterceptor implements HandlerInterceptor{
	private static Logger logger = Logger.getLogger(AuditingInterceptor.class);
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
		logger.debug("审核拦截器.....");
		User u = (User)arg0.getSession().getAttribute(CommonInfo.userInfo);
		if(u.getType() != UserType.AUDITING.getCode()){
			arg1.setStatus(401);
			return false;
		}
		else{
			return true;
		}
	}

}