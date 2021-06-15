package kr.co.wincom.imcs.api.getNSPSI;

import java.util.List;

import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSPSIDao {
	
	// 검수 STB 여부 조회
	public List<String> getTestSbc(GetNSPSIRequestVO paramVO);
	
	// Nscreen EPG전체 스케줄정보 조회
	public List<GetNSPSIResponseVO> getNSPSIList1(GetNSPSIRequestVO paramVO);
	public List<GetNSPSIResponseVO> getNSPSIList2(GetNSPSIRequestVO paramVO);

}
