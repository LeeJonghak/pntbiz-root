package admin.error.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
	// 권한없음
	@RequestMapping(value = "/notpriv", method=RequestMethod.GET)
	public ModelAndView notpriv(Model model, HttpServletRequest request) throws IOException {	
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("notpriv");
		return mnv;
	}	
}