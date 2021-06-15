package kr.co.wincom.imcs.api.getFXReposited;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;

public interface GetFXRepositedService {
	// 찜목록 리스트 조회
	public GetFXRepositedResultVO getFXReposited(GetFXRepositedRequestVO requestVO) throws Exception;
	
	// 시청목록 조회
	public List<GetFXRepositedResponseVO> getRepositedList(GetFXRepositedRequestVO paramVO) throws Exception; 
	
	// 카테고리 정보 조회
	public CateInfoVO getCateInfo(GetFXRepositedRequestVO paramVO) throws Exception;
	
	// 앨범정보 조회
	public AlbumInfoVO getAlbumInfo(GetFXRepositedRequestVO paramVO) throws Exception;
	
	// 이미지 파일명 조회
	public String getImgFileName(GetFXRepositedRequestVO paramVO) throws Exception;
}
