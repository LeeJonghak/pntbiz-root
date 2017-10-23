package api.presence.bo.external;

import core.common.config.domain.InterfaceConfig;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by ucjung on 2017-06-16.
 */
public interface ExternalInterface {
    public Object request(Map<String, Object> requestParam, InterfaceConfig config);
    public Future<Object> requestAsync(Map<String, Object> requestParam, InterfaceConfig config);
}
