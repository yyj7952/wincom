package kr.co.wincom.imcs.api.getNSVodPlayIP;

public interface GetNSVodPlayIPService {

	// 기지국 코드에 따른 CDN Play IP 정보를 제공한다.
	public GetNSVodPlayIPResultVO getNSVodPlayIP(GetNSVodPlayIPRequestVO paramVO);

}
