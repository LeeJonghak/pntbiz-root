package core.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import framework.database.EnumValue;

/**
 * Created by nsyun on 17. 8. 24..
 */
public enum AssignUnassignType implements EnumValue<String> {

	ASSIGN("A"), UNASSIGN("U"), CREATE("C"), MODIFY("M"), DELETE("D");

	private String value;

	AssignUnassignType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@JsonValue
	public String getNameJsonValue() {
		String string = super.toString();
		if(string!=null) {
			return string.toLowerCase();
		} else {
			return null;
		}
	}

}
