package kr.co.wincom.imcs.api.getNSMnuListDtl2;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

public interface GetNSMnuListDtl2Service {
	
	// main method
	public GetNSMnuListDtl2ResultVO getNSMnuListDtl2(GetNSMnuListDtl2RequestVO paramVO);
	
}
