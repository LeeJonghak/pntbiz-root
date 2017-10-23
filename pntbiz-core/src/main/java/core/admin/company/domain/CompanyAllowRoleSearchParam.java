package core.admin.company.domain;

import framework.web.util.PagingParam;

public class CompanyAllowRoleSearchParam extends PagingParam {
	private int comNum;

	public int getComNum() {
		return comNum;
	}

	public void setComNum(int comNum) {
		this.comNum = comNum;
	}
}
