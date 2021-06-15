package kr.co.wincom.imcs.api.getNSMnuList;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSMnuListDao {
	
	// 테스트 가입자 여부 조회
	public List<HashMap<String, String>> getTestSbc(GetNSMnuListRequestVO paramVO);
	
	//카테고리 레벨조회
	public HashMap<String, String> getCateLevel(GetNSMnuListRequestVO paramVO);
	public HashMap<String, String> getCateLevelMake(GetNSMnuListRequestVO paramVO);
	
	//카테고리 ID조회
	public String getCateId(GetNSMnuListRequestVO paramVO);

	//부모(Parent) 버전 조회
	public HashMap<String, String> getParentVersion(GetNSMnuListRequestVO paramVO);
	public HashMap<String, String> getParentVersion2(GetNSMnuListRequestVO paramVO);
	
	/////////////////////////////////////////////////////////////////////////////////
	// 테스트 가입자 여부 조회
	public List<HashMap<String, String>> getTestSbcMake(GetNSMnuListRequestVO paramVO);
	
	public List<GetNSMnuListResponseVO> getNSMnuList(GetNSMnuListRequestVO requestVO) throws Exception;
	
	//still image
	public List<HashMap<String, String>> getStillImageFileName(String category_id) throws Exception;
	
	//series info
	public String getSeriesInfo (GetNSMnuListRequestVO paramVO) throws Exception;
	
	//getNSMnuListDtl 응답값에서 예고편에 대한 본편 앨범ID를 주기 위해 조회
	public String getFpAlbumInfo (GetNSMnuListResponseVO paramVO) throws Exception;
	
	//M3U8 info
	public List<HashMap<String, String>> getChnlM3u8Search(GetNSMnuListResponseVO paramVO) throws Exception;
	
	//getListInfo
	public HashMap<String, String> getListInfo(GetNSMnuListResponseVO paramVO) throws Exception;

	//getPosterImgFileName
	public String getPosterImgFileName(String album_id) throws Exception;
	
	//getSubLevel
	public HashMap<String, String> getSubLevel(GetNSMnuListRequestVO paramVO) throws Exception;
	
	//getAlbumInfo
	public HashMap<String, String> getAlbumInfo(String album_id) throws Exception;
	
	//getProductType
	public String getProductType(String album_id) throws Exception;
}
