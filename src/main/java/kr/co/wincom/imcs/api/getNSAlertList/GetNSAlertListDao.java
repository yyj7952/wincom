package kr.co.wincom.imcs.api.getNSAlertList;

import java.util.List;

import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSAlertListDao
{

	// 채널코드정보 조회
	List<String> getChnlCode(GetNSAlertListRequestVO requestVO);

	// 알람받기 리스트 조회1  (채널코드가 없는 경우)
	List<GetNSAlertListResponseVO> getAlertListInfo1(GetNSAlertListRequestVO requestVO);

	// 알람받기 리스트 조회2  (채널코드가 있는 경우)
	List<GetNSAlertListResponseVO> getAlertListInfo2(GetNSAlertListRequestVO requestVO);

	// 카테고리 조회
	List<CateInfoVO> getCateInfo(GetNSAlertListRequestVO requestVO);

	// 상품타입 정보 조회
	List<String> getProductType(GetNSAlertListRequestVO requestVO);

	// 앨범정보 조회1
	List<AlbumInfoVO> getAlbumInfo1(GetNSAlertListRequestVO requestVO);

	// 앨범정보 조회2
	List<AlbumInfoVO> getAlbumInfo2(GetNSAlertListRequestVO requestVO);

	// 장르정보가 T일 경우의 이미지파일명 조회
	List<StillImageVO> getImageFileName(GetNSAlertListRequestVO requestVO);
	
	// 장르정보가 T가 아닌 경우의 이미지파일명 조회 
	List<String> getStillFileName(GetNSAlertListRequestVO requestVO);
}
