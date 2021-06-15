package kr.co.wincom.imcs.api.addNSAlert;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface AddNSAlertDao
{
	// 알람받기 중복 체크
	public Integer getAlertDupChk(AddNSAlertRequestVO paramVO);

	// 알림받기 인덱스 가져오기
	public Integer getAlertIndex(String saId);

	// 알람받기 순번 조회
	public Integer getAlertCount(String saId);

	// 예약 앨범정보 조회
	public List<HashMap> getReservedInfo(String contsId);

	// 알람받기 등록
	public int addNSAlertInfo(AddNSAlertRequestVO paramVO);

	
}
