package kr.co.wincom.imcs.api.getFXProdInfo;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetFXProdInfoDao {
	// 검수 STB 여부 조회
	List<String> getTestSbc(GetFXProdInfoRequestVO requestVO);

	// 가입상품정보 조회
	List<String> getProdId(GetFXProdInfoRequestVO requestVO);

	// 요금제 가입상품정보 조회
	List<GetFXProdInfoResponseVO> getProdInfo1(GetFXProdInfoRequestVO requestVO);
	List<GetFXProdInfoResponseVO> getProdInfo2(GetFXProdInfoRequestVO requestVO);

	// 요금제 이미지 정보 조회
	List<StillImageVO> getImageFileName(GetFXProdInfoRequestVO requestVO);

	// 상품정보 조회
	@SuppressWarnings("rawtypes")
	List<HashMap<String, String>> getCutsomProdInfo(GetFXProdInfoRequestVO requestVO);

	// HDTV PROD SBC 조회
	List<ComCdVO> getProdSbc(GetFXProdInfoRequestVO requestVO);
	

	
}
