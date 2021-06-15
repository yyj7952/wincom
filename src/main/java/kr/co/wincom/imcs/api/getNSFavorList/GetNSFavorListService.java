package kr.co.wincom.imcs.api.getNSFavorList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;


public interface GetNSFavorListService {
	
	// 날짜 정보 조회
	public void getSysDate(GetNSFavorListRequestVO paramVO) throws Exception;
	// 찜목록 리스트 조회
	public GetNSFavorListResultVO getNSFavorList(GetNSFavorListRequestVO requestVO) throws Exception;
	
	// 채널코드 정보 조회
	public void getChnlCode(GetNSFavorListRequestVO paramVO) throws Exception;
	
	// 카테고리 정보 조회
	public CateInfoVO getCateInfo(GetNSFavorListRequestVO paramVO) throws Exception;
	
	// 찜목록 정보 조회
	public List<GetNSFavorListResponseVO> getFavorInfoList(GetNSFavorListRequestVO paramVO) throws Exception;
	
	// 앨범 정보 조회
	public AlbumInfoVO getAlbumInfo(GetNSFavorListRequestVO paramVO) throws Exception;
	
	// 상품타입 정보 조회
	public String getProductType(GetNSFavorListRequestVO paramVO) throws Exception;
	
	// 이미지 파일명 조회
	public List<StillImageVO> getImageFileName(GetNSFavorListRequestVO paramVO) throws Exception;
	
	// 스틸 이미지 파일명 조회
	public String getStillFileName(GetNSFavorListRequestVO paramVO) throws Exception;
	
}
