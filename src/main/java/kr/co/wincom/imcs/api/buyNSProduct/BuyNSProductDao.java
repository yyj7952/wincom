package kr.co.wincom.imcs.api.buyNSProduct;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyNSProductDao {
	
	public List<String> getNscnId(BuyNSProductRequestVO paramVO);
	
	public List<ComProdInfoVO> getProdInfo(BuyNSProductRequestVO paramVO);
	
	public List<ComSbcVO> getCustomerInfo(BuyNSProductRequestVO paramVO);
	
	public List<ComDupCHk> getBuyDupChk(BuyNSProductRequestVO paramVO);
	
	public List<ComDupCHk> getBuyFreeCouponChk(BuyNSProductRequestVO paramVO);
	
	public Integer insertBuyProduct(BuyNSProductRequestVO paramVO);
	
	// 2019.10.30 - VOD 정산 프로세스 개선 : 메타 정보 저장
	public Integer insBuyMeta(BuyNSProductRequestVO paramVO);
	
	// 2020.06.26 - 영화월정액 1개월 체험권
	public List<String> getFreeCouponId(BuyNSProductRequestVO paramVO);
	
	public Integer getFreeCouponCnt(BuyNSProductRequestVO paramVO);
}
