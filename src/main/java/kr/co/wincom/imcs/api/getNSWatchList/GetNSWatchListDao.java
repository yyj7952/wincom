package kr.co.wincom.imcs.api.getNSWatchList;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSWatchListDao {
	
	// 검수 STB여부 조회
	public HashMap<String, String> getTestSbc(GetNSWatchListRequestVO paramVO) throws Exception;

	//시청목록 가져오기...
	public List<GetNSWatchListResponseVO> getSubscribeList1(GetNSWatchListRequestVO paramVO) throws Exception;
	public List<GetNSWatchListResponseVO> getSubscribeList2(GetNSWatchListRequestVO paramVO) throws Exception;
	public List<GetNSWatchListResponseVO> getSubscribeList3(GetNSWatchListRequestVO paramVO) throws Exception;
	
	//구매체크
	public HashMap<String, String> getBuyCheck(GetNSWatchListResponseVO responseVO) throws Exception;
	
	//데이터프리체크
	public String getDatafreeCheck(GetNSWatchListResponseVO requestVO) throws Exception;
	
	//구매체크
	public List<String> getSvodCheck(GetNSWatchListResponseVO requestVO) throws Exception;
	
	//IPTV구매체크
	public HashMap<String, String> getIptvBuyCheck(GetNSWatchListResponseVO requestVO) throws Exception;
	
	//IPTV 가입체크 (해당앨범의 SVOD 상품 가져오기)
	public List<String> getIptvSvodCheck(GetNSWatchListResponseVO requestVO) throws Exception;
	
	public addNextSeriesResponseVO addNextSeries(GetNSWatchListResponseVO requestVO) throws Exception;
	
	//모바일 가입자 가입 상품 정보 조회
	public List<String> getNscCustProd(GetNSWatchListRequestVO requestVO) throws Exception;
	
	//iptv 가입자 가입 상품 정보 조회
	public List<String> getIptvCustProd(GetNSWatchListRequestVO requestVO) throws Exception;
	
	// CATEGORY_TYPE 을 구하기 위해서 kids_type 을 조회함 - 모바일
	public String getKidsType_1(GetNSWatchListRequestVO paramVO) throws Exception;
	
	// CATEGORY_TYPE 을 구하기 위해서 kids_type 을 조회함 - IPTV
	public String getKidsType_2(GetNSWatchListRequestVO paramVO) throws Exception;
	
	public int kidProductCd(GetNSWatchListResponseVO paramVO) throws Exception;
		
}
