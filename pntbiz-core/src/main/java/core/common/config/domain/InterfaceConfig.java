package core.common.config.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.common.enums.InterfaceBindingType;
import core.common.enums.InterfaceCommandType;
import framework.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 연동정보 설정을 위한 VO
 *
 * * 회사별로 설정
 *
 *
 * Created by ucjung on 2017-08-28.
 */
public class InterfaceConfig {
    private Long interfaceNum;
    private Integer comNum;
    private InterfaceBindingType interfaceBindingType;  // 연동 바인딩 유형
    private String bindingZoneId;                       // 연동 바인딩 Zone ID
    private String bindingZoneName;
    private InterfaceCommandType interfaceCommandType;  // 연동 이벤트 타입 정보

    private Map<String, String> targetInfo;              // 연동대상정보
    private Map<String, String> headers;                // 메시지 헤더 정보
    private Map<String, BodyMetaData> bodyMetaData;          // 메시지 바디 구성 메타정보

    private Integer modDate = 0;
    private Integer regDate = 0;

    public String getTargetInfoItem(String key) {
        Object result = targetInfo.get(key);
        if (result instanceof Integer) {
            return String.valueOf(result);
        } else {
            return targetInfo.get(key);
        }
    }

    public void putTargetInfoItem(String key, String value) {
        targetInfo.put(key, value);
    }

    public String getHeaderItem(String key) {
        return headers.get(key);
    }

    public void putHeaderItem(String key, String value) {
        headers.put(key, value);
    }

    public Long getInterfaceNum() {
        return interfaceNum;
    }

    public void setInterfaceNum(Long interfaceNum) {
        this.interfaceNum = interfaceNum;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public InterfaceBindingType getInterfaceBindingType() {
        return interfaceBindingType;
    }

    public void setInterfaceBindingType(InterfaceBindingType interfaceBindingType) {
        this.interfaceBindingType = interfaceBindingType;
    }

    public String getBindingZoneId() {
        return bindingZoneId;
    }

    public void setBindingZoneId(String bindingZoneId) {
        this.bindingZoneId = bindingZoneId;
    }

    public String getBindingZoneName() {
        return bindingZoneName;
    }

    public void setBindingZoneName(String bindingZoneName) {
        this.bindingZoneName = bindingZoneName;
    }

    public InterfaceCommandType getInterfaceCommandType() {
        return interfaceCommandType;
    }

    public void setInterfaceCommandType(InterfaceCommandType interfaceCommandType) {
        this.interfaceCommandType = interfaceCommandType;
    }

    public String getTargetInfo() {
        return JsonUtils.writeValue(targetInfo);
    }

    public void setTargetInfo(String targetInfo) {
        this.targetInfo = JsonUtils.readValue(targetInfo, HashMap.class);
    }

    public String getHeaders() {
        return JsonUtils.writeValue(headers);
    }

    public void setHeaders(String headers) {
        this.headers = JsonUtils.readValue(headers, HashMap.class);
    }

    public String getBodyMetaData() {
        return JsonUtils.writeValue(bodyMetaData);
    }

    public void setBodyMetaData(String bodyMetaData) {
        this.bodyMetaData = JsonUtils.readValue(bodyMetaData, HashMap.class);
    }

    @JsonIgnore
    public Map<String, String> getTargetInfoMap() {
        return targetInfo;
    }

    @JsonIgnore
    public Map<String, String> getHeadersMap() {
        return headers;
    }

    @JsonIgnore
    public Map<String, BodyMetaData> getBodyMetaDataMap() {
        return bodyMetaData;
    }

    public Integer getModDate() {
        return modDate;
    }

    public void setModDate(Integer modDate) {
        this.modDate = modDate;
    }

    public Integer getRegDate() {
        return regDate;
    }

    public void setRegDate(Integer regDate) {
        this.regDate = regDate;
    }

    @JsonIgnore
    public String getMapKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(interfaceBindingType.getValue());
        sb.append("_");
        sb.append(interfaceCommandType.getValue());
        if (bindingZoneId != null && !bindingZoneId.equals("")) {
            sb.append("_");
            sb.append(bindingZoneId);
        }
        return sb.toString();
    }
}
