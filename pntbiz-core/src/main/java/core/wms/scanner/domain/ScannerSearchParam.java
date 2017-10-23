package core.wms.scanner.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

/**
 * 스캐너 검색 항목
 * edit: nohsoo 2015-04-23 floor 항목 추가
 */
public class ScannerSearchParam extends PagingParam {

    private Integer comNum;
    private String floor;
    private String opt;
    private String keyword;

    public Integer getComNum() {
        return comNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
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
        String queryString = "";
        queryString += ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
        return queryString;
    }

}
