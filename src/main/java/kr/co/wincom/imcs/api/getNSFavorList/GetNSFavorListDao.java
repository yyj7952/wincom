package kr.co.wincom.imcs.api.getNSFavorList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSFavorListDao
{
	// 채널코드 정보 조회
	public List<String> getSysDate(GetNSFavorListRequestVO requestVO);
	
	public List<GetNSFavorListResponseVO> getNSFavorList(GetNSFavorListRequestVO paramVO);
	
	// 채널코드 정보 조회
	public List<String> getChnlCode(GetNSFavorListRequestVO requestVO);

	// 찜목록 리스트 조회1 (채널코드 미존재 시)
	public List<GetNSFavorListResponseVO> getFavorInfoList1(GetNSFavorListRequestVO requestVO);
	
	// 찜목록 리스트 조회2 (채널코드 존재 시)
	public List<GetNSFavorListResponseVO> getFavorInfoList2(GetNSFavorListRequestVO requestVO);
	
	// 카테고리 정보 조회
	public List<CateInfoVO> getCateInfo(GetNSFavorListRequestVO requestVO);

	// 앨범 정보 조회 1
	public List<AlbumInfoVO> getAlbumInfo1(GetNSFavorListRequestVO requestVO);

	// 앨범 정보 조회 2
	public List<AlbumInfoVO> getAlbumInfo2(GetNSFavorListRequestVO requestVO);
	
	// 상품타입 정보 조회
	public List<String> getProductType(GetNSFavorListRequestVO requestVO);
	
	// 이미지 파일명 조회
	public List<StillImageVO> getImageFileName(GetNSFavorListRequestVO requestVO);
	
	// 스틸 이미지 파일명 조회
	public List<String> getStillFileName(GetNSFavorListRequestVO requestVO);

	
}
