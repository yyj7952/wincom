package kr.co.wincom.imcs.api.getNSContReview;

import java.util.List;

public interface GetNSContReviewService {
	
	public GetNSContReviewResultVO getNSContReview(GetNSContReviewRequestVO paramVO);
	
	public List<GetNSContReviewResponseVO> getWatchaReviewList(GetNSContReviewRequestVO paramVO) throws Exception;
	
}
