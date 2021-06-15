package kr.co.wincom.imcs.api.useNSPresent;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface UseNSPresentDao {

	// 구매 상품과 중복 여부 체크
	public Integer getPresentDupCk(UseNSPresentRequestVO vo);
	
	// 이벤트타입 정보 조회
	public List<String> getEventType(UseNSPresentRequestVO vo);
	
	public Integer useNSPresentUpdate(UseNSPresentRequestVO vo);
	
		
}
