package api.beacon.service;

import core.api.beacon.dao.NodeDao;
import core.api.beacon.domain.Node;
import core.api.contents.domain.Contents;
import framework.web.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 */
@Service
public class NodeServiceImpl implements NodeService {

    @Autowired
    private NodeDao nodeDao;

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
	public Node getNode(Node node) throws DataAccessException {
		Node nodeInfo = nodeDao.getNode(node);
		return nodeInfo;
	}

    @Override
    public List<?> getNodeContentsList(String UUID, String floor) throws DataAccessException, ParseException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("floor", floor);
        List<?> list = nodeDao.getNodeContentsList(param);
        list = bindNodeContentsList(list);

        return list;
    }

    @Override
    public List<?> getNodeContentsList(String UUID, String floor, String type) throws DataAccessException, ParseException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("floor", floor);
        param.put("type", type);
        List<?> list = nodeDao.getNodeContentsList(param);
        list = bindNodeContentsList(list);

        return list;
    }

    @Override
    public List<?> bindNodeContentsList(List<?> list) throws ParseException {
        List<Contents> clist = new ArrayList<Contents>();
        for(int i=0; i<list.size(); i++) {
            Contents contents = (Contents) list.get(i);
            contents = bindNodeContentsInfo(contents);
            clist.add(contents);
        }
        return clist;
    }

    @Override
    public Contents bindNodeContentsInfo(Contents contents) throws ParseException {
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
    public List<?> getNodeList(String UUID, String floor) throws DataAccessException {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("floor", floor);
        List<?> list = this.nodeDao.getNodeList(param);
        List<?> cateList = this.nodeDao.getNodeCategoryList();
        HashMap<String, String> cateMap = new HashMap<String, String>();
        for(Object obj: cateList) {
            @SuppressWarnings("unchecked")
			HashMap<String, String> cate = (HashMap<String, String>)obj;
            String sCD   = cate.get("sCD");
            String sName = cate.get("sName");
            cateMap.put(sCD, sName);
        }

        for(Object obj:list) {
            Node node = (Node)obj;
            node.setCateName(cateMap.get(node.getCate()));
        }

        return list;
    }

    @Override
    public List<?> getNodeList(String UUID, String floor, String type) throws DataAccessException {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("floor", floor);
        param.put("type", type);
        List<?> list = this.nodeDao.getNodeList(param);
        List<?> cateList = this.nodeDao.getNodeCategoryList();
        HashMap<String, String> cateMap = new HashMap<String, String>();
        for(Object obj: cateList) {
            @SuppressWarnings("unchecked")
			HashMap<String, String> cate = (HashMap<String, String>)obj;
            String sCD   = cate.get("sCD");
            String sName = cate.get("sName");
            cateMap.put(sCD, sName);
        }

        for(Object obj:list) {
            Node node = (Node)obj;
            node.setCateName(cateMap.get(node.getCate()));
        }

        return list;
    }

    @Override
    public List<?> getNodeEdgeList(String UUID, String floor) throws DataAccessException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("floor", floor);
        List<?> list = this.nodeDao.getNodeEdgeList(param);
        return list;
    }

    @Override
    public List<?> getNodeEdgeList(String UUID, String floor, String type) throws DataAccessException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("floor", floor);
        param.put("type", type);
        List<?> list = this.nodeDao.getNodeEdgeList(param);
        return list;
    }

    @Override
    public List<?> getNodeCategoryList() throws DataAccessException {
        List<?> list = this.nodeDao.getNodeCategoryList();
        return list;

    }

	@Override
	@Transactional
	public void deleteNode(String UUID, Long nodeNum) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("UUID", UUID);
		param.put("nodeNum", nodeNum);
		this.nodeDao.deleteNode(param);
	}

	@Override
	@Transactional
	public void deleteNodePair(String UUID, Integer nodeID) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("UUID", UUID);
		param.put("nodeID", nodeID);
		this.nodeDao.deleteNodePair(param);
	}

}
