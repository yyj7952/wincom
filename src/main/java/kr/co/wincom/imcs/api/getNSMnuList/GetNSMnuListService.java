package kr.co.wincom.imcs.api.getNSMnuList;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.getNSChList.GetNSChListRequestVO;

public interface GetNSMnuListService {
	
	public GetNSMnuListResultVO getNSMnuList(GetNSMnuListRequestVO paramVO);
	
	//검수 STB 여부 조회
	public void getTestSbc(GetNSMnuListRequestVO paramVO) throws Exception;

	//카테고리 레벨 조회
	public void getCateLevel(GetNSMnuListRequestVO paramVO) throws Exception;
}
