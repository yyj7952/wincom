package kr.co.wincom.imcs.api.rmNSAlert;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface RmNSAlertService {
	
	// 알람받기 삭제 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public RmNSAlertResultVO rmNSAlert(RmNSAlertRequestVO requestVO) throws Exception;
	
	// 알람받기 삭제
	public int rmNSAlertInfo(RmNSAlertRequestVO paramVO) throws Exception;
	
	// 알람받기 인덱스 수정
	public int uptAlertIndex(RmNSAlertRequestVO paramVO) throws Exception;
	
}
