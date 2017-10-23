package admin.calibration.service;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;

import core.admin.calibration.domain.Calibration;
import core.admin.calibration.domain.CalibrationSearchParam;

public interface CalibrationService {
	// Calibration list
	public Integer getCalibrationCount(CalibrationSearchParam param) throws DataAccessException;
	public List<?> getCalibrationList(CalibrationSearchParam param) throws DataAccessException;
	public List<?> bindCalibrationList(List<?> list, List<?> markerCD, List<?> telecomCD) throws ParseException;
	
	// Calibration info
	public Calibration getCalibrationInfo(Calibration calibration) throws DataAccessException;
	public Calibration bindCalibration(Calibration calibration, List<?> markerCD, List<?> telecomCD) throws ParseException;
	
	// Calibration Transaction
	public void registerCalibration(HttpServletRequest request, Calibration calibration) throws DataAccessException;
	public void modifyCalibration(HttpServletRequest request, Calibration calibration) throws DataAccessException;
	public void removeCalibration(Calibration calibration) throws DataAccessException;	

}
