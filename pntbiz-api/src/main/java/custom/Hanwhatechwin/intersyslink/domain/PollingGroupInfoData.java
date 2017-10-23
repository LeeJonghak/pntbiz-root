package custom.Hanwhatechwin.intersyslink.domain;

/**
 * Created by jhlee on 2017-09-20.
 */
public class PollingGroupInfoData {
    private String pollingGroupId;
    private String cycle;
    private String sleepTime;
    private String tagPerThread;
    private String action;

    public String getPollingGroupId() {
        return pollingGroupId;
    }

    public void setPollingGroupId(String pollingGroupId) {
        this.pollingGroupId = pollingGroupId;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getTagPerThread() {
        return tagPerThread;
    }

    public void setTagPerThread(String tagPerThread) {
        this.tagPerThread = tagPerThread;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
