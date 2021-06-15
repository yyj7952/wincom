package kr.co.wincom.imcs.api.getNSDMPurDtl;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSDMPurDtlDao
{
	
	// 구매 상세 정보 조회
	public List<GetNSDMPurDtlResponseVO> getBuyDmDetail(GetNSDMPurDtlRequestVO paramVO);
	
	// 구매 상세 정보 조회 - IPTV(엔스크린)
	public List<GetNSDMPurDtlResponseVO> getBuyDmDetail_iptv(GetNSDMPurDtlRequestVO paramVO);
	
	
}
