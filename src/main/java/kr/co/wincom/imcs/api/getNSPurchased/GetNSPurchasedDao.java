package kr.co.wincom.imcs.api.getNSPurchased;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.getNSLists.GetNSListsRequestVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;

@Repository
public interface GetNSPurchasedDao
{
	// 검수 STB여부 조회 (20180724 권형도)
	public List<String> getTestSbc(GetNSPurchasedRequestVO requestVO);

	// 구매내역 리스트 조회
	public List<GetNSPurchasedResponseVO> getNSPurchasedListNsc(GetNSPurchasedRequestVO paramVO);
	
	// nScreen 구매내역 리스트 조회
	public List<GetNSPurchasedResponseVO> getNSPurchasedListNsc2(GetNSPurchasedRequestVO paramVO);
	
	// 카테고리 명 조회
	public List<String> getCateName(GetNSPurchasedRequestVO paramVO);

	// 카테고리 구분 정보 조회
	public List<CateInfoVO> getCateGbList(GetNSPurchasedRequestVO paramVO);
	
	// 카테고리 구분 정보 조회
	public List<CateInfoVO> getCateGbList2(GetNSPurchasedRequestVO paramVO);
	
	// 부가세요율 조회
	public List<String> getSurtaxRateInfo();
}
