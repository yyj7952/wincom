package kr.co.wincom.imcs.api.getNSSeriesStat;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.getNSBuyList.GetNSBuyListRequestVO;

@Repository
public interface GetNSSeriesStatDao {
	
	// 페어링 체크
	public List<HashMap> getCustPairingChk (GetNSSeriesStatRequestVO requestVO);
	
	// 앨범 ID로 해당 앨범의 앨범 정보 및 svod only 여부 조회
	public List<getNSSeriesStatResponseVO> getAlbumInfo(GetNSSeriesStatRequestVO paramVO);
	
	//SVOD에 편성된 앨범의 경우 가입 여부를 체크
	public List<String> getSvodAlbum(GetNSSeriesStatRequestVO paramVO);
	
	// 구매 여부 조회 
	public List<HashMap> getBuyChk (GetNSSeriesStatRequestVO requestVO);
	
	// 엔스크린 가입자의 경우 I30가입자의 가입 여부 체크.
	public List<String> getNscSubscriptionInfo(GetNSSeriesStatRequestVO paramVO);
	
	// 구매 여부 조회 
	public List<HashMap> getNscBuyChk (GetNSSeriesStatRequestVO requestVO);
	
	//시청 이력 및 이어보기 정보 조회
	public List<HashMap> getNscLinkTime(GetNSSeriesStatRequestVO paramVO);
	
	//시청 이력 및 이어보기 정보 조회
	public List<HashMap> getIptvLinkTime(GetNSSeriesStatRequestVO paramVO);
}
