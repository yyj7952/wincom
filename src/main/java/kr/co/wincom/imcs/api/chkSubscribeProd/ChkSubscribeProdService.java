package kr.co.wincom.imcs.api.chkSubscribeProd;

public interface ChkSubscribeProdService {
	
	// 컨텐츠의 적법절차 구매 여부 조회 API
	public ChkSubscribeProdResultVO chkSubscribeProd(ChkSubscribeProdRequestVO paramVO);
	
}
