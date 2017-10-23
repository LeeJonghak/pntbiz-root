package api.beacon.service;

import api.beacon.domain.BeaconExternalRequestParam;
import core.common.beacon.dao.BeaconExternalDao;
import core.common.beacon.dao.BeaconExternalLogDao;
import core.common.beacon.dao.BeaconRestrictedZoneDao;
import core.common.beacon.dao.BeaconRestrictedZoneLogDao;
import core.common.beacon.domain.*;
import core.common.enums.AssignUnassignType;
import core.common.enums.BooleanType;
import core.common.enums.ZoneType;
import framework.database.EnumValue;
import framework.database.ValueEnumTypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by nsyun on 17. 7. 20..
 */
@Service
public class BeaconExternalServiceImpl implements BeaconExternalService {

	@Autowired
	private BeaconExternalDao beaconExternalDao;

	@Autowired
	private BeaconRestrictedZoneDao beaconRestrictedZoneDao;

	@Autowired
	private BeaconExternalLogDao beaconExternalLogDao;

	@Autowired
	private BeaconRestrictedZoneLogDao beaconRestrictedZoneLogDao;

	@Override
	public void assign(Integer beaconNum, BeaconExternalRequestParam requestParam) {
		assign(beaconNum.longValue(), requestParam);
	}

	@Override
	@Transactional
	public void assign(Long beaconNum, BeaconExternalRequestParam requestParam) {

		BeaconExternal beaconParam = new BeaconExternal();
		beaconParam.setBeaconNum(beaconNum);
		BeaconExternal externalInfo = beaconExternalDao.getBeaconExternalInfo(beaconParam);

		/**
		 * 비콘 외부데이터 설정
		 */
		BeaconExternal beaconExternalVo = new BeaconExternal();
		beaconExternalVo.setBeaconNum(beaconNum);
		beaconExternalVo.setExternalId(requestParam.getExternalId() == null ? "" : requestParam.getExternalId());
		beaconExternalVo.setBarcode(requestParam.getBarcode() == null ? "" : requestParam.getBarcode());
		beaconExternalVo.setExternalAttribute(requestParam.getExternalAttribute());
		BooleanType zonePermitted = BooleanType.FALSE;
		if(StringUtils.isNotBlank(requestParam.getRestrictedZonePermitted())) {
			zonePermitted = BooleanType.valueOf(
					String.valueOf(requestParam.getRestrictedZonePermitted()).toUpperCase()
			);
			beaconExternalVo.setRestrictedZonePermitted(zonePermitted);
		} else {
			beaconExternalVo.setRestrictedZonePermitted(BooleanType.FALSE);
		}
		beaconExternalDao.updateBeaconExternalInfo(beaconExternalVo);

		// 비콘 외부데이터 로그 생성
		BeaconExternalLog externalLog = new BeaconExternalLog();
		externalLog.setType(AssignUnassignType.ASSIGN);
		externalLog.setBeaconNum(beaconNum);
		beaconExternalLogDao.insertBeaconExternalLog(externalLog);

		/**
		 * 인가/비인가구역 설정
		 */
		BeaconRestrictedZone deleteParam = new BeaconRestrictedZone();
		deleteParam.setBeaconNum(beaconNum);
		beaconRestrictedZoneDao.deleteBeaconRestrictedZone(deleteParam);
		for (Map<String, Object> restrictedZoneMap : requestParam.getRestrictedZone()) {

			BeaconRestrictedZone restrictedZoneVo = new BeaconRestrictedZone();
			restrictedZoneVo.setBeaconNum(beaconNum);
			restrictedZoneVo.setZoneType(
					ZoneType.valueOf(
							String.valueOf(restrictedZoneMap.get("zoneType")).toUpperCase()
					)
			);
			restrictedZoneVo.setZoneId(String.valueOf(restrictedZoneMap.get("zoneId")));
			restrictedZoneVo.setPermitted(zonePermitted);
			restrictedZoneVo.setAdditionalAttribute((Map<String, Object>) restrictedZoneMap.get("additionalAttribute"));
			restrictedZoneVo.setStartDate((Integer)restrictedZoneMap.get("startDate"));
			restrictedZoneVo.setEndDate((Integer) restrictedZoneMap.get("endDate"));

			beaconRestrictedZoneDao.insertBeaconRestrictedZone(restrictedZoneVo);
		}

		// 인가/비인가구역 로그 생성
		BeaconRestrictedZoneLog zoneLog = new BeaconRestrictedZoneLog();
		zoneLog.setType(AssignUnassignType.ASSIGN);
		zoneLog.setRefLogNum(externalLog.getLogNum());
		zoneLog.setBeaconNum(beaconNum);
		beaconRestrictedZoneLogDao.insertBeaconRestrictedZoneLog(zoneLog);

	}


	@Override
	public void unassign(Integer beaconNum) {
		unassign(beaconNum.longValue());
	}

	@Override
	@Transactional
	public void unassign(Long beaconNum) {

		BeaconExternal beaconParam = new BeaconExternal();
		beaconParam.setBeaconNum(beaconNum);
		BeaconExternal externalInfo = beaconExternalDao.getBeaconExternalInfo(beaconParam);
		if(externalInfo.getExternalId()!=null && externalInfo.getBarcode()!=null) {
			// 비콘 외부데이터 로그 생성
			BeaconExternalLog externalLog = new BeaconExternalLog();
			externalLog.setType(AssignUnassignType.UNASSIGN);
			externalLog.setBeaconNum(beaconNum);
			beaconExternalLogDao.insertBeaconExternalLog(externalLog);

			/**
			 * 비콘 외부데이터 제거
			 */
			BeaconExternal beaconExternalVo = new BeaconExternal();
			beaconExternalVo.setBeaconNum(beaconNum);
			beaconExternalVo.setExternalId(null);
			beaconExternalVo.setBarcode(null);
			beaconExternalVo.setExternalAttributeRaw(null);
			beaconExternalDao.updateBeaconExternalInfo(beaconExternalVo);

			// 인가/비인가구역 로그 생성
			BeaconRestrictedZoneLog zoneLog = new BeaconRestrictedZoneLog();
			zoneLog.setType(AssignUnassignType.UNASSIGN);
			zoneLog.setRefLogNum(externalLog.getLogNum());
			zoneLog.setBeaconNum(beaconNum);
			beaconRestrictedZoneLogDao.insertBeaconRestrictedZoneLog(zoneLog);

			/**
			 * 인가/비인가구역 제거
			 */
			BeaconRestrictedZone deleteParam = new BeaconRestrictedZone();
			deleteParam.setBeaconNum(beaconNum);
			beaconRestrictedZoneDao.deleteBeaconRestrictedZone(deleteParam);
		}

	}

	@Override
	public BeaconExternal info(Integer beaconNum) {
		return info(beaconNum.longValue());
	}

	@Override
	public BeaconExternal info(Long beaconNum) {
		BeaconExternal param = new BeaconExternal();
		param.setBeaconNum(beaconNum);

		List<BeaconExternalWidthRestrictedZone> list = beaconExternalDao.getBeaconExternalWidthRestrictedZoneList(param);
		if(list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<BeaconExternalWidthRestrictedZone> list(String UUID, String floor) {
		BeaconExternal param = new BeaconExternal();
		param.setUUID(UUID);
		param.setFloor(floor);

		List<BeaconExternalWidthRestrictedZone> list = beaconExternalDao.getBeaconExternalWidthRestrictedZoneList(param);

		return list;

	}

	public List<BeaconExternalLog> listExternalLog(Integer beaconNum) {
		return listExternalLog(beaconNum.longValue(), null);
	}

	public List<BeaconExternalLog> listExternalLog(Integer beaconNum, AssignUnassignType type) {
		return listExternalLog(beaconNum.longValue(), type);
	}

	public List<BeaconExternalLog> listExternalLog(Long beaconNum) {
		return listExternalLog(beaconNum, null);
	}

	public List<BeaconExternalLog> listExternalLog(Long beaconNum, AssignUnassignType type) {

		BeaconExternalLog param = new BeaconExternalLog();
		param.setBeaconNum(beaconNum);
		if(type!=null) {
			param.setType(type);
		}
		List<BeaconExternalLog> list = beaconExternalLogDao.listBeaconExternalLog(param);

		return list;
	}

	public List<BeaconRestrictedZoneLog> listRestrictedZoneLog(Integer beaconNum) {
		return listRestrictedZoneLog(beaconNum.longValue(), null);
	}

	public List<BeaconRestrictedZoneLog> listRestrictedZoneLog(Integer beaconNum, AssignUnassignType type) {
		return listRestrictedZoneLog(beaconNum.longValue(), type);
	}

	public List<BeaconRestrictedZoneLog> listRestrictedZoneLog(Long beaconNum) {
		return listRestrictedZoneLog(beaconNum, null);
	}

	public List<BeaconRestrictedZoneLog> listRestrictedZoneLog(Long beaconNum, AssignUnassignType type) {
		BeaconRestrictedZoneLog param = new BeaconRestrictedZoneLog();
		param.setBeaconNum(beaconNum);
		if(type!=null) {
			param.setType(type);
		}
		List<BeaconRestrictedZoneLog> list = beaconRestrictedZoneLogDao.listBeaconRestrictedZoneLog(param);

		return list;
	}

}
