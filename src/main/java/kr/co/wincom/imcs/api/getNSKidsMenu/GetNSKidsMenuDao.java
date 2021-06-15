package kr.co.wincom.imcs.api.getNSKidsMenu;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSKidsMenuDao
{
	public HashMap<String, String> getUserInfo(GetNSKidsMenuRequestVO paramVO);
	
	public KidsMenuCategoryInfo_VO getCategoryInfo(GetNSKidsMenuRequestVO paramVO);
	
	public List<KidsMenuMenuList_VO> listCategoryTopMenu(GetNSKidsMenuRequestVO paramVO);
	
	public List<KidsMenuMenuList_VO> listCategoryTopMenuBook(GetNSKidsMenuRequestVO paramVO);
	
	public KidsMenuCategoryInfo_VO getCategoryInfo_2(GetNSKidsMenuRequestVO paramVO);
	
	public List<KidsMenuMenuList_VO> listCategoryMenuList(GetNSKidsMenuRequestVO paramVO);
	
	public KidsMenuCategoryInfo_VO getCategoryInfo_type_c(GetNSKidsMenuRequestVO paramVO);
	
	public KidsMenuCategoryInfo_VO getCategoryInfo_type_L(GetNSKidsMenuRequestVO paramVO);
	
	public List<KidsMenuMenuList_VO> listCategoryLevelMenu(GetNSKidsMenuRequestVO paramVO);
	
	public List<KidsMenuGuide_VO> listGuideAlbums(GetNSKidsMenuRequestVO paramVO);
	
	public KidsMenuGuide_VO getGuideTempLevelInfo(GetNSKidsMenuRequestVO paramVO);
	
	public String getTempSerCatId(GetNSKidsMenuRequestVO paramVO);
	
	public List<KidsMenuMenuList_VO> listCategoryTopMenuBookWatch(GetNSKidsMenuRequestVO paramVO);
	
	public String getPPMSubscribe(GetNSKidsMenuRequestVO paramVO);
	
	public String getPPMSubscribeMobile(GetNSKidsMenuRequestVO paramVO);
	
	public List<KidsMenuMenuList_VO> listCategoryTopMenu_CatType_C(GetNSKidsMenuRequestVO paramVO);
	
	public HashMap<String, String> getMessageOfTypeH();
}
