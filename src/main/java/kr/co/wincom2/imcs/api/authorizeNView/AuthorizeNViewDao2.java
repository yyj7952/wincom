package kr.co.wincom2.imcs.api.authorizeNView;

import kr.co.wincom.imcs.api.authorizeNView.AuthorizeNViewRequestVO;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeNViewDao2 {

	public Integer insertWatchHisNScreen2(AuthorizeNViewRequestVO paramVO);
	
	public Integer updateSetTimeNScreen(AuthorizeNViewRequestVO paramVO);
	
	public Integer insertSetTimeNScreen(AuthorizeNViewRequestVO paramVO);
	
	public String setTimeNScreenChk(AuthorizeNViewRequestVO paramVO);
	
	// 2019.11.01 - VOD 정산 프로세스 개선 : 메타 정보 저장
	public Integer insWatchMeta(AuthorizeNViewRequestVO paramVO);	
}
