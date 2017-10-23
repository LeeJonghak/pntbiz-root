package core.api.beacon.domain;

import core.api.contents.domain.Contents;

import java.util.ArrayList;

/**
 * TB_NODE
 *
 * @author nohsoo 2015-03-10
 */
public class Node {

    private Long nodeNum;
    private Integer comNum;
    private Integer nodeID;
    private String floor;
    private String nodeName;
    private Double lat;
    private Double lng;
    private String cate;
    private String cateName;
    private String jointName;
    private ArrayList<Contents> contents;
    private String type;
	private String areaName;

    public Long getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(Long nodeNum) {
        this.nodeNum = nodeNum;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public Integer getNodeID() {
        return nodeID;
    }

    public void setNodeID(Integer nodeID) {
        this.nodeID = nodeID;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public ArrayList<Contents> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Contents> contents) {
        this.contents = contents;
    }

    public String getJointName() {
        return jointName;
    }

    public void setJointName(String jointName) {
        this.jointName = jointName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
