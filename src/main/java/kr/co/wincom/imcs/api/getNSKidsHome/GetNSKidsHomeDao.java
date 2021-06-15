package kr.co.wincom.imcs.api.getNSKidsHome;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSKidsHomeDao
{
	public HashMap<String, String> getUserInfo(GetNSKidsHomeRequestVO paramVO);
	
	public KidsHomeWatchInfo_1_VO getWatchInfo_1(GetNSKidsHomeRequestVO paramVO);
	
	public KidsHomeWatchInfo_album_VO getWatchInfoAlbum(GetNSKidsHomeRequestVO paramVO);
	
	public List<KidsHomeMenu_VO> listKidsHomeMenu(GetNSKidsHomeRequestVO paramVO);
	
	public List<KidsHomeRecom_VO> listKidsHomeRecom(GetNSKidsHomeRequestVO paramVO);
	
	public String getSeriesTypeDirCategoryId(GetNSKidsHomeRequestVO paramVO);
}
