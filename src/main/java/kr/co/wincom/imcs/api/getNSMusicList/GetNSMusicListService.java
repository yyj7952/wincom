package kr.co.wincom.imcs.api.getNSMusicList;

import java.util.List;

public interface GetNSMusicListService {
	
	//메인페이지 뮤직리스트 정보 조회 API
	public GetNSMusicListResultVO getNSMusicList(GetNSMusicListRequestVO paramVO);

	//공연큐시트 조회
	public List<GetNSMusicListResponseVO> getNSMusicCuesheetList(GetNSMusicListRequestVO paramVO) throws Exception;
}
