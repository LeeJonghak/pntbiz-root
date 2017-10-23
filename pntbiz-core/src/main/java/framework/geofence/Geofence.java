package framework.geofence;

import java.util.List;
import java.util.Map;

/**
 * Geofence
 * create by jhlee 17.07.27
 * history
 * 17.07.27 최초 작성 - 기존 containLatlng 함수 포함
 */
public class Geofence {

    public static class LatLng {
        double lat = 0;
        double lng = 0;
        public LatLng() {}
        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
        public double getLat() {
            return lat;
        }
        public void setLat(double lat) {
            this.lat = lat;
        }
        public double getLng() {
            return lng;
        }
        public void setLng(double lng) {
            this.lng = lng;
        }
    }


    public static class Pos {
        double x = 0;
        double y = 0;
        public Pos() {}
        public Pos(double x, double y) {
            this.x = x;
            this.y = y;
        }
        public double getX() {
            return x;
        }
        public void setX(double x) {
            this.x = x;
        }
        public double getY() {
            return y;
        }
        public void setY(double y) {
            this.y = y;
        }
    }

    public static class Circle {
        double lat = 0;
        double lng = 0;
        int r = 0;
        public Circle() {}
        public Circle(double lat, double lng, int r) {
            this.lat = lat;
            this.lng = lng;
            this.r = r;
        }
        public double getLat() {
            return lat;
        }
        public void setLat(double lat) {
            this.lat = lat;
        }
        public double getLng() {
            return lng;
        }
        public void setLng(double lng) {
            this.lng = lng;
        }
        public int getR() {
            return r;
        }
        public void setR(int r) {
            this.r = r;
        }
    }

    /**
     * 지오펜스 체크(다각형 체크)
     * @param bounds 위경도 리스트
     * @param lat 위도
     * @param lng 경도
     * @return
     */
    public static boolean containLatlng(List<LatLng> bounds, Double lat, Double lng) {
        boolean inPoly = false;
        Integer numPaths = 1;
        Integer numPoints = 0;
        Integer j = 0;
        List<LatLng> path = null;
        for(int p = 0; p < numPaths; p++) {
            path = bounds;
            numPoints = path.size();
            j = numPoints - 1;
            for (int i = 0; i < numPoints; i++) {
                LatLng vertex1 = path.get(i);
                LatLng vertex2 = path.get(j);
                if (vertex1.getLng() < lng && vertex2.getLng() >= lng || vertex2.getLng() < lng && vertex1.getLng() >= lng) {
                    if (vertex1.getLat() + (lng - vertex1.getLng()) / (vertex2.getLng() - vertex1.getLng()) * (vertex2.getLat() - vertex1.getLat()) < lat) {
                        inPoly = !inPoly;
                    }
                }
                j = i;
            }
        }
        return inPoly;
    }

    /**
     * 지오펜스체크 (원 체크)
     * @param clat 원 위도
     * @param clng 원 경도
     * @param cr 원 반지름
     * @param lat 위도
     * @param lng 경도
     * @return
     */
    public static boolean containLatlng(double clat, double clng, int cr, Double lat, Double lng) {
        return true;
    }

//    @Deprecated
//    public static boolean containLatlng(List<Map<String, Double>> bounds, Double lat, Double lng) {
//        boolean inPoly = false;
//        Integer numPaths = 1;
//        Integer numPoints = 0;
//        Integer j = 0;
//        List<Map<String, Double>> path = null;
//        for(int p = 0; p < numPaths; p++) {
//            path = bounds;
//            numPoints = path.size();
//            j = numPoints - 1;
//            for (int i = 0; i < numPoints; i++) {
//                Map<String, Double> vertex1 = path.get(i);
//                Map<String, Double> vertex2 = path.get(j);
//                if (vertex1.get("lng") < lng && vertex2.get("lng") >= lng || vertex2.get("lng") < lng && vertex1.get("lng") >= lng) {
//                    if (vertex1.get("lat")+ (lng - vertex1.get("lng")) / (vertex2.get("lng") - vertex1.get("lng")) * (vertex2.get("lat") - vertex1.get("lat")) < lat) {
//                        inPoly = !inPoly;
//                    }
//                }
//                j = i;
//            }
//        }
//        return inPoly;
//    }

    /**
     * 위경도 > 이미지 상대 좌표 변환
     * @param upperRightLatLng 지도 이미지의 우상단 위경도
     * @param lowerLeftLatLng 지도 이미지의 좌하단 위경도
     * @param upperLeftPos (0,0)
     * @param lowerRightPos 화면의 크기가 1000*500일경우 (1000,500)
     * @param latlng 현재 위경도 좌표
     * @return
     */
    public static Pos convertPosition(LatLng upperRightLatLng, LatLng lowerLeftLatLng, Pos upperLeftPos, Pos lowerRightPos, LatLng latlng) {
        Pos xyPos = new Pos(0,0);
        double diffLat = upperRightLatLng.getLat() - lowerLeftLatLng.getLat(); //위도 차이
        double diffLng = upperRightLatLng.getLng() - lowerLeftLatLng.getLng(); //경도 차이
        double diffX = lowerRightPos.getX() - upperLeftPos.getX(); // X 차이
        double diffY = lowerRightPos.getY() - upperLeftPos.getY();  // Y 차이
        xyPos.setX(upperLeftPos.getX() + (latlng.getLng() - lowerLeftLatLng.getLng())/diffLng * diffX); //X좌표 계산
        xyPos.setY(upperLeftPos.getY() + (upperRightLatLng.getLat() - latlng.getLat())/diffLat * diffY); //Y좌표 계산
        return xyPos;
    }

    /**
     * 이미지 상대좌표 > 위경도 좌표 변환
     * @param upperRightLatLng 지도 이미지의 우상단 위경도
     * @param lowerLeftLatLng 지도 이미지의 좌하단 위경도
     * @param upperLeftPos (0,0)
     * @param lowerRightPos 화면의 크기가 1000*500일경우 (1000,500)
     * @param xyPos 현재 상대 좌표
     * @return
     */
    public static LatLng convertLatLng(LatLng upperRightLatLng, LatLng lowerLeftLatLng, Pos upperLeftPos, Pos lowerRightPos, Pos xyPos) {
        LatLng latlng = new LatLng(0, 0);
        double diffLat = upperRightLatLng.getLat() - lowerLeftLatLng.getLat(); //위도 차이
        double diffLng = upperRightLatLng.getLng() - lowerLeftLatLng.getLng(); //경도 차이
        double diffX = lowerRightPos.getX() - upperLeftPos.getX(); // X 차이
        double diffY = lowerRightPos.getY() - upperLeftPos.getY();  // Y 차이
        latlng.setLng(lowerLeftLatLng.getLng() + (xyPos.getX() - upperLeftPos.getX())/diffX * diffLng); // 경도 계산
        latlng.setLat(lowerLeftLatLng.getLat() + (lowerRightPos.getY() - xyPos.getY())/diffY * diffLat); // 위도 계산
        return latlng;
    }

    /**
     * 위경도 시작지점에서 종료지점까지의 거리 계산 (m단위)
     * @param latlng1   위경도 시작지점
     * @param latlng2   위경도 종료지점
     * @return
     */
    public static double calcurateDistance(LatLng latlng1, LatLng latlng2) {
        double theta, dist;
        theta = latlng1.getLng() - latlng2.getLng();
        dist = Math.sin(deg2rad(latlng1.getLat())) * Math.sin(deg2rad(latlng2.getLat())) + Math.cos(deg2rad(latlng1.getLat()))
                * Math.cos(deg2rad(latlng2.getLat())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환
        return dist;
    }
    private static double deg2rad(double deg) {
        return (double)(deg * Math.PI / (double)180d);
    }
    private static double rad2deg(double rad) {
        return (double)(rad * (double)180d / Math.PI);
    }
}