package custom.Hanwhatechwin.presence.bo.handler;

import api.log.service.LogService;
import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import api.presence.bo.handler.PresenceTaskEventHandler;
import api.presence.bo.service.PresenceServiceDelegator;
import api.presence.bo.service.RestApiRequestService;
import api.presence.bo.service.SocketRequestService;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import core.common.beacon.domain.BeaconExternalWidthRestrictedZone;
import core.common.enums.PresenceDataType;
import core.common.enums.SocketCommandType;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.common.presence.bo.domain.ZoneInOutState;
import custom.Hanwhatechwin.intersyslink.domain.FSProtocolHeader;
import custom.Hanwhatechwin.intersyslink.domain.TagValueData;
import custom.Hanwhatechwin.intersyslink.service.HanwhatechwinService;
import framework.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlee on 2017-09-25.
 */
//@Component
public class HanwhatechwinBeaconSendLocationPostHandler implements PresenceTaskEventHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestApiRequestService restApiRequestService;
    private SocketRequestService socketRequestService;
    private PresenceDataPackage presenceVo;
    private LogService logService;
    private Gson gson = new Gson();
    private HanwhatechwinService hanwhatechwinService;
//    private JmsTemplate jmsTemplate;

    public HanwhatechwinBeaconSendLocationPostHandler(HanwhatechwinService hanwhatechwinService) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//        ServletContext conext = session.getServletContext();
//        WebApplicationContext wContext = WebApplicationContextUtils.getWebApplicationContext(conext);
//        jmsTemplate = (JmsTemplate)wContext.getBean("activemqJmsTemplate");
        this.hanwhatechwinService  = hanwhatechwinService;
    }

    @Override
    public void doEvent(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        this.presenceVo = presenceVo;
        socketRequestService = serviceDelegator.getSocketRequestService();
        ScannerPresenceRequestParam requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        ScannerPresenceRedis currentPresenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);
        Map<String, ZoneInOutState> geofencesInOutStates = currentPresenceRedis.getGeofencesInOutState();
        sendTagValueData(requestParam, currentPresenceRedis, geofencesInOutStates);
    }

//    private void sendTagValueData(ZoneInOutState zoneInOutState) {
private void sendTagValueData(ScannerPresenceRequestParam requestParam, ScannerPresenceRedis currentPresenceRedis, Map<String, ZoneInOutState> geofencesInOutStates) {
        socketRequestService.send(SocketCommandType.UPDATE_MARKER, presenceVo);
        String time = DateUtil.getDate("yyyyMMddHHmmss");
        String serverId = "ServerId";
        // 비콘 태그명 규칙
        BeaconExternalWidthRestrictedZone beaconExternal = (BeaconExternalWidthRestrictedZone)presenceVo.getTypeData(PresenceDataType.BEACON_EXTERTNAL);
        String barcode = beaconExternal.getBarcode();
        String tagName = hanwhatechwinService.setTagName(barcode, beaconExternal.getExternalAttributeRaw());
        String tagData = "";
        // Zone 정보
        for (String key: geofencesInOutStates.keySet()) {
            // 중복펜스가 없다는 가정하에 1개만으로 처리
            ZoneInOutState zoneInOutState = geofencesInOutStates.get(key);
            tagData = hanwhatechwinService.setTagData(requestParam, currentPresenceRedis, zoneInOutState, beaconExternal.getExternalAttributeRaw());
            break;
        }
        String tagValue = tagName + "=" + tagData;
        String tagId = serverId + "#" + tagName;

//        String tagGroup
        // InterSysLink 태그값 전송
        // requestType 102 : Tag Value Data
        // dataType 02 : Tag Value Data
        FSProtocolHeader header = new FSProtocolHeader();
        ByteString bs = ByteString.EMPTY;
        header.setRequestType("102");
        header.setDataType("02");
        header.setRealTime(time);

        List<TagValueData> list = new ArrayList<TagValueData>();
        TagValueData tagValueData = new TagValueData();
        tagValueData.setTagId(tagId);
        tagValueData.setTagName(tagName);
        tagValueData.setTagValue(tagValue);
        tagValueData.setChangeTagValue("");
        tagValueData.setServerId(serverId);
        tagValueData.setReadTime(time);
        tagValueData.setCategoryCode1("");
        tagValueData.setQuality("");
        list.add(tagValueData);

        hanwhatechwinService.sendTagValueData("pntbiz", header, list);
    }
}
