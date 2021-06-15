package kr.co.wincom.imcs.api.getNSVisitDtl;

import java.util.List;

import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;

public interface GetNSVisitDtlService {

	// 맛집/여행지 상세 정보 조회 API
	public GetNSVisitDtlResultVO getNSVisitDtl(GetNSVisitDtlRequestVO paramVO);

	// Face-Matching 준비여부 조회
	public FmInfoVO getFminfo(GetNSVisitDtlRequestVO paramVO) throws Exception;

	// 트레일러 정보 조회
	public List<ComTrailerVO> getTrailerInfo(GetNSVisitDtlRequestVO paramVO) throws Exception;

	// 스틸이미지 정보 조회
	public List<StillImageVO> getStillImage(GetNSVisitDtlRequestVO paramVO) throws Exception;
	
	// 맛집/여행지 상세 정보 조회
	public List<GetNSVisitDtlResponseVO> getNSVisitDtlList(GetNSVisitDtlRequestVO paramVO) throws Exception;
	 	 
}
