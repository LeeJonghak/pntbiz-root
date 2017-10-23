package api.service.controller;

import api.common.service.CommonService;
import core.api.service.domain.AttendanceSeminar;
import core.api.service.domain.AttendanceSeminarMgr;
import api.service.service.AttendanceSeminarService;
import framework.web.util.DateUtil;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 입장,퇴장 출격 기능
 *     1일 1회 입장, 퇴장 로깅
 * create: 2015-07-28 nohsoo
 */
@Controller
public class AttendanceSeminarController {

    @Autowired
    private AttendanceSeminarService attendanceSeminarService;

    @Autowired
    private CommonService commonService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="/attendanceSeminar/enter", method= RequestMethod.POST)
    @ResponseBody
    public Object in(HttpServletRequest request) throws IOException {

        Map<String, Object> res = new HashMap<String, Object>();

        String str = JsonUtil.getJson(request);
        JsonNode node = JsonUtil.toNode(str);

        String UUID = StringUtil.NVL(node.path("UUID").getTextValue(), "");
        String macAddr = StringUtil.NVL(node.path("macAddr").getTextValue(), "");
        String deviceInfo = StringUtil.NVL(node.path("deviceInfo").getTextValue(), "");
        Integer majorVer = null;
        Integer minorVer = null;
        if(node.has("majorVer")) {
            majorVer = node.path("majorVer").getIntValue();
        }
        if(node.has("minorVer")) {
            minorVer = node.path("minorVer").getIntValue();
        }

        commonService.checkAuthorized(UUID);

        String phoneNumber = StringUtil.NVL(node.path("phoneNumber").getTextValue(), "");
        String subject = StringUtil.NVL(node.path("subject").getTextValue(), "");
        String attdDate = DateUtil.getDate("yyyyMMdd");

        // 필수 값 체크
        if("".equals(UUID) || "".equals(phoneNumber) || "".equals(deviceInfo) || "".equals(attdDate) || "".equals(macAddr) || majorVer==null || minorVer==null) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
            try {

                /**
                 * 세미나 정보 확인
                 */
                Map<String, Object> mgrParam = new HashMap<String, Object>();
                mgrParam.put("UUID", UUID);
                mgrParam.put("majorVer", majorVer);
                mgrParam.put("minorVer", minorVer);
                mgrParam.put("macAddr", macAddr);

                AttendanceSeminarMgr mgrInfo = attendanceSeminarService.getAttendanceSeminarMsgInfo(mgrParam);
                if(mgrInfo==null) {
                    /**
                     * 세미나 정보가 없을경우 203 에러코드 반환
                     */
                    res.put("result", "error");
                    res.put("code", "203");

                } else {

                    /**
                     * deviceInfo 값 체크
                     */
                    HashMap<String, Object> deviceCheckParam = new HashMap<String, Object>();
                    deviceCheckParam.put("UUID", UUID);
                    deviceCheckParam.put("majorVer", majorVer);
                    deviceCheckParam.put("minorVer", minorVer);
                    deviceCheckParam.put("deviceInfo", deviceInfo);
                    deviceCheckParam.put("attdDate", attdDate);
                    Integer deviceCheckCnt = attendanceSeminarService.getAttendanceSeminarCount(deviceCheckParam);
                    if(deviceCheckCnt>0) {
                        // 동일한 device 에서 한 세미나에 대하여 1회만 출석 가능

                        res.put("result", "error");
                        res.put("code", "204");
                    } else {


                        /**
                         * 입장로그가 존재하는지 확인
                         */
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("UUID", UUID);
                        param.put("phoneNumber", phoneNumber);
                        param.put("state", "E");
                        param.put("majorVer", majorVer);
                        param.put("minorVer", minorVer);
                        param.put("attdDate", attdDate);
                        Integer cnt = attendanceSeminarService.getAttendanceSeminarCount(param);

                        if (cnt > 0) {
                            /**
                             * 이미 추가된 로그
                             */
                            res.put("result", "error");
                            res.put("code", "201");
                        } else {

                            /**
                             * 입장로그 추가
                             */
                            AttendanceSeminar vo = new AttendanceSeminar();
                            vo.setUUID(UUID);
                            vo.setPhoneNumber(phoneNumber);
                            vo.setDeviceInfo(deviceInfo);
                            vo.setMajorVer(majorVer);
                            vo.setMinorVer(minorVer);
                            vo.setMacAddr(macAddr);
                            vo.setState("E");
                            vo.setAttdDate(attdDate);
                            vo.setSubject(subject);
                            attendanceSeminarService.registerAttendanceSeminar(vo);

                            logger.debug("attendanceSeminar {} ", vo);
                            res.put("result", "success");
                            res.put("subject", mgrInfo.getSubject());
                            res.put("code", "0");
                        }

                    }
                }
            } catch(DataAccessException dae) {
                res.put("result", "error");
                res.put("code", "303");
            }
        }
        return res;
//        JsonUtil jsonUtil = new JsonUtil(res);
//        String json = jsonUtil.toJson();
//        return json;
    }

    @RequestMapping(value="/attendanceSeminar/leave", method= RequestMethod.POST)
    @ResponseBody
    public Object out(HttpServletRequest request) throws IOException {
        Map<String, Object> res = new HashMap<String, Object>();

        String str = JsonUtil.getJson(request);
        JsonNode node = JsonUtil.toNode(str);

        String UUID = StringUtil.NVL(node.path("UUID").getTextValue(), "");
        String macAddr = StringUtil.NVL(node.path("macAddr").getTextValue(), "");
        Integer majorVer = null;
        Integer minorVer = null;
        if(node.has("majorVer")) {
            majorVer = node.path("majorVer").getIntValue();
        }
        if(node.has("minorVer")) {
            minorVer = node.path("minorVer").getIntValue();
        }
        String phoneNumber = StringUtil.NVL(node.path("phoneNumber").getTextValue(), "");
        String deviceInfo = StringUtil.NVL(node.path("deviceInfo").getTextValue(), "");
        String subject = StringUtil.NVL(node.path("subject").getTextValue(), "");
        String attdDate = DateUtil.getDate("yyyyMMdd");

        // 필수 값 체크
        if("".equals(UUID) || "".equals(phoneNumber) || "".equals(deviceInfo) || "".equals(attdDate) || "".equals(macAddr) || majorVer==null || minorVer==null) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
            try {

                /**
                 * 세미나 정보 확인
                 */
                Map<String, Object> mgrParam = new HashMap<String, Object>();
                mgrParam.put("UUID", UUID);
                mgrParam.put("majorVer", majorVer);
                mgrParam.put("minorVer", minorVer);
                mgrParam.put("macAddr", macAddr);
                AttendanceSeminarMgr mgrInfo = attendanceSeminarService.getAttendanceSeminarMsgInfo(mgrParam);
                if(mgrInfo==null) {
                    /**
                     * 세미나 정보가 없을경우 203 에러코드 반환
                     */
                    res.put("result", "error");
                    res.put("code", "203");
                } else {

                    /**
                     * 입장로그가 존재하는지 확인
                     */
                    Map<String, Object> param1 = new HashMap<String, Object>();
                    param1.put("UUID", UUID);
                    param1.put("phoneNumber", phoneNumber);
                    param1.put("state", "E");
                    param1.put("majorVer", majorVer);
                    param1.put("minorVer", minorVer);
                    param1.put("attdDate", attdDate);
                    Integer cnt1 = attendanceSeminarService.getAttendanceSeminarCount(param1);

                    /**
                     * 퇴장로그가 존재하는지 확인
                     */
                    Map<String, Object> param2 = new HashMap<String, Object>();
                    param2.put("UUID", UUID);
                    param2.put("phoneNumber", phoneNumber);
                    param2.put("state", "L");
                    param2.put("majorVer", majorVer);
                    param2.put("minorVer", minorVer);
                    param2.put("attdDate", attdDate);
                    Integer cnt2 = attendanceSeminarService.getAttendanceSeminarCount(param2);

                    if(cnt2>0) {
                        /**
                         * 이미 추가된 로그
                         */
                        res.put("result", "error");
                        res.put("code", "201");
                    } else if(cnt1<=0) {
                        /**
                         * 입장로그가 존재하지 않음
                         */
                        res.put("result", "error");
                        res.put("code", "202");
                    } else {


                        /**
                         * 퇴장로그 추가
                         */
                        AttendanceSeminar vo = new AttendanceSeminar();
                        vo.setUUID(UUID);
                        vo.setMajorVer(majorVer);
                        vo.setMinorVer(minorVer);
                        vo.setMacAddr(macAddr);
                        vo.setPhoneNumber(phoneNumber);
                        vo.setDeviceInfo(deviceInfo);
                        vo.setState("L");
                        vo.setAttdDate(attdDate);
                        vo.setSubject(subject);
                        attendanceSeminarService.registerAttendanceSeminar(vo);

                        logger.debug("attendanceSeminar {} ", vo);
                        res.put("result", "success");
                        res.put("subject", mgrInfo.getSubject());
                        res.put("code", "0");
                    }
                }
            } catch(DataAccessException dae) {
                res.put("result", "error");
                res.put("code", "303");
            }
        }
        return res;
//        JsonUtil jsonUtil = new JsonUtil(res);
//        String json = jsonUtil.toJson();
//        return json;
    }

}
