package api.contents.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import core.common.code.dao.CodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import framework.web.util.StringUtil;
import core.api.contents.dao.ContentsDao;
import core.api.contents.domain.Contents;

@Service
public class ContentsServiceImpl implements ContentsService {
	@Autowired
	private ContentsDao contentsDao;
	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	@Autowired
	private CodeDao codeDao;
	
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
	public List<?> getContentsList(Contents contents) throws DataAccessException {
		List<?> contentsList = null;
		contentsList = contentsDao.getContentsList(contents);
		logger.info("getContentsList {}", contentsList.size());
		return contentsList;
	}

	@Override
	public Contents getContentsInfo(Contents contents) throws DataAccessException {
		Contents contentsInfo = null;
		contentsInfo = contentsDao.getContentsInfo(contents);
		logger.info("getContentsInfo", contentsInfo);	
		return contentsInfo;
	}
	
	@Override
	public Contents getContentsMessage(Contents contents) throws DataAccessException {
		Contents contentsMessage = null;
		contentsMessage = contentsDao.getContentsMessage(contents);
		logger.info("getContentsMessage", contentsMessage);	
		return contentsMessage;
	}
	
	@Override
	public List<?> bindContentsList(List<?> list) throws ParseException {
		List<Contents> clist = new ArrayList<Contents>();
		for(int i=0; i<list.size(); i++) {
			Contents contents = (Contents) list.get(i);
			contents = bindContentsInfo(contents);
			clist.add(contents);
		}
		return clist;
	}
	
	@Override
	public Contents bindContentsInfo(Contents contents) throws ParseException {
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
//		contents.setAcName(StringUtil.NVL(contents.getAcName(), ""));
//		contents.setRefNum(StringUtil.NVL(contents.getRefNum(), 0));
//		contents.setRefType(StringUtil.NVL(contents.getRefType(), ""));
//		contents.setRefSubType(StringUtil.NVL(contents.getRefSubType(), ""));
//		contents.setEvtNum(StringUtil.NVL(contents.getEvtNum(), 0));		
		return contents;
	}

}
