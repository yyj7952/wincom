package kr.co.wincom.imcs.api.buyNSPresent;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyNSPresentDao {
	
	// 가입자 정보 조회
	public List<ComSbcVO> getCustomerInfo(BuyNSPresentRequestVO paramVO);
	
	// 가입자 구매상품 여부 조회
	public List<Integer> CustomProductChk(BuyNSPresentRequestVO paramVO);
	
	// 가입자 구매내역 중복 체크
	public HashMap<String, Object> getPresentDupCk(BuyNSPresentRequestVO paramVO);
	
	// 이벤트 구매내역 조회
	public Integer getEventCk(BuyNSPresentRequestVO paramVO);
	
	// 선물 컨텐츠 보관함 저장 (PT_VO_BUY_DETAIL)
	public Integer insertBuyPresent2(BuyNSPresentRequestVO paramVO);
	
	// 선물 구매내역 저장 (PT_VO_BUY)
	public Integer insertBuyPresent1(BuyNSPresentRequestVO paramVO);
	
	// 보낸 선물내역 저장 (PT_VO_PRESENT)
	public Integer insertBuyPresentP(BuyNSPresentRequestVO paramVO);
	
	public List<ComPriceVO> getBillType(BuyNSPresentRequestVO paramVO);
		
	// 2019.10.30 - VOD 정산 프로세스 개선 : 메타 정보 저장
	public Integer insBuyMeta(BuyNSPresentRequestVO paramVO);
}
