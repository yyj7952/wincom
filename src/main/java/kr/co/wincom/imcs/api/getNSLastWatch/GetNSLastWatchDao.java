package kr.co.wincom.imcs.api.getNSLastWatch;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSLastWatchDao
{
	public HashMap<String, String> getTestSbc(GetNSLastWatchRequestVO paramVO);
	
	public HashMap<String, String> getLastWathcInfo(GetNSLastWatchRequestVO paramVO);
}
