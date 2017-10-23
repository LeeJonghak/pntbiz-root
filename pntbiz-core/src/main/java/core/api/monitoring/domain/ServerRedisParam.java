package core.api.monitoring.domain;

public class ServerRedisParam{
    private String hostname;
    private String name;

    private String proc;


    private String ip;
    private String macAddr;
    private String cpu;
    private String mem;

    private long lasttime;
    private long downtime;
    private long uptime;

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


    public String getKey(){
        return "SERVER_" + getHostname();
    }
}
