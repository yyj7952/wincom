package kr.co.wincom.imcs.api.getNSPresent;

import java.util.List;

public interface GetNSPresentService {
	
	// 선물함 가져오기 API
	public GetNSPresentResultVO getNSPresent(GetNSPresentRequestVO requestVO);
	
	// 받은/보낸 선물함 조회
	public List<GetNSPresentResponseVO> getNSPresentList(GetNSPresentRequestVO paramVO) throws Exception;
	
	// 선물 상세 정보 조회
	public GetNSPresentDtlVO getNSPresentDtl(GetNSPresentRequestVO paramVO) throws Exception;
	
	// 카테고리 ID 조회
	public String getNSCatId(GetNSPresentRequestVO paramVO) throws Exception;
	
	// 이미지 파일명 조회
	public String getStillImage(GetNSPresentRequestVO paramVO) throws Exception;
	 	 	 
}
