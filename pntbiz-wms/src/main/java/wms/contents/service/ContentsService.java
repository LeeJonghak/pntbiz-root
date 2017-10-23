package wms.contents.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import wms.component.auth.LoginDetail;
import core.wms.contents.domain.Contents;
import core.wms.contents.domain.ContentsMapping;
import core.wms.contents.domain.ContentsMappingSearchParam;
import core.wms.contents.domain.ContentsSearchParam;
import core.wms.contents.domain.ContentsType;
import core.wms.contents.domain.ContentsTypeComponent;
import core.wms.contents.domain.ContentsTypeSearchParam;

public interface ContentsService {
	// Contents list
	public Integer getContentsCount(ContentsSearchParam param) throws DataAccessException;
	public List<?> getContentsList(ContentsSearchParam param) throws DataAccessException;
	public List<?> bindContentsList(List<?> list, List<?> conCD) throws ParseException;
	
	// Contents info
	public Contents getContentsInfo(Contents contents) throws DataAccessException;
	public Contents bindContents(Contents contents) throws ParseException;
	
	public Map<String, String> uploadContentsFile(MultipartRequest multi, Contents contents) throws IOException;
	public String deleteContentsFile(Contents contents, String fileType, String num, String type) throws IOException, SftpException;	
	public void updateContentsBlankFile(Contents contents, String fileType, String num) throws DataAccessException;
	public Integer getContentsFileCount(MultipartRequest multi);
		
	// Contents Transaction
	public void registerContents(MultipartRequest multi, Contents contents) throws DataAccessException, IOException, SftpException;
	public void modifyContents(MultipartRequest multi, Contents contents) throws DataAccessException, IOException, SftpException;
	public void removeContents(Contents contents) throws DataAccessException, IOException, SftpException;	
	
	// Contents Mapping list
	public Integer getContentsMappingCount(ContentsMappingSearchParam param) throws DataAccessException;
	public List<?> getContentsMappingList(ContentsMappingSearchParam param) throws DataAccessException;
	public List<?> bindContentsMappingList(List<?> list, List<?> refCD, List<?> refCD2, List<?> refSubCD, List<?> conCD) throws ParseException;
	public List<ContentsMapping> bindMappingDataList(Integer comNum, String mappingType) throws DataAccessException;
	public void assignContentsReference(LoginDetail loginDetail, ContentsMapping contentsMapping) throws Exception, DataAccessException;
	public String unassignContentsReference(String mappingInfo, String delType) throws Exception;
	
	// Contents Type
	public ContentsType getContentsTypeInfo(ContentsType contentsType) throws DataAccessException;
	public Integer getContentsTypeCount(ContentsTypeSearchParam param) throws DataAccessException;
	public List<?> getContentsTypeList(ContentsTypeSearchParam param) throws DataAccessException;	
	
	public void registerContentsType(ContentsType contentsType, List<ContentsTypeComponent> contentsTypeComponentList) throws DataAccessException;
	public void modifyContentsType(ContentsType contentsType, List<ContentsTypeComponent> contentsTypeComponentList) throws DataAccessException;
	public void removeContentsType(ContentsType contentsType) throws DataAccessException;
	
	// Contents Type Component
	public List<?> getContentsTypeComponentList(ContentsTypeComponent contentsTypeComponent) throws DataAccessException;
	
}
