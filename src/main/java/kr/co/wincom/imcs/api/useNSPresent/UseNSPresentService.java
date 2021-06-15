package kr.co.wincom.imcs.api.useNSPresent;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface UseNSPresentService {
	
	// VOD 선물 사용 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public UseNSPresentResultVO useNSPresent(UseNSPresentRequestVO paramVO);

	// 이벤트타입 정보조회
	public String getEventType(UseNSPresentRequestVO paramVO) throws Exception;

	// VOD 선물사용
	public int usePresent(UseNSPresentRequestVO paramVO) throws Exception;
	 	 	 
}
