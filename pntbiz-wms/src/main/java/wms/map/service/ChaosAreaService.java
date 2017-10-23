package wms.map.service;

import wms.component.auth.LoginDetail;
import core.wms.map.domain.ChaosArea;

import java.util.List;

/**
 * 혼돈영역 서비스
 * create: nohsoo 2015-04-24
 */
public interface ChaosAreaService {

    public List<?> getChaosAreaListAll(LoginDetail loginDetail, String floor);

    public ChaosArea registerChaosArea(LoginDetail loginDetail, String floor, Double lat, Double lng, Float radius);

    public void modifyChaosArea(LoginDetail loginDetail, Integer areaNum, Double lat, Double lng, Float radius);

    public void deleteChaosArea(LoginDetail loginDetail, Integer areaNum);
}
