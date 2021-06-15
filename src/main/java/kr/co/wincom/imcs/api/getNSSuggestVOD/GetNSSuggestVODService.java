package kr.co.wincom.imcs.api.getNSSuggestVOD;

import java.util.List;



public interface GetNSSuggestVODService {
	
	// 추천 VOD 정보 가져오기 API 호출 
	public GetNSSuggestVODResultVO getNSSuggestVOD(GetNSSuggestVODRequestVO paramVO);
	 
	// 검수 STB 여부 조회
	public GetNSSuggestVODRequestVO getTestSbc(GetNSSuggestVODRequestVO paramVO) throws Exception;
	
	// 랭킹 조회(앨범 ID X)
	public void getRanking1(GetNSSuggestVODRequestVO paramVO) throws Exception;
	
	// 랭킹 조회(앨범 ID O)
	public void getRanking2(GetNSSuggestVODRequestVO paramVO) throws Exception;
	
	// 추천 VOD 조회
	public List<GetNSSuggestVODResponseVO> getNSSuggestVODList(GetNSSuggestVODRequestVO paramVO) throws Exception;
	
	// 추천 VOD 상세조회
	public List<GetNSSuggestVODResponseVO> getNSSuggestVODDtl(GetNSSuggestVODRequestVO paramVO) throws Exception;

}
