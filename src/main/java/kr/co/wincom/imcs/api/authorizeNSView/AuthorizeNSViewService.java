package kr.co.wincom.imcs.api.authorizeNSView;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface AuthorizeNSViewService {
	
	// SVOD시청 인증정보 및 파일명 조회 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public AuthorizeNSViewResultVO authorizeNSView(AuthorizeNSViewRequestVO requestVO);
	
}
