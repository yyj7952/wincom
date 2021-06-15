package kr.co.wincom.imcs.api.getNSLiveStat;


public interface GetNSLiveStatService
{
	/**
	 * 
	 * @param requestVO
	 * @return
	 * @see 구매/가입여부 확인 API 호출 
	 */
	public GetNSLiveStatResultVO getNSLiveStat(GetNSLiveStatRequestVO requestVO) throws Exception;
}
