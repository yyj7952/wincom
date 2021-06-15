package kr.co.wincom.imcs.api.getNSChPGM;

import java.util.List;
import kr.co.wincom.imcs.common.vo.StillImageVO;

public interface GetNSChPGMService {

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

}
