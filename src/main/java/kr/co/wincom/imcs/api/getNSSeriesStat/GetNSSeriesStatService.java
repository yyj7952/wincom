package kr.co.wincom.imcs.api.getNSSeriesStat;

import java.util.HashMap;
import java.util.List;


public interface GetNSSeriesStatService {
	// 페어링 체크
	public int getCustPairingChk (GetNSSeriesStatRequestVO paramVO) throws Exception;
	
	// 멀티뷰 채널정보 조회 API
	public getNSSeriesStatResultVO getNSSeriesStat(GetNSSeriesStatRequestVO paramVO);

	// 앨범 ID로 해당 앨범의 앨범 정보 및 svod only 여부 조회
	public List<getNSSeriesStatResponseVO> getAlbumInfo(GetNSSeriesStatRequestVO paramVO) throws Exception;
	
	//SVOD에 편성된 앨범의 경우 가입 여부를 체크
	public List<String> getSvodAlbum(GetNSSeriesStatRequestVO paramVO) throws Exception;
	
	// 엔스크린 가입자의 경우 I30가입자의 가입 여부 체크.
	public List<String> getNscSubscriptionInfo(GetNSSeriesStatRequestVO paramVO) throws Exception;
		
	//구매 여부 조회
	public List<HashMap> getBuyChk (GetNSSeriesStatRequestVO requestVO) throws Exception;
	
	//엔스크린 구매 여부 조회
	public List<HashMap> getNscBuyChk(GetNSSeriesStatRequestVO paramVO) throws Exception;
	
	//시청 이력 및 이어보기 정보 조회
	public List<HashMap> getNscLinkTime(GetNSSeriesStatRequestVO paramVO)throws Exception;
		
	//시청 이력 및 이어보기 정보 조회
	public List<HashMap> getIptvLinkTime(GetNSSeriesStatRequestVO paramVO)throws Exception;

}
