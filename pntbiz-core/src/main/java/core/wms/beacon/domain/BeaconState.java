package core.wms.beacon.domain;

public class BeaconState {
	
	private Integer logNum;
	private String SUUID;
	private String UUID;
	private Integer majorVer;
	private Integer minorVer;
	private Integer battery;	
	private String regDate;
	
	private Integer chartCount1;
	private Integer chartCount2;
	private Integer chartCount3;
	private Integer chartCount4;
	private Integer chartCount5;
	private Integer chartCount6;
	private Integer chartCount7;
	
	public Integer getLogNum() {
		return logNum;
	}
	public void setLogNum(Integer logNum) {
		this.logNum = logNum;
	}	
	public String getSUUID() {
		return SUUID;
	}
	public void setSUUID(String sUUID) {
		SUUID = sUUID;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public Integer getMajorVer() {
		return majorVer;
	}
	public void setMajorVer(Integer majorVer) {
		this.majorVer = majorVer;
	}
	public Integer getMinorVer() {
		return minorVer;
	}
	public void setMinorVer(Integer minorVer) {
		this.minorVer = minorVer;
	}
	public Integer getBattery() {
		return battery;
	}
	public void setBattery(Integer battery) {
		this.battery = battery;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public Integer getChartCount1() {
		return chartCount1;
	}
	public void setChartCount1(Integer chartCount1) {
		this.chartCount1 = chartCount1;
	}
	public Integer getChartCount2() {
		return chartCount2;
	}
	public void setChartCount2(Integer chartCount2) {
		this.chartCount2 = chartCount2;
	}
	public Integer getChartCount3() {
		return chartCount3;
	}
	public void setChartCount3(Integer chartCount3) {
		this.chartCount3 = chartCount3;
	}
	public Integer getChartCount4() {
		return chartCount4;
	}
	public void setChartCount4(Integer chartCount4) {
		this.chartCount4 = chartCount4;
	}
	public Integer getChartCount5() {
		return chartCount5;
	}
	public void setChartCount5(Integer chartCount5) {
		this.chartCount5 = chartCount5;
	}
	public Integer getChartCount6() {
		return chartCount6;
	}
	public void setChartCount6(Integer chartCount6) {
		this.chartCount6 = chartCount6;
	}
	public Integer getChartCount7() {
		return chartCount7;
	}
	public void setChartCount7(Integer chartCount7) {
		this.chartCount7 = chartCount7;
	}
}
