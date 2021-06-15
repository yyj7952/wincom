package kr.co.wincom.imcs.api.getNSVPSI;

import java.util.List;

import kr.co.wincom.imcs.api.getNSVPSI.GetNSVPSIRequestVO;
import kr.co.wincom.imcs.api.getNSVPSI.GetNSVPSIResultVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

public interface GetNSMakeVPSIService {

	// GetNSMakeLists API
	public GetNSVPSIResultVO getNSMakeVPSIs(GetNSVPSIRequestVO requestVO) throws Exception;
	
	// GetNSLists에서도 사용하기 위하여 모듈화 
	public GetNSVPSIResultVO getNSMakeVPSI(GetNSVPSIRequestVO paramVO, String apiInfo) throws Exception;
	
	// 가상채널 EPG전체 스케줄정보 가져오기 API
	public GetNSVPSIResultVO getNSVPSI(GetNSVPSIRequestVO paramVO);

	// 검수 STB 여부 조회
	public void getTestSbc(GetNSVPSIRequestVO paramVO) throws Exception;
	
	//  Nscreen 가상채널 EPG전체 스케줄정보 조회
	public List<GetNSVPSIResponseVO> getNSVPSIList(GetNSVPSIRequestVO paramVO) throws Exception;
	
	// 이미지 정보 조회
	public List<StillImageVO> getImgUrl(GetNSVPSIRequestVO paramVO) throws Exception;
	
	// 이미지 파일 정보 조회
	public String getImgFileName(GetNSVPSIRequestVO paramVO) throws Exception;
	
	// 가상 채널 정보 조회
	public GetNSVPSIResponseVO getPrInfo(GetNSVPSIRequestVO paramVO) throws Exception;

}
