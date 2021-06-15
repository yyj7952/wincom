package kr.co.wincom.imcs.api.getNSContInfo;

import java.util.HashMap;
import java.util.List;

public interface GetNSContInfoService {
	
	//메인페이지 뮤직리스트 정보 조회 API
	public GetNSContInfoResultVO getNSContInfo(GetNSContInfoRequestVO paramVO);

	//가입자 정보 가져오기 검수 여부
	public void getTestSbc(GetNSContInfoRequestVO paramVO);
	
	//카테고리 유효성 체크 및 편성 여부 체크
	public void getChkCate(GetNSContInfoRequestVO paramVO);
	
	//컨텐츠 정보 가져오기
	public List<AlbumInfo> getContInfoList(GetNSContInfoRequestVO requestVO);
	
	//장르 정보 가져오기 (UFLIX)
	public String getGenreInfo(AlbumInfo albumInfo, GetNSContInfoRequestVO paramVO);
	
	//이미지 정보 가져오기
	public ImageInfo getImgInfo(AlbumInfo albumInfo, GetNSContInfoRequestVO paramVO);
}
