package kr.co.wincom.imcs.api.getNSContDtl;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

public interface GetNSContDtlService {
	// ContDtl 메인 메소드
	public GetNSContDtlResultVO getNSContDtl(GetNSContDtlRequestVO paramVO);

	// TEST계정 여부 조회
	public String testSbc(GetNSContDtlRequestVO paramVO) throws Exception;
	
	// 컨텐츠 상세 정보 조회
	public List<GetNSContDtlResponseVO> getNSContDtlList(GetNSContDtlRequestVO paramVO) throws Exception;
	
	// OST 정보 조회
	public List<OstInfoVO> getOstInfo(GetNSContDtlRequestVO paramVO) throws Exception;
	
	// 왓챠(별점) 정보 조회
	public ComWatchaVO getWatchaInfo(GetNSContDtlRequestVO paramVO) throws Exception;
	
	// 상품타입 정보조회
	public String getProductType(GetNSContDtlRequestVO paramVO) throws Exception;
	 
	// 트레일러 정보 조회
	public ComTrailerVO getTrailerInfo(GetNSContDtlRequestVO paramVO) throws Exception;
	
	// 이미지파일명 조회
	public List<StillImageVO> getStillImage(GetNSContDtlRequestVO paramVO) throws Exception;
	
	// 장르명 조회 
	public List<HashMap<String, String>> getGenreName(GetNSContDtlRequestVO paramVO) throws Exception;
	
	// 썸네일 이미지명 조회
	public String getThumbnail(GetNSContDtlRequestVO paramVO) throws Exception;
	 
	// SVOD 패키지 정보 조회 
	public List<SvodPkgVO> getSvodPkg(GetNSContDtlRequestVO paramVO) throws Exception;
	
	// 패키지 타입 및 가격정보 조회
	public List<ContTypeVO> getPkgType(GetNSContDtlRequestVO paramVO) throws Exception;
	 
	// 상품타입 및 가격정보 조회
	public List<ContTypeVO> getContsType(GetNSContDtlRequestVO paramVO) throws Exception;
	 
	// SVOD 상품 유무 조회
	public Integer ContsTypeChk(GetNSContDtlRequestVO paramVO) throws Exception;
	 
	// SVOD 상품 유무 조회 2
	public Integer ContsTypeChk2(GetNSContDtlRequestVO paramVO) throws Exception;
	 
	// 포인트 유무 조회
//	public String getPointYN(GetNSContDtlRequestVO paramVO) throws Exception;

	// Face-Match 준비여부 조회
	public FmInfoVO getFminfo(GetNSContDtlRequestVO paramVO) throws Exception;
}
