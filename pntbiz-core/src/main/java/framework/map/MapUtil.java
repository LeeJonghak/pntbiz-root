package framework.map;

/**
 * Created by ucjung on 2017-06-02.
 */
public class MapUtil {

    /**
     * GeoLocation 거리 계산
     * GeoLocation 두 좌표간의 거래를 미터 단위로 계산하여 반환
     *
     * @param startLocation 시작위치
     * @param endLocation   종료위치
     * @return
     */
    public static Double getDistance(GeoLocation startLocation, GeoLocation endLocation) {
        return 6371 * 1000 *
                Math.acos(
                        Math.cos(
                                Math.toRadians(startLocation.getLatitude())
                ) * Math.cos(
                        Math.toRadians( endLocation.getLatitude())
                ) * Math.cos(
                        Math.toRadians( endLocation.getLongitude()) - Math.toRadians(startLocation.getLongitude())
                ) + Math.sin(
                        Math.toRadians(startLocation.getLatitude())
                ) * Math.sin(
                        Math.toRadians( endLocation.getLatitude() )
                )
        );
    }
}
