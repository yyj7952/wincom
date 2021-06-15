package kr.co.wincom.imcs.api.getNSAlbumList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSAlbumListDao
{

	// 검수 STB 여부 조회
	List<String> getViewFlag2(GetNSAlbumListRequestVO requestVO);

	// 카테고리 정보 조회
	List<GetNSAlbumListResponseVO> getSubCateInfoN1(GetNSAlbumListRequestVO requestVO);
	List<GetNSAlbumListResponseVO> getSubCateInfoN2(GetNSAlbumListRequestVO requestVO);
	List<GetNSAlbumListResponseVO> getSubCateInfoE(GetNSAlbumListRequestVO requestVO);
	

	// 앨범정보 조회
	List<AlbumInfoVO> getAlbumInfo(GetNSAlbumListRequestVO requestVO);

	// 샘플 파일 정보 조회
	List<ComTrailerVO> getSampleInfo(GetNSAlbumListRequestVO requestVO);
	
	// 스틸 이미지명 조회
	List<StillImageVO> getStillFileName(GetNSAlbumListRequestVO requestVO);
}
