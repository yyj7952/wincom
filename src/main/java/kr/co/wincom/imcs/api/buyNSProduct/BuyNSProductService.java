package kr.co.wincom.imcs.api.buyNSProduct;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComSbcVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface BuyNSProductService {
	
	 @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	 public BuyNSProductResultVO buyNSProduct(BuyNSProductRequestVO paramVO) throws Exception;
	 
	 public List<ComSbcVO> getCustomerInfo(BuyNSProductRequestVO paramVO) throws Exception;
	 	 	 
	 public Integer getBuyDupChk(BuyNSProductRequestVO paramVO) throws Exception;
	 	 
	 public Integer insertBuyProduct(BuyNSProductRequestVO paramVO) throws Exception;
	 	 	 	 
}
