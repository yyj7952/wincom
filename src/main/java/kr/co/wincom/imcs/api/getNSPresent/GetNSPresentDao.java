package kr.co.wincom.imcs.api.getNSPresent;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSPresentDao {
	
	// 오늘 날짜/시간 조회
	public String getSysdate();
	
	// 받은/보낸 선물함 조회
	public List<GetNSPresentResponseVO> getNSPresent(GetNSPresentRequestVO paramVO);
	
	// 선물 상세 정보 조회
	public List<GetNSPresentDtlVO> getNSPresentDtl(GetNSPresentRequestVO paramVO);
	
	// 카테고리 ID 조회
	public List<String> getNSCatId(GetNSPresentRequestVO paramVO);
	
	// 이미지 파일명 조회
	public List<String> getStillImage(GetNSPresentRequestVO paramVO);

}
