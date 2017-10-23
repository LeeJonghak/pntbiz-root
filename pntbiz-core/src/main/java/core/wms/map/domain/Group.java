package core.wms.map.domain;

/**
 * 그룹(TB_GROUP)
 * 2015-05-07 nohsoo
 */
public class Group {

    /**
     * 그룹고유번호
     * type: int, length: 10
     */
    private Integer groupNum;

    /**
     * 그룹명
     * type: varchar, length: 50
     */
    private String groupName;

    /**
     * 업체고유번호
     * type:int, length:10
     */
    private Integer comNum;

    /**
     * 등록시간
     * type:int, length:10
     */
    private Integer regDate;

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public Integer getRegDate() {
        return regDate;
    }

    public void setRegDate(Integer regDate) {
        this.regDate = regDate;
    }
}
