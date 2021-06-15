package kr.co.wincom.imcs.api.getNSAlbumList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;


public interface GetNSAlbumListService {

	public GetNSAlbumListResultVO getNSAlbumList(GetNSAlbumListRequestVO requestVO);
	
	// 이미지명 조회
	public String getStillFileName(GetNSAlbumListRequestVO paramVO) throws Exception;
	 
	// 앨범 정보 조회
	public ComTrailerVO getSampleInfo(GetNSAlbumListRequestVO paramVO) throws Exception;
	
	// 앨범 리스트 조회
	public AlbumInfoVO getAlbumListInfo(GetNSAlbumListRequestVO paramVO) throws Exception;
	
	// 카테고리 정보 조회
	public List<GetNSAlbumListResponseVO> getSubCateInfo(GetNSAlbumListRequestVO paramVO) throws Exception;
	    
	// View Flag 조회
	public void getViewFlag2(GetNSAlbumListRequestVO paramVO) throws Exception;
	
}
