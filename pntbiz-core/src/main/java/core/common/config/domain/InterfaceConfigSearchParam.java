package core.common.config.domain;

import core.common.enums.InterfaceBindingType;
import core.common.enums.InterfaceCommandType;
import framework.util.JsonUtils;
import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

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
public class InterfaceConfigSearchParam extends PagingParam{
    private Integer comNum;
    private String UUID;                                // 회사 UUID
    private InterfaceBindingType interfaceBindingType;  // 연동 바인딩 유형
    private String bindingZoneId;                       // 연동 바인딩 Zone ID
    private String bindingZoneName;
    private InterfaceCommandType interfaceCommandType;  // 연동 이벤트 타입 정보

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (comNum != null) sb.append(comNum + "_");
        if (UUID != null) sb.append(UUID + "_");
        if (interfaceBindingType != null) sb.append(interfaceBindingType.getValue().toString() + "_");
        if (interfaceCommandType != null) sb.append(interfaceCommandType.getValue().toString() + "_");
        if (bindingZoneId != null) sb.append(bindingZoneId + "_");
        return  sb.toString();
    }

    public String getQueryString() {
        if(interfaceBindingType==null) {
            if(interfaceCommandType==null)
                return ("");
            else
                return ("&interfaceCommandType="+interfaceCommandType);
        }
        else {
            if(interfaceCommandType==null)
                return ("&interfaceBindingType="+interfaceBindingType);
            else
                return ("&interfaceBindingType="+interfaceBindingType+"&interfaceCommandType="+interfaceCommandType);
        }
    }

}
