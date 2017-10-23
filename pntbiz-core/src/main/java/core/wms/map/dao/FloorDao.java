package core.wms.map.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.wms.admin.company.domain.Company;
import framework.db.dao.BaseDao;
import core.wms.map.domain.Floor;

@Repository
public class FloorDao extends BaseDao {

	public Integer getFloorCount(Floor floor) throws DataAccessException {
		return (Integer) select("getFloorCount", floor);
	}

	public Integer checkFloorDuplication(Floor floor) throws DataAccessException {
		return (Integer) select("checkFloorDuplication", floor);
	}

	public Floor getFloorInfo(Floor floor) throws DataAccessException {
		return (Floor) select("getFloorInfo", floor);
	}

	public void insertFloor(Floor floor) throws DataAccessException {
		insert("insertFloor", floor);
	}

	public void updateFloor(Floor floor) throws DataAccessException {
		update("updateFloor", floor);
	}

	public void updateFloorBlankImage(Floor floor) throws DataAccessException {
		update("updateFloorBlankImage", floor);
	}

	public void deleteFloor(Floor floor) throws DataAccessException {
		delete("deleteFloor", floor);
	}

	public List<?> getFloorList(Floor floor) throws DataAccessException {
		return (List<?>) list("getFloorList", floor);
	}

	public List<?> getFloorGroup(Floor floor) throws DataAccessException {
		return (List<?>) list("getFloorGroup", floor);
	}

    /*
     * TB_FLOOR_CODE
     */
    public List<?> getFloorCodeList(Company floor) throws DataAccessException {
        return (List<?>) list("getFloorCodeList1", floor);
    }
}
