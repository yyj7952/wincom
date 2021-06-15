package kr.co.wincom.imcs.api.getNSChPGM2;


public interface GetNSChPGM2Service {

	// 가상채널 EPG전체 스케줄정보 가져오기 API
	public GetNSChPGM2ResultVO getNSChPGM2(GetNSChPGM2RequestVO paramVO);

	// 검수 STB 여부 조회
	public void getTestSbc(GetNSChPGM2RequestVO paramVO) throws Exception;
	

}
