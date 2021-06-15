package kr.co.wincom.imcs.api.authorizeVView;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;
import kr.co.wincom.imcs.common.vo.ComCdnVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

public interface AuthorizeVViewService {
	
	 public AuthorizeVViewResultVO authorizeVView(AuthorizeVViewRequestVO paramVO);
	 
	 // PPV 여부 조회
	 public void getPpvYn(AuthorizeVViewRequestVO paramVO) throws Exception;
	 
	 // 워터마크 여부 조회
	 public String getWatermarkCk(AuthorizeVViewRequestVO paramVO) throws Exception;
	 
	 // 자막 정보 조회
	 public AuthorizeVViewResponseVO getSmiInfo(AuthorizeVViewRequestVO paramVO) throws Exception;
	 
	 // 노드 정보 조회
	 public String getNodeCd(AuthorizePlayIpVO playIpVO, AuthorizeVViewRequestVO paramVO) throws Exception;
	 
	 // CDN 정보 조회
	 public AuthorizePlayIpVO getVodServer1(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) throws Exception;
	 public AuthorizePlayIpVO getVodServer2(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) throws Exception;
	 public AuthorizePlayIpVO getVodServer3(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) throws Exception;
	 public AuthorizePlayIpVO getVodServer4(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) throws Exception;

	 // 썸네일 이미지 정보 조회
	 public void getThumnailInfo(AuthorizeVViewRequestVO paramVO, AuthorizeVViewResponseVO tempVO) throws Exception;

	 public List<AuthorizePlayIpVO> getVodM3u8Search(AuthorizeVViewRequestVO paramVO, AuthorizeVViewResponseVO tempVO) throws Exception;
}
