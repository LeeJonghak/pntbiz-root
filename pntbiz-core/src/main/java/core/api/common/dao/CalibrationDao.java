package core.api.common.dao;

import core.api.common.domain.Calibration;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

import java.util.List;

@Repository
public class CalibrationDao extends BaseDao {

	public Calibration getCalibrationInfo(Calibration calibration) throws DataAccessException {
		return (Calibration) select("getCalibrationInfo", calibration);
	}

    public List<?> getCalibrationList(Calibration calibration) throws DataAccessException {
        return list("getCalibrationInfo", calibration);
    }

	public Calibration getCalibrationInfoByNum(Calibration calibration) throws DataAccessException {
		return (Calibration) select("getCalibrationInfoByNum", calibration);
	}

	public void insertCalibration(Calibration calibration) throws DataAccessException {
		insert("insertCalibration", calibration);
	}
	
	public void updateCalibration(Calibration calibration) throws DataAccessException {
		update("updateCalibration", calibration);
	}
}
