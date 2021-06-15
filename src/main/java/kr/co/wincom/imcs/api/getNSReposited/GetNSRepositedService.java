package kr.co.wincom.imcs.api.getNSReposited;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;


public interface GetNSRepositedService {
	// 시청목록 조회
	public GetNSRepositedResultVO getNSReposited(GetNSRepositedRequestVO requestVO) throws Exception;
	
	// 구매정보 조회
	public HashMap<String, String> getBuyInfo(GetNSRepositedRequestVO paramVO, long tp1, GetNSRepositedResponseVO tempVO) throws Exception;
	
	// 이미지파일명 조회
	public String getImageFileName(GetNSRepositedRequestVO paramVO) throws Exception;
	
	// 상품 타입 조회 1
	public List<GetNSRepositedResponseVO> getProductType(GetNSRepositedRequestVO paramVO) throws Exception;
	
	// 스틸 이미지 정보 조회
	public String getStillFileName(GetNSRepositedRequestVO paramVO) throws Exception;
	
	// 앨범 정보 조회
	public AlbumInfoVO getAlbumInfo(GetNSRepositedRequestVO paramVO) throws Exception;
	
	// 카테고리 정보 조회
	public CateInfoVO getCateInfo(GetNSRepositedRequestVO paramVO) throws Exception;
	
	// 구매내역 리스트 조회
	public List<GetNSRepositedResponseVO> getWatchingList(GetNSRepositedRequestVO paramVO) throws Exception;

	//  이어보기 시간 조회
	public String getLinkTime(GetNSRepositedRequestVO paramVO, GetNSRepositedResponseVO tempVO) throws Exception;
}
