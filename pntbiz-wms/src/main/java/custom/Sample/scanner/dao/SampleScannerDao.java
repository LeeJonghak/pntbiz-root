package custom.Sample.scanner.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import core.wms.scanner.dao.ScannerDao;
import core.wms.scanner.domain.ScannerSearchParam;

import java.util.List;

@Repository
public class SampleScannerDao extends ScannerDao {

	@Value("#{config['site.shortName']}")
	private String siteShortName;

	@Override
	public Integer getScannerCount(ScannerSearchParam param) throws DataAccessException {
		return (Integer) select("getScannerCount" + siteShortName, param);
	}

	@Override
	public List<?> getScannerList(ScannerSearchParam param) throws DataAccessException {
		return (List<?>) list("getScannerList" + siteShortName, param);
	}	

}