package kr.co.wincom.imcs.api.getNSVPSI;

import java.util.List;

import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSVPSIDao {
	
	// 검수 STB 여부 조회
	public List<String> getTestSbc(GetNSVPSIRequestVO paramVO);
	
	public int selectPRGSCHEDULE();
	
	// Nscreen 가상채널 EPG전체 스케줄정보 조회
	public List<GetNSVPSIResponseVO> getNSVPSIList1(GetNSVPSIRequestVO paramVO);
	public List<GetNSVPSIResponseVO> getNSVPSIList2(GetNSVPSIRequestVO paramVO);
	public List<GetNSVPSIResponseVO> getNSVPSIList3(GetNSVPSIRequestVO paramVO);
	
	// 이미지 정보 조회
	public List<StillImageVO> getImgUrl(GetNSVPSIRequestVO paramVO);
	
	// 이미지 파일 정보 조회
	public List<String> getImgFileName(GetNSVPSIRequestVO paramVO);
	
	// 가상 채널 정보 조회
	public List<GetNSVPSIResponseVO> getPrInfo(GetNSVPSIRequestVO paramVO);
}
