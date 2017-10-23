package core.api.map.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class FloorCode {
	private Integer floorCodeNum;
	private Integer comNum;
    private String UUID;
	private String nodeId;
	private String nodeName;
    private String nodeField;
	private String typeCode;
	private String upperNodeId;
	private Integer levelNo;
	private Integer sortNo;
	private String useFlag;

    private List<FloorCode> childNode;

    public Integer getFloorCodeNum() {
        return floorCodeNum;
    }
    public void setFloorCodeNum(Integer floorCodeNum) {
        this.floorCodeNum = floorCodeNum;
    }
    @JsonIgnore
    public Integer getComNum() {
        return comNum;
    }
    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }
    @JsonIgnore
    public String getUUID() {
        return UUID;
    }
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    public String getNodeId() {
        return nodeId;
    }
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    public String getTypeCode() {
        return typeCode;
    }
    public String getNodeName() {
        return nodeName;
    }

    public String getNodeField() {
        return nodeField;
    }

    public void setNodeField(String nodeField) {
        this.nodeField = nodeField;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    public String getUpperNodeId() {
        return upperNodeId;
    }
    public void setUpperNodeId(String upperNodeId) {
        this.upperNodeId = upperNodeId;
    }
    public Integer getLevelNo() {
        return levelNo;
    }
    public void setLevelNo(Integer levelNo) {
        this.levelNo = levelNo;
    }
    public Integer getSortNo() {
        return sortNo;
    }
    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
    @JsonIgnore
    public String getUseFlag() {
        return useFlag;
    }
    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }

    public List<FloorCode> getChildNode() {
        return childNode;
    }

    public void setChildNode(List<FloorCode> childNode) {
        this.childNode = childNode;
    }

}


