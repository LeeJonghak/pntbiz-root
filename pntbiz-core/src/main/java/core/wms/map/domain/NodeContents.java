package core.wms.map.domain;

/**
 */
public class NodeContents extends Node {

    private Integer conNum;
    private String refType;
    private String refSubType;
    private Integer refNum;
    private Integer evtNum;
    
	public Integer getConNum() {
		return conNum;
	}
	public void setConNum(Integer conNum) {
		this.conNum = conNum;
	}
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public String getRefSubType() {
		return refSubType;
	}
	public void setRefSubType(String refSubType) {
		this.refSubType = refSubType;
	}
	public Integer getRefNum() {
		return refNum;
	}
	public void setRefNum(Integer refNum) {
		this.refNum = refNum;
	}
	public Integer getEvtNum() {
		return evtNum;
	}
	public void setEvtNum(Integer evtNum) {
		this.evtNum = evtNum;
	}
    
}
