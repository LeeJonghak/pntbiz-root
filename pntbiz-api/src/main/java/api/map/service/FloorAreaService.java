package api.map.service;

import core.api.log.domain.PresenceLog;
import core.api.map.domain.FloorAreaInfo;

/**
 * 층 구역 정보 서비스 인터페이스
 *
 * Created by ucjung on 2017-04-16.
 */
public interface FloorAreaService {
   FloorAreaInfo getAreaInfo(PresenceLog presenceLog) throws Exception;
}
