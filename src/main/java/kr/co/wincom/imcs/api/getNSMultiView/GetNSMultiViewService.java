package kr.co.wincom.imcs.api.getNSMultiView;

import java.util.List;



public interface GetNSMultiViewService {
	// 멀티뷰 채널정보 조회 API
	public GetNSMultiViewResultVO getNSMultiView(GetNSMultiViewRequestVO paramVO);

	// 멀티뷰 채널정보 조회
	public List<GetNSMultiViewResponseVO> getNSMultiViewList(GetNSMultiViewRequestVO paramVO) throws Exception;
}
