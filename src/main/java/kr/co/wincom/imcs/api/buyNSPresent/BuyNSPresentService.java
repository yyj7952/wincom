package kr.co.wincom.imcs.api.buyNSPresent;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface BuyNSPresentService {
	
	// VOD 선물 구매 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public BuyNSPresentResultVO buyNSPresent(BuyNSPresentRequestVO paramVO);
	
	// 가입자 정보 조회
	public List<ComSbcVO> getCustomerInfo(BuyNSPresentRequestVO paramVO) throws Exception;
	
	// 상품가격정보 조회
	public List<ComPriceVO> getBillTypeInfo(BuyNSPresentRequestVO paramVO) throws Exception;
	
	// 가입자 구매상품 여부 조회
	public int CustomProductChk(BuyNSPresentRequestVO paramVO) throws Exception;
	
	// 선물 컨텐츠 보관함 저장 (PT_VO_BUY_DETAIL)
	public void insertBuyPresent2(BuyNSPresentRequestVO paramVO) throws Exception;
	
	// 선물구매내역 저장 (PT_VO_BUY)
	public void insertBuyPresent1(BuyNSPresentRequestVO paramVO) throws Exception;
	
	// 보낸 선물내역 저장 (PT_VO_PRESENT)
	public void insertBuyPresentP(BuyNSPresentRequestVO paramVO) throws Exception;
	
}
