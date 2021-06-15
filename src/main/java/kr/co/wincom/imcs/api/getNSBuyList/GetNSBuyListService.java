package kr.co.wincom.imcs.api.getNSBuyList;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;


public interface GetNSBuyListService
{
	public GetNSBuyListResultVO getNSBuyList(GetNSBuyListRequestVO requestVO) throws Exception;	
	// 페어링 체크
	public int getCustPairingChk (GetNSBuyListRequestVO paramVO) throws Exception;
	// 구매 리스트 조회 	
	public List<GetNSBuyListResponseVO> getNSBuyLists(GetNSBuyListRequestVO requestVO) throws Exception;	
	//편성 및 상품 타입 정보 조회
	public int getTypeInfo(GetNSBuyListRequestVO requestVO, GetNSBuyListResponseVO tempVO, String contId) throws Exception;
	
}
