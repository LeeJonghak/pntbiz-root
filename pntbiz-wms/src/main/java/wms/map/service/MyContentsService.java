package wms.map.service;

import wms.component.auth.LoginDetail;
import framework.web.util.PagingParam;

import java.util.List;

/**
 */
public interface MyContentsService {

    public List<?> getMyBeaconContentsList(LoginDetail loginDetail, Long beaconNum);
    public List<?> getMyNodeContentsList(LoginDetail loginDetail, Long nodeNum);
    public List<?> getContentsList(LoginDetail loginDetail, Long refNum, String refType, PagingParam paging, String opt, String keyword);
    public Integer countContentsList(LoginDetail loginDetail, Long refNum, String refType, String opt, String keyword);
    public List<?> getMyGeofencingContentsList(LoginDetail loginDetail, Long fcNum);
    public void assignMyBeaconContent(LoginDetail loginDetail, Long beaconNum, Long[] contentNum);
    public void unassignMyBeaconContent(LoginDetail loginDetail, Long beaconNum, Long[] contentNum);
    public void assignMyGeofencingContent(LoginDetail loginDetail, String fenceEventType, Long fcNum, Long[] conNums) throws Exception;
    public void unassignMyGeofencingContent(LoginDetail loginDetail, Long fcNum, String[] contentNum) throws Exception;
    public List<?> getCodeActionAll(LoginDetail loginDetail);
    public void assignMyBeaconCodeAction(LoginDetail loginDetail, Long beaconNum, Integer[] codeNums);
    public void unassignMyBeaconCodeAction(LoginDetail loginDetail, Long beaconNum, Integer[] codeNums);
    public void assignMyGeofencingCodeAction(LoginDetail loginDetail, Long fcNum, String fenceEventTyp, Integer[] codeNums) throws Exception;
    public void unassignMyGeofencingCodeAction(LoginDetail loginDetail, Long fcNum, String[] codeNums) throws Exception;
    public List<?> getMyGeofencingCodeActionList(LoginDetail loginDetail, Long fcNum);

    public List<?> getMyBeaconCodeActionList(LoginDetail loginDetail, long beaconNum);

    public void assignMyNodeContent(LoginDetail loginDetail, Long nodeNum, Long[] conNums);
    public void unassignMyNodeContent(LoginDetail loginDetail, Long nodeNum, Long[] conNums);
}
