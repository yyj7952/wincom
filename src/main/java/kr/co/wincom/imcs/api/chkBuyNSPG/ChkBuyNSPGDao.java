package kr.co.wincom.imcs.api.chkBuyNSPG;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;

import org.springframework.stereotype.Repository;

@Repository
public interface ChkBuyNSPGDao {
	
	// 상태, 개통여부 및 쿠폰값 가져오기
	public List<ComSbcVO> getSbcInfo(ChkBuyNSPGRequestVO paramVO);

	// 가격정보 조회
	public List<ComPriceVO> getBillType(ChkBuyNSPGRequestVO paramVO);	
	
	// 가입자 구매상품 여부 조회 
	public List<Integer> getBuyDupChk(ChkBuyNSPGRequestVO paramVO);
	
	// 구매내역 조회 1
	public List<HashMap> buyPresentDupChk1(ChkBuyNSPGRequestVO paramVO);
	
	// 구매내역 조회 2
	public List<HashMap> buyPresentDupChk2(ChkBuyNSPGRequestVO paramVO);
	
	// 구매내역 조회 3
	public List<HashMap> buyPresentDupChk3(ChkBuyNSPGRequestVO paramVO);
	
	// 패키지 컨텐츠 조회
	public List<ContTypeVO> getPackageContent(ChkBuyNSPGRequestVO paramVO);
	
	// ppsID 로 패키지 컨텐츠 조회
	public List<ContTypeVO> getPackageContent2(ChkBuyNSPGRequestVO paramVO);
	
	// 단품 컨텐츠 정보 조회
	public List<ContTypeVO> getContGenre(ChkBuyNSPGRequestVO paramVO);
	
	// 데이터프리 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO paramVO);
	
	// 데이터 프리 구매여부 조회
	public List<String> getBuyDataFreeInfo(ChkBuyNSPGRequestVO paramVO);
	
	//아이돌라이브 유료콘서트 구매날자 가능 조회
	public Integer chkConsert(ChkBuyNSPGRequestVO paramVO);
		
}
