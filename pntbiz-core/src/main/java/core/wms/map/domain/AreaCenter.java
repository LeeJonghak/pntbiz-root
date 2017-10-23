package core.wms.map.domain;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class AreaCenter {

    static public class LatLng {
        private Double lat;
        private Double lng;

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

    private Integer areaNum;

    private Integer comNum;

    private String floor;

    private String areaName;

    private List<AreaLatlng> latlngs = new ArrayList<AreaLatlng>();

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



    public LatLng getCenter() {

        Double minLat = null;
        Double maxLat = null;
        Double minLng = null;
        Double maxLng = null;
        for(AreaLatlng latLngInfo: this.latlngs) {
            if(minLat==null || minLat > latLngInfo.getLat()) {
                minLat = latLngInfo.getLat();
            }
            if(maxLat==null || maxLat < latLngInfo.getLat()) {
                maxLat = latLngInfo.getLat();
            }
            if(minLng==null || minLng > latLngInfo.getLng()) {
                minLng = latLngInfo.getLng();
            }
            if(maxLng==null || maxLng < latLngInfo.getLng()) {
                maxLng = latLngInfo.getLng();
            }
        }
        AreaCenter.LatLng latlng = new AreaCenter.LatLng();
        latlng.setLat(Math.round((minLat + ((maxLat - minLat) / 2)) * 1000000) / 1000000.0);
        latlng.setLng(Math.round((minLng + ((maxLng - minLng) / 2)) * 1000000) / 1000000.0);

        return latlng;
    }

    public List<AreaLatlng> getLatlngs() {
        return latlngs;
    }

    public void setLatlngs(List<AreaLatlng> latlngs) {
        this.latlngs = latlngs;
    }
}
