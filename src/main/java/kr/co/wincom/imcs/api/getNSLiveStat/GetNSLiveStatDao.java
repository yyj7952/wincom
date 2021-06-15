package kr.co.wincom.imcs.api.getNSLiveStat;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComSvodVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSLiveStatDao
{
	// 오늘 날짜 가져오기
	public String getThisDate();

	// 월정액상품정보 조회
	public List<GetNSLiveStatResponseVO> getPPMProdInfo(GetNSLiveStatRequestVO requestVO);

	// 구매중복 체크 (FVOD)
	public HashMap<String, Object> getBuyDupChk1(GetNSLiveStatRequestVO paramVO);
	
	// 구매중복 체크 (PPV)
	public HashMap<String, Object> getBuyDupChk2(GetNSLiveStatRequestVO paramVO);
	
	// 구매중복 체크 (PVOD)
	public HashMap<String, Object> getBuyDupChk3(GetNSLiveStatRequestVO paramVO);

	// 유료 콘서트 채널 여부 조회
	public String getCuesheetId(GetNSLiveStatRequestVO requestVO);
	
	// 데이터프리 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO paramVO);
	
	// 엔스크린 기능 제공 단말 및 엔스크린 컨텐츠일 경우 페어링된 가입자의 구매/가입 여부를 확인
	@SuppressWarnings("rawtypes")
	public List<HashMap> getNScreenPairingInfo(GetNSLiveStatRequestVO requestVO);
	
	// 엔스크린(NSCREEN) 구매 여부 체크
	public HashMap<String, String> getNScreenBuyChk(GetNSLiveStatRequestVO requestVO);		

}