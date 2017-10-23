package api.presence.service;

import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import core.api.presence.dao.PresenceDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PresenceServiceImpl implements PresenceService {
	
	@Autowired
	private PresenceDao presenceDao;
	private Logger logger = LoggerFactory.getLogger(getClass());


}
