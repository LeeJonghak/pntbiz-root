package custom.Sample.common.controller;

import core.api.common.domain.Company;
import api.common.service.CommonService;
import framework.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SampleCommonController {

	@Value("#{config['sms.sender.number']}")
	private String smsSenderNumber;

	@Value("#{config['sms.api.id']}")
	private String smsApiId;

	@Value("#{config['sms.api.key']}")
	private String smsApiKey;

	@Autowired
	private CommonService commonService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 헬스체크
	@RequestMapping(value="/health", method=RequestMethod.GET)
	@ResponseBody
	public Object sync(HttpServletRequest request) throws IOException, ServletException {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("result", "success");
		res.put("customizing", "Sample Site Customize Controller");
		res.put("code", "0");
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

	// 업체정보
	@RequestMapping(value="/company/info/{UUID}", method=RequestMethod.GET)
	@ResponseBody
	public Object companyInfo(HttpServletRequest request, Company company,
							  @PathVariable("UUID") String UUID) throws IOException, ServletException {
		Map<String, Object> res = new HashMap<String, Object>();
		UUID = StringUtil.NVL(UUID, "");

		// 필수 값 체크
		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			company.setUUID(UUID);
			Company companyInfo = commonService.getCompanyInfoByUUID(company);
			if(companyInfo == null) {
				res.put("result", "error");
				res.put("message", "request company is not existed.");
				res.put("code", "303");
			} else {
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", companyInfo);
				logger.info("companyInfo list : {}", companyInfo);
			}
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

}