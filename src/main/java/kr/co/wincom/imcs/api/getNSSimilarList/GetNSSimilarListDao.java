package kr.co.wincom.imcs.api.getNSSimilarList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSSimilarListDao {
	// 검수 STB여부 및 VIEW FLAG 조회
	public GetNSSimilarListRequestVO testSbc(GetNSSimilarListRequestVO paramVO);
	
	// 비슷한영화 조회
	public List<GetNSSimilarMovieVO> getNSSimilarMovie(GetNSSimilarListRequestVO paramVO);
	
	// 카테고리명 조회
	public List<CateInfoVO> getCategory(GetNSSimilarListRequestVO paramVO);
	
	// 비슷한영화 상세정보 조회
	public List<GetNSSimilarListResponseVO> getSimilar(GetNSSimilarListRequestVO paramVO);
	
	// 왓챠정보 조회
	public List<ComWatchaVO> getWatchaInfo(GetNSSimilarListRequestVO paramVO);
	
	// 상품타입 조회
	public List<String> getProductType(GetNSSimilarListRequestVO paramVO);
	
}
