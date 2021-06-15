package kr.co.wincom.imcs.api.getNSCatBillInfo;

import java.util.List;



public interface GetNSCatBillInfoService {
	// 멀티뷰 채널정보 조회 API
	public GetNSCatBillInfoResultVO getNSCatBillInfo(GetNSCatBillInfoRequestVO paramVO);

	// 앨범 ID로 해당 앨범의 앨범 정보 및 svod only 여부 조회
	public List<GetNSCatBillInfoResponseVO> getAlbumInfo(GetNSCatBillInfoRequestVO paramVO) throws Exception;
	
	//인앱 정보 조회
	public String getInappInfo(GetNSCatBillInfoRequestVO paramVO, GetNSCatBillInfoResponseVO tempVO) throws Exception;
}
