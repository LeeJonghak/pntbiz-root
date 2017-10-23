package wms.help;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelpController {		
	
	@RequestMapping(value="/help/{locale}/{page:.+}", method=RequestMethod.GET)
	public ModelAndView help(Model model, HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "locale")String locale,
			@PathVariable(value = "page")String page
			) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		System.out.println("page : " + page); 
		String help = "/help/" + locale + "/" + page;
		mnv.setViewName(help);	
		return mnv;
	}
}