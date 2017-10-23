package framework.gabiasms;

public class GabiaSmsResult {
	private String code = "";
	private String mesg = "";
	
	public GabiaSmsResult(String code, String mesg) {
		
		this.code = code;
		this.mesg = mesg;
		
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	public String getMesg()
	{
		return this.mesg;
	}
	
	
}
