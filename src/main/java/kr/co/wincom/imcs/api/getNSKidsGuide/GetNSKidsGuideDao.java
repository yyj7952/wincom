package kr.co.wincom.imcs.api.getNSKidsGuide;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSKidsGuideDao
{
	public HashMap<String, String> getUserInfo(GetNSKidsGuideRequestVO paramVO);
	
	public HashMap<String, String> getKidsGuideCatInfo(GetNSKidsGuideRequestVO paramVO);
	
	public List<GetNSKidsGuide_VO> listKidsGuide(GetNSKidsGuideRequestVO paramVO);
}
