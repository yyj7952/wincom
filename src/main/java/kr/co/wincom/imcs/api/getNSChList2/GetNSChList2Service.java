package kr.co.wincom.imcs.api.getNSChList2;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;



public interface GetNSChList2Service {
	
	// 가상채널 EPG전체 채널정보 가져오기 API 
	public GetNSChList2ResultVO getNSChList2(GetNSChList2RequestVO paramVO);
	
	// 검수 STB 여부 조회
	public void getTestSbc(GetNSChList2RequestVO paramVO) throws Exception;
	
	// 가입 상품 조회
	public void getmProdId(GetNSChList2RequestVO paramVO) throws Exception;

}
