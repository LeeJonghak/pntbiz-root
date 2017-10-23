package api.presence.bo.service;

import org.springframework.scheduling.annotation.Async;

/**
 * Created by ucjung on 2017-06-28.
 */
public interface LostBeaconSignalService {
    @Async
    void excute(String key);
}
