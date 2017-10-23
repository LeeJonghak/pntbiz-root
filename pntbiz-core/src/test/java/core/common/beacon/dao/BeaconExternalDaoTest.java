package core.common.beacon.dao;

import core.common.beacon.domain.BeaconExternal;
import core.common.beacon.domain.BeaconExternalWidthRestrictedZone;
import core.common.beacon.domain.BeaconRestrictedZone;
import core.common.enums.BooleanType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testbase.JUnitTestBase;

import java.util.List;

/**
 * Created by nsyun on 17. 7. 19..
 */
public class BeaconExternalDaoTest extends JUnitTestBase {

	@Autowired
	private BeaconExternalDao beaconExternalDao;

	@Test
	public void testGetBeaconExternalInfo() {

		BeaconExternal param = new BeaconExternal();
		param.setUUID("706E7430-F5F8-466E-AFF9-25556B57FE6D");
		param.setMajorVer(60003);
		param.setMinorVer(1234);
		BeaconExternal beaconExternalInfo = beaconExternalDao.getBeaconExternalInfo(param);

		System.out.println(beaconExternalInfo.getExternalAttribute());

		Assert.assertNotNull(beaconExternalInfo);
	}

	@Test
	public void testUpdateBeaconExternalInfo() {

		BeaconExternal param = new BeaconExternal();
		param.setUUID("706E7430-F5F8-466E-AFF9-25556B57FE6D");
		param.setMajorVer(60003);
		param.setMinorVer(1234);
		param.setExternalId("test");
		param.setRestrictedZonePermitted(BooleanType.TRUE);
		param.addExternalAttribute("name", "nohsoo", "윤노수");

		beaconExternalDao.updateBeaconExternalInfo(param);

		BeaconExternal beaconExternalInfo = beaconExternalDao.getBeaconExternalInfo(param);
		Assert.assertNotNull(beaconExternalInfo.getExternalId());

		/*
		할당 해제
		BeaconExternal param = new BeaconExternal();
		param.setUUID("706E7430-F5F8-466E-AFF9-25556B57FE6D");
		param.setMajorVer(10001);
		param.setMinorVer(1);
		param.setExternalId(null);
		param.addExternalAttributeRaw(null);
		beaconExternalDao.updateBeaconExternalInfo(param);

		 */
	}

	@Test
	public void testGetBeaconExternalList() {
		BeaconExternal param = new BeaconExternal();
		param.setUUID("706E7430-F5F8-466E-AFF9-25556B57FE6D");
		param.setFloor("3");
		List<BeaconExternal> list = beaconExternalDao.getBeaconExternalList(param);

		Assert.assertNotNull(list);
	}

	@Test
	public void testGetBeaconExternalWidthRestrictedZoneList() {
		BeaconExternal param = new BeaconExternal();
		param.setUUID("706E7430-F5F8-466E-AFF9-25556B57FE6D");

		List<BeaconExternalWidthRestrictedZone> list = beaconExternalDao.getBeaconExternalWidthRestrictedZoneList(param);
		Assert.assertNotNull(list);
	}

}