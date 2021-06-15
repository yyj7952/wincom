package kr.co.wincom.imcs.api.getNSReposited;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.getNSPurchased.GetNSPurchasedRequestVO;
import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSRepositedDao
{
	// FX_TYPE이 N인 시청이력 리스트
	public List<GetNSRepositedResponseVO> getWatchingListTypeN(GetNSRepositedRequestVO paramVO);
	
	// FX_TYPE이 N이 아닌 시청이력 리스트
	public List<GetNSRepositedResponseVO> getWatchingListTypeE(GetNSRepositedRequestVO paramVO);
	
	// 이어보기 시간 조회
	public List<String> getLinkTime(GetNSRepositedRequestVO paramVO);

	// 카테고리 정보 조회
	public List<CateInfoVO> getCateInfo(GetNSRepositedRequestVO paramVO);

	// 앨범 정보 조회
	public List<AlbumInfoVO> getAlbumInfo(GetNSRepositedRequestVO paramVO);
	
	// 이미지 스틸컷 정보 조회
	public List<String> getStillFileName(GetNSRepositedRequestVO paramVO);

	// 상품 타입 정보 조회1
	public List<GetNSRepositedResponseVO> getProductTypeInfo1(GetNSRepositedRequestVO paramVO);
	
	// 상품 타입 정보 조회2
	public List<GetNSRepositedResponseVO> getProductTypeInfo2(GetNSRepositedRequestVO paramVO);

	// 구매 정보 조회
	public HashMap<String, String> getBuyInfo(GetNSRepositedRequestVO paramVO);

	// 선물함 구매 정보 조회
	public HashMap<String, String> getPresentInfo(GetNSRepositedRequestVO paramVO);
	
	// 스틸이미지 정보 조회
	public List<StillImageVO> getImageFileName(GetNSRepositedRequestVO paramVO);
	
	// 데이터 프리 구매여부 조회
	public List<String> getBuyDataFreeInfo(GetNSRepositedRequestVO paramVO);
	
	// 엔스크린 구매연동 여부 및 테스트 계정 확인
	public List<HashMap> getCustPairingChk(GetNSRepositedRequestVO paramVO);
	
	// 엔스크린 인 경우 시청이력 리스트
	public List<GetNSRepositedResponseVO> getWatchingListTypeNScreen(GetNSRepositedRequestVO paramVO);
	
	// 구매 정보 조회 - 엔스크린
	public HashMap<String, String> getBuyInfoNScreen(GetNSRepositedRequestVO paramVO);
	
	// 선물함 정보 조회 - 엔스크린
	public HashMap<String, String> getPresentInfoNScreen(GetNSRepositedRequestVO paramVO);
	
	// 엔스크린 구매만료 여부check
	public HashMap<String, String> getNScreenBuyChk(GetNSRepositedRequestVO paramVO);
	
	// 검수 STB여부 조회 (20180724 권형도)
	public List<String> getTestSbc(GetNSRepositedRequestVO requestVO);
}
