package api.contents.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import core.api.contents.domain.Contents;

public interface ContentsService {
	public List<?> getContentsList(Contents contents) throws DataAccessException;
	public Contents getContentsInfo(Contents contents) throws DataAccessException;
	public Contents getContentsMessage(Contents contents) throws DataAccessException;
	public List<?> bindContentsList(List<?> list) throws ParseException;
	public Contents bindContentsInfo(Contents contents) throws ParseException;
}
