package custom.Hanwhatechwin.intersyslink.service;

import api.presence.bo.domain.ScannerPresenceRequestParam;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import core.common.enums.ZoneInOutStateType;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.common.presence.bo.domain.ZoneInOutState;
import custom.Hanwhatechwin.intersyslink.domain.*;
import framework.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlee on 2017-09-21.
 */
@Service
public class HanwhatechwinServiceImpl implements HanwhatechwinService {

    private Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(getClass());

    private JmsTemplate jmsTemplate;

    private BytesMessage bytesMessage;

    public HanwhatechwinServiceImpl(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public FSProtocolHeaderOuter.FSProtocolHeader setHeader(FSProtocolHeader header, FSProtocolBodyOuter.FSProtocolBody body) {
        String date = DateUtil.getDate("yyyyMMddHHmmss");
        ByteString bodyByteString = ByteString.EMPTY;
        if(body != null) bodyByteString = body.toByteString();
//        ByteString bs = ByteString.EMPTY;
        // require field
        // requestType, dataType, encryptMode, compressMode, agentType, sourceId,
        // targetId, realTime, heartbeat, requestId, fsProtocolBodyBytes
        FSProtocolHeaderOuter.FSProtocolHeader data = FSProtocolHeaderOuter.FSProtocolHeader.newBuilder()
                .setRequestType(header.getRequestType())
                .setDataType(header.getDataType())
                .setEncryptMode(header.getEncryptMode())
                .setCompressMode(header.getCompressMode())
                .setAgentType(header.getAgentType())
                .setSourceId(header.getSourceId())
                .setTargetId(header.getTargetId())
                .setRealTime(date)
                .setHeartbeat(header.getHeartbeat())
                .setRequestId(header.getRequestId())
                .setFsProtocolBodyBytes(bodyByteString)
                .build();
        return data;
    }

    @Override
    public void sendAgentReboot(final String destination, FSProtocolHeader header) {
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, null);
        final byte[] messageBytes = data.toByteArray();
//        jmsTemplate.convertAndSend(destination, data);
        System.out.println("destination : " + jmsTemplate.getDefaultDestinationName());
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                bytesMessage = session.createBytesMessage ();
                bytesMessage.writeBytes(messageBytes);
                return bytesMessage;
            }
        });
    }

    @Override
    public void sendHeartbeat(String destination, FSProtocolHeader header) {
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, null);
        jmsTemplate.convertAndSend(destination, data);
    }

    @Override
    public void sendTagValueData(String destination, FSProtocolHeader header, List<TagValueData> tagValueDataList) {
        FSProtocolBodyOuter.FSProtocolBody body = setTagValueBody(tagValueDataList);
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, body);
        jmsTemplate.convertAndSend(destination, data);
    }

    @Override
    public void sendTagInfoData(String destination, FSProtocolHeader header, List<TagInfoData> tagInfoDataList) {
        FSProtocolBodyOuter.FSProtocolBody body = setTagInfoDataBody(tagInfoDataList);
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, body);
        jmsTemplate.convertAndSend(destination, data);
    }

    @Override
    public void sendServerStatusData(String destination, FSProtocolHeader header, List<ServerStatusData> serverStatusDataList) {
        FSProtocolBodyOuter.FSProtocolBody body = setServerStatusDataBody(serverStatusDataList);
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, body);
        jmsTemplate.convertAndSend(destination, data);
    }

    @Override
    public void sendConnectionInfoData(String destination, FSProtocolHeader header, List<ConnectionInfoData> connectionInfoDataList) {
        FSProtocolBodyOuter.FSProtocolBody body = setConnectionInfoDataBody(connectionInfoDataList);
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, body);
        jmsTemplate.convertAndSend(destination, data);
    }

    @Override
    public void sendPollingGroupInfoData(String destination, FSProtocolHeader header, List<PollingGroupInfoData> pollingGroupInfoDataList) {
        FSProtocolBodyOuter.FSProtocolBody body = setPollingGroupInfoDataBody(pollingGroupInfoDataList);
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, body);
        jmsTemplate.convertAndSend(destination, data);
    }

    @Override
    public void sendTagValueRequestData(String destination, FSProtocolHeader header, List<TagValueRequestData> tagValueRequestDataList) {
        FSProtocolBodyOuter.FSProtocolBody body = setTagValueRequestDataBody(tagValueRequestDataList);
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, body);
        jmsTemplate.convertAndSend(destination, data);
    }

    @Override
    public void sendCollenctionEquipmentInfoData(String destination, FSProtocolHeader header, List<CollectionEquipmentInfoData> collectionEquipmentInfoDataList) {
        FSProtocolBodyOuter.FSProtocolBody body = setCollectionEquipmentInfoDataBody(collectionEquipmentInfoDataList);
        FSProtocolHeaderOuter.FSProtocolHeader data = setHeader(header, body);
        jmsTemplate.convertAndSend(destination, data);
    }

    @Override
    public String setTagName(String barcode, String externalAttributeRaw) {
        String tagName = "";
        String ioName = "";
        String tagGroupCode = "";
        String param = "";
        String delimeter = "_";
        List<Map<String, Object>> extAttrList = gson.fromJson(externalAttributeRaw, List.class);
        for(Map<String, Object> extAttr : extAttrList) {
            if(extAttr.get("key").equals("tagGroupCode")) {
                tagGroupCode = (String)extAttr.get("value");
            }
            if(extAttr.get("key").equals("param")) {
                param = (String)extAttr.get("value");
            }
        }
        if(barcode.equals("")) logger.info("Barcode is blank...");
        if(tagGroupCode.equals("")) logger.info("TagGroupCode is blank...");
        ioName = barcode + delimeter + tagGroupCode;
        tagName = ioName + delimeter + param;
        return tagName;
    }

    @Override
    public String setTagData(ScannerPresenceRequestParam requestParam, ScannerPresenceRedis currentPresenceRedis,
                             ZoneInOutState zoneInOutState, String externalAttributeRaw) {
        /**
         * 1. 오더 번호 : BeaconTag와 매핑된 오더번호
         * 2. 존 아이디 : BeaconTag의 존(지오펜스)아이디를 나타내는 정보
         * 3. 존 In/Out 상태 : BeaconTag의 존(지오펜스) In/Out 상태를 나타내는 정보
         * 4. 존 In 일시 : BeaconTag의 존(지오펜스) In 일시를 나타내는 정보
         * 5. 존 Out 일시 : BeaconTag의 존(지오펜스) Out 일시를 나타내는  정보
         * 6. 위도 : BeaconTag의 위도를 나타내는 정보
         * 7. 경도 : BeaconTag의 경도를 나타내는 정보
         * 8. 배터리 레벨 : BeaconTag의 배터리 레벨을 나타내는 정보
         *
         * e.g. 0000004452^12^IN^20170914162100^20170914163100^12.23^13.23^99
         */
        String tagData = "";
        String delimiter = "^";
        String orderNo = "";
        List<Map<String, Object>> extAttrList = gson.fromJson(externalAttributeRaw, List.class);
        for (Map<String, Object> extAttr : extAttrList) {
            if (extAttr.get("key").equals("orderNo")) {
                orderNo = (String) extAttr.get("value");
            }
        }
        String zoneId = zoneInOutState.getZoneId();
        // 테크윈이 요구하는 정책에 따라 In 조건을 변경
        ZoneInOutStateType zoneInOutStateType = zoneInOutState.getState();
        String inoutState = "";
        switch (zoneInOutStateType) {
//            case IN_PROCESSING:
            case IN:
            case STAY_PROCESSING:
            case STAY:
            case STAYING:
            case OUT_PROCESSING:
                inoutState = "IN";
                break;
//            case OUT :
//            case DONE :
            default :
                inoutState = "OUT";
                break;
        }
        // 존 In 일시 (정책에 따라 inTime, stayTime을 사용할 지 정함)
        String inTime = DateUtil.timestamp2str(zoneInOutState.getInTime(), "yyyyMMddHHmmss");
//        String inTime = zoneInOutState.getStayTime();
        String outTime = DateUtil.timestamp2str(zoneInOutState.getOutTime(), "yyyyMMddHHmmss");
        String lat = currentPresenceRedis.getLat().toString();
        String lng = currentPresenceRedis.getLng().toString();
        String battery = requestParam.getBattery().toString();
        tagData = orderNo + delimiter
                + zoneId + delimiter
                + inTime + delimiter
                + outTime + delimiter
                + lat + delimiter
                + lng + delimiter
                + battery;
        return tagData;
    }

    private FSProtocolBodyOuter.FSProtocolBody setTagValueBody(List<TagValueData> tagValueList) {
        FSProtocolBodyOuter.FSProtocolBody.Builder builder =  FSProtocolBodyOuter.FSProtocolBody.newBuilder();
        FSProtocolBodyOuter.FSProtocolBody.TagValueData.Builder tagBuilder = FSProtocolBodyOuter.FSProtocolBody.TagValueData.newBuilder();
        for(TagValueData tagValue : tagValueList) {
            FSProtocolBodyOuter.FSProtocolBody.TagValueData body = setTagValueData(tagValue).build();
            builder.addTagValueData(body);
        }
        return builder.build();
    }

    private FSProtocolBodyOuter.FSProtocolBody setTagInfoDataBody(List<TagInfoData> tagInfoDataList) {
        FSProtocolBodyOuter.FSProtocolBody.Builder builder =  FSProtocolBodyOuter.FSProtocolBody.newBuilder();
        FSProtocolBodyOuter.FSProtocolBody.TagInfoData.Builder tagBuilder = FSProtocolBodyOuter.FSProtocolBody.TagInfoData.newBuilder();
        for(TagInfoData tagInfo : tagInfoDataList) {
            FSProtocolBodyOuter.FSProtocolBody.TagInfoData body = setTagInfoData(tagInfo).build();
            builder.addTagInfoData(body);
        }
        return builder.build();
    }

    private FSProtocolBodyOuter.FSProtocolBody setServerStatusDataBody(List<ServerStatusData> statusDataList) {
        FSProtocolBodyOuter.FSProtocolBody.Builder builder =  FSProtocolBodyOuter.FSProtocolBody.newBuilder();
        FSProtocolBodyOuter.FSProtocolBody.ServerStatusData.Builder tagBuilder = FSProtocolBodyOuter.FSProtocolBody.ServerStatusData.newBuilder();
        for(ServerStatusData serverStatusData : statusDataList) {
            FSProtocolBodyOuter.FSProtocolBody.ServerStatusData body = setServerStatusData(serverStatusData).build();
            builder.addServerStatusData(body);
        }
        return builder.build();
    }

    private FSProtocolBodyOuter.FSProtocolBody setPollingGroupInfoDataBody(List<PollingGroupInfoData> pollingGroupInfoDataList) {
        FSProtocolBodyOuter.FSProtocolBody.Builder builder =  FSProtocolBodyOuter.FSProtocolBody.newBuilder();
        FSProtocolBodyOuter.FSProtocolBody.PollingGroupInfoData.Builder tagBuilder = FSProtocolBodyOuter.FSProtocolBody.PollingGroupInfoData.newBuilder();
        for(PollingGroupInfoData pollingGroupInfoData : pollingGroupInfoDataList) {
            FSProtocolBodyOuter.FSProtocolBody.PollingGroupInfoData body = setPollingGroupInfoData(pollingGroupInfoData).build();
            builder.addPollingGroupInfoData(body);
        }
        return builder.build();
    }

    private FSProtocolBodyOuter.FSProtocolBody setConnectionInfoDataBody(List<ConnectionInfoData> connectionInfoDataList) {
        FSProtocolBodyOuter.FSProtocolBody.Builder builder =  FSProtocolBodyOuter.FSProtocolBody.newBuilder();
        FSProtocolBodyOuter.FSProtocolBody.ConnectionInfoData.Builder tagBuilder = FSProtocolBodyOuter.FSProtocolBody.ConnectionInfoData.newBuilder();
        for(ConnectionInfoData connectionInfoData : connectionInfoDataList) {
            FSProtocolBodyOuter.FSProtocolBody.ConnectionInfoData body = setConnectionInfoData(connectionInfoData).build();
            builder.addConnectionInfoData(body);
        }
        return builder.build();
    }

    private FSProtocolBodyOuter.FSProtocolBody setTagValueRequestDataBody(List<TagValueRequestData> tagValueRequestDataList) {
        FSProtocolBodyOuter.FSProtocolBody.Builder builder =  FSProtocolBodyOuter.FSProtocolBody.newBuilder();
        FSProtocolBodyOuter.FSProtocolBody.TagValueRequestData.Builder tagBuilder = FSProtocolBodyOuter.FSProtocolBody.TagValueRequestData.newBuilder();
        for(TagValueRequestData tagValueRequestData : tagValueRequestDataList) {
            FSProtocolBodyOuter.FSProtocolBody.TagValueRequestData body = setTagValueRequestData(tagValueRequestData).build();
            builder.addTagValueRequestData(body);
        }
        return builder.build();
    }

    private FSProtocolBodyOuter.FSProtocolBody setCollectionEquipmentInfoDataBody(List<CollectionEquipmentInfoData> collectionEquipmentInfoDataList) {
        FSProtocolBodyOuter.FSProtocolBody.Builder builder =  FSProtocolBodyOuter.FSProtocolBody.newBuilder();
        FSProtocolBodyOuter.FSProtocolBody.CltEqmtInfoData.Builder tagBuilder = FSProtocolBodyOuter.FSProtocolBody.CltEqmtInfoData.newBuilder();
        for(CollectionEquipmentInfoData collectionEquipmentInfoData : collectionEquipmentInfoDataList) {
            FSProtocolBodyOuter.FSProtocolBody.CltEqmtInfoData body = setCollectionEquipmentInfoData(collectionEquipmentInfoData).build();
            builder.addCltEqmtInfoData(body);
        }
        return builder.build();
    }

    private FSProtocolBodyOuter.FSProtocolBody.TagValueData.Builder setTagValueData(TagValueData tagValue) {
        FSProtocolBodyOuter.FSProtocolBody.TagValueData.Builder builder = FSProtocolBodyOuter.FSProtocolBody.TagValueData.newBuilder()
                .setTagId(tagValue.getTagId())
                .setTagName(tagValue.getTagName())
                .setTagValue(tagValue.getTagValue())
                .setChangeTagValue(tagValue.getChangeTagValue())
                .setServerId(tagValue.getServerId())
                .setReadTime(tagValue.getReadTime())
                .setCategoryCode1(tagValue.getCategoryCode1())
                .setQuality(tagValue.getQuality());
        return builder;
    }

    private FSProtocolBodyOuter.FSProtocolBody.TagInfoData.Builder setTagInfoData(TagInfoData tagInfo) {
        FSProtocolBodyOuter.FSProtocolBody.TagInfoData.Builder builder = FSProtocolBodyOuter.FSProtocolBody.TagInfoData.newBuilder()
                .setTagId(tagInfo.getTagId())
                .setIoName(tagInfo.getIoName())
                .setParamName(tagInfo.getParamName())
                .setTagName(tagInfo.getTagName())
                .setMntrYN(tagInfo.getMntrYN())
                .setTagType(tagInfo.getTagType())
                .setTagAttribute(tagInfo.getTagAttribute())
                .setTagValueType(tagInfo.getTagValueType())
                .setDescription(tagInfo.getDescription())
                .setCltEqmtId(tagInfo.getCltEqmtId())
                .setAid(tagInfo.getAid())
                .setDescrip(tagInfo.getDescrip())
                .setConnectionId(tagInfo.getConnectionId())
                .setPollingGroupId(tagInfo.getPollingGroupId())
                .setModbusSlaveId(tagInfo.getModbusSlaveId())
                .setModbusStartAddr(tagInfo.getModbusStartAddr())
                .setModbusIoType(tagInfo.getModbusIoType())
                .setModbusModuloType(tagInfo.getModbusIoType())
                .setOpcElement(tagInfo.getOpcElement())
                .setFilePath(tagInfo.getFilePath())
                .setCountryCode(tagInfo.getCountryCode())
                .setRegionCode(tagInfo.getRegionCode())
                .setCategoryCode1(tagInfo.getCategoryCode1())
                .setCategoryCode2(tagInfo.getCategoryCode2())
                .setCategoryCode3(tagInfo.getCategoryCode3())
                .setCategoryCode4(tagInfo.getCategoryCode4())
                .setDataBlockNum(tagInfo.getDataBlockNum())
                .setDataBlockAddr(tagInfo.getDataBlockAddr())
                .setNameSpaceIndex(tagInfo.getNameSpaceIndex())
                .setIdentifier(tagInfo.getIdentifier())
                .setStartAddr(tagInfo.getStartAddr())
                .setTextLength(tagInfo.getTextLength())
                .setDeviceType(tagInfo.getDeviceType())
                .setTableName(tagInfo.getTableName())
                .setRawDataId(tagInfo.getRawDataId())
                .setIdColumn(tagInfo.getIdColumn())
                .setValueColumn(tagInfo.getValueColumn())
                .setCheckColumn(tagInfo.getCheckColumn())
                .setDbAction(tagInfo.getDbAction());
        return builder;
    }

    private FSProtocolBodyOuter.FSProtocolBody.ServerStatusData.Builder setServerStatusData(ServerStatusData serverStatusData) {
        FSProtocolBodyOuter.FSProtocolBody.ServerStatusData.Builder builder = FSProtocolBodyOuter.FSProtocolBody.ServerStatusData.newBuilder()
                .setServiceName(serverStatusData.getServiceName())
                .setPid(serverStatusData.getPid())
                .setCpu(serverStatusData.getCpu())
                .setMemory(serverStatusData.getMemory())
                .setProgramPath(serverStatusData.getProgramPath())
                .setArgument(serverStatusData.getArgument())
                .setReadTime(serverStatusData.getReadTime());
        return builder;
    }

    private FSProtocolBodyOuter.FSProtocolBody.PollingGroupInfoData.Builder setPollingGroupInfoData(PollingGroupInfoData pollingGroupInfoData) {
        FSProtocolBodyOuter.FSProtocolBody.PollingGroupInfoData.Builder builder = FSProtocolBodyOuter.FSProtocolBody.PollingGroupInfoData.newBuilder()
                .setPollingGroupId(pollingGroupInfoData.getPollingGroupId())
                .setCycle(pollingGroupInfoData.getCycle())
                .setSleepTime(pollingGroupInfoData.getSleepTime())
                .setTagPerThread(pollingGroupInfoData.getTagPerThread())
                .setAction(pollingGroupInfoData.getAction());
        return builder;
    }

    private FSProtocolBodyOuter.FSProtocolBody.ConnectionInfoData.Builder setConnectionInfoData(ConnectionInfoData connectionInfoData) {
        FSProtocolBodyOuter.FSProtocolBody.ConnectionInfoData.Builder builder = FSProtocolBodyOuter.FSProtocolBody.ConnectionInfoData.newBuilder()
                .setConnectionId(connectionInfoData.getConnectionId())
                .setConnectionName(connectionInfoData.getConnectionName())
                .setIp(connectionInfoData.getIp())
                .setPort(connectionInfoData.getPort())
                .setTimeout(connectionInfoData.getTimeout())
                .setEndianValue(connectionInfoData.getEndianValue())
                .setAction(connectionInfoData.getAction())
                .setSubUrl(connectionInfoData.getSubUrl())
                .setLogicalStationNumber(connectionInfoData.getLogicalStationNumber())
                .setRackNum(connectionInfoData.getRackNum())
                .setSlotNum(connectionInfoData.getSlotNum())
                .setDbName(connectionInfoData.getDbName())
                .setId(connectionInfoData.getId());
        return builder;
    }

    private FSProtocolBodyOuter.FSProtocolBody.TagValueRequestData.Builder setTagValueRequestData(TagValueRequestData tagValueRequestData) {
        FSProtocolBodyOuter.FSProtocolBody.TagValueRequestData.Builder builder = FSProtocolBodyOuter.FSProtocolBody.TagValueRequestData.newBuilder()
                .setAgentId(tagValueRequestData.getAgentId())
                .setIoName(tagValueRequestData.getIoName());
        return builder;
    }

    private FSProtocolBodyOuter.FSProtocolBody.CltEqmtInfoData.Builder setCollectionEquipmentInfoData(CollectionEquipmentInfoData collectionEquipmentInfoData) {
        FSProtocolBodyOuter.FSProtocolBody.CltEqmtInfoData.Builder builder = FSProtocolBodyOuter.FSProtocolBody.CltEqmtInfoData.newBuilder()
                .setCltEqmtId(collectionEquipmentInfoData.getCltEqmtId())
                .setCltEqmtNm(collectionEquipmentInfoData.getCltEqmtNm())
                .setCltEqmtHich(collectionEquipmentInfoData.getCltEqmtHich())
                .setCltAddrRange(collectionEquipmentInfoData.getCltAddrRange())
                .setCltEqmtIp(collectionEquipmentInfoData.getCltEqmtIp())
                .setParntsCltEqmtId(collectionEquipmentInfoData.getParntsCltEqmtId())
                .setSortNum(collectionEquipmentInfoData.getSortNum())
                .setDescript(collectionEquipmentInfoData.getDescript());
        return builder;
    }
}
