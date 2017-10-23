package api.map.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import core.api.map.dao.FloorDao;
import core.api.map.domain.Floor;
import framework.web.util.StringUtil;

@Service
public class FloorServiceImpl implements FloorService {
	
	@Autowired
	private FloorDao floorDao;
	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	@Value("#{config['contentsURL']}")
	private String contentsURL;
	@Value("#{config['floor.image.path']}")
	private String floorImagePath;
	@Value("#{config['floor.image.src']}")
	private String floorImageSrc;

	@Override
	public List<?> getFloorList(Floor floor) throws DataAccessException {
		List<?> floorList = null;
		floorList = floorDao.getFloorList(floor);
		logger.info("getFloorList {}", floorList.size());
		return floorList;
	}

	@Override
	public List<?> bindFloorList(List<?> list) throws ParseException {		
		List<Floor> flist = new ArrayList<Floor>();
		for(int i=0; i<list.size(); i++) {
			Floor floor = (Floor) list.get(i);
			floor = bindFloor(floor);
			flist.add(floor);
		}
		return flist;
	}
	
	@Override
	public Floor bindFloor(Floor floor) throws ParseException {
		String floorImageUrl = contentsURL + "/" + floor.getComNum() + floorImageSrc + "/";
		String imgUrl = ("".equals(StringUtil.NVL(floor.getImgSrc(), ""))) ? "" : floorImageUrl + floor.getImgSrc();
		floor.setImgURL(imgUrl);
		return floor;
	}

}
