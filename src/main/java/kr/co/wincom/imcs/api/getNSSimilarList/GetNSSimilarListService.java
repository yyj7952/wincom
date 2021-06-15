package kr.co.wincom.imcs.api.getNSSimilarList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;

public interface GetNSSimilarListService {
	
	// 비슷한 영화(왓챠)정보 조회 API
	public GetNSSimilarListResultVO getNSSimilarList(GetNSSimilarListRequestVO paramVO);

	// 검수 STB여부 및 VIEW FLAG 조회
	public void testSbc(GetNSSimilarListRequestVO paramVO) throws Exception;
	
	// 비슷한영화 조회
	public GetNSSimilarMovieVO getNSSimilarMovie(GetNSSimilarListRequestVO paramVO) throws Exception;

	// 상품타입 조회
	public String getProductType(GetNSSimilarListRequestVO paramVO) throws Exception;
	
	// 카테고리 정보 조회
	public CateInfoVO getCategory(GetNSSimilarListRequestVO paramVO) throws Exception;

	// 비슷한 영화 컨텐츠 상세 정보 조회
	public List<GetNSSimilarListResponseVO> getSimilar(GetNSSimilarListRequestVO paramVO) throws Exception;

	// 비슷한영화의 왓챠정보 조회
	public ComWatchaVO getWatchaInfo(GetNSSimilarListRequestVO paramVO) throws Exception;
	
	// 비슷한영화의 상세 정보 조회
	public GetNSSimilarListResponseVO getSimilarInfo(GetNSSimilarListRequestVO paramVO) throws Exception;
}
