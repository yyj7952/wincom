package kr.co.wincom.imcs.api.buyNSContent;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



public interface BuyNSContentService {
	
	 @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	 public BuyNSContentResultVO buyNSContent(BuyNSContentRequestVO rd1);
	 
	 public String getSysdate() throws Exception;
	 
	 public BuyNSContentRequestVO buyContent_Cust_Sel( BuyNSContentRequestVO rd1);
	 
	 public void buyContent_Bill_Type( BuyNSContentRequestVO rd1);
	 
	 public int buyContent_Custom_Product( BuyNSContentRequestVO rd1);
	 
	 public List<BuyNSContentRequestVO> getSbcInfo(BuyNSContentRequestVO vo);
	 
	 public List<BuyNSContentRequestVO> getBillType(BuyNSContentRequestVO vo);
	 
	 public int CustomProductChk(BuyNSContentRequestVO vo);
	 
	 public Integer buyContent_Dup_Chk(BuyNSContentRequestVO vo);
	 
	 public Integer ContentStat_Evnt_Chk(BuyNSContentRequestVO vo);
	 
	 public List<BuyNSContentResponseVO> getPkgContent(BuyNSContentRequestVO vo);
	 
	 public Integer buyContent_Ins3( BuyNSContentRequestVO vo);
	 
	 public void buyContent_Genre_Sel( BuyNSContentRequestVO rd1);
	 
	 public List<BuyNSContentRequestVO> getGenreType(BuyNSContentRequestVO vo);
	 
	 public Integer buyContent_Ins4(BuyNSContentRequestVO vo);
	 
	 public Integer buyContent_Ins1(BuyNSContentRequestVO vo);
	 
	 public Integer buyContent_Ins2( BuyNSContentRequestVO rd1);
	 
	 public List<BuyNSContentResponseVO> getProduct(BuyNSContentRequestVO vo);
	 
	 public Integer buyContentIns2(BuyNSContentRequestVO vo);
	 
	 	 	 
}
