package kr.co.wincom.imcs.api.getNSComBotList;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSComBotListDao {
	// test_sbc 조회
	public HashMap<String, String> getTestSbc (GetNSComBotListRequestVO paramVO);
	
	public HashMap<String, String> getEXPInfo (GetNSComBotListRequestVO paramVO);
	
	// 상품정보 조회
	public List<GetNSComBotListResponseVO> getNSComBotList(GetNSComBotListRequestVO paramVO);
	

}
