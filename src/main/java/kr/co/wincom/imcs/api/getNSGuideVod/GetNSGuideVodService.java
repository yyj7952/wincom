package kr.co.wincom.imcs.api.getNSGuideVod;

import java.util.List;

import kr.co.wincom.imcs.common.vo.UrlListVO;


public interface GetNSGuideVodService {
	// GUIDE VOD 정보 가져오기 API
	public GetNSGuideVodResultVO getNSGuideVod(GetNSGuideVodRequestVO requestVO) throws Exception;
	
	// 서버 IP 리스트 조회
	public UrlListVO getServerIpList(GetNSGuideVodRequestVO paramVO) throws Exception;

	// BASE_CD를 기반으로 서버IP 리스트 조회
	public UrlListVO getServerIpByBaseCd(GetNSGuideVodRequestVO paramVO) throws Exception;
	
	// SA_ID, STB_MAC 을 기반으로 서버IP 리스트 조회
	public UrlListVO getServerIpBySaMac(GetNSGuideVodRequestVO paramVO) throws Exception;
	
	// SA_ID, STB_MAC 을 기반으로 서버IP 리스트 조회
	public UrlListVO getServerIpBySaMacDong(GetNSGuideVodRequestVO paramVO) throws Exception;
	
	// GUIDE VOD 리스트를 조회한다
	public List<GetNSGuideVodResponseVO> getGuideVodInfo(GetNSGuideVodRequestVO paramVO) throws Exception;
}
