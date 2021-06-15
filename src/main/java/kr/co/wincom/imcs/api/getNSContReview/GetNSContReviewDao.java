package kr.co.wincom.imcs.api.getNSContReview;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSContReviewDao {
	
	public List<GetNSContReviewResponseVO> getWatchaReviewList(GetNSContReviewRequestVO requestVO);
	
	public List<GetNSContReviewResponseVO> getWatchaReviewList2(GetNSContReviewRequestVO requestVO);

}
