package kr.co.wincom.imcs.api.getNSMakePrefIP;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("rawtypes")
public interface GetNSMakePrefIPDao {
	
	//컨텐츠 정보 가져오기
	public List<GetNSMakePrefIPResponseVO> getPrefixIP(GetNSMakePrefIPRequestVO requestVO);
}
