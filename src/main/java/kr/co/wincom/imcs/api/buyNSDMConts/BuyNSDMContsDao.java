package kr.co.wincom.imcs.api.buyNSDMConts;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.buyNSConts.BuyNSContsRequestVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyNSDMContsDao
{
	
	// 시청 여부 조회
	public List<BuyNSDMContsRequestVO> getBuyChkMIMS(BuyNSDMContsRequestVO paramVO);
	
	// 가입자 상태, 개통여부 조회
	public List<ComSbcVO> getSbcInfo(BuyNSDMContsRequestVO paramVO);
	
	// 상품정보조회 (정액/종량)
	public List<ComPriceVO> getBillType(BuyNSDMContsRequestVO paramVO);
	
	// 데이터프리 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO paramVO);
	
	// 가입자 구매상품 여부 조회
	public List<Integer> getCustomerProdChk(BuyNSDMContsRequestVO paramVO);
	
	// 이벤트 구매내역 조회
	public Integer getEventChk(BuyNSDMContsRequestVO paramVO);
	
	// 기존 데이터 프리 구매내역 조회
	public List<ComDupCHk> chkDatafreeDup(BuyNSDMContsRequestVO paramVO);

	// 기존구매내역 조회 (예약구매용)
	public List<HashMap<String, Object>> getBuyDupChkR(BuyNSDMContsRequestVO paramVO);
	
	// 기존구매내역 조회 (FVOD)
	public List<HashMap<String, Object>> getBuyDupChkType0(BuyNSDMContsRequestVO paramVO);
	
	// 기존구매내역 조회 (PPV)
	public List<HashMap<String, Object>> getBuyDupChkType1(BuyNSDMContsRequestVO paramVO);
	
	// 기존구매내역 조회 (PVOD)
	public List<HashMap<String, Object>> getBuyDupChkType2(BuyNSDMContsRequestVO paramVO);
	
	// 패키지 컨텐츠 보관함 조회
	public List<ContTypeVO> getPkgContent(BuyNSDMContsRequestVO paramVO);
	
	// 패키지 컨텐츠 보관함 조회
	public List<ContTypeVO> getPkgContent2(BuyNSDMContsRequestVO paramVO);
		
	
	// 패키지 컨텐츠 보관함 저장
	public Integer insBuyConts3(BuyNSDMContsRequestVO paramVO);
	
	// 단품 장르 정보 조회
	public List<ContTypeVO> getGenreType(BuyNSDMContsRequestVO paramVO);
	
	// 단품 컨텐츠 보관함 저장
	public Integer insBuyConts4(BuyNSDMContsRequestVO paramVO);
	
	// 컨텐츠 할인정보 등록
	public Integer buyInsDiscount(BuyNSDMContsRequestVO paramVO);
	
	// 데이터프리 할인정보 등록
	public Integer buyInsDfDiscount(BuyNSDMContsRequestVO paramVO);
	
	// 구매내역 저장1
	public Integer insBuyConts1(BuyNSDMContsRequestVO paramVO);
	
	// 패키지 상품정보 조회
	public List<ContTypeVO> getProduct(BuyNSDMContsRequestVO paramVO);
	
	// 구매내역 저장2 
	public Integer insBuyConts2(BuyNSDMContsRequestVO paramVO);
	
	// 데이터프리 구매 내역 저장(FOD)
	public Integer insertDatafreeContent1(BuyNSDMContsRequestVO paramVO);
		
	// 데이터프리 구매 내역 저장(PPV)
	public Integer insertDatafreeContent2(BuyNSDMContsRequestVO paramVO);
	
	// 데이터프리 구매 내역 저장
	public Integer insertDatafreeDetail(BuyNSDMContsRequestVO paramVO);
	
	// 장르 정보 조회
	public List<GenreInfoVO> getGenreInfo(BuyNSDMContsRequestVO paramVO);
	
	// 쿠폰정보 조회
	public ComCpnVO getCpnPossibleList(BuyNSDMContsRequestVO paramVO);
	
	// 쿠폰정보 저장
	public Integer insCpnInfo(BuyNSDMContsRequestVO paramVO);
	
	// 스탬프 정보 조회
	public ComCpnVO getStmPossibleList(BuyNSDMContsRequestVO paramVO);
	
	// 스탬프 정보 저장
	public Integer insStmInfo(BuyNSDMContsRequestVO paramVO);
	
	// 사용 쿠폰조회
	public ComCpnVO getUseCpnPossibleList(BuyNSDMContsRequestVO paramVO);
	
	// 2019.10.30 - VOD 정산 프로세스 개선 : 메타 정보 저장
	public Integer insBuyMeta(BuyNSDMContsRequestVO paramVO);
	
	// 아이들나라 편성 조회
	public HashMap<String, String> getKidsChk(BuyNSDMContsRequestVO paramVO);
	
	// 특정 APP별 결제/할인 수단 제어
	public List<BlockVO> getPaymentBlock(BuyNSDMContsRequestVO paramVO);
	
	//아이돌라이브 유료콘서트 구매날자 가능 조회
	public Integer chkConsert(BuyNSDMContsRequestVO paramVO);
		
}
