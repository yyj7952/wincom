package kr.co.wincom.imcs.api.rmNSAlert;

import org.springframework.stereotype.Repository;

@Repository
public interface RmNSAlertDao
{
	// 알람받기 삭제
	public int rmNSAlertInfo(RmNSAlertRequestVO paramVO);

	// 알람받기 인덱스 수정 
	public int uptAlertIndex(RmNSAlertRequestVO paramVO);
	
	
}
