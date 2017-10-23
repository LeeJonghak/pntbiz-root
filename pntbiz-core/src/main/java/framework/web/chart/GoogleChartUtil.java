package framework.web.chart;

import framework.web.util.NumberUtil;
import framework.web.util.StringUtil;

import java.util.List;
 
public class GoogleChartUtil {
	
	/**
	 * 작은따옴표 처리
	 * @param data
	 * @param chk
	 * @return
	 */
	public List<String> setQuotation(List<String> data, boolean chk) {
		int cnt = data.size();
		if(cnt > 0) {
			for(int i=0; i<cnt; i++) {
				data.set(i, data.get(i).replace("'", "\'"));
				if(chk == true && NumberUtil.isNumeric(data.get(i))) {
					data.set(i, data.get(i));
				} else {
					data.set(i, "'"+data.get(i)+"'");
				}
			}
		}		
		return data;
	}
	
	/**
	 * 데이터 괄호 처리
	 * @param data
	 * @return
	 */
	public String setBracket(List<String> data)
	{	
		return "[" + StringUtil.toString(data, ",") + "]";
	}
	
	/**
	 * 괄호 제거
	 * @param data
	 * @return
	 */
	public String removeBracket(String data) { 
		data = data.substring(1, data.lastIndexOf(data)-2);
		return data;
	}
	
	/**
	 * 작은 따옴표 제거
	 * @param data
	 * @return
	 */
	public List<String> removeQuotation(List<String> data) {
		int cnt = data.size();
		if(cnt > 0) {
			for(int i=0; i<cnt; i++) {
				if(data.get(i).substring(0, 1).equals("'")) {
					data.set(i, removeBracket(data.get(i)));
				}
			}
		}		
		return data;
	}
	
}