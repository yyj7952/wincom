package kr.co.wincom.imcs.api.rmNSAllAlert;

import org.springframework.stereotype.Repository;

@Repository
public interface RmNSAllAlertDao
{
	// 알람받기 전체 삭제
	public int rmNSAllAlertInfo(RmNSAllAlertRequestVO paramVO);
	
}
