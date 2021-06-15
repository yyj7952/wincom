package kr.co.wincom.imcs.api.authorizeNSView;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeNSViewDao
{	
	// 가입자 가입상품 정보조회
	public List<String>		getCustomProduct(AuthorizeNSViewRequestVO requestVO);
	
	// 앨범 편성된 상품 정보 조회
	public List<AuthorizeNSCateVO> getAlbumProduct(AuthorizeNSViewRequestVO requestVO);
	
	// 앨범 편성된 상품정보 조회
	public List<ContTypeVO>	getAlbumProductRelation(AuthorizeNSViewRequestVO requestVO);
	
	// 월정액 상품 구매여부
	@SuppressWarnings("rawtypes")
	public List<HashMap> getProductBuy(AuthorizeNSViewRequestVO requestVO);
	
	// 컨텐츠 DRM 여부 조회
	public String getWaterMarkYn(AuthorizeNSViewRequestVO requestVO);
		
	// 앨범 부가 정보 조회
	public List<HashMap<String, String>> getAlbumInfo(AuthorizeNSViewRequestVO requestVO);
	
	// 이어보기 시간정보 조회 (LinkTime)
	public String getLinkTime(AuthorizeNSViewRequestVO paramVO);
	
	// 장르정보 조회 (대장르, 중장르, 소장르)
	public List<GenreInfoVO> getGenreString(AuthorizeNSViewRequestVO paramVO);
	
	// 시스템 시간 가져오기
	public HashMap<String, String> getSysdateInfo();
	
	// terr_type, ago_date 가져오기
	public HashMap<String, String> getNScreenWatchSubscriptionInfo(AuthorizeNSViewRequestVO paramVO);
	
	// 시청이력 등록 - INSERT 쿼리
	public int setWatchHist(AuthorizeNSViewRequestVO paramVO);
	
	// SET_TIME 테이블 이어보기 시간 설정
	public Integer updatePtVoSetTimePtt(AuthorizeNSViewRequestVO paramVO);
	
	// SET_TIME 테이블 이어보기 시간 설정
	public Integer insertPtVoSetTimePtt(AuthorizeNSViewRequestVO paramVO);
	
	// 앨범 포스터 정보 조회
	@SuppressWarnings("rawtypes")
	public List<HashMap> getAlbumPoster(AuthorizeNSViewRequestVO paramVO);
	
	// Live Hevc 다운로드 컨텐츠 파일정보 조회
	public List<String> getLiveHevcFileName(AuthorizeNSViewRequestVO paramVO);
	
	// 노드 정보 조회(와이파이): RANGE_IP_CD 길이 '2'
	// WI-FI 사용자에게 응답 할 RANGE_IP_CD의 첫번째 자리는 'W'문자열, 두번째 자리는 숫자 1자리로 협의(방송운영팀)
	public List<String> getNodeCd01(String nodeGroup);
	
	// 노드 정보 조회(와이파이): RANGE_IP_CD 길이 '3'
	// WI-FI 사용자에게 응답 할 RANGE_IP_CD의 첫번째 자리는 'W'문자열, 두번째 자리는 숫자 1자리로 협의(방송운영팀)
	public List<String> getNodeCd02(String nodeGroup);
	
	// 지역노드 정보 조회
	public List<String> getNodeCd(String baseCondi);
	
	// 다운로드 CDN IP 정보 조회 01
	public List<HashMap<String, String>> getCdnIp(AuthorizeNSViewRequestVO paramVO); 
	
	// 다운로드 CDN IP 정보 조회 02
	public List<HashMap<String, String>> getCdnIp02(AuthorizeNSViewRequestVO paramVO);
	
	// 다운로드 CDN IP 정보 조회 03
	public List<HashMap<String, String>> getCdnIp03(AuthorizeNSViewRequestVO paramVO);
	
	// 다운로드 CDN IP 정보 조회 04
	public List<HashMap<String, String>> getCdnIp04(AuthorizeNSViewRequestVO paramVO);
	
	// 스트리밍 CDN IP 정보 조회 01
	public List<HashMap<String, String>> getStreamingCdnIp01(AuthorizeNSViewRequestVO paramVO);
	
	// 스트리밍 CDN IP 정보 조회 02
	public List<HashMap<String, String>> getStreamingCdnIp02(AuthorizeNSViewRequestVO paramVO);
	
	// 스트리밍 CDN IP 정보 조회 03
	public List<HashMap<String, String>> getStreamingCdnIp03(AuthorizeNSViewRequestVO paramVO);
		
	// 스트리밍 CDN IP 정보 조회 04
	public List<HashMap<String, String>> getStreamingCdnIp04(AuthorizeNSViewRequestVO paramVO);
	
	// 발급가능 쿠폰 리스트 조회
	public ComCpnVO getCpnPossibleList(AuthorizeNSViewRequestVO paramVO);
	
	// 쿠폰 존재 여부 체크
	public int getCpnChk(AuthorizeNSViewRequestVO paramVO);
	
	// 쿠폰 정보 입력
	public int insCpnInfo(AuthorizeNSViewRequestVO paramVO);	
	
	// 스탬프 리스트 조회
	public ComCpnVO getStmPossibleList(AuthorizeNSViewRequestVO paramVO);	
	
	// 스탬프 존재 여부 체크
	public int getStmChk(AuthorizeNSViewRequestVO paramVO);	
	
	// 스탬프 정보 입력
	public int insStmInfo(AuthorizeNSViewRequestVO paramVO);
	
	// 사용 가능 쿠폰 리스트 조회
	public ComCpnVO getUseCpnPossibleList(AuthorizeNSViewRequestVO paramVO);
	
	// Face-Match 준비여부 조회
	public List<FmInfoVO> getFmInfo(AuthorizeNSViewRequestVO requestVO);
	
	// HEVC_YN을 체크하기 위하여 ASSET_ID 조회
	public List<String> getAssetId(AuthorizeNSViewRequestVO requestVO);
	
	// 시즌여부(HDTV) 조회
	public List<String> getSeasonInfo(AuthorizeNSViewRequestVO paramVO);
	
	public List<HashMap<String, String>> getVodProfile(AuthorizeNSViewRequestVO paramVO);
	
	// 2019.11.01 - VOD 정산 프로세스 개선 : 메타 정보 저장
	public Integer insWatchMeta(AuthorizeNSViewRequestVO paramVO);	
	
	// 2020.03.26 - 모바일 아이들나라 : 키즈카테고리 속성 가져오기
	public String getKidsGb(AuthorizeNSViewRequestVO paramVO);
}
