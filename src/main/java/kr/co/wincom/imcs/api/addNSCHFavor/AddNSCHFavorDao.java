package kr.co.wincom.imcs.api.addNSCHFavor;

import org.springframework.stereotype.Repository;

@Repository
public interface AddNSCHFavorDao {
	
	// 선호채널 중복 체크
	public Integer getCHFavorDupCk(AddNSCHFavorRequestVO paramVO);
	
	// 선호채널 인덱스 조회
	public String getCHFavorIndex(AddNSCHFavorRequestVO paramVO);
	
	// 선호채널 등록
	public Integer insertCHFavor(AddNSCHFavorRequestVO paramVO);
		
}
