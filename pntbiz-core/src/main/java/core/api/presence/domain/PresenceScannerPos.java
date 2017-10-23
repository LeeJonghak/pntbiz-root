package core.api.presence.domain;

/**
 * Presence Scanner Post VO
 *
 * Created by ucjung on 2017-06-02.
 */
public class PresenceScannerPos {
    private Double lat;
    private Double lng;
    private Integer r;

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

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }
}
