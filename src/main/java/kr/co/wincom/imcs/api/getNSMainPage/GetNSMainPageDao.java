package kr.co.wincom.imcs.api.getNSMainPage;

import java.util.List;

import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSMainPageDao {
	// 행정동 코드 조회
	public List<String> getDongYn(GetNSMainPageRequestVO paramVO);
	
	// 가입자 조회
	public String getTestSbc(GetNSMainPageRequestVO paramVO);
	
	// 실시간 인기채널 프로그램 조회
	public List<GetNSMainPageChannelVO> getChnlList(GetNSMainPageRequestVO paramVO);
	
	// 서브 카테고리 정보 조회
	public List<GetNSMainPageSubVO> getSubList(GetNSMainPageRequestVO paramVO);
	
	// 스틸이미지명 조회
	public List<StillImageVO> getStillImage(String contsId);
	
	// 시리즈 카테고리 ID 조회
	public String getSerCatInfo(GetNSMainPageSubVO subVO);
	
	// 메인 카테고리 정보 조회
	public List<GetNSMainPageMainVO> getNSMainList(GetNSMainPageRequestVO paramVO);
	
	// 하위레벨 카테고리 여부 조회
	public CateInfoVO getParentCatYn(GetNSMainPageRequestVO paramVO);
	
}
