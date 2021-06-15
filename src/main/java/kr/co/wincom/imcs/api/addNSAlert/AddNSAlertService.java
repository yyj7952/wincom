package kr.co.wincom.imcs.api.addNSAlert;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface AddNSAlertService
{
	// 찜목록 등록 API 호출 
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public AddNSAlertResultVO AddNSAlert(AddNSAlertRequestVO requestVO) throws Exception;
	
	// 찜목록 등록
	public int addNSAlertInfo(AddNSAlertRequestVO paramVO) throws Exception;
	
	
}
