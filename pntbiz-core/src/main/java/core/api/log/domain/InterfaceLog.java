package core.api.log.domain;

/**
 * Created by ucjung on 2017-09-13.
 */
public class InterfaceLog {
    private Long logNum;
    private Long interfaceNum;
    private Integer comNum;
    private String target;
    private String requestMessage;
    private String responseCode;
    private String responseMessage;
    private Integer regDate;

    public Long getLogNum() {
        return logNum;
    }
    public void setLogNum(Long logNum) {
        this.logNum = logNum;
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
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String getRequestMessage() {
        return requestMessage;
    }
    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
    public String getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
    public Integer getRegDate() {
        return regDate;
    }
    public void setRegDate(Integer regDate) {
        this.regDate = regDate;
    }
}
