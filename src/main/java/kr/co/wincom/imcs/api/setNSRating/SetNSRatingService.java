package kr.co.wincom.imcs.api.setNSRating;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



public interface SetNSRatingService {
	
	 @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	 public SetNSRatingResultVO setNSRating(SetNSRatingRequestVO paramVO);
	 /*
	 public Integer rating_Chk(SetNSRatingRequestVO paramVO) throws Exception;
		
	 public Integer updateRating(SetNSRatingRequestVO paramVO) throws Exception;
		
	 public Integer insertRating(SetNSRatingRequestVO paramVO) throws Exception;*/
	 	 	 
}
