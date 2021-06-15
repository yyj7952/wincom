package kr.co.wincom.imcs.api.getNSContStat;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComSvodVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSContStatDao
{
	// 오늘 날짜 가져오기
	public String getThisDate();

	// 월정액상품정보 조회
	public List<GetNSContStatResponseVO> getPPMProdInfo(GetNSContStatRequestVO requestVO);

	// SVOD상품정보 조회 FX_TYPE = "N"
	public List<ComSvodVO> getSvodInfoN(GetNSContStatRequestVO requestVO);
	
	// SVOD상품정보 조회 FX_TYPE = "H"
	public List<ComSvodVO> getSvodInfoH(GetNSContStatRequestVO requestVO);
	
	// SVOD상품정보 조회 FX_TYPE = ELSE
	public List<ComSvodVO> getSvodInfoE(GetNSContStatRequestVO requestVO);

	// 서브 상품의 경우 메인 상품의 정보를 제공
	public ComSvodVO getSubSvodInfo(GetNSContStatRequestVO paramVO);

	// 구매중복 체크 (FVOD)
	public HashMap<String, Object> getBuyDupChk1(GetNSContStatRequestVO paramVO);
	
	// 구매중복 체크 (PPV)
	public HashMap<String, Object> getBuyDupChk2(GetNSContStatRequestVO paramVO);
	
	// 구매중복 체크 (PVOD)
	public HashMap<String, Object> getBuyDupChk3(GetNSContStatRequestVO paramVO);

	// 멀까?
	public List<String> getProductId(GetNSContStatRequestVO requestVO);

	// 주상품 가입여부 조회
	public List<GetNSContStatResponseVO> getMProdId(GetNSContStatRequestVO requestVO);

	// 유플릭스 상품 구매여부 조회
	public List<HashMap<String, String>> getUflixBuyChk(GetNSContStatRequestVO requestVO);

	// 프리미엄 편성 여부 조회
	public List<String> getPremiumYn(GetNSContStatRequestVO requestVO);

	// 받은 선물 여부 조회
	public HashMap<String, Object> getPresentInfo(GetNSContStatRequestVO requestVO);

	// 발급가능 쿠폰정보조회 - DB FUNCTION 호출
	public ComCpnVO getCpnInfo(GetNSContStatRequestVO paramVO);

	// 발급가능 스탬프정보조회 - DB FUNCTION 호출
	public ComCpnVO getStmInfo(GetNSContStatRequestVO paramVO);

	// 사용가능 쿠폰정보조회 - DB FUNCTION 호출
	public ComCpnVO getUseCpnInfo(GetNSContStatRequestVO paramVO);

	// 사용가능 글로벌 쿠폰정보조회 - DB FUNCTION 호출
	public ComCpnVO getGblCpnInfo(GetNSContStatRequestVO paramVO);
	
	// 데이터프리 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO paramVO);
	
	// 데이터 프리 구매 여부 조회
	public HashMap<String, String> getBuyDataFreeInfo(GetNSContStatRequestVO requestVO);
	
	// SVOD 인 경우에는 컨텐츠가 편성된 상품만 체크 (번들포함)
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getUflixProdYnInfo(GetNSContStatRequestVO requestVO);
	
	// 구매형상품(하루권) 유효여부 체크
	public List<String> getHaruYnInfo(GetNSContStatRequestVO requestVO);
	
	// 찜한 컨텐츠 여부 확인
	public List<String> chkFavorInfo(GetNSContStatRequestVO requestVO);
	
	// 엔스크린 기능 제공 단말 및 엔스크린 컨텐츠일 경우 페어링된 가입자의 구매/가입 여부를 확인
	@SuppressWarnings("rawtypes")
	public List<HashMap> getNScreenPairingInfo(GetNSContStatRequestVO requestVO);
	
	// 엔스크린(NSCREEN) 구매 여부 체크
	public HashMap<String, String> getNScreenBuyChk(GetNSContStatRequestVO requestVO);

	// 엔스크린(NSCREEN) - 가입자 가입상품 정보
	public List<String> getNScreenProductCdList(GetNSContStatRequestVO paramVO);
	
	// 엔스크린(NSCREEN) - 앨범 상품 정보
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getNScreenAlbumProducInfotList(GetNSContStatRequestVO paramVO);

	
	public int chkCategory(GetNSContStatRequestVO paramVO);
	
	public int kidProductCd(GetNSContStatRequestVO paramVO);
		

}