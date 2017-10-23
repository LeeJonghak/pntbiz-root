package api.presence.bo.external;

import api.log.service.LogService;
import core.api.log.domain.InterfaceLog;
import core.common.config.domain.InterfaceConfig;
import framework.web.http.RequestClient;
import framework.web.http.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * 외부 RestAPI 연동 공통 모듈
 *
 * Created by ucjung on 2017-06-11.
 */
@Component
public class RestApiInterfaceImpl implements ExternalInterface {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogService logService;

    @Autowired
    private RequestClient requestClient;

    @Override
    public Object request(Map<String, Object> requestParam, InterfaceConfig config) {
        return callRestApi(requestParam, config);
    }

    @Override
    @Async
    public Future<Object> requestAsync(Map<String, Object> requestParam, InterfaceConfig config) {
        return new AsyncResult<Object>(callRestApi(requestParam, config));
    }

    private Map<String, String>  callRestApi(Map<String, Object> requestParam, InterfaceConfig config) {

        RequestData requestData = new RequestData();
        requestData.setProtocol(config.getTargetInfoItem("protocol"));
        requestData.setHost(config.getTargetInfoItem("host"));
        if (config.getTargetInfoItem("port") != null)
            requestData.setPort(Integer.parseInt(config.getTargetInfoItem("port")));
        requestData.setPath(config.getTargetInfoItem("uri"));
        if (config.getTargetInfoItem("method") != null)
            requestData.setMethod(HttpMethod.resolve(config.getTargetInfoItem("method")));
        else
            requestData.setMethod(HttpMethod.POST);

        requestData.setHeaders(config.getHeadersMap());
        requestData.setBodies(requestParam);

        Map<String, String> response = requestApi(requestData);

        writeInterfaceLog(config, response);

        return response;
    }

    private Map<String, String> requestApi(RequestData requestData) {
        return requestClient.request(requestData);
    }

    private void writeInterfaceLog(InterfaceConfig config, Map<String, String> response) {

        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setComNum(config.getComNum());
        interfaceLog.setInterfaceNum(config.getInterfaceNum());
        interfaceLog.setTarget(response.get("target"));
        interfaceLog.setRequestMessage(response.get("requestMessage"));
        interfaceLog.setResponseCode(response.get("responseCode"));
        interfaceLog.setResponseMessage(response.get("responseMessage"));

        logService.registerInterfaceLog(interfaceLog);
    }

}

