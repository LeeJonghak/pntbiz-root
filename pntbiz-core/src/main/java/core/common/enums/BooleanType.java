package core.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import framework.database.EnumValue;

/**
 * Created by nsyun on 17. 8. 24..
 */
@JsonDeserialize(using = BooleanTypeDeserializer.class)
public enum BooleanType implements EnumValue<String> {

	TRUE("1"), FALSE("0");

	private String value;

	BooleanType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@JsonValue
	public boolean getNameJsonValue() {
		if(TRUE.getValue().equals(value)) {
			return true;
		} else {
			return false;
		}
	}
}
