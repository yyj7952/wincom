package kr.co.wincom.imcs.api.getNSAlbumStat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSAlbumStatDao
{
	public HashMap<String, String> getCustPairingChk(GetNSAlbumStatRequestVO paramVO);
	
	public List<HashMap<String, String>> listMultiAlbumInfo(GetNSAlbumStatRequestVO paramVO);
	
	public List<String> listSvodCheck(GetNSAlbumStatRequestVO paramVO);
	
	public List<HashMap<String, String>> listContsBuyCheck(GetNSAlbumStatRequestVO paramVO);
	
	public List<String> listNScreenSvodCheck(GetNSAlbumStatRequestVO paramVO);
	
	public List<HashMap<String, String>> listNScreenContsBuyCheck(GetNSAlbumStatRequestVO paramVO);
	
	public List<HashMap<String, String>> listReadingCnt(GetNSAlbumStatRequestVO paramVO);
	
	public ArrayList<String> kidProductCd(GetNSAlbumStatRequestVO paramVO);
}
