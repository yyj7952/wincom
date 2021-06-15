package kr.co.wincom.imcs.api.getNSBuyList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.api.getNSLists.GetNSListsRequestVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSBuyListDao
{
	
	// 페어링 체크
	public List<HashMap> getCustPairingChk (GetNSBuyListRequestVO requestVO);
	// 테스트 가입자 여부 조회
	public String getTestSbc (GetNSBuyListRequestVO requestVO);
	// 구매 리스트 조회
	public List<GetNSBuyListResponseVO> getNSBuyList_1 (GetNSBuyListRequestVO requestVO);
	// 구매 리스트 조회
	public List<GetNSBuyListResponseVO> getNSBuyList_2 (GetNSBuyListRequestVO requestVO);
	// 구매 리스트 조회
	public List<GetNSBuyListResponseVO> getNSBuyList_3 (GetNSBuyListRequestVO requestVO);
	// 편성 및 상품 타입 정보 조회 
	public List<HashMap> getTypeInfo(String requestVO);

}
