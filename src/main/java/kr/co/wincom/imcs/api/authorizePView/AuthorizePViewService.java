package kr.co.wincom.imcs.api.authorizePView;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;
import kr.co.wincom.imcs.common.vo.ComCdnVO;


public interface AuthorizePViewService {
	
	 public AuthorizePViewResultVO authorizePView(AuthorizePViewRequestVO paramVO);
	 
	 // 워터마크 여부 조회
	 public String getWatermarkYn(AuthorizePViewRequestVO paramVO) throws Exception;
	 
	 // 자막파일 정보 조회
	 public AuthorizePViewResponseVO getSmiInfo(AuthorizePViewRequestVO paramVO) throws Exception;
	 
	 // 노드 정보 조회
	 public String getNodeCd(AuthorizePlayIpVO playIpVO, AuthorizePViewRequestVO paramVO) throws Exception;
	 
	 // CDN 정보 조회
	 public AuthorizePlayIpVO getVodServer1(AuthorizePViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) throws Exception;
	 public AuthorizePlayIpVO getVodServer2(AuthorizePViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) throws Exception;
	 public AuthorizePlayIpVO getVodServer3(AuthorizePViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) throws Exception;
	 public AuthorizePlayIpVO getVodServer4(AuthorizePViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) throws Exception;
	 	
	// 구간 점프 이미지 파일 조회
	public void getThumnailInfo(AuthorizePViewRequestVO paramVO, AuthorizePViewResponseVO tempVO) throws Exception;
	
	public List<AuthorizePlayIpVO> getVodM3u8Search(AuthorizePViewRequestVO paramVO, AuthorizePViewResponseVO tempVO) throws Exception;
}
