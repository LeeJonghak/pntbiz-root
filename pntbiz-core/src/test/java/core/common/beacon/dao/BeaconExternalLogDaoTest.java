package core.common.beacon.dao;

import core.common.beacon.domain.BeaconExternalLog;
import core.common.enums.AssignUnassignType;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testbase.JUnitTestBase;

import java.util.List;

/**
 * Created by nsyun on 17. 8. 18..
 */
public class BeaconExternalLogDaoTest extends JUnitTestBase {

	@Autowired
	private BeaconExternalLogDao beaconExternalLogDao;

	@Test
	public void testInsertBeaconExternalLog() {

		BeaconExternalLog vo = new BeaconExternalLog();
		vo.setBeaconNum(105177);
		vo.setType(AssignUnassignType.ASSIGN);
		beaconExternalLogDao.insertBeaconExternalLog(vo);

	}

	@Test
	public void testListBeaconExternalLog() {
		BeaconExternalLog param = new BeaconExternalLog();
		param.setBeaconNum(105177);
		List<BeaconExternalLog> list = beaconExternalLogDao.listBeaconExternalLog(param);
		Assert.assertNotNull(list);

	}
}
