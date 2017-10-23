package api.beacon.service;

import core.api.beacon.domain.BeaconContents;
import core.api.map.domain.FloorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import framework.web.util.StringUtil;
import core.api.beacon.dao.BeaconDao;
import core.api.beacon.domain.Beacon;
import core.api.beacon.domain.BeaconState;
import core.api.contents.domain.Contents;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BeaconServiceImpl implements BeaconService {
	
	@Autowired
	private BeaconDao beaconDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("#{config['contentsURL']}")
	private String contentsURL;
	@Value("#{config['contents.image.path']}")
	private String contentsImagePath;
	@Value("#{config['contents.image.src']}")
	private String contentsImageSrc;	
	@Value("#{config['contents.sound.path']}")
	private String contentsSoundPath;
	@Value("#{config['contents.sound.src']}")
	private String contentsSoundSrc;	

	@Override
	public Beacon getBeaconInfo(Beacon beacon) throws DataAccessException {
		Beacon beaconInfo = null;
		beaconInfo = beaconDao.getBeaconInfo(beacon);
		logger.info("beaconInfo {}", beaconInfo);
		return beaconInfo;
	}

	@Override
	public Beacon getBeaconInfoByMacAddr(String macAddr) throws DataAccessException {
		Beacon beaconInfo = null;
		beaconInfo = beaconDao.getBeaconInfoByMacAddr(macAddr);
		logger.info("beaconInfo {}", beaconInfo);
		return beaconInfo;
	}

    @Override
    public List<?> getBeaconList(String UUID, Integer majorVer, Integer minorVer, String conType) throws DataAccessException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("majorVer", majorVer);
        param.put("minorVer", minorVer);
        param.put("conType", conType);
        List<?> list = this.beaconDao.getBeaconList(param);
        return list;
    }
    
    @Override
    public List<?> getBeaconList(String UUID) throws DataAccessException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        List<?> list = this.beaconDao.getBeaconListByAll(param);
        return list;
    }

    @Override
    public List<?> getBeaconContentsList(String UUID, Integer majorVer, Integer minorVer, String conType) throws ParseException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("majorVer", majorVer);
        param.put("minorVer", minorVer);
        param.put("conType", conType);
        List<?> list = beaconDao.getBeaconContentsList(param);
        list = bindBeaconContentsList(list);

        return list;
    }
    
    @Override
	public List<?> bindBeaconContentsList(List<?> list) throws ParseException {
		List<Contents> clist = new ArrayList<Contents>();
		for(int i=0; i<list.size(); i++) {
			Contents contents = (Contents) list.get(i);
			contents = bindBeaconContentsInfo(contents);
			clist.add(contents);
		}
		return clist;
	}
	
	@Override
	public Contents bindBeaconContentsInfo(Contents contents) throws ParseException {
		String contentsImageURL = contentsURL + "/" + contents.getComNum() + contentsImageSrc + "/";
		String contentsSoundURL = contentsURL + "/" + contents.getComNum() + contentsSoundSrc + "/";

		String imgURL1 = ("".equals(StringUtil.NVL(contents.getImgSrc1(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc1(), 0, 6) + "/" + contents.getImgSrc1();
		String imgURL2 = ("".equals(StringUtil.NVL(contents.getImgSrc2(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc2(), 0, 6) + "/" + contents.getImgSrc2();
		String imgURL3 = ("".equals(StringUtil.NVL(contents.getImgSrc3(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc3(), 0, 6) + "/" + contents.getImgSrc3();
		String imgURL4 = ("".equals(StringUtil.NVL(contents.getImgSrc4(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc4(), 0, 6) + "/" + contents.getImgSrc4();
		String imgURL5 = ("".equals(StringUtil.NVL(contents.getImgSrc5(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc5(), 0, 6) + "/" + contents.getImgSrc5();
		String soundURL1 = ("".equals(StringUtil.NVL(contents.getSoundSrc1(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc1(), 0, 6) + "/" + contents.getSoundSrc1();
		String soundURL2 = ("".equals(StringUtil.NVL(contents.getSoundSrc2(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc2(), 0, 6) + "/" + contents.getSoundSrc2();
		String soundURL3 = ("".equals(StringUtil.NVL(contents.getSoundSrc3(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc3(), 0, 6) + "/" + contents.getSoundSrc3();
		contents.setImgURL1(imgURL1);
		contents.setImgURL2(imgURL2);
		contents.setImgURL3(imgURL3);
		contents.setImgURL4(imgURL4);
		contents.setImgURL5(imgURL5);
		contents.setSoundURL1(soundURL1);
		contents.setSoundURL2(soundURL2);
		contents.setSoundURL3(soundURL3);
		return contents;
	}

    @Override
    public List<?> getBeaconActionList(String UUID, Integer majorVer, Integer minorVer, String conType) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("majorVer", majorVer);
        param.put("minorVer", minorVer);
        param.put("conType", conType);
        List<?> list = beaconDao.getBeaconActionList(param);
        return list;
    }
    
    @Override
	//@Transactional  MYISAM으로 변경
	public void registerBeaconState(BeaconState beaconState)	throws DataAccessException {
		beaconDao.insertBeaconState(beaconState);
	}

	@Override
	public List<BeaconContents> getBeaconFloorCodeList(String UUID, String conType, List<FloorCode> floorCodeList) throws DataAccessException {
		List<BeaconContents> list = new ArrayList<BeaconContents>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("UUID", UUID);
		param.put("conType", conType);
		param.put("floorCodeList", floorCodeList);
		list = beaconDao.getBeaconFloorCodeList(param);
		return list;
	}

	@Override
	public List<BeaconContents> getBeaconFloorCodeListByField(String UUID, String conType, List<FloorCode> floorCodeList) throws DataAccessException {
		List<BeaconContents> list = new ArrayList<BeaconContents>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("UUID", UUID);
		param.put("conType", conType);
		param.put("floorCodeList", floorCodeList);
		list = beaconDao.getBeaconFloorCodeListByField(param);
		return list;
	}

	@Override
	public void updateScannerBeaconState(Beacon beacon) {
		beaconDao.updateScannerBeaconState(beacon);
	}

}