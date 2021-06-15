package kr.co.wincom.imcs.api.getNSChPGM2;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSChPGM2Dao {
	
	// 검수 STB 여부 조회
	public List<String> getTestSbc(GetNSChPGM2RequestVO paramVO);
	
	
}
