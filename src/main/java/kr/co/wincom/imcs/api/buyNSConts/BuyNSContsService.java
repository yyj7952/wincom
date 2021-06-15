package kr.co.wincom.imcs.api.buyNSConts;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



public interface BuyNSContsService {
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public BuyNSContsResultVO buyNSConts(BuyNSContsRequestVO paramVO);
	
	// 가입자 상태, 개통여부 조회
	public ComSbcVO getSbcInfo(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 상품정보 조회 (정액/종량)
	public ComPriceVO getBillType(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 가입자 구매상품 여부 조회
	public int getCustomerProdChk(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 패키지 컨텐츠 보관함 조회
	public List<ContTypeVO> getPkgContent(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 구매내역 저장
	public Integer insBuyConts1(BuyNSContsRequestVO paramVO) throws Exception;
	public Integer insBuyConts2(BuyNSContsRequestVO paramVO) throws Exception;
	public Integer insBuyConts3(BuyNSContsRequestVO paramVO) throws Exception;
	public Integer insBuyConts4(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 단품 장르 정보 조회
	public ContTypeVO getGenreType(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 패키지 상품정보 조회
	public ContTypeVO getProduct(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 장르 정보 조회
	public String getGenreInfo(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 쿠폰 정보 조회 (SMARTUX 함수 이용)
	public ComCpnVO getCpnInfo(BuyNSContsRequestVO paramVO) throws Exception;
	
	// 스탬프 조회 후 스탬프 존재시 INSERT
	public int insStmInfo(BuyNSContsRequestVO paramVO, String szData) throws Exception;
	
	// 쿠폰정보 조회 후 쿠폰 존재시 INSERT
	public int insCpnInfo(BuyNSContsRequestVO paramVO, String szData) throws Exception;
	
	
	
	
}
