package core.common.beacon.domain;

import java.util.List;

/**
 * Created by nsyun on 17. 8. 18..
 */
public class BeaconExternalWidthRestrictedZone extends BeaconExternal {

	private List<BeaconRestrictedZone> restrictedZone;

	public List<BeaconRestrictedZone> getRestrictedZone() {
		return restrictedZone;
	}

	public void setRestrictedZone(List<BeaconRestrictedZone> restrictedZone) {
		this.restrictedZone = restrictedZone;
	}
}
