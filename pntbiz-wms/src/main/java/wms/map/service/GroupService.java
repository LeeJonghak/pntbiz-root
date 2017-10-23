package wms.map.service;

import wms.component.auth.LoginDetail;
import framework.web.util.PagingParam;
import core.wms.map.domain.Group;

import java.util.HashMap;
import java.util.List;

/**
 * 그룹 관리(비콘, 노드, 펜스 등을 그룹으로 설정 가능)
 * 2015-05-07 nohsoo
 */
public interface GroupService {

    List<?> getGroupAll(LoginDetail loginDetail);

    List<?> getGroupList(PagingParam paging, HashMap<String, Object> param);

    Integer getGroupCount(PagingParam paging, HashMap<String, Object> param);

    Group getGroup(Group group);

    public void registerGroup(LoginDetail loginDetail, Group group);

    void modifyGroup(Group group);

    void deleteGroup(Group group);

}
