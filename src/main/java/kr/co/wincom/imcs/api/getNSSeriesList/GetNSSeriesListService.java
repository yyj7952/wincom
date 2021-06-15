package kr.co.wincom.imcs.api.getNSSeriesList;

import java.util.HashMap;
import java.util.List;

public interface GetNSSeriesListService {
	
	//메인페이지 뮤직리스트 정보 조회 API
	public GetNSSeriesListResultVO getNSSeriesList(GetNSSeriesListRequestVO paramVO);
	
	// 카테고리 상세정보 조회
	public HashMap<String, String> getCateInfo(GetNSSeriesListRequestVO paramVO) throws Exception;

	// 편성정보 조회
	public List<GetNSSeriesListResponseVO> getContScheduleList(GetNSSeriesListRequestVO paramVO, String AWS_web) throws Exception;
}
