package custom.Hanwhatechwin.intersyslink.domain;

import com.google.protobuf.ByteString;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by jhlee on 2017-09-11.
 */
public class FSProtocolHeader {
    @NotEmpty
    private String requestType;
    @NotEmpty
    private String dataType;
    @NotEmpty
    private String encryptMode = "0";
    @NotEmpty
    private String compressMode = "0";
    @NotEmpty
    private String agentType = "I";
    @NotEmpty
    private String sourceId = "AgentID";
    @NotEmpty
    private String targetId = "N001";
    @NotEmpty
    private String serverId = "SVR_300";
    @NotEmpty
    private String realTime;
    @NotEmpty
    private String heartbeat = "0";
    @NotEmpty
    private String requestId = "0";
    @NotEmpty
    private ByteString fsProtocolBodyBytes;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getEncryptMode() {
        return encryptMode;
    }

    public void setEncryptMode(String encryptMode) {
        this.encryptMode = encryptMode;
    }

    public String getCompressMode() {
        return compressMode;
    }

    public void setCompressMode(String compressMode) {
        this.compressMode = compressMode;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ByteString getFsProtocolBodyBytes() {
        return fsProtocolBodyBytes;
    }

    public void setFsProtocolBodyBytes(ByteString fsProtocolBodyBytes) {
        this.fsProtocolBodyBytes = fsProtocolBodyBytes;
    }
}

