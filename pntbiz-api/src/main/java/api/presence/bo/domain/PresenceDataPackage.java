package api.presence.bo.domain;

import core.common.enums.PresenceDataType;

import java.util.HashMap;
import java.util.Map;

/**
 * Presence 처리에 필요한 데이타 관리 Object Model
 * Created by ucjung on 2017-06-05.
 */
public class PresenceDataPackage {

    private String mode = "S";

    private Map<Object, Object> dataPackage = new HashMap<>();

    @Deprecated
    public <T> T getClassData(Class valueType) {
        return (T) dataPackage.get(valueType);
    }

    @Deprecated
    public <T> void setClassData(T classData) {
        dataPackage.put(classData.getClass(), classData);
    }

    @Deprecated
    public boolean isExistClassData(Class valueType) {
        return dataPackage.containsKey(valueType);
    }

    public <T> T getTypeData(PresenceDataType presenceDataType) {
        return (T) dataPackage.get(presenceDataType);
    }

    public <T> void setTypeData(PresenceDataType presenceDataType, T classData) {
        dataPackage.put(presenceDataType, classData);
    }

    public boolean isExistTypeData(PresenceDataType presenceDataType) {
        return dataPackage.containsKey(presenceDataType);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }


}
