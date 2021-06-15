package kr.co.wincom.imcs.api.getFXRelation;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComWatchaVO;



public interface GetFXRelationService {
	// 연관컨텐츠 리스트 조회
	public GetFXRelationResultVO getFXRelation(GetFXRelationRequestVO requestVO) throws Exception;
	
	// 검수 STB 여부 조회
	public void getTestSbc(GetFXRelationRequestVO paramVO) throws Exception;
	
	// 카테고리ID 조회 (cat_id 미존재 시)
	public String getCatId(GetFXRelationRequestVO paramVO) throws Exception;
	
	// 연관컨텐츠 리스트 조회
	public List<GetFXRelationResponseVO> getRelationList(GetFXRelationRequestVO paramVO) throws Exception;
	
	// 연관컨텐츠 상세정보 조회
	public GetFXRelationSubVO getContsInfo1(GetFXRelationRequestVO paramVO) throws Exception;
	public GetFXRelationSubVO getContsInfo2(GetFXRelationRequestVO paramVO) throws Exception;
	
	// 이미지 파일명 조회
	public String getImgFileName(GetFXRelationRequestVO paramVO) throws Exception;
	
	// 상품타입정보 조회
	public String getProdType(GetFXRelationRequestVO paramVO) throws Exception;
	
	// 시리즈 카테고리 여부 조회
	public String getSerCatId(GetFXRelationRequestVO paramVO) throws Exception;
	
	// 왓챠 정보 조회
	public ComWatchaVO getWatchaInfo(GetFXRelationRequestVO paramVO) throws Exception;
}
