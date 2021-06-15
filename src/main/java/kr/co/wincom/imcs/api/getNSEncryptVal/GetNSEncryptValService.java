package kr.co.wincom.imcs.api.getNSEncryptVal;



public interface GetNSEncryptValService
{
	// 컨텐츠 리스트 조회 API
	public GetNSEncryptValResultVO getNSEncryptVal(GetNSEncryptValRequestVO requestVO) throws Exception;

	//암호키 조회
	public String getEnctyptKey(GetNSEncryptValRequestVO paramVO) throws Exception;
	
	
}
