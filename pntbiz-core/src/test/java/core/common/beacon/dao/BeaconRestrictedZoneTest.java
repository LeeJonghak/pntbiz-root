package core.common.beacon.dao;

import core.common.beacon.domain.BeaconExternal;
import core.common.beacon.domain.BeaconRestrictedZone;
import core.common.enums.BooleanType;
import core.common.enums.ZoneType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testbase.JUnitTestBase;

import java.util.List;

/**
 * Created by nsyun on 17. 8. 17..
 */
public class BeaconRestrictedZoneTest extends JUnitTestBase {

	@Autowired
	private BeaconRestrictedZoneDao beaconRestrictedZoneDao;


	@Test
	public void testGetBeaconRestrictedZoneInfo() throws Exception {
		BeaconRestrictedZone param = new BeaconRestrictedZone();

		param.setBeaconNum(172544);
		param.setZoneType(ZoneType.FLOOR);
		param.setZoneId("1");
		BeaconRestrictedZone info = beaconRestrictedZoneDao.getBeaconRestrictedZoneInfo(param);
		Assert.assertNotNull(info);
	}

	@Test
	public void testGetBeaconRestrictedZoneList() throws Exception {

		BeaconRestrictedZone param = new BeaconRestrictedZone();
		param.setBeaconNum(172544);
		List<BeaconRestrictedZone> list =  beaconRestrictedZoneDao.getBeaconRestrictedZoneList(param);

		if(list.size()>0) {
			System.out.println(list.get(0).getAdditionalAttribute().get("name"));
		}

		Assert.assertNotNull(list);
	}

	@Test
	public void testInsertBeaconRestrictedZone() throws Exception {

		BeaconRestrictedZone vo = new BeaconRestrictedZone();
		vo.setBeaconNum(172544);
		vo.setZoneType(ZoneType.FLOOR);
		vo.setZoneId("2");
		vo.setPermitted(BooleanType.FALSE);
		vo.putAdditionalAttribute("name", "test");

		beaconRestrictedZoneDao.insertBeaconRestrictedZone(vo);
	}

	@Test
	public void testUpdateBeaconRestrictedZone() throws Exception {
		BeaconRestrictedZone param = new BeaconRestrictedZone();
		param.setBeaconNum(172544);
		param.setZoneType(ZoneType.FLOOR);
		param.setZoneId("1");
		BeaconRestrictedZone info = beaconRestrictedZoneDao.getBeaconRestrictedZoneInfo(param);
		info.setPermitted(BooleanType.TRUE);
		beaconRestrictedZoneDao.updateBeaconRestrictedZone(info);

		BeaconRestrictedZone param2 = new BeaconRestrictedZone();
		param2.setBeaconNum(172544);
		param2.setZoneType(ZoneType.FLOOR);
		param2.setZoneId("1");
		BeaconRestrictedZone info2 = beaconRestrictedZoneDao.getBeaconRestrictedZoneInfo(param2);

		Assert.assertEquals(String.valueOf(info2.getZoneId()), "100");
	}

	@Test
	public void testDeleteBeaconRestrictedZone() throws Exception {

		BeaconRestrictedZone param = new BeaconRestrictedZone();
		param.setBeaconNum(172544);
		param.setZoneType(ZoneType.FLOOR);
		param.setZoneId("1");
		beaconRestrictedZoneDao.deleteBeaconRestrictedZone(param);


	}

}
