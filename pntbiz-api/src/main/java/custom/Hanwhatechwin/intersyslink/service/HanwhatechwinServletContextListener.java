package custom.Hanwhatechwin.intersyslink.service;

import com.google.protobuf.ByteString;
import custom.Hanwhatechwin.intersyslink.domain.FSProtocolHeader;
import custom.Hanwhatechwin.intersyslink.domain.FSProtocolHeaderOuter;
import framework.web.util.DateUtil;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by jhlee on 2017-09-19.
 */
@WebListener
public class HanwhatechwinServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        ServletContext ctx = sce.getServletContext();
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
        HanwhatechwinService hanwhatechwinService = (HanwhatechwinService) springContext.getBean("hanwhatechwinService");
        JmsTemplate jmsTemplate = (JmsTemplate) springContext.getBean("activemqJmsTemplate");

        // reboot 신호 처리
        System.out.println("########### ServletContextListener Start ###########");
        // requestType 197 : Agent Reboot신호
        // dataType 07 : Agent Reboot
        FSProtocolHeader header = new FSProtocolHeader();
        ByteString bs = ByteString.EMPTY;
        header.setRequestType("197");
        header.setDataType("07");
        header.setRealTime(DateUtil.getDate("yyyyMMddHHmmss"));
        String date = DateUtil.getDate("yyyyMMddHHmmss");
        ByteString bodyByteString = ByteString.EMPTY;

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
        jmsTemplate.convertAndSend("pntbiz", data);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
