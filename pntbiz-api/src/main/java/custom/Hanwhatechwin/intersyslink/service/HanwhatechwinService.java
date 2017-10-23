package custom.Hanwhatechwin.intersyslink.service;

import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.common.presence.bo.domain.ZoneInOutState;
import custom.Hanwhatechwin.intersyslink.domain.*;

import javax.jms.Message;
import java.util.List;

/**
 * Created by jhlee on 2017-09-21.
 */
public interface HanwhatechwinService {
    FSProtocolHeaderOuter.FSProtocolHeader setHeader(FSProtocolHeader header, FSProtocolBodyOuter.FSProtocolBody body);
    void sendAgentReboot(String destination, FSProtocolHeader header);
    void sendHeartbeat(String destination, FSProtocolHeader header);
    void sendTagValueData(String destination, FSProtocolHeader header, List<TagValueData> tagValueDataList);
    void sendTagInfoData(String destination, FSProtocolHeader header, List<TagInfoData> tagInfoDataList);
    void sendServerStatusData(String destination, FSProtocolHeader header, List<ServerStatusData> serverStatusDataList);
    void sendConnectionInfoData(String destination, FSProtocolHeader header, List<ConnectionInfoData> connectionInfoDataList);
    void sendPollingGroupInfoData(String destination, FSProtocolHeader header, List<PollingGroupInfoData> pollingGroupInfoDataList);
    void sendTagValueRequestData(String destination, FSProtocolHeader header, List<TagValueRequestData> tagValueRequestDataList);
    void sendCollenctionEquipmentInfoData(String destination, FSProtocolHeader header, List<CollectionEquipmentInfoData> collectionEquipmentInfoDataList);

    String setTagName(String barcode, String externalAttributeRaw);
    String setTagData(ScannerPresenceRequestParam requestParam, ScannerPresenceRedis currentPresenceRedis,
                      ZoneInOutState zoneInOutState, String externalAttributeRaw);
}
