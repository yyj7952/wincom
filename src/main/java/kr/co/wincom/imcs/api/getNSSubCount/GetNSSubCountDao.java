package kr.co.wincom.imcs.api.getNSSubCount;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSSubCountDao
{
	public HashMap<String, String> getUserInfo(GetNSSubCountRequestVO paramVO);
	
	public int getCategorySubContsCnt(GetNSSubCountRequestVO paramVO);
}
