package kr.co.wincom.imcs.api.getFXContStat;


public interface GetFXContStatService {
	// 찜목록 리스트 조회 API
	public GetFXContStatResultVO getFXContStat(GetFXContStatRequestVO requestVO) throws Exception;
	
}
