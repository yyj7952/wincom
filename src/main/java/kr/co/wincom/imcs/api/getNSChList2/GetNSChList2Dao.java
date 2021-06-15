package kr.co.wincom.imcs.api.getNSChList2;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSChList2Dao {
	
	// 테스트 가입자 여부 조회
	public List<String> getTestSbc(GetNSChList2RequestVO paramVO);
	
	// 가입 상품 조회
	public List<String> getmProdId(GetNSChList2RequestVO paramVO);
	
}
