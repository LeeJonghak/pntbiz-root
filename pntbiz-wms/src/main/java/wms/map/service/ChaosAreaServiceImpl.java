package wms.map.service;

import wms.component.auth.LoginDetail;
import core.wms.map.dao.ChaosAreaDao;
import core.wms.map.domain.ChaosArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 혼돈영역 서비스(구현체)
 * @author nohsoo 2015-04-24
 */
@Service
public class ChaosAreaServiceImpl implements ChaosAreaService {

    @Autowired
    private ChaosAreaDao chaosAreaDao;

    /**
     * 혼돈지역 추가
     * create: nohsoo 2015-04-22
     *
     * @param loginDetail
     * @param floor
     * @param lat
     * @param lng
     * @param radius
     * @return
     */
    @Override
    public ChaosArea registerChaosArea(LoginDetail loginDetail, String floor, Double lat, Double lng, Float radius) {

        ChaosArea vo = new ChaosArea();
        vo.setComNum(loginDetail.getCompanyNumber());
        vo.setFloor(floor);
        vo.setLat(lat);
        vo.setLng(lng);
        vo.setRadius(radius);
        this.chaosAreaDao.insertChaosArea(vo);

        return vo;
    }

    /**
     * 혼돈지역 전체 항목 조회
     * create: nohsoo 2015-04-22
     *
     * @param loginDetail 로그인객체
     * @param floor 층번호
     * @return
     */
    @Override
    public List<?> getChaosAreaListAll(LoginDetail loginDetail, String floor) {
        ChaosArea param = new ChaosArea();
        param.setComNum(loginDetail.getCompanyNumber());
        param.setFloor(floor);
        List<?> list = chaosAreaDao.getChaosAreaListAll(param);
        return list;
    }

    /**
     * 혼돈지역 정보 수정
     * create: nohsoo 2015-04-22
     *
     * @param loginDetail 로그인객체
     * @param areaNum 고유번호(조회키)
     * @param lat 위도
     * @param lng 경도
     * @param radius 반지
     */
    @Override
    public void modifyChaosArea(LoginDetail loginDetail, Integer areaNum, Double lat, Double lng, Float radius) {
        ChaosArea vo = new ChaosArea();
        vo.setComNum(loginDetail.getCompanyNumber());
        // where
        vo.setAreaNum(areaNum);
        // set
        vo.setLat(lat);
        vo.setLng(lng);
        vo.setRadius(radius);
        chaosAreaDao.modifyChaosArea(vo);
    }

    /**
     * 혼돈지역 삭제 처리
     * create: nohsoo 2015-04-23
     *
     * @param loginDetail
     * @param areaNum
     */
    @Override
    public void deleteChaosArea(LoginDetail loginDetail, Integer areaNum) {
        ChaosArea param = new ChaosArea();
        param.setComNum(loginDetail.getCompanyNumber());
        param.setAreaNum(areaNum);
        chaosAreaDao.deleteChaosArea(param);
    }

}
