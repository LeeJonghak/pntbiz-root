package core.wms.map.domain;

import java.util.ArrayList;

public class Floor {
	private Integer floorNum;
	private Integer comNum;
	private String floor;
	private Double swLat;
	private Double swLng;
	private Double neLat;
	private Double neLng;
	private Float deg;
	private String imgSrc;
	private String imgURL;
	private Integer modDate;
	private Integer regDate;

    private String floorName;

    private ArrayList<FloorRtlLocation> rtlLocationArr;

    public ArrayList<FloorRtlLocation> getRtlLocationArr() {
        return rtlLocationArr;
    }
    public void setRtlLocationArr(ArrayList<FloorRtlLocation> rtlLocationArr) {
        this.rtlLocationArr = rtlLocationArr;
    }
    public Integer getFloorNum() {
		return floorNum;
	}
	public void setFloorNum(Integer floorNum) {
		this.floorNum = floorNum;
	}
	public Integer getComNum() {
		return comNum;
	}
	public void setComNum(Integer comNum) {
		this.comNum = comNum;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public Double getSwLat() {
		return swLat;
	}
	public void setSwLat(Double swLat) {
		this.swLat = swLat;
	}
	public Double getSwLng() {
		return swLng;
	}
	public void setSwLng(Double swLng) {
		this.swLng = swLng;
	}
	public Double getNeLat() {
		return neLat;
	}
	public void setNeLat(Double neLat) {
		this.neLat = neLat;
	}
	public Double getNeLng() {
		return neLng;
	}
	public void setNeLng(Double neLng) {
		this.neLng = neLng;
	}
	public Float getDeg() {
		return deg;
	}
	public void setDeg(Float deg) {
		this.deg = deg;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
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


