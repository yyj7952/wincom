package kr.co.wincom.imcs.api.getNSLists;

import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.vo.UrlListVO;

public interface GetNSMakeListsService {

	// GetNSMakeLists API
	public GetNSListsResultVO getNSMakeLists(GetNSListsRequestVO requestVO) throws Exception;
	
	// GetNSLists에서도 사용하기 위하여 모듈화 
	public GetNSListsResultVO getMakeList(GetNSListsRequestVO paramVO, String apiInfo) throws Exception;
	
	// 상품타입 정보조회
	public String getProductType(GetNSListsRequestVO paramVO) throws Exception;
	
	// 앨범 정보 조회2
	public HashMap<String, String> getLastInfo2(GetNSListsRequestVO paramVO) throws Exception;
	
	// 서브 카테고리 유무 조회
	public int getExistSubCat(GetNSListsRequestVO paramVO) throws Exception;
	
	// 방영일 정보 조회
	public String getOnairDate(GetNSListsRequestVO paramVO) throws Exception;
	
	// 마지막 앨범 정보 조회
	public HashMap<String, String> getLastInfo(GetNSListsRequestVO paramVO) throws Exception;
	
	// 시리즈 정보 조회
	public String getCatSerInfo(GetNSListsRequestVO paramVO) throws Exception;
	
	// 이미지파일명 조회
	public String getImageFileName(GetNSListsRequestVO paramVO) throws Exception;
	
	// 컨텐츠 정보 조회
	public HashMap<String, String> getContentInfo(GetNSListsRequestVO paramVO) throws Exception;
	
	// VOD 서버 URL 리스트 조회
	public UrlListVO getVodUrlList(GetNSListsRequestVO paramVO) throws Exception;
	
	// 구매가능 목록 조회
	public List<GetNSListsResponseVO> getPurchableList(GetNSListsRequestVO paramVO) throws Exception;
	
	// 구매가능 목록 조회 (카테고리 ID가 VC로 시작하는 경우)
	public List<GetNSListsResponseVO> getVcPurchableList(GetNSListsRequestVO paramVO) throws Exception;
	
	// 구매가능 목록 조회 (카테고리 ID가 CA로 시작하는 경우)
	public List<GetNSListsResponseVO> getCaPurchableList(GetNSListsRequestVO paramVO) throws Exception; 
	
	// 카테고리 상세정보 조회
	public HashMap<String, String> getCateInfo(GetNSListsRequestVO paramVO) throws Exception; 
	
	// 검수 STB여부 조회
	public String getTestSbc(GetNSListsRequestVO paramVO) throws Exception; 
	
}
