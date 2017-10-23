package wms.map.service;

import wms.component.auth.LoginDetail;
import framework.web.util.PagingParam;
import core.wms.map.dao.GroupDao;
import core.wms.map.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * 그룹 관리(비콘, 노드, 펜스 등을 그룹으로 설정 가능)
 * 2015-05-07 nohsoo
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;

    @Override
    public List<?> getGroupAll(LoginDetail loginDetail) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        return groupDao.getGroupList(param);
    }

    @Override
    public List<?> getGroupList(PagingParam paging, HashMap<String, Object> param) {
        param.put("firstItemNo", paging.getFirstItemNo());
        param.put("pageSize", paging.getPageSize());
        param.put("page", paging.getPage());
        return groupDao.getGroupList(param);
    }

    @Override
    public Integer getGroupCount(PagingParam paging, HashMap<String, Object> param) {
        return groupDao.getGroupCount(param);
    }

    @Override
    public Group getGroup(Group group) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("groupNum", group.getGroupNum());
        return groupDao.getGroup(param);
    }

    @Override
    @Transactional
    public void registerGroup(LoginDetail loginDetail, Group group) {
        group.setComNum(loginDetail.getCompanyNumber());
        groupDao.insertGroup(group);
    }

    @Override
    @Transactional
    public void modifyGroup(Group group) {
        groupDao.modifyGroup(group);
    }

    @Override
    @Transactional
    public void deleteGroup(Group group) {
        groupDao.deleteGroup(group);
    }
}
