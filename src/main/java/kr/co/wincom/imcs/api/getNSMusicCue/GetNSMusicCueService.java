package kr.co.wincom.imcs.api.getNSMusicCue;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCueSheetItemVO;
import kr.co.wincom.imcs.common.vo.ComCueSheetMstVO;

public interface GetNSMusicCueService {
	
	//메인페이지 뮤직리스트 정보 조회 API
	public GetNSMusicCueResultVO getNSMusicCue(GetNSMusicCueRequestVO paramVO);

	//뮤직큐시트 조회
	public List<ComCueSheetMstVO> getNSMusicCueMst(GetNSMusicCueRequestVO paramVO) throws Exception;
	
	//큐시트 아이템별 멤버 정보 가져오기
	public ComCueSheetItemVO getItemInfo(ComCueSheetItemVO nCueItemVO, GetNSMusicCueRequestVO paramVO) throws Exception;
	
	//큐시트 옴니뷰 정보 가져오기
	public List<ComCueSheetItemVO> getCueOmniVInfo(GetNSMusicCueRequestVO paramVO, ComCueSheetItemVO cueItemVO) throws Exception;
	
	//옴니뷰 영상 M3U8 정보 가져오기
	public ComCueSheetItemVO getM3U8Info(ComCueSheetItemVO nCueItemVO, GetNSMusicCueRequestVO paramVO) throws Exception;
	
}
