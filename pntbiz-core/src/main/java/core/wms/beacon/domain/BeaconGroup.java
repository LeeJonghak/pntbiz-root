package core.wms.beacon.domain;

public class BeaconGroup {
	
	private Integer comNum;
	private Integer beaconGroupNum;
	private String beaconGroupName;
	private Integer beaconCount;
	private Integer modDate;
    private Integer regDate;    
    
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public Integer getBeaconGroupNum() {
		return beaconGroupNum;
	}
	public void setBeaconGroupNum(Integer beaconGroupNum) {
		this.beaconGroupNum = beaconGroupNum;
	}
	public String getBeaconGroupName() {
		return beaconGroupName;
	}
	public void setBeaconGroupName(String beaconGroupName) {
		this.beaconGroupName = beaconGroupName;
	}	
	public Integer getBeaconCount() {
		return beaconCount;
	}
	public void setBeaconCount(Integer beaconCount) {
		this.beaconCount = beaconCount;
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