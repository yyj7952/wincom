package kr.co.wincom.imcs.api.getNSKidsWatch;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSKidsWatchDao
{
	public HashMap<String, String> getNowDate();
	
	public HashMap<String, String> getType_A_W(GetNSKidsWatchRequestVO paramVO);
	
	public int getBookCount();
	
	public List<HashMap<String, String>> listBookManageInfo(GetNSKidsWatchRequestVO paramVO);
	
	public List<HashMap<String, String>> listVer3AlbumNmReadCnt(GetNSKidsWatchRequestVO paramVO);
	
	public String getVer3BookReadRate(GetNSKidsWatchRequestVO paramVO);
}
