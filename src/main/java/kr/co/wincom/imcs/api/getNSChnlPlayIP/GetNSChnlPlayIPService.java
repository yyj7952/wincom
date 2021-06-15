package kr.co.wincom.imcs.api.getNSChnlPlayIP;

public interface GetNSChnlPlayIPService {

	// 기지국 코드에 따른 CDN Play IP 정보를 제공한다.
	public GetNSChnlPlayIPResultVO getNSChnlPlayIP(GetNSChnlPlayIPRequestVO paramVO);

}
