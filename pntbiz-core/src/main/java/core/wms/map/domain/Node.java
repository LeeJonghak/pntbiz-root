package core.wms.map.domain;

import java.util.List;

/**
 */
public class Node {

	private List<Long> nodeNumArray;
    private Long nodeNum;
    private Integer comNum;
    private Integer nodeID;
    private String floor;
    private String nodeName;
    private Double lat;
    private Double lng;
    private String cate;
    private String jointName;
    private String type;
	private String areaName;
	private Integer areaNum;

	public List<Long> getNodeNumArray() {
		return nodeNumArray;
	}

	public void setNodeNumArray(List<Long> nodeNumArray) {
		this.nodeNumArray = nodeNumArray;
	}

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

    public Integer getNodeId() {
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

	public Integer getAreaNum() {
		return areaNum;
	}

	public void setAreaNum(Integer areaNum) {
		this.areaNum = areaNum;
	}
}
