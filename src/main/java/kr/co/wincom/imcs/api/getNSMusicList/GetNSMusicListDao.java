package kr.co.wincom.imcs.api.getNSMusicList;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSMusicListDao {
	
	// 검수 STB여부 조회
	public List<String> getTestSbc(GetNSMusicListRequestVO requestVO);

	// 전/중/후
	public List<GetNSMusicListResponseVO> getNSMusicCuesheetList(GetNSMusicListRequestVO requestVO);

}
