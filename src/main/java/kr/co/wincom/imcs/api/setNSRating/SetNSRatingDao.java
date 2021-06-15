package kr.co.wincom.imcs.api.setNSRating;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SetNSRatingDao {
	
	// 연령정보 조회
	public List<Integer> getRatingChk(SetNSRatingRequestVO vo);
	
	public Integer updateRating(SetNSRatingRequestVO vo);
	
	public Integer insertRating(SetNSRatingRequestVO vo);
		
}
