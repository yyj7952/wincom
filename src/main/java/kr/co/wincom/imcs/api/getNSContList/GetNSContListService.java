package kr.co.wincom.imcs.api.getNSContList;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;


public interface GetNSContListService
{
	// 컨텐츠 리스트 조회 API
	public GetNSContListResultVO getNSContList(GetNSContListRequestVO requestVO) throws Exception;
	
	// 컨텐츠 상품 및 가격정보 조회
	public List<ContTypeVO> getContsInfo(GetNSContListRequestVO paramVO) throws Exception;
	
	// 패키지 상품 및 가격정보 조회
	public List<ContTypeVO> getPkgInfo(GetNSContListRequestVO paramVO) throws Exception;
	
	// 포인트 유무 조회
//    public String getPointYN(GetNSContListRequestVO paramVO) throws Exception;
    
    // 대장르 카테고리명 조회 
	public List<HashMap<String, String>> getGenreName(GetNSContListRequestVO paramVO) throws Exception;
    
	// 스틸 이미지명 조회
	public List<StillImageVO> getStillImage(GetNSContListRequestVO paramVO) throws Exception;
	
	// 평점(왓챠)정보 조회
	public List<ComWatchaVO> getWatchaInfo(GetNSContListRequestVO paramVO) throws Exception;
	
	// OST 정보조회
	public List<OstInfoVO> getOstInfo(GetNSContListRequestVO paramVO) throws Exception;
	
	// 컨텐츠 상세정보 조회(앨범정보 조회)
	public GetNSContListResponseVO getContDesc(GetNSContListRequestVO paramVO) throws Exception;
	
	// 컨텐츠 리스트 조회 
	public List<GetNSContListResponseVO> getContList(GetNSContListRequestVO paramVO) throws Exception; 
	
	// 트레일러 URL리스트 조회
	public ComTrailerVO getTrilerList(GetNSContListRequestVO paramVO) throws Exception;
	
	// SVOD PKG 정보 조회
	public List<SvodPkgVO> getSvodPkg(GetNSContListRequestVO paramVO) throws Exception;
	
	// 검수 STB 여부 조회
    public String testSbc(GetNSContListRequestVO paramVO) throws Exception;
	
	
	
	
}
