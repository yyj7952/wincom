package kr.co.wincom.imcs.api.getNSChPGM;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.getNSChPGM.GetNSChPGMRequestVO;
import kr.co.wincom.imcs.api.getNSChPGM.GetNSChPGMResultVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

public interface GetNSMakeChPGMService {

	// GetNSMakeLists API
	public GetNSChPGMResultVO getNSMakeChPGMs(GetNSChPGMRequestVO requestVO) throws Exception;
	
	// GetNSLists에서도 사용하기 위하여 모듈화 
	public GetNSChPGMResultVO getNSMakeChPGM(GetNSChPGMRequestVO paramVO, String apiInfo) throws Exception;
	
	// 가상채널 EPG전체 스케줄정보 가져오기 API
	public GetNSChPGMResultVO getNSChPGM(GetNSChPGMRequestVO paramVO);

	// 검수 STB 여부 조회
	public void getTestSbc(GetNSChPGMRequestVO paramVO) throws Exception;
	
	//  Nscreen 가상채널 EPG전체 스케줄정보 조회
	public List<GetNSChPGMResponseVO> getNSChPGMList(GetNSChPGMRequestVO paramVO) throws Exception;
	
	// 이미지 정보 조회
	public List<StillImageVO> getImgUrl(GetNSChPGMRequestVO paramVO) throws Exception;
	
	// 이미지 파일 정보 조회
	public String getImgFileName(GetNSChPGMRequestVO paramVO) throws Exception;
	
	// 가상 채널 정보 조회
	public GetNSChPGMResponseVO getPrInfo(GetNSChPGMRequestVO paramVO) throws Exception;
	
	// 옴니뷰 정보 조회
	@SuppressWarnings("rawtypes")
	public List<HashMap> getVitualIdInfo(GetNSChPGMResponseVO paramVO, GetNSChPGMRequestVO vo) throws Exception;

}
