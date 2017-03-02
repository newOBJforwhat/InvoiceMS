package Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import Common.Controller.OutputStringController;

@Controller
public class Hello extends OutputStringController{
	//接口形式
	@RequestMapping(value="/Hello",produces="text/html;charset=UTF-8")
	@ResponseBody 
	public String sayHello(){
		System.out.println("访问了Hello!");
		return failure("失败");
	}
	//restful接口
	@RequestMapping(value = "/start/{name}", produces = "text/html;charset=UTF-8")
	@ResponseBody 
	public String start2(@PathVariable("name") String name1) {
		System.out.println(name1);
		return "start";
	}
}
