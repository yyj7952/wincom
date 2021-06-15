package kr.co.wincom.imcs.api.getNSVisitDtl;

import java.util.List;

import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSVisitDtlDao {
	
	// 맛집/여행지 상세 정보 조회
	public List<GetNSVisitDtlResponseVO> getNSVisitDtl(GetNSVisitDtlRequestVO paramVO);
	
	// Face-Matching 준비여부 조회
	public List<FmInfoVO> getFminfo(GetNSVisitDtlRequestVO paramVO);
	
	// 트레일러 정보 조회
	public List<ComTrailerVO> getTrailerInfo(GetNSVisitDtlRequestVO paramVO);
	
    // 스틸이미지 정보 조회
	public List<StillImageVO> getStillImage(GetNSVisitDtlRequestVO paramVO);
	
	// 데이터 프리 유/무료 여부 조회
	public List<String> getDataFreeBillYn(GetNSVisitDtlRequestVO paramVO);
		
}
