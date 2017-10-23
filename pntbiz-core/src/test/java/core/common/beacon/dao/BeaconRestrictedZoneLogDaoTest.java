package core.common.beacon.dao;

import core.common.beacon.domain.BeaconRestrictedZone;
import core.common.beacon.domain.BeaconRestrictedZoneLog;
import core.common.enums.AssignUnassignType;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testbase.JUnitTestBase;

import java.util.List;

/**
 * Created by nsyun on 17. 8. 18..
 */
public class BeaconRestrictedZoneLogDaoTest extends JUnitTestBase {

	@Autowired
	private BeaconRestrictedZoneLogDao beaconRestrictedZoneLogDao;

	@Test
	public void testInsertBeaconRestrictedZoneLog() {

		/*BeaconRestrictedZoneLog vo = new BeaconRestrictedZoneLog();
		vo.setType(BeaconRestrictedZoneLog.Type.ASSIGN);
		vo.setBeaconNum(172544);
		beaconRestrictedZoneLogDao.insertBeaconRestrictedZoneLog(vo);*/

		BeaconRestrictedZoneLog vo = new BeaconRestrictedZoneLog();
		vo.setType(AssignUnassignType.ASSIGN);
		vo.setBeaconNum(172544);
		beaconRestrictedZoneLogDao.insertBeaconRestrictedZoneLog(vo);

	}

	@Test
	public void testListBeaconRestrictedZoneLog() {
		BeaconRestrictedZoneLog param = new BeaconRestrictedZoneLog();
		param.setBeaconNum(105177);
		List<BeaconRestrictedZoneLog> list = beaconRestrictedZoneLogDao.listBeaconRestrictedZoneLog(param);
		Assert.assertNotNull(list);

	}
}
