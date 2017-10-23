package api.beacon.service;

import java.util.List;

/**
 * 혼돈영역 서비스
 * create: nohsoo 2015-04-22
 */
public interface ChaosAreaService {

    public List<?> getChaosAreaListAll(String companyUUID);

    public List<?> getChaosAreaListAll(String companyUUID, String floor);

}
