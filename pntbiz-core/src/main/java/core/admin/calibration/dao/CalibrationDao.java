package core.admin.calibration.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.admin.calibration.domain.Calibration;
import core.admin.calibration.domain.CalibrationSearchParam;
import framework.db.dao.BaseDao;

@Repository
public class CalibrationDao extends BaseDao {
	
	public Integer getCalibrationCount(CalibrationSearchParam param) throws DataAccessException {
		return (Integer) select("getCalibrationCount", param);
	}
	
	public List<?> getCalibrationList(CalibrationSearchParam param) throws DataAccessException {
		return (List<?>) list("getCalibrationList", param);
	}
	
	public Calibration getCalibrationInfo(Calibration calibration) throws DataAccessException {
		return (Calibration) select("getCalibrationInfo", calibration);
	}
	
	public void insertCalibration(Calibration calibration) throws DataAccessException {
		insert("insertCalibration", calibration);
	}
	
	public void updateCalibration(Calibration calibration) throws DataAccessException {
		update("updateCalibration", calibration);
	}
	
	public void deleteCalibration(Calibration calibration) throws DataAccessException {
		delete("deleteCalibration", calibration);
	}
	
}
