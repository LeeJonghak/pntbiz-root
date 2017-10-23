package wms.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import framework.web.util.StringUtil;
import core.wms.tracking.domain.PresenceBeaconLog;
import core.wms.tracking.domain.PresenceGfLog;
import core.wms.tracking.domain.PresenceLog;
import wms.tracking.service.PresenceBeaconService;
import wms.tracking.service.PresenceService;

@Controller
public class LoggingController {

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private PresenceBeaconService presenceBeaconService;

    @RequestMapping(value="/logging/presence.do")
    public void insertPresence(PresenceLog param) throws ParseException{
        //set null value
        param.setSUUID(StringUtil.NVL(param.getSUUID(), ""));
        param.setUUID(StringUtil.NVL(param.getUUID(), ""));
        param.setMajorVer(StringUtil.NVL(param.getMajorVer()));
        param.setMinorVer(StringUtil.NVL(param.getMinorVer()));
        param.setLng(StringUtil.NVL(param.getLng()));
        param.setLat(StringUtil.NVL(param.getLat()));
        param.setFloor(StringUtil.NVL(param.getFloor()));
        param.setBeaconName(StringUtil.NVL(param.getBeaconName()));
        param.setTargetName(StringUtil.NVL(param.getTargetName()));
        param.setLogDesc(StringUtil.NVL(param.getLogDesc()));

        presenceService.insertPresenceLog(param);
    }

    @RequestMapping(value="/logging/presenceBeacon.do")
    public void insertPresenceBeaconLog(PresenceBeaconLog param) throws ParseException{
        param.setUUID(StringUtil.NVL(param.getUUID(), ""));
        param.setLng(StringUtil.NVL(param.getLng()));
        param.setLat(StringUtil.NVL(param.getLat()));
        param.setFloor(StringUtil.NVL(param.getFloor()));
        param.setDeviceInfo(StringUtil.NVL(param.getDeviceInfo()));
        param.setPhoneNumber(StringUtil.NVL(param.getPhoneNumber()));

        presenceBeaconService.insertPresenceBeaconLog(param);
    }

    @RequestMapping(value="/logging/presenceGeofence.do")
    public void insertPresenceGeofenceLog(PresenceGfLog param) throws ParseException{
        param.setSUUID(StringUtil.NVL(param.getSUUID(), ""));
        param.setUUID(StringUtil.NVL(param.getUUID(), ""));
        param.setMajorVer(StringUtil.NVL(param.getMajorVer()));
        param.setMinorVer(StringUtil.NVL(param.getMinorVer()));
        param.setFcNum(StringUtil.NVL(param.getFcNum()));
        param.setFcName(StringUtil.NVL(param.getFcName()));
        param.setFloor(StringUtil.NVL(param.getFloor()));
        param.setBeaconName(StringUtil.NVL(param.getBeaconName()));
        param.setTargetName(StringUtil.NVL(param.getTargetName()));
        param.setLogDesc(StringUtil.NVL(param.getLogDesc()));
        param.setInDate(StringUtil.NVL(param.getInDate()));
        param.setOutDate(StringUtil.NVL(param.getOutDate()));

        presenceService.insertPresenceGeofenceLog(param);
    }
}