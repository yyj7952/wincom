package kr.co.wincom.imcs.api.buyNSConts;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.buyNSConts.BuyNSContsRequestVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyNSContsDao {
	
	// 구매ID 여부 조회
	public Integer chkPaymentId(BuyNSContsRequestVO paramVO);
	
	// 가입자 상태, 개통여부 조회
	public List<ComSbcVO> getSbcInfo(BuyNSContsRequestVO paramVO);
	
	// 상품정보조회 (정액/종량)
	public List<ComPriceVO> getBillType(BuyNSContsRequestVO paramVO);
	
	// 인앱 가격정보 조회	
	public String getApprovalInfo(BuyNSContsRequestVO paramVO);
	
	// 가입자 구매상품 여부 조회
	public List<Integer> getCustomerProdChk(BuyNSContsRequestVO paramVO);
	
	// 이벤트 구매내역 조회
	public Integer getEventChk(BuyNSContsRequestVO paramVO);
	
	// 기존구매내역 조회 (예약구매용)
	public List<HashMap<String, Object>> getBuyDupChkR(BuyNSContsRequestVO paramVO);
	
	// 기존구매내역 조회 (FVOD)
	public List<HashMap<String, Object>> getBuyDupChkType0(BuyNSContsRequestVO paramVO);
	
	// 기존구매내역 조회 (PPV)
	public List<HashMap<String, Object>> getBuyDupChkType1(BuyNSContsRequestVO paramVO);
	
	// 기존구매내역 조회 (PVOD)
	public List<HashMap<String, Object>> getBuyDupChkType2(BuyNSContsRequestVO paramVO);
	
	// 패키지 컨텐츠 보관함 조회
	public List<ContTypeVO> getPkgContent(BuyNSContsRequestVO paramVO);
	
	// 패키지 컨텐츠 보관함 저장
	public Integer insBuyConts3(BuyNSContsRequestVO paramVO);
	
	// 단품 장르 정보 조회
	public List<ContTypeVO> getGenreType(BuyNSContsRequestVO paramVO);
	
	// 단품 컨텐츠 보관함 저장
	public Integer insBuyConts4(BuyNSContsRequestVO paramVO);
		
	// 구매내역 저장1
	public Integer insBuyConts1(BuyNSContsRequestVO paramVO);
	
	// 패키지 상품정보 조회
	public List<ContTypeVO> getProduct(BuyNSContsRequestVO paramVO);
	
	// 구매내역 저장2 
	public Integer insBuyConts2(BuyNSContsRequestVO paramVO);
	
	// 장르 정보 조회
	public List<GenreInfoVO> getGenreInfo(BuyNSContsRequestVO paramVO);
	
	// 쿠폰정보 조회
	public ComCpnVO getCpnPossibleList(BuyNSContsRequestVO paramVO);
	
	// 쿠폰정보 저장
	public Integer insCpnInfo(BuyNSContsRequestVO paramVO);
	
	// 스탬프 정보 조회
	public ComCpnVO getStmPossibleList(BuyNSContsRequestVO paramVO);
	
	// 스탬프 정보 저장
	public Integer insStmInfo(BuyNSContsRequestVO paramVO);
	
	// 사용 쿠폰조회
	public ComCpnVO getUseCpnPossibleList(BuyNSContsRequestVO paramVO);
	
	// 데이터프리 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO paramVO);
	
	// 기존 데이터 프리 구매내역 조회
	public List<ComDupCHk> chkDatafreeDup(BuyNSContsRequestVO paramVO);
	
	// 데이터프리 구매 내역 저장(FOD)
	public Integer insertDatafreeContent1(BuyNSContsRequestVO paramVO);
		
	// 데이터프리 구매 내역 저장(PPV)
	public Integer insertDatafreeContent2(BuyNSContsRequestVO paramVO);
	
	// 데이터프리 구매 내역 저장
	public Integer insertDatafreeDetail(BuyNSContsRequestVO paramVO);

	// 2019.10.30 - VOD 정산 프로세스 개선 : 메타 정보 저장
	public Integer insBuyMeta(BuyNSContsRequestVO paramVO);
}