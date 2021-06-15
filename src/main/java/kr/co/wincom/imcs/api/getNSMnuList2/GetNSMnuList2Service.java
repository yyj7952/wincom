package kr.co.wincom.imcs.api.getNSMnuList2;



public interface GetNSMnuList2Service {
	
	public GetNSMnuList2ResultVO getNSMnuList2(GetNSMnuList2RequestVO paramVO);
	
	//검수 STB 여부 조회
	public void getTestSbc(GetNSMnuList2RequestVO paramVO) throws Exception;
	
	//카테고리 레벨 조회
	public void getCateLevel(GetNSMnuList2RequestVO paramVO) throws Exception;
}
