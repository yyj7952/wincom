package kr.co.wincom.imcs.api.authorizeNView;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdnVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;
import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface AuthorizeNViewService {
	
	// SVOD시청 인증정보 및 파일명 조회 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public AuthorizeNViewResultVO authorizeNView(AuthorizeNViewRequestVO paramVO);

	// 쿠폰 정보 조회 (SMARTUX 함수 이용)
	public ComCpnVO getCpnInfo(AuthorizeNViewRequestVO paramVO) throws Exception; 
	
	// 스탬프 조회 후 스탬프 존재시 INSERT
	public int insStmInfo(AuthorizeNViewRequestVO paramVO, String szData) throws Exception;
	
	// 쿠폰정보 조회 후 쿠폰 존재시 INSERT
	public int insCpnInfo(AuthorizeNViewRequestVO paramVO, String szData) throws Exception;
	
	// HEVC_YN을 체크하기 위하여 ASSET_ID 조회
	public String getAssetId(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// Face-Match 준비여부 조회
	public FmInfoVO getFmInfo(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 장르 정보 조회
	public GenreInfoVO getGenreInfo(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// CDN INFO 조회 로직 모음
	public AuthorizePlayIpVO getCdnInfo(AuthorizePlayIpVO PlayIpVO, AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 지역노드 정보 조회
	public String getNodeCd(AuthorizePlayIpVO playIpVO, AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 이벤트 타입조회
	public int getEventType(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// SVOD 가 아닌 상품정보 조회
	public List<ContTypeVO> getProdInfo(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 구매목록 조회
	public Integer chkBuyCont(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 선물내역 조회
	public Integer getPresentCnt(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 워터마크 정보 조회
	public String getWatermarkChk(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 자막정보 조회
	public AuthorizeNViewResponseVO getSmiInfo(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 시청이력 저장
	public Integer insertWatchHis(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// 구간정보 이미지 조회
	public List<HashMap> getThumnailInfo(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// REAL HD 파일명 조회
	public String getLiveHevcFileName(AuthorizeNViewRequestVO paramVO) throws Exception;
	
	// M3U8
	public List<AuthorizePlayIpVO> getVodM3u8Search(AuthorizeNViewRequestVO paramVO, AuthorizeNViewResponseVO tempVO, String AWS_app, String AWS_web) throws Exception;

}
