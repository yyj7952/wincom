package kr.co.wincom.imcs.api.getNSMusicCue;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.common.vo.ComCueSheetItemVO;
import kr.co.wincom.imcs.common.vo.ComCueSheetMstVO;

@Repository
public interface GetNSMusicCueDao {
	
	// 검수 STB여부 조회
	public List<String> getTestSbc(GetNSMusicCueRequestVO requestVO);
	
	//앨범 구분 정보 가져오기
	public String getAlbumGb(GetNSMusicCueRequestVO requestVO);
	
	//큐시트 마스터 정보 가져오기
	public List<ComCueSheetMstVO> getCueSheetMst1(GetNSMusicCueRequestVO requestVO);
	public List<ComCueSheetMstVO> getCueSheetMst2(GetNSMusicCueRequestVO requestVO);
	
	//큐시트 아이템 정보 가져오기
	public List<ComCueSheetItemVO> getCueSheetItem1(ComCueSheetMstVO requestVO);
	public List<ComCueSheetItemVO> getCueSheetItem2(ComCueSheetMstVO requestVO);
	
	//큐시트 아이템별 멤버 정보 가져오기
	public List<ComCueSheetItemVO> getItemInfo(ComCueSheetItemVO nCueItemVO);
	
	//큐시트 아이템 외 부가정보 가져오기
	public List<ComCueSheetItemVO> getCueAddInfo(GetNSMusicCueRequestVO nCueItemVO);
	
	//큐시트 옴니뷰 정보 가져오기
	public List<ComCueSheetItemVO> getCueOmniVInfo1(ComCueSheetMstVO nCueMst);
	public List<ComCueSheetItemVO> getCueOmniVInfo2(ComCueSheetMstVO nCueMst);
	
	//옴니뷰 영상 M3U8 정보 가져오기
	public List<ComCueSheetItemVO> getM3U8Info(ComCueSheetItemVO nCueItemVO);
	
	// 전/중/후
	public List<GetNSMusicCueResponseVO> getNSMusicCueList(GetNSMusicCueRequestVO requestVO);

}
