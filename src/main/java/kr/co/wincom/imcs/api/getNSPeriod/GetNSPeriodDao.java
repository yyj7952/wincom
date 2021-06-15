package kr.co.wincom.imcs.api.getNSPeriod;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComVersionVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSPeriodDao {
	
	// 가입자 해피콜 완료 여부 조회
	public List<String> getVodUseYn(GetNSPeriodRequestVO paramVO);
	
	// 가입자 연령 정보 조회
	public List<String> getPrInfo(GetNSPeriodRequestVO paramVO);

	// 카테고리 업데이트 주기 조회
	public List<String> getCateUpdPeriod();
	
	// 카테고리 버전정보 조회 (Guide VOD, 카테고리 리스트)
	public List<ComVersionVO> getVodVersion();
	
	// 서브 카테고리 Version 조회
	public List<ComVersionVO> getSubVersion();
	
	// 컨텐츠 업데이트 주기 조회
	public List<String> getConUpdPeriod();
	
	// EPG 스케줄 업데이트 주기 조회
	public List<String> getPSIUpdPeriod();
	
		
}
