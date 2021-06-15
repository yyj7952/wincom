package kr.co.wincom.imcs.api.getFXContStat;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComOfferVO;
import kr.co.wincom.imcs.common.vo.ComSvodVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetFXContStatDao {

	// IMCS 상품 여부 조회
	List<HashMap<String, String>> getImcsProdId(GetFXContStatRequestVO paramVO);

	// 검수 STB 여부 조회
	@SuppressWarnings("rawtypes")
	List<HashMap<String, String>> getTestSbc(GetFXContStatRequestVO paramVO);

	// 프리미엄 여부 조회
	@SuppressWarnings("rawtypes")
	List<HashMap<String, String>> getPremiumYn(GetFXContStatRequestVO paramVO);

	// 컨텐츠 정보 조회
	List<GetFXContStatResponseVO> getProdInfo(GetFXContStatRequestVO paramVO);

	// 컨텐츠 정보 조회2
	@SuppressWarnings("rawtypes")
	List<HashMap<String, String>> getProdInfo2(GetFXContStatRequestVO paramVO);

	// 쿠폰 정보 조회
	List<ComOfferVO> getCpnInfo(GetFXContStatRequestVO paramVO);

	// 컨텐츠 상세정보 조회
	List<GetFXContStatResponseVO> getContType(GetFXContStatRequestVO paramVO);

	// 컨텐츠 코드 정보 조회
	List<String> getProdCd(GetFXContStatRequestVO paramVO);
	List<String> getProdCd2(GetFXContStatRequestVO paramVO);

	// 구매 중복 체크
	HashMap<String, Object> getBuyDupChk1(GetFXContStatRequestVO paramVO);		// FVOD
	HashMap<String, Object> getBuyDupChk2(GetFXContStatRequestVO paramVO);		// PPV
	HashMap<String, Object> getBuyDupChk3(GetFXContStatRequestVO paramVO);		// PVOD

	// 이벤트 여부 체크
	Integer getEventChk(GetFXContStatRequestVO paramVO);

	// 이벤트 상품 구매내역 체크
	Integer getEventDupChk(GetFXContStatRequestVO paramVO);

	// 컨텐츠 코드 정보 조회 (PPM)
	List<String> getPpmProdYn(GetFXContStatRequestVO paramVO);

	// PPM 컨텐츠 상세정보 조회
	List<GetFXContStatResponseVO> getPpmType(GetFXContStatRequestVO paramVO);

	// SVOD 상품 정보 조회
	List<ComSvodVO> getSvodInfo(GetFXContStatRequestVO paramVO);
	
	
	
}
