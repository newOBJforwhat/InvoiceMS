package Common.Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 * 过滤所有.jsp资源的请求
 * Servlet Filter implementation class ResourcesFilter
 */
@WebFilter(filterName="ResourcesFilter",urlPatterns="*.jsp")
public class ResourcesFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ResourcesFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("过滤.jsp请求");
		HttpServletResponse servletResp = (HttpServletResponse)response;
		servletResp.sendRedirect("/InvoiceMS/index");//重定向到首页
//		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
