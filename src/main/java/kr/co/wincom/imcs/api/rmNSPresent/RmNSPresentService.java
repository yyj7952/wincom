package kr.co.wincom.imcs.api.rmNSPresent;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface RmNSPresentService {
	
	// VOD 선물 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSPresentResultVO rmNSPresent(RmNSPresentRequestVO paramVO);
	 
	// VOD 선물 삭제
	public Integer rmNSPresentUpdate(RmNSPresentRequestVO paramVO) throws Exception;
	
}
