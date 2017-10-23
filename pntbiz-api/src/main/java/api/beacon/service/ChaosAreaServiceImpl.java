package api.beacon.service;

import core.api.beacon.dao.ChaosAreaDao;
import core.api.beacon.domain.ChaosArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 혼돈영역 서비스
 * create: nohsoo 2015-04-22
 */
@Service
public class ChaosAreaServiceImpl implements ChaosAreaService {

    @Autowired
    private ChaosAreaDao chaosAreaDao;

    @Override
    public List<?> getChaosAreaListAll(String companyUUID) {
        ChaosArea param = new ChaosArea();
        param.setCompanyUUID(companyUUID);
        List<?> list = chaosAreaDao.list("getChaosAreaListAll", param);
        return list;
    }

    @Override
    public List<?> getChaosAreaListAll(String companyUUID, String floor) {
        ChaosArea param = new ChaosArea();
        param.setCompanyUUID(companyUUID);
        param.setFloor(floor);
        List<?> list = chaosAreaDao.list("getChaosAreaListAll", param);
        return list;
    }
}
