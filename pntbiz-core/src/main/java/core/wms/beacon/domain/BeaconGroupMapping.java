package core.wms.beacon.domain;

public class BeaconGroupMapping {
	private Integer beaconGroupNum;
	private Long beaconNum;	
	private Integer fromBeaconGroupNum;
	private Long fromBeaconNum;

	public Integer getBeaconGroupNum() {
		return beaconGroupNum;
	}
	public void setBeaconGroupNum(Integer beaconGroupNum) {
		this.beaconGroupNum = beaconGroupNum;
	}
	public Long getBeaconNum() {
		return beaconNum;
	}
	public void setBeaconNum(Long beaconNum) {
		this.beaconNum = beaconNum;
	}
	public Integer getFromBeaconGroupNum() {
		return fromBeaconGroupNum;
	}
	public void setFromBeaconGroupNum(Integer fromBeaconGroupNum) {
		this.fromBeaconGroupNum = fromBeaconGroupNum;
	}
	public Long getFromBeaconNum() {
		return fromBeaconNum;
	}
	public void setFromBeaconNum(Long fromBeaconNum) {
		this.fromBeaconNum = fromBeaconNum;
	}
}
