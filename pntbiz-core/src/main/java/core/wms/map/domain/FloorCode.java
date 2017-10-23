package core.wms.map.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class FloorCode extends PagingParam{
	private Integer floorCodeNum;
	private Integer comNum;
	private String nodeId;
	private String nodeName1;

    private String nodeField;
	private String typeCode;
	private String upperNodeId;
	private Integer levelNo;
	private Integer sortNo;
	private String useFlag;

    private String opt;
    private String keyword;

    public String getOpt() {
        return opt;
    }
    public void setOpt(String opt) {
        this.opt = opt;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public Integer getFloorCodeNum() {
        return floorCodeNum;
    }
    public void setFloorCodeNum(Integer floorCodeNum) {
        this.floorCodeNum = floorCodeNum;
    }
    public Integer getComNum() {
        return comNum;
    }
    public void setComNum(Integer comNum) {
        this.comNum = comNum;
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
    public String getNodeName1() {
        return nodeName1;
    }
    public void setNodeName1(String nodeName1) {
        this.nodeName1 = nodeName1;
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
    public String getUseFlag() {
        return useFlag;
    }
    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
    }
    public String getNodeField() { return nodeField; }
    public void setNodeField(String nodeField) { this.nodeField=nodeField;}


    public String getQueryString() {
        String opt = StringUtil.NVL(getOpt());
        String keyword = StringUtil.NVL(getKeyword());
        String queryString = "";
        queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
        return queryString;
    }
}


