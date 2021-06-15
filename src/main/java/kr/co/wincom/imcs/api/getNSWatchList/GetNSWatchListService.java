package kr.co.wincom.imcs.api.getNSWatchList;

import java.util.HashMap;
import java.util.List;

public interface GetNSWatchListService {
	
	//메인페이지 뮤직리스트 정보 조회 API
	public GetNSWatchListResultVO getNSWatchList(GetNSWatchListRequestVO paramVO);
	
}
