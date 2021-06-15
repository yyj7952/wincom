package kr.co.wincom.imcs.api.getFXFavorList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetFXFavorListDao
{
	// 찜목록 조회
	List<GetFXFavorListResponseVO> getFavorList(GetFXFavorListRequestVO requestVO);

	// 카테고리 정보 조회
	List<CateInfoVO> getCateInfo(GetFXFavorListRequestVO requestVO);

	// 앨범정보 조회
	List<AlbumInfoVO> getAlbumInfo(GetFXFavorListRequestVO requestVO);

	// 상품타입정보 조회
	List<String> getProdType1(GetFXFavorListRequestVO requestVO);
	List<String> getProdType2(GetFXFavorListRequestVO requestVO);

	// 이미지 파일명 조회
	List<StillImageVO> getImageFileName(GetFXFavorListRequestVO requestVO);
	List<String> getStillFileName(GetFXFavorListRequestVO requestVO);
	
	

	
}
