package framework.geofence;

import org.junit.Test;

/**
 * Created by jhlee on 2017-08-21.
 */
public class GeofenceTest {

    @Test
    public void convertLocationTest() throws Exception {
        // 왼쪽 위
//        double lat = 37.811446;
//        double lng = 126.765324;
        // 왼쪽 아래
//        double lat = 37.810949;
//        double lng = 126.765324;
        // 오른쪽 아래
//        double lat = 37.810949;
//        double lng = 126.766235;
        // 오른쪽 위
        double lat = 37.811446;
        double lng = 126.766235;
        Geofence.LatLng latlng = new Geofence.LatLng(lat, lng);
        // 지도이미지 위경도
        Geofence.LatLng upperRightLatLng = new Geofence.LatLng(37.811446, 126.766235);
        Geofence.LatLng lowerLeftLatLng = new Geofence.LatLng(37.810949, 126.765324);
        // ism 이미지 (원본이미지 크기)
        Geofence.Pos upperLeftPos = new Geofence.Pos(0, 0);
        Geofence.Pos lowerRightPos = new Geofence.Pos(1467, 824);
        // 변환
        Geofence.Pos xy = Geofence.convertPosition(upperRightLatLng, lowerLeftLatLng, upperLeftPos, lowerRightPos, latlng);
    }

    @Test
    public void convertLatLngTest() throws Exception {
        // 왼쪽 위
//        double lat = 37.811446;
//        double lng = 126.765324;
        // 왼쪽 아래
//        double lat = 37.810949;
//        double lng = 126.765324;
        // 오른쪽 아래
//        double lat = 37.810949;
//        double lng = 126.766235;
        // 오른쪽 위
//        double lat = 37.811446;
//        double lng = 126.766235;

        double x = 0;
        double y = 824;

        Geofence.Pos xyPos = new Geofence.Pos(x, y);
        // 지도이미지 위경도
        Geofence.LatLng upperRightLatLng = new Geofence.LatLng(37.811446, 126.766235);
        Geofence.LatLng lowerLeftLatLng = new Geofence.LatLng(37.810949, 126.765324);
        // ism 이미지 (원본이미지 크기)
        Geofence.Pos upperLeftPos = new Geofence.Pos(0, 0);
        Geofence.Pos lowerRightPos = new Geofence.Pos(1467, 824);
        // 변환
        Geofence.LatLng latlng = Geofence.convertLatLng(upperRightLatLng, lowerLeftLatLng, upperLeftPos, lowerRightPos, xyPos);
    }

    @Test
    public void calcurateDistanceTest() throws Exception {
        double lat1 = 37.511161;    // 사무실 왼쪽 위
        double lng1 = 127.056595;
        double lat2 = 37.511189;    // 사무실 오른쪽 위
        double lng2 = 127.056753;
        Geofence.LatLng latlng1 = new Geofence.LatLng(lat1, lng1);
        Geofence.LatLng latlng2 = new Geofence.LatLng(lat2, lng2);
        double distance = Geofence.calcurateDistance(latlng1, latlng2);
    }

    @Test
    public void calcurateDistanceTest2() throws Exception {

        double width = 900;
        double height = 506;
        // 이미지 실제 거리
//    가로 97 pixel : 15m  : 0.154639175257732 px/m
//    세로 137 pixel :  21m : 0.1532846715328467 px/m
//    avg px/m  0.1539619233952893
        double absDist = width * 0.1539619233952893;

        // 73 pixel = 11.23922040785612
        double lat1 = 37.80995;
        double lng1 = 126.76693;
        double lat2 = 37.80995;
        double lng2 = 126.767892;
        Geofence.LatLng latlng1 = new Geofence.LatLng(lat1, lng1);
        Geofence.LatLng latlng2 = new Geofence.LatLng(lat2, lng2);
        double mapDist = Geofence.calcurateDistance(latlng1, latlng2);
        double ratio = absDist / mapDist;

        double lat3 = 37.810002;
        double lng3 = 126.767015;
        double lat4 = 37.810002;
        double lng4 = 126.767118;
        Geofence.LatLng latlng3 = new Geofence.LatLng(lat3, lng3);
        Geofence.LatLng latlng4 = new Geofence.LatLng(lat4, lng4);
        double distance = Geofence.calcurateDistance(latlng3, latlng4);
        double real = distance * ratio;
    }

    @Test
    public void calcurateRelativeValueStaticTest() throws  Exception {
        // 왼쪽 위
        double ulLat = 37.811446;
        double ulLng = 126.765324;
        // 왼쪽 아래
        double llLat = 37.810949;
        double llLng = 126.765324;
        // 오른쪽 아래
        double lrLat = 37.810949;
        double lrLng = 126.766235;
        // 오른쪽 위
        double urLat = 37.811446;
        double urLng = 126.766235;

        // 변환크기
        double meter = 5;

        // lgd 픽셀당 거리
//        double width = 1467;
//        double height = 824;
        double width = 900;
        double height = 506;
        // 이미지 실제 거리
//    가로 97 pixel : 15m  : 0.154639175257732 px/m
//    세로 137 pixel :  21m : 0.1532846715328467 px/m
//    avg px/m  0.1539619233952893
        double absDist = width * 0.1539619233952893;

        // 직선
        Geofence.LatLng latlng1 = new Geofence.LatLng(ulLat, ulLng);
        Geofence.LatLng latlng2 = new Geofence.LatLng(urLat, urLng);
        // pnt 위경도 상대적인 거리 80.02531081875988
        double relDist = Geofence.calcurateDistance(latlng1, latlng2);
        // 1보다 클경우 pnt 맵이 실제크기보다 작게 처리함
        double distRatio = absDist / relDist;

        // LGD 왼쪽 위
        double ulX = 50;
        double ulY = 50;
        // LGD 오른쪽 아래
        double lrX = 200;
        double lrY = 200;

        // 5M 전방 사이징 크게  18.3375 픽셀을 키워야 함.
        double widePixel = 5 / absDist;

        // 5M 넓게 계산한 좌표
        double upperLeftX = ulX - widePixel;
        double upperLeftY = ulY - widePixel;

        double lowerLeftX = ulX - widePixel;
        double lowerLeftY = lrY + widePixel;

        double lowerRightX = lrX + widePixel;
        double lowerRightY = lrY + widePixel;

        double upperRightX = lrX + widePixel;
        double upperRightY = ulY - widePixel;

        // 4개의 좌표를 pnt 위경도로 변경
        // 지도이미지 위경도
        Geofence.LatLng mapUpperRightLatLng = new Geofence.LatLng(37.811446, 126.766235);
        Geofence.LatLng mapLowerLeftLatLng = new Geofence.LatLng(37.810949, 126.765324);
        // ism 이미지 (원본이미지 크기)
        Geofence.Pos imgUpperLeftPos = new Geofence.Pos(0, 0);
        Geofence.Pos imgLowerRightPos = new Geofence.Pos(1467, 824);
        // 변환
        Geofence.Pos upperLeftPos = new Geofence.Pos(upperLeftX, upperLeftY);
        Geofence.Pos lowerLeftPos = new Geofence.Pos(lowerLeftX, lowerLeftY);
        Geofence.Pos lowerRightPos = new Geofence.Pos(lowerRightX, lowerRightY);
        Geofence.Pos upperRightPos = new Geofence.Pos(upperRightX, upperRightY);
        Geofence.LatLng upperLeftLatlng = Geofence.convertLatLng(mapUpperRightLatLng, mapLowerLeftLatLng, imgUpperLeftPos, imgLowerRightPos, upperLeftPos);
        Geofence.LatLng lowerLeftLatlng = Geofence.convertLatLng(mapUpperRightLatLng, mapLowerLeftLatLng, imgUpperLeftPos, imgLowerRightPos, lowerLeftPos);
        Geofence.LatLng lowerRightLatlng = Geofence.convertLatLng(mapUpperRightLatLng, mapLowerLeftLatLng, imgUpperLeftPos, imgLowerRightPos, lowerRightPos);
        Geofence.LatLng upperRightLatlng = Geofence.convertLatLng(mapUpperRightLatLng, mapLowerLeftLatLng, imgUpperLeftPos, imgLowerRightPos, upperRightPos);
        // 해당 변환 좌표를 지오펜스 영역으로 저장
        System.out.println("end");
    }
}
