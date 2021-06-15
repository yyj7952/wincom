package kr.co.wincom.imcs.api.getNSProdinfo;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSProdinfoDao {
	
	// 가상채털 장치 코드 조회
	public List<ComSbcVO> getAtrctChnlDvCd(GetNSProdinfoRequestVO paramVO);
	
	// HDTV SBC의 공통 코드, 명 조회
	public List<ComCdVO> getProdSbc();
	
	// 검수 사용자 여부 조회
	public List<HashMap<String, String>> getTestSbc(GetNSProdinfoRequestVO paramVO);
	
	// 가입자의 구매 상품정보 조회
	public List<HashMap<String, String>> getCutsomProdList(GetNSProdinfoRequestVO paramVO);
	
	// 가입자의 가입 상품정보 조회
	public List<HashMap<String, String>> getCutsomProdList2(GetNSProdinfoRequestVO paramVO);
	
	// 가입자의 가입가능 상품정보 조회
	public List<HashMap<String, String>> getCutsomProdList3(GetNSProdinfoRequestVO paramVO);
	
	// 상품정보 조회
	public List<GetNSProdinfoResponseVO> getNSProdinfoList(GetNSProdinfoRequestVO paramVO);
	
	// 상품정보 조회(하위호환성)
	public List<GetNSProdinfoResponseVO> getNSProdinfoList2(GetNSProdinfoRequestVO paramVO);
	
	// 이미지 정보 조회
	public List<StillImageVO> getImage(GetNSProdinfoRequestVO paramVO);
	
	// 부가세요율 조회
	public List<String> getSurtaxRateInfo();
	
	// 멤버십 정기차감 정보 조회
	public GetNSProdinfoResponseVO getMemberDeductionInfo(GetNSProdinfoRequestVO paramVO);
	
}
