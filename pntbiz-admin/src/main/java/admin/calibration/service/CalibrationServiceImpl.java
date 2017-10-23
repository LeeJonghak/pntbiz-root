package admin.calibration.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import core.common.code.dao.CodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.admin.calibration.dao.CalibrationDao;
import core.admin.calibration.domain.Calibration;
import core.admin.calibration.domain.CalibrationSearchParam;
import core.common.code.domain.Code;

import framework.web.util.StringUtil;

@Service
public class CalibrationServiceImpl implements CalibrationService {
	@Autowired
	private CalibrationDao calibrationDao;
	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	@Autowired
	private CodeDao codeDao;
	
	@Override
	public Integer getCalibrationCount(CalibrationSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = calibrationDao.getCalibrationCount(param);
		logger.info("getCalibrationCount {}", cnt);	
		return cnt;
	}
	
	@Override
	public List<?> getCalibrationList(CalibrationSearchParam param) throws DataAccessException {
		List<?> calibrationList = null;
		calibrationList = calibrationDao.getCalibrationList(param);
		logger.info("getCalibrationList {}", calibrationList.size());
		return calibrationList;
	}

	@Override
	public List<?> bindCalibrationList(List<?> list, List<?> markerCD, List<?> telecomCD) throws ParseException {
		List<Calibration> clist = new ArrayList<Calibration>();		
		for(int i=0; i<list.size(); i++) {
			Calibration calibration = (Calibration) list.get(i);
			calibration = bindCalibration(calibration, markerCD, telecomCD);
			clist.add(calibration);
		}
		return clist;
	}
	
	@Override
	public Calibration bindCalibration(Calibration calibration, List<?> markerCD, List<?> telecomCD) throws ParseException {		
		for(int i=0; i<markerCD.size(); i++) {
			Code code = (Code) markerCD.get(i);
			if(calibration.getMaker().equals(code.getsCD())) {
				calibration.setMaker(code.getsName());
			}
		}
		for(int i=0; i<telecomCD.size(); i++) {
			Code code = (Code) telecomCD.get(i);
			if(calibration.getTelecom().equals(code.getsCD())) {
				calibration.setTelecom(code.getsName());
			}
		}
		return calibration;
	}
	
	@Override
	public Calibration getCalibrationInfo(Calibration calibration) throws DataAccessException {
		Calibration calibrationInfo = null;
		calibrationInfo = calibrationDao.getCalibrationInfo(calibration);
		logger.info("getCalibrationInfo {}", calibrationInfo);	
		return calibrationInfo;
	}
	
	@Override
	@Transactional
	public void registerCalibration(HttpServletRequest request, Calibration calibration 
			) throws DataAccessException{
		calibration.setMaker(StringUtil.NVL(request.getParameter("maker"), ""));
		calibration.setTelecom(StringUtil.NVL(request.getParameter("telecom"), ""));
		calibration.setModelName(StringUtil.NVL(request.getParameter("modelName"), ""));
		calibration.setDeviceName(StringUtil.NVL(request.getParameter("deviceName"), ""));
		calibration.setOs(StringUtil.NVL(request.getParameter("os"), ""));
		calibration.setRssi(Integer.parseInt(StringUtil.NVL(request.getParameter("rssi"), "-90")));		
		calibrationDao.insertCalibration(calibration);
	}
	
	@Override
	@Transactional
	public void modifyCalibration(HttpServletRequest request, Calibration calibration
			) throws DataAccessException{
		calibration.setMaker(StringUtil.NVL(request.getParameter("maker"), ""));
		calibration.setTelecom(StringUtil.NVL(request.getParameter("telecom"), ""));
		calibration.setModelName(StringUtil.NVL(request.getParameter("modelName"), ""));
		calibration.setDeviceName(StringUtil.NVL(request.getParameter("deviceName"), ""));
		calibration.setOs(StringUtil.NVL(request.getParameter("os"), ""));
		calibration.setRssi(Integer.parseInt(StringUtil.NVL(request.getParameter("rssi"), "-90")));
		calibrationDao.updateCalibration(calibration);
	}
	
	@Override
	@Transactional
	public void removeCalibration(Calibration calibration) throws DataAccessException {
		calibrationDao.deleteCalibration(calibration);
	}

}
