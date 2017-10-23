package admin.index.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
 
@Controller
public class IndexController {	
	
	@RequestMapping("/index.do")
	public ModelAndView list(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException{
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/index");
		
		String text = "index page";
		mnv.addObject("text", text);
		return mnv;
	}
}