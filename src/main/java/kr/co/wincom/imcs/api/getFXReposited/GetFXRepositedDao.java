package kr.co.wincom.imcs.api.getFXReposited;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetFXRepositedDao{

	// 시청목록 조회
	List<GetFXRepositedResponseVO> getRepositedList(GetFXRepositedRequestVO requestVO);
	List<GetFXRepositedResponseVO> getRepositedList1(GetFXRepositedRequestVO requestVO);
	List<GetFXRepositedResponseVO> getRepositedList2(GetFXRepositedRequestVO requestVO);

	// 카테고리 정보 조회
	List<CateInfoVO> getCateInfo(GetFXRepositedRequestVO requestVO);

	// 앨범정보 조회
	List<AlbumInfoVO> getAlbumInfo(GetFXRepositedRequestVO requestVO);

	// 이미지파일명 조회
	List<StillImageVO> getImgFileName(GetFXRepositedRequestVO requestVO);

	
}
