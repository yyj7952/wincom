package kr.co.wincom.imcs.api.authorizeNView;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;
import kr.co.wincom.imcs.common.vo.M3u8AWSProfileVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeNViewDao {
	
	// 상품 정보 조회
	public List<ContTypeVO> getProdInfo(AuthorizeNViewRequestVO paramVO);
	
	// 구매 내역 조회
	public Integer chkBuyCont(AuthorizeNViewRequestVO paramVO);
	
	// 이어보기 시간정보 조회
	public String getLinkTime(AuthorizeNViewRequestVO paramVO);
	public String getLinkTimeN(AuthorizeNViewRequestVO paramVO);
	
	// 워터마크 정보 조회
	public List<String> getWatermarkChk(AuthorizeNViewRequestVO paramVO);
	
	// 자막정보 조회
	public List<AuthorizeNViewResponseVO> getSmiInfo(AuthorizeNViewRequestVO paramVO);
	
	// 노드정보 조회
	public List<String> getNodeCd(AuthorizePlayIpVO playIpVO);
	
	// 상품ID 조회
	public String getProductId(AuthorizeNViewRequestVO paramVO);
	
	// 시청이력 저장
	public Integer insertWatchHis(AuthorizeNViewRequestVO paramVO);
	
	// 시청이력 저장 - 엔스크린
	public Integer insertWatchHisNScreen(AuthorizeNViewRequestVO paramVO);
	public Integer updateWatchHisNScreen(AuthorizeNViewRequestVO paramVO);
	
	// 썸네일 이미지 조회
	@SuppressWarnings("rawtypes")
	public List<HashMap> getThumnailInfo(AuthorizeNViewRequestVO paramVO);
	
	// 이벤트 타입 조회
	public List<String> getEventType(AuthorizeNViewRequestVO paramVO);

	// 예약구매 컨텐츠 구매정보 취득
	public HashMap<String, Object> getBuyInfo(AuthorizeNViewRequestVO paramVO);

	// 선물내역 조회
	public Integer getPresentCnt(AuthorizeNViewRequestVO paramVO);

	// VOD 서버 정보 조회
	public List<AuthorizePlayIpVO> getVodServer1(AuthorizeNViewRequestVO paramVO);
	public List<AuthorizePlayIpVO> getVodServer2(AuthorizeNViewRequestVO paramVO);
	public List<AuthorizePlayIpVO> getVodServer3(AuthorizeNViewRequestVO paramVO);
	public List<AuthorizePlayIpVO> getVodServer4(AuthorizeNViewRequestVO paramVO);
	public List<AuthorizePlayIpVO> getVodServer5(AuthorizeNViewRequestVO paramVO);
	public List<AuthorizePlayIpVO> getVodServer6(AuthorizeNViewRequestVO paramVO);
	public List<AuthorizePlayIpVO> getVodServer7(AuthorizeNViewRequestVO paramVO);
	public List<AuthorizePlayIpVO> getVodServer8(AuthorizeNViewRequestVO paramVO);
	
	// REAL HD 파일명 조회
	public List<String> getLiveHevcFile(AuthorizeNViewRequestVO requestVO);

	// 장르 정보 조회
	public List<GenreInfoVO> getGenreInfo(AuthorizeNViewRequestVO requestVO);

	// FM정보 조회
	public List<FmInfoVO> getFmInfo(AuthorizeNViewRequestVO requestVO);

	// HEVC_YN을 체크하기 위하여 ASSET_ID 조회
	public List<String> getAssetId(AuthorizeNViewRequestVO requestVO);

	
	// 구매목록 조회
	public Integer chkBuyContType1(AuthorizeNViewRequestVO paramVO);
	
	// 구매목록 조회
	public Integer chkBuyContType2(AuthorizeNViewRequestVO paramVO);

	// 발급 가능 쿠폰 리스트 조회
	public ComCpnVO getCpnPossibleList(AuthorizeNViewRequestVO paramVO);
	
	// 쿠폰 존재 여부 체크
	public int getCpnChk(AuthorizeNViewRequestVO paramVO);
	
	// 쿠폰 정보 입력
	public int insCpnInfo(AuthorizeNViewRequestVO paramVO);

	// 스탬프 리스트 조회
	public ComCpnVO getStmPossibleList(AuthorizeNViewRequestVO paramVO);

	// 스탬프 존재 여부 체크
	public int getStmChk(AuthorizeNViewRequestVO paramVO);

	// 스탬프 정보 입력
	public int insStmInfo(AuthorizeNViewRequestVO paramVO); 

		
	// 사용 가능 쿠폰 리스트 조회
	public ComCpnVO getUseCpnPossibleList(AuthorizeNViewRequestVO paramVO);

	// 예약구매 컨텐츠 최초 시청시, 구매정보 갱신
	public int uptBuyContsExDate(AuthorizeNViewRequestVO paramVO);

	// 데이터 프리 구매 여부 조회
	public List<String> getBuyDataFreeInfo(AuthorizeNViewRequestVO requestVO);
	
	// 노드 정보 조회
	public List<String> getNodeCdLoadBalancing1(AuthorizePlayIpVO playIpVO);
	
	// 노드 정보 조회
	public List<String> getNodeCdLoadBalancing2(AuthorizePlayIpVO playIpVO);

	// 시즌여부(HDTV) 조회
	public List<String> getSeasonInfo(AuthorizeNViewRequestVO paramVO);
	
	// 암호키 조회
	public List<String> getEnctyptKey();
	
	// 엔스크린 가입여부 확인
	public List<String> getNScreenPairingChk(AuthorizeNViewRequestVO paramVO);
	
	// 엔스크린(NSCREEN) 구매 여부 체크
	public HashMap<String, String> getNScreenBuyChk(AuthorizeNViewRequestVO paramVO);
	
	// 엔스크린(NSCREEN) - 가입자 가입상품 정보
	public List<String> getNScreenProductCdList(AuthorizeNViewRequestVO paramVO);
	
	// 엔스크린(NSCREEN) - 앨범 상품 정보
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getNScreenAlbumProducInfotList(AuthorizeNViewRequestVO paramVO);
	
	// 시스템 시간 가져오기
	public HashMap<String, String> getSysdateInfo();
	
	// 엔스크린 구매연동으로 시청 + 가입권한 가져오기
	public HashMap<String, String> getNScreenWatchSubscriptionInfo();
	
	// 엔스크린(NSCREEN) - Asset 정보
	public HashMap<String, String> getNScreenAssetInfo(AuthorizeNViewRequestVO paramVO);

	// VOD m3u8 정보 조회
	public List<M3u8AWSProfileVO> getVodProfile(AuthorizeNViewRequestVO paramVO);
	
	// VOD AWS m3u8 정보 조회
	public List<M3u8AWSProfileVO> getVodProfileAWS(AuthorizeNViewRequestVO paramVO);
	
	// 2020.03.26 - 모바일 아이들나라 : 키즈카테고리 속성 가져오기
	public String getKidsGb(AuthorizeNViewRequestVO paramVO);	
	
	
	
	public String chkCategory(AuthorizeNViewRequestVO paramVO);
	
	public List<String> kidProductCd(AuthorizeNViewRequestVO paramVO);
	
}
