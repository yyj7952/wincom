package kr.co.wincom.imcs.api.getFXRelation;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComWatchaVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetFXRelationDao{

	// 검수 STB 여부 조회
	public List<String> getTestSbc(GetFXRelationRequestVO requestVO);

	// 카테고리 ID 조회
	public List<String> getCatId(GetFXRelationRequestVO requestVO);

	// 연관컨텐츠 리스트 조회
	public List<GetFXRelationResponseVO> getRelationList(GetFXRelationRequestVO requestVO);

	// 연관컨텐츠 상세정보 조회1
	public List<GetFXRelationSubVO> getContsInfo1(GetFXRelationRequestVO requestVO);

	// 연관컨텐츠 상세정보 조회2
	public List<GetFXRelationSubVO> getContsInfo2(GetFXRelationRequestVO requestVO);

	// 이미지파일명 조회
	public List<String> getImgFileName(GetFXRelationRequestVO requestVO);

	// 상품타입정보 조회
	public List<String> getProdType(GetFXRelationRequestVO requestVO);

	// 시리즈 카테고리 ID조회
	public List<String> getSerCatId(GetFXRelationRequestVO requestVO);

	// 왓챠 정보 조회
	public List<ComWatchaVO> getWatchaInfo(GetFXRelationRequestVO requestVO);
	
	
	
}
