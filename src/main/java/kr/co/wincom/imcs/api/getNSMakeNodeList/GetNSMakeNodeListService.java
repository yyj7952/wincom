package kr.co.wincom.imcs.api.getNSMakeNodeList;

import kr.co.wincom.imcs.api.getNSMakeNodeList.GetNSMakeNodeListRequestVO;
import kr.co.wincom.imcs.api.getNSMakeNodeList.GetNSMakeNodeListResultVO;

public interface GetNSMakeNodeListService {

	// GetNSMakeLists API
	public GetNSMakeNodeListResultVO getNSMakeNodeList(GetNSMakeNodeListRequestVO requestVO) throws Exception;

}
