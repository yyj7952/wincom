package kr.co.wincom.imcs.api.getNSPSI;

import java.util.List;
import kr.co.wincom.imcs.common.vo.StillImageVO;

public interface GetNSPSIService {

	// 가상채널 EPG전체 스케줄정보 가져오기 API
	public GetNSPSIResultVO getNSPSI(GetNSPSIRequestVO paramVO);

	// 검수 STB 여부 조회
	public void getTestSbc(GetNSPSIRequestVO paramVO) throws Exception;
	
	//  Nscreen 가상채널 EPG전체 스케줄정보 조회
	public List<GetNSPSIResponseVO> getNSPSIList(GetNSPSIRequestVO paramVO) throws Exception;

}
