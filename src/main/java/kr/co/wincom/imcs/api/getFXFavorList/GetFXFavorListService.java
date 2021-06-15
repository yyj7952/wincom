package kr.co.wincom.imcs.api.getFXFavorList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;

public interface GetFXFavorListService {
	// 찜목록 리스트 조회 API
	public GetFXFavorListResultVO getFXFavorList(GetFXFavorListRequestVO requestVO) throws Exception;
	
	// 찜목록 조회
	public List<GetFXFavorListResponseVO> getFavorList(GetFXFavorListRequestVO paramVO) throws Exception;
	
	// 카테고리 정보 조회
	public CateInfoVO getCateInfo(GetFXFavorListRequestVO paramVO) throws Exception;
	
	// 앨범 정보 조회
	public AlbumInfoVO getAlbumInfo(GetFXFavorListRequestVO paramVO) throws Exception;
	
	// 상품타입 정보 조회
	public String getProdType(GetFXFavorListRequestVO paramVO) throws Exception;
	
	// 이미지 파일명 조회
	public String getImageFileName(GetFXFavorListRequestVO paramVO) throws Exception;
	
	// 스틸이미지 파일명 조회
	public String getStillFileName(GetFXFavorListRequestVO paramVO) throws Exception;
}
