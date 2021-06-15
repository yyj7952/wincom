package kr.co.wincom.imcs.api.buyContsCp;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyContsCpDao {
	
	// 가입자 상태, 개통여부 조회
	public List<ComSbcVO> getSbcInfo(BuyContsCpRequestVO paramVO);
	
	// 상품가격(정액/종량) 정보 조회
	public List<ComPriceVO> getBillTypeInfo(BuyContsCpRequestVO paramVO);
	
	// 가입자가 구입한 상품 조회
	public List<Integer> CustomProductChk(BuyContsCpRequestVO paramVO);
	
	// 이벤트 구매내역 체크
	public Integer getEventChk(BuyContsCpRequestVO paramVO);
	
	// 구매내역 중복 체크 (FVOD)
	public List<ComDupCHk> buyContentDupChk1(BuyContsCpRequestVO paramVO);
	
	// 구매내역 중복 체크 (PPV)
	public List<ComDupCHk> buyContentDupChk2(BuyContsCpRequestVO paramVO);
	
	// 구매내역 중복 체크 (PVOD)
	public List<ComDupCHk> buyContentDupChk3(BuyContsCpRequestVO paramVO);
	
	// 패키지 컨텐츠 정보 조회
	public List<ContTypeVO> getPkgContent(BuyContsCpRequestVO paramVO);
	
	// 패키지별 컨텐츠 내역 저장
	public Integer insertBuyContent3(BuyContsCpRequestVO paramVO);
	
	// 단품 컨텐츠 정보 조회
	public List<ContTypeVO> getGenreType(BuyContsCpRequestVO paramVO);
	
	// 단품 컨텐츠 내역 저장
	public Integer insertBuyContent4(BuyContsCpRequestVO paramVO);
	
	// 구매 내역 저장
	public Integer insertBuyContent1(BuyContsCpRequestVO paramVO);
	
	// 상품 ID 조회
	public List<ContTypeVO> getProduct(BuyContsCpRequestVO paramVO);

	// 구매 내역 저장
	public Integer insertBuyContent2(BuyContsCpRequestVO paramVO);
	
	// 데이터프리 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO paramVO);
	
	// 기존 데이터 프리 구매내역 조회
	public List<ComDupCHk> chkDatafreeDup(BuyContsCpRequestVO paramVO);
	
	// 데이터프리 구매 내역 저장(FOD + DATA 쿠폰 공제)
	public Integer insertDatafreeContent1(BuyContsCpRequestVO paramVO);
		
	// 데이터프리 구매 내역 저장(PPV + (DATA 쿠폰 공제 || PPV + DATA 쿠폰 공제 ) )
	public Integer insertDatafreeContent2(BuyContsCpRequestVO paramVO);
	
	// 데이터프리 구매 내역 저장(PPV + !(DATA 쿠폰 공제 || PPV + DATA 쿠폰 공제 ) )
	public Integer insertDatafreeContent3(BuyContsCpRequestVO paramVO);
	
	// 데이터프리 구매 내역 저장(PPV + !(DATA 쿠폰 공제 || PPV + DATA 쿠폰 공제 ) )
	public Integer insertDatafreeDetail(BuyContsCpRequestVO paramVO);

	// 2019.10.30 - VOD 정산 프로세스 개선 : 메타 정보 저장
	public Integer insBuyMeta(BuyContsCpRequestVO paramVO);
}
