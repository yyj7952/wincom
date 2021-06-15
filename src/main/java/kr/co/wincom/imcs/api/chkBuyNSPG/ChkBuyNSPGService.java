package kr.co.wincom.imcs.api.chkBuyNSPG;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ChkBuyNSPGService {
	
	// 구매전 오류체크 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public ChkBuyNSPGResultVO chkBuyNSPG(ChkBuyNSPGRequestVO paramVO) throws Exception;

	// 가입자 구매상품 여부 조회
	public int getBuyDupChk(ChkBuyNSPGRequestVO paramVO) throws Exception;

	// 상태, 개통여부 및 쿠폰값 가져오기
	//public List<ComSbcVO> getSbcInfo(ChkBuyNSPGRequestVO paramVO) throws Exception;
	public List<ComSbcVO> buyContent_Cust_Sel(ChkBuyNSPGRequestVO paramVO) throws Exception;

	// 가격정보 조회
    //public ComPriceVO getBillType(ChkBuyNSPGRequestVO paramVO) throws Exception;
    public ComPriceVO buyContent_Bill_Type(ChkBuyNSPGRequestVO paramVO) throws Exception;
    
    // 패키지 컨텐츠 조회
    public ContTypeVO getPackageContent(ChkBuyNSPGRequestVO paramVO) throws Exception;
    
    // 단품 상품정보 조회
    public ContTypeVO getContGenre(ChkBuyNSPGRequestVO paramVO) throws Exception;
    
}
