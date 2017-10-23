package custom.Hanwhatechwin.intersyslink.service;

import api.common.service.ActivemqService;
import com.google.protobuf.ByteString;
import custom.Hanwhatechwin.intersyslink.domain.*;
import framework.web.util.DateUtil;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ProducerId;
import org.apache.activemq.util.ByteArrayInputStream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhlee on 2017-09-19.
 */
public class HanwhatechwinActivemqServiceImpl implements ActivemqService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void sendMessage() {

    }

    @Override
    public void receiveMessage(Message message) throws JMSException, IOException {

        if(message instanceof BytesMessage) {
            BytesMessage byteMessage = (BytesMessage) message;
            byte[] bytesArray = new byte[(int)byteMessage.getBodyLength()];
            byteMessage.readBytes(bytesArray);
            ByteString byteString = ByteString.copyFrom(bytesArray);
            FSProtocolHeaderOuter.FSProtocolHeader header = FSProtocolHeaderOuter.FSProtocolHeader.parseFrom(byteString);

            switch(header.getRequestType()) {
                // Agent to InterSysLink
                case "101" :
                    break;
                case "102" :
                    System.out.println("######### Tag Value Data #########");
                    System.out.println("======= data type : " + header.getDataType() + " =======");
                    break;
                case "103" :
                    break;
                case "104" :
                    break;
                case "197" :
                    System.out.println("######### Agent Reboot #########");
                    System.out.println("======= data type : " + header.getDataType() + " =======");
                    break;
                case "198" :
                    break;
                case "199" :
                    System.out.println("######### Agent Heartbeat #########");
                    System.out.println("======= data type : " + header.getDataType() + " =======");
                    break;

                // InterSysLink to Agent
                case "201" :
                    break;
                case "202" :
                    break;
                case "203" :
                    System.out.println("######### InterSysLink Set Tag #########");
                    System.out.println("======= data type : " + header.getDataType() + " =======");
                    FSProtocolBodyOuter.FSProtocolBody body = FSProtocolBodyOuter.FSProtocolBody.parseFrom(header.getFsProtocolBodyBytes());
                    int size = body.getTagValueDataCount();
                    List<FSProtocolBodyOuter.FSProtocolBody.TagValueData> list = new ArrayList<FSProtocolBodyOuter.FSProtocolBody.TagValueData>();
                    System.out.println("======= TagValue Size : " + size+ " =======");
                    for(int i=0; i < size; i++) {
                        FSProtocolBodyOuter.FSProtocolBody.TagValueData tagValueData = body.getTagValueData(i);
                        list.add(i, tagValueData);
                        String tagValue = tagValueData.getTagValue();
                        System.out.println("======= TagValue : (" + i+ ") " + tagValue + "=======");
                    }
                    break;
                case "204" :
                    break;
                case "205" :
                    break;

                default :
                    break;
            }
            System.out.println(header);
        }
        if (message instanceof ObjectMessage) {
            ActiveMQObjectMessage msg = (ActiveMQObjectMessage) message;
            FSProtocolHeaderOuter.FSProtocolHeader data = null;
            try {
                data = (FSProtocolHeaderOuter.FSProtocolHeader) msg.getObject();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            FSProtocolHeader header = new FSProtocolHeader();
            BeanUtils.copyProperties(data, header);

            switch(header.getRequestType()) {
                // Agent to InterSysLink
                case "101" :
                    break;
                case "102" :
                    System.out.println("######### Tag Value Data #########");
                    System.out.println("======= data type : " + header.getDataType() + " =======");
                    break;
                case "103" :
                    break;
                case "104" :
                    break;
                case "197" :
                    System.out.println("######### Agent Reboot #########");
                    System.out.println("======= data type : " + header.getDataType() + " =======");
                    break;
                case "198" :
                    break;
                case "199" :
                    System.out.println("######### Agent Heartbeat #########");
                    System.out.println("======= data type : " + header.getDataType() + " =======");
                    break;

                // InterSysLink to Agent
                case "201" :
                    break;
                case "202" :
                    break;
                case "203" :
                    System.out.println("######### InterSysLink Set Tag #########");
                    System.out.println("======= data type : " + header.getDataType() + " =======");
                    FSProtocolBodyOuter.FSProtocolBody body = FSProtocolBodyOuter.FSProtocolBody.parseFrom(header.getFsProtocolBodyBytes());
                    int size = body.getTagValueDataCount();
                    List<FSProtocolBodyOuter.FSProtocolBody.TagValueData> list = new ArrayList<FSProtocolBodyOuter.FSProtocolBody.TagValueData>();
                    System.out.println("======= TagValue Size : " + size+ " =======");
                    for(int i=0; i < size; i++) {
                        FSProtocolBodyOuter.FSProtocolBody.TagValueData tagValueData = body.getTagValueData(i);
                        list.add(i, tagValueData);
                        String tagValue = tagValueData.getTagValue();
                        System.out.println("======= TagValue : (" + i+ ") " + tagValue + "=======");
                    }
                    break;
                case "204" :
                    break;
                case "205" :
                    break;

                default :
                    break;
            }
            msg.acknowledge();
            System.out.println(header);
        }
    }
}
