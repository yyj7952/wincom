package kr.co.wincom.imcs.api.getNSMakePrefIP;

import kr.co.wincom.imcs.api.getNSMakePrefIP.GetNSMakePrefIPRequestVO;
import kr.co.wincom.imcs.api.getNSMakePrefIP.GetNSMakePrefIPResultVO;

public interface GetNSMakePrefIPService {

	// GetNSMakeLists API
	public GetNSMakePrefIPResultVO getNSMakePrefIP(GetNSMakePrefIPRequestVO requestVO) throws Exception;

}
