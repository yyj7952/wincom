package kr.co.wincom.imcs.api.getNSPeriod;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComVersionVO;

public interface GetNSPeriodService {
	
	 public GetNSPeriodResultVO getNSPeriod(GetNSPeriodRequestVO requestVO);
	 
	 // 가입자 해피콜완료 여부 조회
	 public String getVodUseYn(GetNSPeriodRequestVO paramVO) throws Exception;
	 
	 // 가입자 연령정보 조회
	 public String getPrInfo(GetNSPeriodRequestVO paramVO, String sqlId) throws Exception;
	 
	 // 카테고리 업데이트 주기 조회
	 public List<String> getCateUpdPeriode(GetNSPeriodRequestVO paramVO) throws Exception;
	 
	 // 버전정보 조회 (Guide VOD, 카테고리 리스트)
	 public List<ComVersionVO> getVodVersion(GetNSPeriodRequestVO paramVO) throws Exception;
	 
	 // 서브 카테고리 Version 조회
	 public List<ComVersionVO> getSubVersion(GetNSPeriodRequestVO paramVO) throws Exception;
	 
	 // 컨텐츠 업데이트 주기 조회
	 public List<String> getConUpdPeriod(GetNSPeriodRequestVO paramVO) throws Exception;
	 
	 // EPG 스케줄 업데이트 주기 조회
	 public List<String> getPSIUpdPeriod(GetNSPeriodRequestVO paramVO) throws Exception;

	 // 파일 Read 시 Key에 해당하는 Value 검색
	 public String getData(String strInput, String strCatID);
	 
	 public boolean queryLock(String LOCKFILE, GetNSPeriodRequestVO paramVO);
	 
	 public void queryUnLock(String LOCKFILE, GetNSPeriodRequestVO paramVO);
}
