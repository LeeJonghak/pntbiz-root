package core.wms.geofencing.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 */
public class GeofencingLatlng {

    private Long fcNum;
    private Double lat;
    private Double lng;
    private Integer orderSeq;
    private Integer radius;

    // ref
    private String fcType;
    private String fcShape;
    private String fcName;
    private String floor;
    private Integer evtEnter;
    private Integer evtLeave;
    private Integer evtStay;
    private String userID;
    private Integer modDate;
    private Integer regDate;


    public Long getFcNum() {
        return fcNum;
    }

    public void setFcNum(Long fcNum) {
        this.fcNum = fcNum;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    @JsonProperty(value = "order")
    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getFcType() {
        return fcType;
    }

    public void setFcType(String fcType) {
        this.fcType = fcType;
    }

    public String getFcShape() {
        return fcShape;
    }

    public void setFcShape(String fcShape) {
        this.fcShape = fcShape;
    }

    public String getFcName() {
        return fcName;
    }

    public void setFcName(String fcName) {
        this.fcName = fcName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getEvtEnter() {
        return evtEnter;
    }

    public void setEvtEnter(Integer evtEnter) {
        this.evtEnter = evtEnter;
    }

    public Integer getEvtLeave() {
        return evtLeave;
    }

    public void setEvtLeave(Integer evtLeave) {
        this.evtLeave = evtLeave;
    }

    public Integer getEvtStay() {
        return evtStay;
    }

    public void setEvtStay(Integer evtStay) {
        this.evtStay = evtStay;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
}
