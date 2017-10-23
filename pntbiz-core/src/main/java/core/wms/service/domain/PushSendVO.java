package core.wms.service.domain;

public class PushSendVO extends EventTranslationVO{
	
	private int pnsId;
	
	private String pnsTitle;
	
	private String pnsMsg;
	
	public int getPnsId() {
		return pnsId;
	}

	public void setPnsId(int pnsId) {
		this.pnsId = pnsId;
	}

	public String getPnsTitle() {
		return pnsTitle;
	}

	public void setPnsTitle(String pnsTitle) {
		this.pnsTitle = pnsTitle;
	}

	public String getPnsMsg() {
		return pnsMsg;
	}

	public void setPnsMsg(String pnsMsg) {
		this.pnsMsg = pnsMsg;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PushSendVO [pnsId=").append(pnsId)
				.append(", pnsTitle=").append(pnsTitle).append(", pnsMsg=")
				.append(pnsMsg).append("]");
		return builder.toString();
	}

}
