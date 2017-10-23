package core.api.scanner.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ScannerPos 정보를 처리 하기 위한 도메인 객체 모델
 *
 * Created by ucjung on 2017-04-16.
 */
public class ScannerPostion {
    private Double lat;
    private Double lng;

    @JsonProperty("r")
    private Integer radius;

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

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
