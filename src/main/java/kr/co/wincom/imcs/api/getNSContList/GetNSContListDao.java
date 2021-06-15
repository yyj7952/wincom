package kr.co.wincom.imcs.api.getNSContList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSContListDao
{

	// TEST계정 유무조회
	public List<String> testSbc(GetNSContListRequestVO requestVO);

	// PKG, SVOD 여부조회
	public List<SvodPkgVO> getSvodPkg(GetNSContListRequestVO requestVO);

	// 트레일러 URL리스트조회
	public List<ComTrailerVO> getTrilerList(GetNSContListRequestVO requestVO);

	// 컨텐츠 리스트조회 > MAIN
	public List<GetNSContListResponseVO> getContList(GetNSContListRequestVO requestVO);

	// 컨텐츠 상세정보조회
	public List<GetNSContListResponseVO> getContDesc(GetNSContListRequestVO requestVO);
	
	// 인앱 정보조회
	public HashMap<String, String> getInappInfo(String price);

	// OST 정보조회
	public List<OstInfoVO> getOstInfo(GetNSContListRequestVO requestVO);

	// 평점(왓챠) 정보조회
	public List<ComWatchaVO> getWatchaInfo(GetNSContListRequestVO requestVO);

	// 스틸이미지명 조회
	public List<StillImageVO> getStillImage(GetNSContListRequestVO requestVO);

	// 대장르 카테고리명 조회
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getGenreName(GetNSContListRequestVO requestVO);

	// 포인트 유무조회
	public List<String> getPointYN(GetNSContListRequestVO requestVO);

	// 구매중복 체크
	public HashMap<String, Object> buyDupCk(GetNSContListRequestVO paramVO);

	// 패키지 상품 및 가격정보 조회
	public List<ContTypeVO> getPkgInfo(GetNSContListRequestVO requestVO);

	// 패키지 구매중복 체크
	public HashMap<String, Object> pkgBuyDupCk(GetNSContListRequestVO paramVO);

	// 컨텐츠 상품 및 가격정보 조회
	public List<ContTypeVO> getContsInfo(GetNSContListRequestVO requestVO);
	
	// 왓챠(별점) 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO paramVO);
	
	// 시즌여부(HDTV) 조회
	public int getSeasonInfo(GetNSContListRequestVO paramVO);	
	
	// 시즌여부(HDTV가 아닌 경우) 조회
	public int getSeasonInfo2(GetNSContListRequestVO paramVO);

}
