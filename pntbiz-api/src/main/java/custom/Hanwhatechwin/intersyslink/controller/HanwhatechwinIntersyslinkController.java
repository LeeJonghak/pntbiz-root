package custom.Hanwhatechwin.intersyslink.controller;

import com.google.protobuf.ByteString;
import core.api.beacon.domain.Beacon;
import custom.Hanwhatechwin.intersyslink.domain.FSProtocolHeader;
import custom.Hanwhatechwin.intersyslink.domain.FSProtocolHeaderOuter;
import custom.Hanwhatechwin.intersyslink.domain.TagValueData;
import custom.Hanwhatechwin.intersyslink.service.HanwhatechwinService;
import framework.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.jms.Message;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhlee on 2017-09-19.
 */

@Controller
public class HanwhatechwinIntersyslinkController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private HanwhatechwinService hanwhatechwinService;

    // 테스트
    @RequestMapping(value="/intersyslink/proto", method=RequestMethod.GET)
    @ResponseBody
    public Object sendProto(HttpServletRequest request) throws IOException, ServletException {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("result", "success");
        res.put("code", "0");
        res.put("data", "custom proto");
        return res;
    }

    // amq 테스트
    @RequestMapping(value="/intersyslink//proto2", method=RequestMethod.GET)
    @ResponseBody
    public Object sendProto2(HttpServletRequest request) throws IOException, ServletException {
        Map<String, Object> res = new HashMap<String, Object>();
        final Beacon beacon = new Beacon();

        String date = DateUtil.getDate("yyyyMMddHHmmss");
        String str = "TestData";
        byte[] byteArray = str.getBytes();
        ByteString bs = ByteString.EMPTY;

        // require field
        // requestType, dataType, encryptMode, compressMode, agentType, sourceId, targetId, realTime, heartbeat, requestId, fsProtocolBodyBytes
        FSProtocolHeaderOuter.FSProtocolHeader data = FSProtocolHeaderOuter.FSProtocolHeader.newBuilder()
                .setRequestType("0")
                .setDataType("01")
                .setEncryptMode("0")
                .setCompressMode("0")
                .setAgentType("I")
                .setSourceId("sourceId")
                .setTargetId("targetId")
                .setRealTime(date)
                .setHeartbeat("10")
                .setRequestId("0")
                .setFsProtocolBodyBytes(bs)
                .build();
        jmsTemplate.convertAndSend("pntbiz", data);
        res.put("result", "success");
        res.put("data", beacon);
        res.put("code", "0");
        return res;
    }

    @RequestMapping(value="/intersyslink/agent/reboot", method=RequestMethod.GET)
    @ResponseBody
    public Object sendAgentReboot(HttpServletRequest request) throws IOException, ServletException {
        Map<String, Object> res = new HashMap<String, Object>();
        // requestType 197 : Agent Reboot신호
        // dataType 07 : Agent Reboot
        FSProtocolHeader header = new FSProtocolHeader();
        ByteString bs = ByteString.EMPTY;
        header.setRequestType("197");
        header.setDataType("07");
        header.setRealTime(DateUtil.getDate("yyyyMMddHHmmss"));
        hanwhatechwinService.sendAgentReboot("pntbiz", header);
        res.put("result", "success");
        res.put("code", "0");
        return res;
    }

    @RequestMapping(value="/intersyslink/agent/reboot2", method=RequestMethod.GET)
    @ResponseBody
    public Object sendAgentReboot2(HttpServletRequest request) throws IOException, ServletException {
        Map<String, Object> res = new HashMap<String, Object>();
        // requestType 197 : Agent Reboot신호
        // dataType 07 : Agent Reboot
        FSProtocolHeader header = new FSProtocolHeader();
        ByteString bs = ByteString.EMPTY;
        header.setRequestType("197");
        header.setDataType("07");
        header.setRealTime(DateUtil.getDate("yyyyMMddHHmmss"));
        hanwhatechwinService.sendAgentReboot("pntbiz_topic", header);
        res.put("result", "success");
        res.put("code", "0");
        return res;
    }

    @RequestMapping(value="/intersyslink/tag/set", method=RequestMethod.GET)
    @ResponseBody
    public Object setTag(HttpServletRequest request) throws IOException, ServletException {
        Map<String, Object> res = new HashMap<String, Object>();
        String time = DateUtil.getDate("yyyyMMddHHmmss");
        // requestType 203 : Tag Set Data Send
        // dataType 03 : Agent Reboot
        FSProtocolHeader header = new FSProtocolHeader();
        ByteString bs = ByteString.EMPTY;
        header.setRequestType("203");
        header.setDataType("03");
        header.setRealTime(time);

        List<TagValueData> list = new ArrayList<TagValueData>();

        String serverId = "BeaconAgent";

        String tagName = "BT0000000001_B_LCINFO";
        String tagData = "0000004452^12^IN^20170914162100^20170914163100^12.23^13.23^99";
        String tagValue = tagName + "=" + tagData;
        String tagId = serverId + "#" + tagName;
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

        String tagName2 = "BT0000000002_B_LCINFO";
        String tagData2 = "0000005563^12^IN^20170914162100^20170914163100^12.23^13.23^99";
        String tagValue2 = tagName2 + "=" + tagData2;
        String tagId2 = serverId + "#" + tagName2;
        TagValueData tagValueData2 = new TagValueData();
        tagValueData2.setTagId(tagId2);
        tagValueData2.setTagName(tagName2);
        tagValueData2.setTagValue(tagValue2);
        tagValueData2.setChangeTagValue("");
        tagValueData2.setServerId(serverId);
        tagValueData2.setReadTime(time);
        tagValueData2.setCategoryCode1("");
        tagValueData2.setQuality("");
        list.add(tagValueData2);

        hanwhatechwinService.sendTagValueData("pntbiz", header, list);

        res.put("result", "success");
        res.put("code", "0");
        return res;
    }
}
