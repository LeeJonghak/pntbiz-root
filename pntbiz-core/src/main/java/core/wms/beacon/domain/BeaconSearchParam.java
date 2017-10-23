package core.wms.beacon.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

/**
 * 스캐너 검색 항목
 * edit: nohsoo 2015-04-23 floor 항목 추가
 */
public class BeaconSearchParam extends PagingParam {

    private Integer comNum;
    private String opt;
    private String keyword;

    private String startMinor;
    private String endMinor;
    private String floor;
    private String beaconType;

    private Integer externalId;
    private Integer barcode;

    public String getStartMinor() {
        return startMinor;
    }

    public void setStartMinor(String startMinor) {
        this.startMinor = startMinor;
    }

    public String getEndMinor() {
        return endMinor;
    }

    public void setEndMinor(String endMinor) {
        this.endMinor = endMinor;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBeaconType() {
        return beaconType;
    }

    public void setBeaconType(String beaconType) {
        this.beaconType = beaconType;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

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
        this.keyword = keyword.trim();
    }

    public String getQueryString() {
        String opt = StringUtil.NVL(getOpt());
        String keyword = StringUtil.NVL(getKeyword());

        return ("".equals(opt) || "".equals(keyword)) ? "" : ("&opt="+ opt + "&keyword=" + keyword);
    }

    public Integer getExternalId() {
        return externalId;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public Integer getBarcode() {
        return barcode;
    }

    public void setBarcode(Integer barcode) {
        this.barcode = barcode;
    }
}
