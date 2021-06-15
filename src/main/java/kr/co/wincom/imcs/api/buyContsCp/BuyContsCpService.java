package kr.co.wincom.imcs.api.buyContsCp;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



public interface BuyContsCpService {
	
	 @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	 public BuyContsCpResultVO buyContsCp(BuyContsCpRequestVO paramVO);
	 
	 // 가입자 상태, 개통여부 조회
	 public List<ComSbcVO> getCustomerInfo(BuyContsCpRequestVO paramVO) throws Exception;
	 
	 // 상품가격(정액/종량) 정보 조회
	 public List<ComPriceVO> getBillTypeInfo(BuyContsCpRequestVO paramVO) throws Exception;
	 
	 // 가입자가 구입한 상품 조회
	 public int CustomProductChk(BuyContsCpRequestVO paramVO) throws Exception;

	 // 이벤트 구매내역 체크
	 public Integer getEventChk(BuyContsCpRequestVO paramVO) throws Exception;
	 
	 // 패키지 컨텐츠 정보 조회
	 public List<ContTypeVO> getPkgContent(BuyContsCpRequestVO paramVO) throws Exception; 
	 
	 // 단품 컨텐츠 정보 조회
	 public List<ContTypeVO> getGenreType(BuyContsCpRequestVO paramVO) throws Exception; 
	
	 // 컨텐츠 정보 조회
	 public ContTypeVO getProduct(BuyContsCpRequestVO paramVO) throws Exception;
	 
	 // 구매 이력 저장
	 public Integer insertBuyContent1(BuyContsCpRequestVO paramVO) throws Exception;
	 public Integer insertBuyContent2( BuyContsCpRequestVO paramVO) throws Exception;
	 public Integer insertBuyContent3( BuyContsCpRequestVO paramVO) throws Exception;
	 public Integer insertBuyContent4(BuyContsCpRequestVO paramVO) throws Exception;

}
