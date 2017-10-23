package api.presence.bo.service;

import core.common.config.domain.InterfaceConfig;
import core.common.config.domain.InterfaceConfigSearchParam;

import java.util.Map;

/**
 * Created by ucjung on 2017-08-29.
 */
public interface InterfaceConfigService {
    public Map<String, InterfaceConfig> gets(InterfaceConfigSearchParam param);

    public Map<String, InterfaceConfig> gets(String SUUID);
}
