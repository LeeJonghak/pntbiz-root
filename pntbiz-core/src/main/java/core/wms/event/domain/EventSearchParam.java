package core.wms.event.domain;

import framework.web.util.PagingParam;
import framework.web.util.StringUtil;

public class EventSearchParam extends PagingParam {
	private Integer comNum;
	private String opt;
	private String keyword;

	private String evtTypeCode;

    public String getEvtTypeCode() {
        return evtTypeCode;
    }

    public void setEvtTypeCode(String evtTypeCode) {
        this.evtTypeCode = evtTypeCode;
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

		return ("".equals(opt) || "".equals(keyword)) ? "" : "&opt="+ opt + "&keyword=" + keyword;
	}
}
