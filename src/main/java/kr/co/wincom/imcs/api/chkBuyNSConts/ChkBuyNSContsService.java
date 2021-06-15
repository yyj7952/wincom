package kr.co.wincom.imcs.api.chkBuyNSConts;

public interface ChkBuyNSContsService {
	
	// 컨텐츠의 적법절차 구매 여부 조회 API
	public ChkBuyNSContsResultVO chkBuyNSConts(ChkBuyNSContsRequestVO paramVO);
	
}
