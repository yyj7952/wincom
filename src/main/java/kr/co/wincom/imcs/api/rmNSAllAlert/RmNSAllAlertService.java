package kr.co.wincom.imcs.api.rmNSAllAlert;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface RmNSAllAlertService
{
	// 알람받기 전체 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSAllAlertResultVO rmNSAllAlert(RmNSAllAlertRequestVO requestVO) throws Exception;
	
	// 알람받기 전체 삭제
	public int rmNSAllAlertInfo(RmNSAllAlertRequestVO paramVO) throws Exception;
}
