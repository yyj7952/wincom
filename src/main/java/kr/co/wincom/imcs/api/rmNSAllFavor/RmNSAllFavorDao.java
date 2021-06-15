package kr.co.wincom.imcs.api.rmNSAllFavor;

import org.springframework.stereotype.Repository;

@Repository
public interface RmNSAllFavorDao
{
	// 찜목록 전체 삭제
	public Integer rmAllFavor(RmNSAllFavorRequestVO paramVO);
	
	// 2018.09.19 - 비디오포털 전체 삭제시에 MIMS 찜도 삭제
	public Integer rmAllFavorMIMS(RmNSAllFavorRequestVO paramVO);
	
}
