package kr.co.wincom.imcs.api.buyNSContent;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.buyNSConts.BuyNSContsRequestVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyNSContentDAO {
	
	// 구매ID 여부 조회
	public Integer chkPaymentId(BuyNSContentRequestVO paramVO);
	
	public String getSysdate();
	
	public List<BuyNSContentRequestVO> getSbcInfo(BuyNSContentRequestVO vo);
	
	public List<BuyNSContentRequestVO> getBillType(BuyNSContentRequestVO vo);
		
	public List<Integer> CustomProductChk(BuyNSContentRequestVO vo);
	
	public Integer ContentStatEvntChk(BuyNSContentRequestVO vo);
	
	public List<HashMap<String, Object>> buyContentDupChkR(BuyNSContentRequestVO vo);
	
	public List<HashMap<String, Object>> buyContentDupChk1(BuyNSContentRequestVO vo);
	
	public List<HashMap<String, Object>> buyContentDupChk2(BuyNSContentRequestVO vo);
	
	public List<HashMap<String, Object>> buyContentDupChk3(BuyNSContentRequestVO vo);
	
	public List<BuyNSContentResponseVO> getPkgContent(BuyNSContentRequestVO vo);
	
	public Integer buyContentIns3(BuyNSContentRequestVO vo);
	
	public List<BuyNSContentRequestVO> getGenreType(BuyNSContentRequestVO vo);
	
	public Integer buyContentIns4(BuyNSContentRequestVO vo);
	
	public Integer buyContentIns1R(BuyNSContentRequestVO vo);
	
	public Integer buyContentIns1(BuyNSContentRequestVO vo);
	
	public List<BuyNSContentResponseVO> getProductR(BuyNSContentRequestVO vo);
	
	public List<BuyNSContentResponseVO> getProduct(BuyNSContentRequestVO vo);
	
	public Integer buyContentIns2R(BuyNSContentRequestVO vo);
	
	public Integer buyContentIns2(BuyNSContentRequestVO vo);
	
	// 장르 정보 조회
	public List<GenreInfoVO> getGenreInfo(BuyNSContentRequestVO paramVO);
	
	// 쿠폰정보 조회
	public ComCpnVO getCpnPossibleList(BuyNSContentRequestVO paramVO);
	
	// 쿠폰정보 저장
	public Integer insCpnInfo(BuyNSContentRequestVO paramVO);
	
	// 스탬프 정보 조회
	public ComCpnVO getStmPossibleList(BuyNSContentRequestVO paramVO);
	
	// 스탬프 정보 저장
	public Integer insStmInfo(BuyNSContentRequestVO paramVO);
	
	// 사용 쿠폰조회
	public ComCpnVO getUseCpnPossibleList(BuyNSContentRequestVO paramVO);

}
