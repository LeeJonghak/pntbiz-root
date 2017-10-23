package framework.database;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by nsyun on 17. 8. 24..
 */
public class ValueEnumTypeHandler<E extends Enum<E> & EnumValue<String>> extends BaseTypeHandler<E> {
	private Class<E> type;
	private final E[] enums;

	public ValueEnumTypeHandler(Class<E> type) {
		if (type == null)
			throw new IllegalArgumentException("Type argument cannot be null");
		this.type = type;
		this.enums = type.getEnumConstants();
		if (this.enums == null)
			throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
	}


	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		}
		for (E enm : enums) {
			if (StringUtils.equals(value, enm.getValue())) {
				return enm;
			}
		}
		throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName());
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnName) throws SQLException {
		String value = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		}
		for (E enm : enums) {
			if (StringUtils.equals(value, enm.getValue())) {
				return enm;
			}
		}
		throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName());
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnName) throws SQLException {
		String value = cs.getString(columnName);
		if (cs.wasNull()) {
			return null;
		}
		for (E enm : enums) {
			if (StringUtils.equals(value, enm.getValue())) {
				return enm;
			}
		}
		throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName());
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.getValue());
	}
}
