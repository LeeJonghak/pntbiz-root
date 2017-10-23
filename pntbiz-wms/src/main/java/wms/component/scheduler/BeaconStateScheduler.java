package wms.component.scheduler;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import framework.web.util.DateUtil;
import core.wms.beacon.domain.BeaconState;
import wms.beacon.service.BeaconService;

@Component("BeaconStateScheduler")
public class BeaconStateScheduler {
	
	@Autowired
	private BeaconService beaconService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void batchBeaconState() throws Exception {
		
		logger.info("cron", "run");
		HashMap<String, Object> param = new HashMap<String, Object>();		
		
		String sDate = DateUtil.getDate("yyyyMMdd") + "000000";
		String eDate = DateUtil.getDate("yyyyMMdd") + "235959";		
		sDate = DateUtil.getAddDate(sDate, 0, "yyyyMMddHHmmss");
		eDate = DateUtil.getAddDate(eDate, 0, "yyyyMMddHHmmss");
		param.put("sDate", sDate);
		param.put("eDate", eDate);
		
		List<BeaconState> list = beaconService.getLogBeaconStateList(param);
		
		if(list.size() > 0) {
			for(int i=0; i<list.size(); i++) {
				BeaconState beaconState = null; 
				beaconState = (BeaconState) list.get(i);
				System.out.println(beaconState.getSUUID() + " : " + beaconState.getUUID() + "-" + beaconState.getMajorVer() + "-" + beaconState.getMinorVer() + " / " + beaconState.getBattery());
				beaconService.modifyBeaconState(beaconState);
			}
		} else {
			
		}
		
	}
}
