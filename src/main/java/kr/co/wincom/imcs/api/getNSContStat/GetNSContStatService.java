package kr.co.wincom.imcs.api.getNSContStat;


public interface GetNSContStatService
{
	/**
	 * 
	 * @param requestVO
	 * @return
	 * @see 구매/가입여부 확인 API 호출 
	 */
	public GetNSContStatResultVO getNSContStat(GetNSContStatRequestVO requestVO) throws Exception;
}
