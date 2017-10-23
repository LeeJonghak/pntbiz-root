package api.common.controller;

import api.common.service.CommonService;
import core.api.common.domain.*;
import core.common.code.domain.Code;
import framework.gabiasms.GabiaSmsApi;
import framework.gabiasms.GabiaSmsResult;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommonController {

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

	// 동기화정보-전체
	@RequestMapping(value="/sync/info/{UUID}", method=RequestMethod.GET)
	@ResponseBody
	public Object sync(HttpServletRequest request, Code code, Sync sync, Company company,
					   @PathVariable("UUID") String UUID
	) throws IOException, ServletException {
		Map<String, Object> res = new HashMap<String, Object>();
		UUID = StringUtil.NVL(UUID, "");
		code.setgCD("SYNC");

		// 필수 값 체크
		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			company.setUUID(UUID);
			Company companyInfo = commonService.getCompanyInfoByUUID(company);
			if(companyInfo == null) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				sync.setComNum(companyInfo.getComNum());
				List<?> list = commonService.getSyncList(sync);
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", list);
				logger.info("sync list : {}", list);
			}
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

	// 동기화정보
	@RequestMapping(value="/sync/info/{UUID}/{type}", method=RequestMethod.GET)
	@ResponseBody
	public Object sync(HttpServletRequest request, Code code,  Sync sync, Company company,
					   @PathVariable("UUID") String UUID,
					   @PathVariable("type") String type
	) throws IOException, ServletException {
		Map<String, Object> res = new HashMap<String, Object>();
		UUID = StringUtil.NVL(UUID, "");
		type = StringUtil.NVL(type, "");
		code.setgCD("SYNC");
		code.setsCD(type);
		Integer chk = commonService.getCodeCheck(code);

		// 필수 값 체크
		if("".equals(UUID) || "".equals(type)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			company.setUUID(UUID);
			Company companyInfo = commonService.getCompanyInfoByUUID(company);
			if(companyInfo == null || chk < 1) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				sync.setComNum(companyInfo.getComNum());
				sync.setSyncType(type);
				Sync syncInfo = commonService.getSyncInfo(sync);
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", syncInfo);
			}
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}


	/**
	 * 캘리브레이션-조회
	 * edit 2015-08-03 없으면 값 내려 주지 않도록 수정(기존은 없으면 디폴트 입력)
	 *                 배열형태로 캘리브레이션 정보 내려주도록 수정
	 * @param request
	 * @param calibration
	 * @param modelName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/calibration/info/{modelName}", method=RequestMethod.GET)
	@ResponseBody
	public Object calibrationInfo(HttpServletRequest request, Calibration calibration,
								  @PathVariable("modelName") String modelName
	) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();
		modelName = StringUtil.NVL(modelName, "");
		calibration.setModelName(modelName);

		if("".equals(modelName)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {

			List<?> calibrationList = commonService.getCalibrationList(calibration);
			logger.debug("calibrationInfo", calibration);

			res.put("result", "success");
			res.put("code", "0");
			res.put("data", calibrationList);
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

	// 캘리브레이션-입력
	@RequestMapping(value="/calibration/info", method=RequestMethod.POST)
	@ResponseBody
	public Object calibrationInfoReg(HttpServletRequest request, Calibration calibration
	) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();

		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);

		String modelName = StringUtil.NVL(node.path("modelName").getTextValue(), "");
		String maker = StringUtil.NVL(node.path("maker").getTextValue(), "Unknown");
		String telecom = StringUtil.NVL(node.path("telecom").getTextValue(), "Unknown");
		String deviceName = StringUtil.NVL(node.path("deviceName").getTextValue(), "Unknown");
		String os = StringUtil.NVL(node.path("os").getTextValue(), "Unknown");
		Integer rssi = node.path("rssi").getIntValue();

		// 필수 값 체크
		if("".equals(modelName) || "".equals(os) || rssi == null) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			try {
				calibration.setModelName(modelName);
				calibration.setMaker(maker);
				calibration.setTelecom(telecom);
				calibration.setDeviceName(deviceName);
				calibration.setOs(os);
				calibration.setRssi(rssi);
				commonService.registerCalibration(calibration);
				res.put("result", "success");
				res.put("code", "0");
			} catch(DataAccessException dae) {
				res.put("result", "error");
				res.put("code", "303");
			}
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

	// 캘리브레이션-수정
	@RequestMapping(value="/calibration/info/{modelName}/{os:.+}", method=RequestMethod.PUT)
	@ResponseBody
	public Object calibrationInfoMod(HttpServletRequest request, Calibration calibration,
									 @PathVariable("modelName") String modelName, @PathVariable("os")String os
	) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();

		modelName = StringUtil.NVL(modelName, "");
		os = StringUtil.NVL(os, "");

		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);

		String maker = StringUtil.NVL(node.path("maker").getTextValue(), "Unknown");
		String telecom = StringUtil.NVL(node.path("telecom").getTextValue(), "Unknown");
		String deviceName = StringUtil.NVL(node.path("deviceName").getTextValue(), "Unknown");
		Integer rssi = node.path("rssi").getIntValue();

		// 필수 값 체크
		if("".equals(modelName) || "".equals(os) || rssi == null) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			try {
				calibration.setModelName(modelName);
				calibration.setOs(os);
				calibration.setMaker(maker);
				calibration.setTelecom(telecom);
				calibration.setDeviceName(deviceName);
				calibration.setRssi(rssi);
				commonService.modifyCalibration(calibration);
				res.put("result", "success");
				res.put("code", "0");
			} catch(DataAccessException dae) {
				res.put("result", "error");
				res.put("code", "303");
			}
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

	/**
	 * 가비아 SMS 발송
	 * @param request
	 * @param phoneNumber 수신자 전화번호
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sms/send/{phoneNumber}", method=RequestMethod.PUT)
	@ResponseBody
	public Object smsSend(HttpServletRequest request,
						  @PathVariable("phoneNumber") String phoneNumber
	) throws Exception {

		Map<String, Object> res = new HashMap<String, Object>();

		phoneNumber = StringUtil.NVL(phoneNumber, "");
		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);

		String message = StringUtil.NVL(node.path("message").getTextValue(), ""); // 메세지 본문
		String sender = StringUtil.NVL(node.path("sender").getTextValue(), ""); // 발송자 전화번호
		String title = StringUtil.NVL(node.path("title").getTextValue(), ""); // LMS 메세지 제목, SMS인경우 제목 사용않됨
		String stype = StringUtil.NVL(node.path("stype").getTextValue(), ""); // 발송 구문(sms/lms)

		GabiaSmsApi gabiaSms = new GabiaSmsApi(smsApiId, smsApiKey);

		String transid = gabiaSms.generateTransId();
		res.put("transid", transid);

		String arr[] = new String[7];
		//arr[0] = "multi_lms";	// SMS/LMS 발송 구분
		arr[0] = StringUtils.isNotBlank(stype)?stype:"sms";	// SMS/LMS 발송 구분
		arr[1] = transid;	// 결과 확인을 위한 KEY (MAX 40byte. 중복되지 않도록 생성하여 전달해 주시기 바랍니다. )
		arr[2] = title;	// LMS 발송시 제목으로 사용 SMS 발송시는 수신자에게 내용이 보이지 않습니다.
		arr[3] = message;	// 본문 (80byte 제한 : SMS의 경우)
		arr[4] = StringUtils.isNotBlank(sender)?sender:smsSenderNumber;		// 발신 번호
		arr[5] = phoneNumber;		// 수신 번호
		arr[6] = "0";		// 예약 일자 "2013-07-30 12:00:00" 또는 "0" 0또는 빈값(null)은 즉시 발송

		SmsSendLog smsSendLogVo = new SmsSendLog();
		smsSendLogVo.setTransid(transid);
		smsSendLogVo.setStype(arr[0]);
		smsSendLogVo.setTitle(arr[2]);
		smsSendLogVo.setMessage(arr[3]);
		smsSendLogVo.setSenderNumber(arr[4]);
		smsSendLogVo.setReceiverNumber(arr[5]);
		smsSendLogVo.setSendDate(arr[6]);
		smsSendLogVo.setStatus("0");
		commonService.insertSmsSendLog(smsSendLogVo);

		try {
			String responseXml = gabiaSms.send(arr);
			logger.debug("response xml : \n" + responseXml);

			GabiaSmsResult sendResult = gabiaSms.getResult(responseXml);
			logger.debug("code = [" + sendResult.getCode() + "] mesg=[" + sendResult.getMesg() + "]");

			if (sendResult.getCode().compareTo("0000") == 0) {
				// 전송 성공
				String resultXml = gabiaSms.getResultXml(responseXml);
				System.out.println("result xml : \n" + resultXml);
				SmsSendLog logInfo = commonService.getSmsSendLogInfo(transid);
				logInfo.setStatus("1");
				logInfo.setResultMessage(sendResult.getMesg());
				commonService.updateSmsSendLog(logInfo);
				res.put("result", "success");
				res.put("code", "0");
			} else {
				// 전송 실패
				res.put("result", "error");
				res.put("code", "200");
				SmsSendLog logInfo = commonService.getSmsSendLogInfo(transid);
				logInfo.setStatus("2");
				logInfo.setResultMessage(sendResult.getMesg());
				commonService.updateSmsSendLog(logInfo);
			}
		} catch(Exception exception) {
			logger.error("exception", exception);
			res.put("result", "error");
			res.put("code", "200");
			SmsSendLog logInfo = commonService.getSmsSendLogInfo(transid);
			logInfo.setStatus("2");
			logInfo.setResultMessage("exception:"+exception.getMessage());
			commonService.updateSmsSendLog(logInfo);
		}

		return res;
	}


}