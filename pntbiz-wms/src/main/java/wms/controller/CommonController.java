package wms.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import framework.web.util.StringUtil;

@Controller
public class CommonController {
	
	// 검증안됨
	@RequestMapping(value="/common/excel.do", method=RequestMethod.POST)
	public ModelAndView excel(Model model, HttpServletRequest request, HttpServletResponse response
			) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/common/excel");
		
		String html = request.getParameter("html");
		String filename = request.getParameter("filename");
		
		html = StringUtil.restoreHtmlString(html);
		filename = StringUtil.restoreHtmlString(filename);
		
		String[] args = {"<table>", "<thead>", "<tbody>", "<tr>", "<th>", "<td>"};
		String[] rep = {"","","","","",""};
		html = StringUtil.replace(html, args, rep);
		
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-type", "application/vnd.ms-excel; name='excel'");			
		response.setHeader("Content-Disposition", "attachment;filename="+filename);
		response.setHeader("Content-Type", "application/ms-excel");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		mnv.addObject("html", html);
		
		return mnv;
	}

	@RequestMapping(value="/common/remoteJson.do", method=RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Object remoteData(@RequestParam("url")String url,
						  @RequestParam(value = "type", defaultValue = "array")String type) {


		RestTemplate restTemplate = new RestTemplate();

		if("array".equals(type)) {
			ResponseEntity<List> result = restTemplate.getForEntity(url, List.class);
			return result.getBody();
		} else {
			ResponseEntity<Map> result = restTemplate.getForEntity(url, Map.class);
			return result.getBody();
		}

	}
}