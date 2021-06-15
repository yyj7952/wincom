package kr.co.wincom.imcs.api.getNSKidsList;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSKidsListDao
{
	public HashMap<String, String> getUserInfo(GetNSKidsListRequestVO paramVO);
	
	public HashMap<String, String> getCatType(GetNSKidsListRequestVO paramVO);
	
	public List<HashMap<String, String>> listCategoryInfo_C_2Lvl(GetNSKidsListRequestVO paramVO);
	
	public List<HashMap<String, String>> listCategoryInfo_T_2Lvl(GetNSKidsListRequestVO paramVO);
	
	public List<HashMap<String, String>> listCategoryInfo_T_3OverLvl(GetNSKidsListRequestVO paramVO);
	
	public List<HashMap<String, String>> listCategoryInfo_G_2_3Lvl(GetNSKidsListRequestVO paramVO);
	
	public List<HashMap<String, String>> listWatchInfo(GetNSKidsListRequestVO paramVO);
	
	public List<HashMap<String, String>> listLastAlbumInfo(GetNSKidsListRequestVO paramVO);
	
	public List<HashMap<String, String>> listHotCatInfo_C();
	
	public List<HashMap<String, String>> listHotCatInfo_B();
	
	public List<HashMap<String, String>> listMessage_G();
	
	public String getAwsSvcFlag();
	
	public String getPosterImgFileName(GetNSKidsListRequestVO paramVO);
	
	public String getCatVersionLevel_pCatId(GetNSKidsListRequestVO paramVO);
	
	public HashMap<String, String> getCatVersionLevel_subVer(GetNSKidsListRequestVO paramVO);
	
	public List<HashMap<String, String>> listKidsContentsCache(GetNSKidsListRequestVO paramVO);
	
	public HashMap<String, String> getContsPoster(String contents_id);
	
	public List<HashMap<String, String>> listWatchHotInfoCont(GetNSKidsListRequestVO paramVO);
}
















