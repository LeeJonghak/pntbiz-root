package api.error.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorController {
	@RequestMapping(value = "/error", method=RequestMethod.GET)
	@ResponseBody
	public Object error(HttpServletRequest request) throws IOException {
		Map<String, Object> res = new HashMap<String, Object>();		
		res.put("result", "error");
		res.put("code", "303");
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}
	
}