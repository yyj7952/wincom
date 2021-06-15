package kr.co.wincom.imcs.api.moveNSFavorIdx;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface MoveNSFavorIdxService
{
	// 찜목록 순서 변경 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public MoveNSFavorIdxResultVO moveNSFavorIdx(MoveNSFavorIdxRequestVO requestVO) throws Exception;
}
