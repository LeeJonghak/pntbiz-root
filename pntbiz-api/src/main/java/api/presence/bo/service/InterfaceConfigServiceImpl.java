package api.presence.bo.service;

import core.api.common.dao.CompanyDao;
import core.api.common.domain.Company;
import core.common.config.dao.InterfaceConfigDao;
import core.common.config.domain.InterfaceConfig;
import core.common.config.domain.InterfaceConfigSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ucjung on 2017-08-29.
 */
@Service
public class InterfaceConfigServiceImpl implements InterfaceConfigService {
    @Autowired
    private InterfaceConfigDao interfaceConfigDao;

    @Autowired
    private CompanyDao companyDao;

    @Override
    @Cacheable(value="INTERFACE_CONFIG", key="#param.toString()")
    public Map<String, InterfaceConfig> gets(InterfaceConfigSearchParam param) {
        List<InterfaceConfig> interfaceConfigs = interfaceConfigDao.gets(param);
        Map<String, InterfaceConfig> result = new HashMap<>();
        for (InterfaceConfig interfaceConfig : interfaceConfigs) {
            result.put(interfaceConfig.getMapKey(), interfaceConfig);
        }
        return result;
    }

    @Override
    @Cacheable(value="INTERFACE_CONFIG", key="#SUUID")
    public Map<String, InterfaceConfig> gets(String SUUID) {
        Company company = new Company();
        company.setUUID(SUUID);
        company = companyDao.getCompanyInfoByUUID(company);
        InterfaceConfigSearchParam param = new InterfaceConfigSearchParam();
        param.setComNum(company.getComNum());
        return this.gets(param);
    }
}
