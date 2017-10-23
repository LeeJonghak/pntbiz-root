package custom.Hanwhatechwin.intersyslink.domain;

/**
 * Created by jhlee on 2017-09-20.
 */
public class ConnectionInfoData {
    private String connectionId;
    private String connectionName;
    private String Ip;
    private String Port;
    private String Timeout;
    private String endianValue;
    private String action;
    private String subUrl;
    private String logicalStationNumber;
    private String rackNum;
    private String slotNum;
    private String dbName;
    private String id;
    private String password;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getTimeout() {
        return Timeout;
    }

    public void setTimeout(String timeout) {
        Timeout = timeout;
    }

    public String getEndianValue() {
        return endianValue;
    }

    public void setEndianValue(String endianValue) {
        this.endianValue = endianValue;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public String getLogicalStationNumber() {
        return logicalStationNumber;
    }

    public void setLogicalStationNumber(String logicalStationNumber) {
        this.logicalStationNumber = logicalStationNumber;
    }

    public String getRackNum() {
        return rackNum;
    }

    public void setRackNum(String rackNum) {
        this.rackNum = rackNum;
    }

    public String getSlotNum() {
        return slotNum;
    }

    public void setSlotNum(String slotNum) {
        this.slotNum = slotNum;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
