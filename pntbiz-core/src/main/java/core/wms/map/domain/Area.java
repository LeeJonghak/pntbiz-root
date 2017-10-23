package core.wms.map.domain;

import java.util.List;

/**
 */
public class Area {

    private Integer areaNum;

    private Integer comNum;

    private String floor;

    private String areaName;

    private List<AreaLatlng> areaLatlngs;

    private Integer regDate;

    public Integer getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(Integer areaNum) {
        this.areaNum = areaNum;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<AreaLatlng> getAreaLatlngs() {
        return areaLatlngs;
    }

    public void setAreaLatlngs(List<AreaLatlng> areaLatlngs) {
        this.areaLatlngs = areaLatlngs;
    }

    public Integer getRegDate() {
        return regDate;
    }

    public void setRegDate(Integer regDate) {
        this.regDate = regDate;
    }
}
