package api.presence.bo.strategy.validator;

import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.enums.PresenceDataType;
import framework.exception.ExceptionType;
import framework.exception.PresenceException;
import api.presence.bo.strategy.AbstractPresenceStrategy;
import framework.web.util.DateUtil;

/**
 * Created by ucjung on 2017-06-08.
 */
public class ScannerRequestDataValidator extends AbstractPresenceStrategy {

    @Override
    protected void doStrategy(PresenceDataPackage presenceVo) {
        if (validate((ScannerPresenceRequestParam) presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM)) == false) {
            throw new PresenceException(ExceptionType.REQUEST_DATA_VALID_EXCEPTION);
        }

        // Request 데이타 처리 시간 설정
        presenceVo.setTypeData(PresenceDataType.DATE_CREATION,  DateUtil.getDate("yyyy-MM-dd a hh:mm:ss"));
    }

    private boolean validate(ScannerPresenceRequestParam requestParam) {
        boolean result;
        try {
            if (requestParam.getUUID().equals("")) return false;
            if (requestParam.getPos().size() == 0) return false;
            /*
            if (requestParam.getMajorVer() == 0) return false;
            if (requestParam.getMinorVer() == 0) return false;
            */
            if (requestParam.getFloor().equals("")) return false;

            return true;
        } catch (Exception ex) {
            logger.debug("{}", ex);
            result = false;
        }
        return result;
    }
}
