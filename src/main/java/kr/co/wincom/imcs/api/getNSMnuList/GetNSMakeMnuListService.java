package kr.co.wincom.imcs.api.getNSMnuList;


import java.util.List;

public interface GetNSMakeMnuListService {

	// GetNSMakeLists API
	public GetNSMnuListResultVO getNSMakeMnuList(GetNSMnuListRequestVO requestVO) throws Exception;
	
	// GetNSLists에서도 사용하기 위하여 모듈화 
	public GetNSMnuListResultVO getNSMakeMnuList(GetNSMnuListRequestVO paramVO, String apiInfo) throws Exception;
	
	public List<GetNSMnuListResponseVO> getNSMnuList(GetNSMnuListRequestVO requestVO) throws Exception;
	
	// 검수 STB 여부 조회
	public void getTestSbc(GetNSMnuListRequestVO paramVO) throws Exception;

}
