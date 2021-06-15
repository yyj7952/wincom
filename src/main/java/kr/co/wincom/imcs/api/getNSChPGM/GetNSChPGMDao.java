package kr.co.wincom.imcs.api.getNSChPGM;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSChPGMDao {
	
	// 검수 STB 여부 조회
	public List<String> getTestSbc(GetNSChPGMRequestVO paramVO);
	
	public int selectPRGSCHEDULE();
	
	// Nscreen 가상채널 EPG전체 스케줄정보 조회
	public List<GetNSChPGMResponseVO> getNSChPGMList(GetNSChPGMRequestVO paramVO);

	// 이미지 정보 조회
	public List<StillImageVO> getImgUrl(GetNSChPGMRequestVO paramVO);
	
	// 이미지 파일 정보 조회
	public List<String> getImgFileName(GetNSChPGMRequestVO paramVO);
	
	// 가상 채널 정보 조회
	public List<GetNSChPGMResponseVO> getPrInfo(GetNSChPGMRequestVO paramVO);
	
	// 포스터 이미지, 가상 채널 및 공연 앨범의 연령등급 및 재생시간 정보를 제공
	@SuppressWarnings("rawtypes")
	public List<HashMap> getImgFileInfo(GetNSChPGMResponseVO paramVO);
	
	// 옴니뷰 정보 조회
	@SuppressWarnings("rawtypes")
	public List<HashMap> getVitualIdInfo(GetNSChPGMResponseVO paramVO);
	
	
}
