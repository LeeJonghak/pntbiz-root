package wms.component.scheduler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wms.component.scheduler.service.SchedulerService;

@Component("LGDisplayScheduler")
public class LGDisplayScheduler {

    @Autowired
    private SchedulerService service;

    private Logger logger = LoggerFactory.getLogger(getClass());

	public void deletePresenceLog() throws Exception {
	    service.deletePresenceLogSchedule();
	}


    public void syncFloorCode() throws Exception {
        int comNum = service.selectComNum();

        service.deleteFloorCode(comNum);
        service.insertFloorCode(comNum);
    }

    public void updateBeaconStatus() throws Exception{
        List<Map<String, Object>> rtnBeaconList = service.selectBeaconList();



        for(Map<String, Object> beacon: rtnBeaconList){
            service.updateRtlBeaconStat(beacon);
        }
    }
}
