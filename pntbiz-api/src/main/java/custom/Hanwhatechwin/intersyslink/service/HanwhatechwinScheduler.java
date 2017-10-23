package custom.Hanwhatechwin.intersyslink.service;

import com.google.protobuf.ByteString;
import custom.Hanwhatechwin.intersyslink.domain.FSProtocolHeader;
import framework.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jhlee on 2017-09-20.
 */
@Component("HanwhatechwinScheduler")
public class HanwhatechwinScheduler {

    @Autowired
    HanwhatechwinService hanwhatechwinService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void sendHeartbeat() throws Exception {
        System.out.println("##################### send heartbeat start ##########################");
        // requestType 199 : Agent heartbeat신호
        // dataType 09 : Agent Heartbeat
        FSProtocolHeader header = new FSProtocolHeader();
        ByteString bs = ByteString.EMPTY;
        header.setRequestType("199");
        header.setDataType("09");
        header.setHeartbeat("1");
        header.setRealTime(DateUtil.getDate("yyyyMMddHHmmss"));
        hanwhatechwinService.sendAgentReboot("pntbiz", header);
    }

}
