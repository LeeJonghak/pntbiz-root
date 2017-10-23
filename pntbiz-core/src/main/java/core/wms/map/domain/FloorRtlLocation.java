package core.wms.map.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class FloorRtlLocation  extends PagingParam{
    private String  safetyWorkLocationId;
    private String safetyWorkLocationName;
    private String regionCode;
    private String safetyWorkLocationTypeCode;
    private String highSafetyWorkLocationId;
    private String locationIdLevelValue;
    private String sortOrderNo;
    private String useFlag;

    private String opt;
    private String keyword;

    public String getSafetyWorkLocationId() {
        return safetyWorkLocationId;
    }
    public void setSafetyWorkLocationId(String safetyWorkLocationId) {
        this.safetyWorkLocationId = safetyWorkLocationId;
    }
    public String getSafetyWorkLocationName() {
        return safetyWorkLocationName;
    }
    public void setSafetyWorkLocationName(String safetyWorkLocationName) {
        this.safetyWorkLocationName = safetyWorkLocationName;
    }
    public String getRegionCode() {
        return regionCode;
    }
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
    public String getSafetyWorkLocationTypeCode() {
        return safetyWorkLocationTypeCode;
    }
    public void setSafetyWorkLocationTypeCode(String safetyWorkLocationTypeCode) {
        this.safetyWorkLocationTypeCode = safetyWorkLocationTypeCode;
    }
    public String getHighSafetyWorkLocationId() {
        return highSafetyWorkLocationId;
    }
    public void setHighSafetyWorkLocationId(String highSafetyWorkLocationId) {
        this.highSafetyWorkLocationId = highSafetyWorkLocationId;
    }
    public String getLocationIdLevelValue() {
        return locationIdLevelValue;
    }
    public void setLocationIdLevelValue(String locationIdLevelValue) {
        this.locationIdLevelValue = locationIdLevelValue;
    }
    public String getSortOrderNo() {
        return sortOrderNo;
    }
    public void setSortOrderNo(String sortOrderNo) {
        this.sortOrderNo = sortOrderNo;
    }
    public String getUseFlag() {
        return useFlag;
    }
    public void setUseFlag(String useFlag) {
        this.useFlag = useFlag;
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
        this.keyword = keyword;
    }

    public String getQueryString() {
        String opt = StringUtil.NVL(getOpt());
        String keyword = StringUtil.NVL(getKeyword());
        String queryString = "";
        queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
        return queryString;
    }
}


