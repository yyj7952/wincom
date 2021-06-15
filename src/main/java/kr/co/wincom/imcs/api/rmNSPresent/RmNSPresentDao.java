package kr.co.wincom.imcs.api.rmNSPresent;

import org.springframework.stereotype.Repository;

@Repository
public interface RmNSPresentDao {
	// VOD 선물 삭제
	public Integer rmNSPresentUpdate(RmNSPresentRequestVO paramVO);
		
}
