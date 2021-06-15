package kr.co.wincom.imcs.api.getNSMultiConts;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;


public interface GetNSMultiContsService
{
	// 컨텐츠 리스트 조회 API
	public GetNSMultiContsResultVO getNSMultiConts(GetNSMultiContsRequestVO requestVO) throws Exception;

	//시청 여부 조회
	public GetNSMultiContsResponseVO IsWatch(GetNSMultiContsRequestVO paramVO) throws Exception;
	
	//링크타입 정보 조회
	public GetNSMultiContsResponseVO GetLinkTime(GetNSMultiContsRequestVO paramVO) throws Exception;
	
	//런타임 정보 조회
	public GetNSMultiContsResponseVO GetAlbumRuntime(GetNSMultiContsRequestVO paramVO) throws Exception;
	
}
