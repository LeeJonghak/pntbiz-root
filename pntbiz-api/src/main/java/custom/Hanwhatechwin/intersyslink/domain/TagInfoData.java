package custom.Hanwhatechwin.intersyslink.domain;

import com.google.protobuf.ByteString;

/**
 * Created by jhlee on 2017-09-20.
 */
public class TagInfoData {
    private String tagId;
    private String ioName;
    private String paramName;
    private String tagName;
    private String mntrYN;
    private String tagType;
    private String tagAttribute;
    private String tagValueType;
    private String description;
    private String cltEqmtId;
    private String aid;
    private ByteString descrip;
    private String connectionId;
    private String pollingGroupId;

    private String modbusSlaveId;
    private String modbusStartAddr;
    private String modbusIoType;
    private String modbusModuloType;

    private String opcElement;

    private String filePath;

    private String countryCode;
    private String regionCode;
    private String categoryCode1;
    private String categoryCode2;
    private String categoryCode3;
    private String categoryCode4;

    private String dataBlockNum;
    private String dataBlockAddr;

    private String nameSpaceIndex;
    private String identifier;

    private String startAddr;
    private String textLength;
    private String deviceType;

    private String tableName;
    private String rawDataId;
    private String idColumn;
    private String valueColumn;
    private String checkColumn;
    private String dbAction;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getIoName() {
        return ioName;
    }

    public void setIoName(String ioName) {
        this.ioName = ioName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getMntrYN() {
        return mntrYN;
    }

    public void setMntrYN(String mntrYN) {
        this.mntrYN = mntrYN;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getTagAttribute() {
        return tagAttribute;
    }

    public void setTagAttribute(String tagAttribute) {
        this.tagAttribute = tagAttribute;
    }

    public String getTagValueType() {
        return tagValueType;
    }

    public void setTagValueType(String tagValueType) {
        this.tagValueType = tagValueType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCltEqmtId() {
        return cltEqmtId;
    }

    public void setCltEqmtId(String cltEqmtId) {
        this.cltEqmtId = cltEqmtId;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public ByteString getDescrip() {
        return descrip;
    }

    public void setDescrip(ByteString descrip) {
        this.descrip = descrip;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getPollingGroupId() {
        return pollingGroupId;
    }

    public void setPollingGroupId(String pollingGroupId) {
        this.pollingGroupId = pollingGroupId;
    }

    public String getModbusSlaveId() {
        return modbusSlaveId;
    }

    public void setModbusSlaveId(String modbusSlaveId) {
        this.modbusSlaveId = modbusSlaveId;
    }

    public String getModbusStartAddr() {
        return modbusStartAddr;
    }

    public void setModbusStartAddr(String modbusStartAddr) {
        this.modbusStartAddr = modbusStartAddr;
    }

    public String getModbusIoType() {
        return modbusIoType;
    }

    public void setModbusIoType(String modbusIoType) {
        this.modbusIoType = modbusIoType;
    }

    public String getModbusModuloType() {
        return modbusModuloType;
    }

    public void setModbusModuloType(String modbusModuloType) {
        this.modbusModuloType = modbusModuloType;
    }

    public String getOpcElement() {
        return opcElement;
    }

    public void setOpcElement(String opcElement) {
        this.opcElement = opcElement;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getCategoryCode1() {
        return categoryCode1;
    }

    public void setCategoryCode1(String categoryCode1) {
        this.categoryCode1 = categoryCode1;
    }

    public String getCategoryCode2() {
        return categoryCode2;
    }

    public void setCategoryCode2(String categoryCode2) {
        this.categoryCode2 = categoryCode2;
    }

    public String getCategoryCode3() {
        return categoryCode3;
    }

    public void setCategoryCode3(String categoryCode3) {
        this.categoryCode3 = categoryCode3;
    }

    public String getCategoryCode4() {
        return categoryCode4;
    }

    public void setCategoryCode4(String categoryCode4) {
        this.categoryCode4 = categoryCode4;
    }

    public String getDataBlockNum() {
        return dataBlockNum;
    }

    public void setDataBlockNum(String dataBlockNum) {
        this.dataBlockNum = dataBlockNum;
    }

    public String getDataBlockAddr() {
        return dataBlockAddr;
    }

    public void setDataBlockAddr(String dataBlockAddr) {
        this.dataBlockAddr = dataBlockAddr;
    }

    public String getNameSpaceIndex() {
        return nameSpaceIndex;
    }

    public void setNameSpaceIndex(String nameSpaceIndex) {
        this.nameSpaceIndex = nameSpaceIndex;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr;
    }

    public String getTextLength() {
        return textLength;
    }

    public void setTextLength(String textLength) {
        this.textLength = textLength;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRawDataId() {
        return rawDataId;
    }

    public void setRawDataId(String rawDataId) {
        this.rawDataId = rawDataId;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getValueColumn() {
        return valueColumn;
    }

    public void setValueColumn(String valueColumn) {
        this.valueColumn = valueColumn;
    }

    public String getCheckColumn() {
        return checkColumn;
    }

    public void setCheckColumn(String checkColumn) {
        this.checkColumn = checkColumn;
    }

    public String getDbAction() {
        return dbAction;
    }

    public void setDbAction(String dbAction) {
        this.dbAction = dbAction;
    }
}
