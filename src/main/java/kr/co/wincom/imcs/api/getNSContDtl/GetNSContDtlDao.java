package kr.co.wincom.imcs.api.getNSContDtl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSContDtlDao {

	// 검수 STB 여부 조회
	public List<String> testSbc(GetNSContDtlRequestVO paramVO);
	
	// 컨텐츠 상세 정보 조회
	public List<GetNSContDtlResponseVO> getNSContDtl(GetNSContDtlRequestVO paramVO);
	
	// 인앱정보 조회
	public HashMap<String, String> getApproval(String suggestedPrice);
	
	// OST 정보 조회
	public List<OstInfoVO> getOstInfo(GetNSContDtlRequestVO paramVO);
	
	// 왓챠(별점) 정보 조회
	public List<ComWatchaVO> getWatchaInfo(GetNSContDtlRequestVO paramVO);
	
	// 상품타입 정보조회
	public List<String> getProductType(GetNSContDtlRequestVO paramVO);
	
	// 구매상품여부 조회
	public ComDupCHk getBuyDupChk(GetNSContDtlRequestVO paramVO);
	
	// 트레일러 정보 조회
	public List<ComTrailerVO> getTrailerInfo(GetNSContDtlRequestVO paramVO);
	
    // 스틸이미지 정보 조회
	public List<StillImageVO> getStillImage(GetNSContDtlRequestVO paramVO);
	
	// 대장르명 조회
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getGenreName(GetNSContDtlRequestVO paramVO);
	
	// 썸네일 이미지명 조회
	public List<String> getThumbnail(GetNSContDtlRequestVO paramVO);
	
	// SVOD 패키지 정보 조회 
	public List<SvodPkgVO> getSvodPkg(GetNSContDtlRequestVO paramVO);
	
	// 패키지 상품정보 및 가격정보 조회 
	public List<ContTypeVO> getPkgType(GetNSContDtlRequestVO paramVO);
    
	// 컨텐츠 상품정보 및 가격정보 조회
	public List<ContTypeVO> getContsType(GetNSContDtlRequestVO paramVO);

	// 컨텐츠 구매 정보 조회1
	public List<Integer> ContsTypeChk(GetNSContDtlRequestVO paramVO);
	
	// 컨텐츠 구매 정보 조회2
	public List<Integer> ContsTypeChk2(GetNSContDtlRequestVO paramVO);
	
	// 컨텐츠 구매 정보 조회3
	public ComDupCHk ContsTypeChk3(GetNSContDtlRequestVO paramVO);
	
	// 포인트 여부 조회
	public List<String> getPointYN(GetNSContDtlRequestVO paramVO);
	
	// Face-Match 여부 조회
	public List<FmInfoVO> getFminfo(GetNSContDtlRequestVO paramVO);
	
	// 패키지 타입정보 조회
	public ComDupCHk getPkgDupChk(GetNSContDtlRequestVO paramVO);
	
	// 왓챠(별점) 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO paramVO);
	
	
	// 시즌여부(HDTV) 조회
	public List<String> getSeasonInfo(GetNSContDtlRequestVO paramVO);
	
	
	// 시즌여부(HDTV가 아닌 경우) 조회
	public List<String> getSeasonInfo2(GetNSContDtlRequestVO paramVO);
	
	// 예고편 앨범 ID 조회
	public List<String> getTasteAlbum(GetNSContDtlRequestVO paramVO);
	
}
