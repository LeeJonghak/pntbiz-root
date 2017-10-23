package wms.map.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import core.wms.admin.company.domain.Company;
import core.wms.map.domain.Floor;

public interface FloorService {
	// Floor Info
	public Integer getFloorCount(Floor floor) throws DataAccessException;
	public boolean checkFloorDuplication(Floor floor) throws DataAccessException;
	public Floor getFloorInfo(Floor floor) throws DataAccessException;
	public Map<String, String> uploadFloorImage(MultipartRequest multi, Floor floor) throws IOException;
	public Map<String, String> uploadFloorImageLocal(MultipartRequest multi, Floor floor) throws IOException;
	public String deleteFloorImage(Floor floor, String num, String type) throws IOException, SftpException;
	public String deleteFloorImageLocal(Floor floor, String num, String type) throws IOException, SftpException;
	public void updateFloorBlankImage(Floor floor, String num) throws DataAccessException;

	// Floor Transaction
	public void registerFloor(MultipartRequest multi, Floor floor) throws DataAccessException, IOException, SftpException;
	public void modifyFloor(MultipartRequest multi, Floor floor) throws DataAccessException, IOException, SftpException;
	public void removeFloor(Floor floor) throws DataAccessException, IOException, SftpException;

	// list
	public List<?> getFloorList(Floor floor) throws DataAccessException;
	public List<Floor> bindFloorList(List<Floor> list) throws ParseException;
	public Floor bindFloor(Floor floor) throws ParseException;
	public List<?> getFloorGroup(Floor floor) throws DataAccessException;

    public List<?> getFloorCodeList(Company floor) throws DataAccessException;

}