package kr.co.wincom.imcs.api.getNSNodeList;

import java.util.List;
import kr.co.wincom.imcs.common.vo.StillImageVO;

public interface GetNSNodeListService {

	// 가상채널 EPG전체 스케줄정보 가져오기 API
	public GetNSNodeListResultVO getNSNodeList(GetNSNodeListRequestVO paramVO);

}
