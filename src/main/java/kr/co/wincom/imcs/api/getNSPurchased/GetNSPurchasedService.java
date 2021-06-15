package kr.co.wincom.imcs.api.getNSPurchased;

import java.util.List;

import kr.co.wincom.imcs.common.vo.CateInfoVO;


public interface GetNSPurchasedService
{
	//구매내역 리스트 가져오기 API 호출 
	public GetNSPurchasedResultVO getNSPurchased(GetNSPurchasedRequestVO requestVO) throws Exception;
	
	//구매내역 리스트 조회
	public List<GetNSPurchasedResponseVO> getNSPurchasedListNsc(GetNSPurchasedRequestVO paramVO) throws Exception;
	
	//카테고리명 조회
	public String getCateName(GetNSPurchasedRequestVO paramVO) throws Exception;
	
	//카테고리 구분 조회
	public CateInfoVO getCateGbList(GetNSPurchasedRequestVO paramVO) throws Exception;
	
	//부가세 요율 조회
	public String getSurtaxRateInfo(GetNSPurchasedRequestVO paramVO) throws Exception;
}
