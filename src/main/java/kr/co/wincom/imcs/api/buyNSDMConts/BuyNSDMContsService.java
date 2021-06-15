package kr.co.wincom.imcs.api.buyNSDMConts;

import java.util.List;

import kr.co.wincom.imcs.api.buyNSConts.BuyNSContsRequestVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;



public interface BuyNSDMContsService
{
	// 컨텐츠 리스트 조회 API
	public BuyNSDMContsResultVO buyNSDMConts(BuyNSDMContsRequestVO requestVO) throws Exception;

	// 파라미터 체크
	public Integer Request_Param_Valid(BuyNSDMContsRequestVO paramVO);
	
	// MIMS를 통한 정상구매인지 여부 체크
	public Integer getBuyChkMIMS(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//가입자 상태, 개통여부 조회
	public ComSbcVO getSbcInfo(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//상품정보 조회 (정액/종량)
	public ComPriceVO getBillType(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//데이터프리 정보 조회
	public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//가입자 구매상품 여부 조회
	public int getCustomerProdChk(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//데이터프리 구매내역 중복 확인
	public Integer chkDatafreeDup(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//패키지 컨텐츠 보관함 조회
	public List<ContTypeVO> getPkgContent(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//패키지 컨텐츠 구매내역 저장
	public Integer insBuyConts3(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//단품 장르 정보 조회
	public ContTypeVO getGenreType(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	//단품 보관함 저장
	public Integer insBuyConts4(BuyNSDMContsRequestVO paramVO) throws Exception;
	
	
}
