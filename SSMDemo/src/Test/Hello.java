package Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import Common.Controller.OutputStringController;

@Controller
public class Hello extends OutputStringController{
	
	
	//接口形式
	@RequestMapping(value="/Hello",produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String sayHello(){
		return failure("失败");
	}
	
}
