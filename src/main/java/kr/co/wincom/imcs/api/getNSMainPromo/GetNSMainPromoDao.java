package kr.co.wincom.imcs.api.getNSMainPromo;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSMainPromoDao
{
	
	// 공지 정보 조회
	public List<GetNSMainPromoResponseVO> getMainPromo(GetNSMainPromoRequestVO param);

	// 공지 메세지 조회
	public List<String> getMessage(GetNSMainPromoRequestVO param);
	
}
