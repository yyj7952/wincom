package kr.co.wincom.imcs.api.getNSMultiConts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSMultiContsDao
{
	
	// 시청 여부 조회
	public List<String> isWatch(GetNSMultiContsRequestVO requestVO);

	// 링크타입 정보 조회
	public List<GetNSMultiContsResponseVO> getLinkTime(GetNSMultiContsRequestVO requestVO);
	
	// 런타입 정보 조회
	public List<String> getAlbumRuntime(GetNSMultiContsRequestVO requestVO);

}
