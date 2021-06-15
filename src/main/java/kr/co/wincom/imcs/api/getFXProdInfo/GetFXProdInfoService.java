package kr.co.wincom.imcs.api.getFXProdInfo;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;


public interface GetFXProdInfoService {
	// 찜목록 리스트 조회 API
	public GetFXProdInfoResultVO getFXProdInfo(GetFXProdInfoRequestVO requestVO) throws Exception;

	// 검수 STB 여부 조회
	public String getTestSbc(GetFXProdInfoRequestVO paramVO) throws Exception;
	
	// 가입상품 조회
	public String getProdId(GetFXProdInfoRequestVO paramVO) throws Exception;
	
	// 요금제 가입상품정보 조회
	public List<GetFXProdInfoResponseVO> getProdInfo(GetFXProdInfoRequestVO paramVO) throws Exception;
	
	// 고객 상품정보 조회
	public List<HashMap<String, String>> getCutsomProdInfo(GetFXProdInfoRequestVO paramVO) throws Exception;
	
	// 이미지 파일명 조회
	public StillImageVO getImageFileName(GetFXProdInfoRequestVO paramVO) throws Exception;
	
	// HDTV PROD SBC 조회
	public List<ComCdVO> getProdSbc(GetFXProdInfoRequestVO paramVO) throws Exception;
}
