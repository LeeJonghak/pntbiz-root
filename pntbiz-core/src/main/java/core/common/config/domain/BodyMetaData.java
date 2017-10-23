package core.common.config.domain;

import core.common.enums.PresenceDataType;

/**
 * Created by ucjung on 2017-08-29.
 */
public class BodyMetaData {
    private String fieldName;
    private PresenceDataType dataType;
    private String dataFiledName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public PresenceDataType getDataType() {
        return dataType;
    }

    public void setDataType(PresenceDataType dataType) {
        this.dataType = dataType;
    }

    public String getDataFiledName() {
        return dataFiledName;
    }

    public void setDataFiledName(String dataFiledName) {
        this.dataFiledName = dataFiledName;
    }
}
