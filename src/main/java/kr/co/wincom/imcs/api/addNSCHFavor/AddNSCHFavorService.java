package kr.co.wincom.imcs.api.addNSCHFavor;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface AddNSCHFavorService {

	// 선호 채널 등록 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public AddNSCHFavorResultVO addNSCHFavor(AddNSCHFavorRequestVO paramVO);
	
}
