package wms.component.scheduler.service;

import framework.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.monitoring.service.MonitoringService;
import wms.scanner.service.ScannerService;

import java.util.Map;
import java.util.Set;

@Component("ScannerStateScheduler")
public class ScannerStateScheduler {
	@Autowired
	private ScannerService scannerService;

	@Autowired
	private MonitoringService service;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void batchScannerState() throws Exception {
		Set<Object> list = service.getRedisScanners("SCANNER_*");

		for(Object key : list) {
			long time = DateUtil.str2timestamp(DateUtil.getDate("yyyyMMddHHmmss"));

			String scannerKey = key.toString();
			Map<Object, Object> scanner = service.getRedisScanner(scannerKey);

			long lastTime = Long.parseLong(scanner.get("lasttime").toString());

			if (time - lastTime > 60) {
				scanner.put("downtime", scanner.get("lasttime"));
			}
		}
	}
}
