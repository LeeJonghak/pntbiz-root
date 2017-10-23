package api.map.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import core.api.map.domain.Floor;

public interface FloorService {
	public List<?> getFloorList(Floor floor) throws DataAccessException;
	public List<?> bindFloorList(List<?> list) throws ParseException;
	public Floor bindFloor(Floor floor) throws ParseException;
}