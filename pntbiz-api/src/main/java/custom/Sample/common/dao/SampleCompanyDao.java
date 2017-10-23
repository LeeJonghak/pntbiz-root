package custom.Sample.common.dao;

import core.api.common.domain.Company;
import core.api.common.dao.CompanyDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class SampleCompanyDao extends CompanyDao {

	@Value("#{config['site.shortName']}")
	private String siteShortName;
	
	public Company getCompanyInfoByUUID(Company company) throws DataAccessException {
		return (Company) select("getCompanyInfoByUUID" + siteShortName, company);
	}
}