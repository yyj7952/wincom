package kr.co.wincom.imcs.api.getNSAlertList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;


public interface GetNSAlertListService
{
	// 알람목록 리스트 가져오기 (lgvod339.pc) 
	public GetNSAlertListResultVO getNSAlertList(GetNSAlertListRequestVO requestVO);
		
	// 채널코드정보 조회
	public void getChnlCode(GetNSAlertListRequestVO paramVO) throws Exception;
	
	// 알람받기 리스트 조회
	public List<GetNSAlertListResponseVO> getAlertListInfo(GetNSAlertListRequestVO paramVO) throws Exception;
	
	// 카테고리 조회
	public CateInfoVO getCateInfo(GetNSAlertListRequestVO paramVO) throws Exception;
	
	// 상품타입 정보 조회
	public String getProductType(GetNSAlertListRequestVO paramVO) throws Exception;
	
	// 앨범정보 조회
	public AlbumInfoVO getAlbumInfo(GetNSAlertListRequestVO paramVO) throws Exception;
	
	// 장르정보가 T일 경우의 이미지파일명 조회 
	public String getImageFileName(GetNSAlertListRequestVO paramVO) throws Exception;
	
	// 장르정보가 T가 아닌 경우의 이미지파일명 조회
	public String getStillFileName(GetNSAlertListRequestVO paramVO) throws Exception;
}
