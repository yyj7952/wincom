package kr.co.wincom.imcs.api.chkBuyNSConts;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface ChkBuyNSContsDao {
	
	// 상품타입 조회
	public List<Integer> chkBuyContsProdType(ChkBuyNSContsRequestVO paramVO);
	
	// PPV/PPS 구매 여부 확인
	public String chkBuyContsContsBuy(ChkBuyNSContsRequestVO paramVO);
	
	// 구매형 상품 존재 여부
	public int getBuyConts(ChkBuyNSContsRequestVO paramVO);
	
	// 데이터 프리 구매 여부 확인
	public String getDataFreeBuy(ChkBuyNSContsRequestVO paramVO);
	
	// 엔스크린(NSCREEN) 페어링 여부 확인
	public HashMap<String, String> getNscreenFairing(ChkBuyNSContsRequestVO paramVO);
	
	// 엔스크린(NSCREEN) 페어링 가입자로 구매 여부 확인
	public HashMap<String, String> getNscreenFairingBuy(ChkBuyNSContsRequestVO paramVO);
	
	// 엔스크린(NSCREEN) 페어링 가입자로 가입 여부 확인
	public List<Integer> getNscreenFairingJoin(ChkBuyNSContsRequestVO paramVO);
	
	public String chkCategory(ChkBuyNSContsRequestVO paramVO);
	
	public List<Integer> kidProductCd(ChkBuyNSContsRequestVO paramVO);
}
