package kr.co.wincom.imcs.api.getNSGuideVod;

import java.util.List;

import kr.co.wincom.imcs.common.vo.UrlListVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSGuideVodDao
{
	// BASE_CD를 기반으로 서버IP 리스트 조회
	public List<UrlListVO> getServerIpbyBaseCd(GetNSGuideVodRequestVO nsGuideVodVO);
	
	// SA_ID, STB_MAC 을 기반으로 서버IP 리스트 조회
	public List<UrlListVO> getServerIpBySaMac(GetNSGuideVodRequestVO nsGuideVodVO);
	
	// SA_ID, STB_MAC 을 기반으로 서버IP 리스트 조회 DONG_CD는 고정
	public List<UrlListVO> getServerIpBySaMacDong(GetNSGuideVodRequestVO nsGuideVodVO);
	
	// GUIDE VOD 리스트를 조회
	public List<GetNSGuideVodResponseVO> getGuideVodInfo(GetNSGuideVodRequestVO paramVO);
}
