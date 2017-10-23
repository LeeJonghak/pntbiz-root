package custom.APGroupYS.beacon.controller;

import api.beacon.domain.BeaconExternalRequestParam;
import api.beacon.service.BeaconExternalService;
import api.beacon.service.BeaconService;
import api.common.service.CommonService;
import core.api.beacon.domain.Beacon;
import core.common.beacon.domain.BeaconExternal;
import framework.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class APGroupYSBeaconController {
    @Autowired
    private ApplicationContext applicationContext;

	@Autowired
	private BeaconService beaconService;

    @Autowired
    private BeaconExternalService beaconExternalService;

	private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 외부정보 설정 API From 출입 시스템
     *
     * 발급시스템에서 소켓으로 설정정보를 제공하면 pntbiz-socket에서 본 API를 호출하여
     * 발급시스템의 출입증 정보를 조회하여 비콘 설정 정보를 업데이트 함.
     *
     * 출입 시스템에서는 방문자에 대한 정보만을 제공함.
     *
     * USE_FLAG == 0 : 설정
     * USE_FLAG == 1 : 해제
     *
     * @param requestJson
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/beacon/external/set", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object beaconExternalSet(
            @RequestBody Map<String, Object> requestJson
    ) throws IOException, ParseException {
        Map<String , Object> response = new HashMap<>();

        // 발급시스템 카드 정보 조회
        Map<String, Object> cardInfo = getCardInfo((String) requestJson.get("cardNo"));
        if (cardInfo == null) {
            throw new BaseException("1001", "카드정보 조회 실패 : " + requestJson.get("cardNo").toString());
        }

        // 발급시스템 비콘 번호 확인
        String beaconNo = cardInfo.get("BEACONNO").toString();
        if (beaconNo.equals("")) {
            throw new BaseException("1002", "비콘정보 설정 안됨 : " + requestJson.get("cardNo").toString());
        }

        // 비콘 정보 조회 by macAddress
        Beacon beacon = beaconService.getBeaconInfoByMacAddr(cardInfo.get("BEACONNO").toString());
        if (beacon == null) {
            throw new BaseException("1002", "비콘정보 조회 실패: " + requestJson.get("BEACONNO").toString());
        }

        // 비콘 외부 설정 정보 초기화
        beaconExternalService.unassign(beacon.getBeaconNum());

        // 카드 사용 여부가 정상인 경우 외부설정 정보 처리리
        if (cardInfo.get("USE_FLAG").toString().equals("0")) {
            setExternalInfo(cardInfo, beacon);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("code", "0");

        return res;
    }

    private void setExternalInfo(Map<String, Object> cardInfo, Beacon beacon) {
        BeaconExternalRequestParam requestParam = new BeaconExternalRequestParam();
        requestParam.setExternalId(cardInfo.get("CARDNO").toString());
        requestParam.setBarcode("");
        requestParam.setRestrictedZonePermitted("true");

        List<Map<String,Object>> externalAttribute = new ArrayList<>();
        externalAttribute.add(setExternalAttribute("SABEON", cardInfo.get("SABEON"), "사번"));
        externalAttribute.add(setExternalAttribute("KOR_NAME", cardInfo.get("KOR_NAME"), "성명"));

        requestParam.setExternalAttribute(externalAttribute);

        List<Map<String,Object>> restrictedZone = new ArrayList<>();
        char[] accessfloors = cardInfo.get("ACCESSFLOOR").toString().toCharArray();

        for (int i = 0 ; i < accessfloors.length ; i ++ ) {
            if (accessfloors[i] == '1')
                restrictedZone.add(setRestrictedZone(String.valueOf(i + 1)));
        }

        requestParam.setRestrictedZone(restrictedZone);
        beaconExternalService.assign(beacon.getBeaconNum(), requestParam);
    }

    public Map<String, Object> getCardInfo(String cardNo) {
        JdbcTemplate sessionTemplate = (JdbcTemplate) applicationContext.getBean("sqlSessionTemplate2");

        StringBuilder sqlStatement = new StringBuilder();
        sqlStatement.append("SELECT CARDNO, REG_COUNT, BEACONNO, KOR_NAME, SABEON, ACCESSFLOOR, COMPANYCODE, COMPANYNAME, SITECODE, SITENAME, DEPARTCODE, DEPARTNAME, POSITIONNAME, USE_FLAG, REG_DATE ");
        sqlStatement.append("  FROM CARD_USER ");
        sqlStatement.append(" WHERE CARDNO = ? ");

        List<Map<String, Object>> result = sessionTemplate.queryForList(
                sqlStatement.toString(),
                new Object[]{cardNo}
        );

        if ( result.size() > 0 ) {
            return result.get(0);
        } else {
            return null;
        }
    }

    private Map<String, Object> setExternalAttribute(String key, Object value, String displayName) {
        Map<String, Object> attribute = new HashMap<>();

        attribute.put("key", key);
        attribute.put("value", value);
        attribute.put("displayName", displayName);

        return attribute;
    }

    private Map<String, Object> setRestrictedZone(String floor) {
        Map<String, Object> zone = new HashMap<>();

        zone.put("zoneType", "floor");
        zone.put("zoneId", floor);

        return zone;
    }

}