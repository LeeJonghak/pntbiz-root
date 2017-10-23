package core.wms.monitoring.domain;

public class Monitoring extends ScannerRedisParam{
    //scanner
    private String name;

    //server
    private String hostname;
    private String proc;
    private String procStatus;

    //ommm
    private String cpuStatus;
    private String memStatus;
    private String pingStatus;

    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getProc() {
        return proc;
    }
    public void setProc(String proc) {
        this.proc = proc;
    }
    public String getProcStatus() {
        return procStatus;
    }
    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }
    public String getCpuStatus() {
        return cpuStatus;
    }
    public void setCpuStatus(String cpuStatus) {
        this.cpuStatus = cpuStatus;
    }
    public String getMemStatus() {
        return memStatus;
    }
    public void setMemStatus(String memStatus) {
        this.memStatus = memStatus;
    }
    public String getPingStatus() {
        return pingStatus;
    }
    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }
}
