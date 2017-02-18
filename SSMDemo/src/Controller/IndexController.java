package Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主页控制器
 * @author ctk
 *
 */
@Controller
public class IndexController {
	@RequestMapping("/index")
	public String index(){
		return "index.jsp";
	}
}
