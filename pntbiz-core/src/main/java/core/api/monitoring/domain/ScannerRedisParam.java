package core.api.monitoring.domain;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import framework.web.util.StringUtil;

public class ScannerRedisParam{
	@JsonProperty("UUID")
	@NotEmpty
    private String UUID;

    private String fwVer;

    private String ip;
    private String macAddr;
    private String cpu;
    private String mem;

    private long lasttime;
    private long downtime;
    private long uptime;

    private Boolean isScanning;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public long getLasttime() {
        return lasttime;
    }

    public void setLasttime(long lasttime) {
        this.lasttime = lasttime;
    }

    public long getDowntime() {
        return downtime;
    }

    public void setDowntime(long downtime) {
        this.downtime = downtime;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    @JsonProperty("UUID")
    public String getUUID() {
        return UUID;
    }

    @JsonProperty("UUID")
    public void setUUID(String uUID) {
        UUID = uUID;
    }

    public String getFwVer() {
        return fwVer;
    }

    public void setFwVer(String fwVer) {
        this.fwVer = fwVer;
    }

    public String getKey(){
        return "SCANNER_" + getUUID() + "_" + StringUtil.removeCharacters(getMacAddr(), ":");
    }

    public Boolean getIsScanning() {
        return isScanning;
    }

    public void setIsScanning(Boolean isScanning) {
        this.isScanning = isScanning;
    }
}
