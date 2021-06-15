package kr.co.wincom.imcs.api.getNSLists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.UrlListVO;

import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("rawtypes")
public interface GetNSListsDao {
	// 검수 STB여부 조회
	public List<String> getTestSbc(GetNSListsRequestVO requestVO);

	// 카테고리 상세정보 조회
	
	public List<HashMap> getCateInfo(GetNSListsRequestVO requestVO);

	// 부모 카테고리ID 정보 조회
	public List<String> getParentCatId(GetNSListsRequestVO requestVO);

	// 부모카테고리 버정 정보 조회	// VC, CA 타입이 아닌경우 (PT_VO_I20_VERSION 테이블 조회)
	public List<HashMap> getI20VerInfo(GetNSListsRequestVO requestVO);

	// 부모카테고리 버정 정보 조회 	// VC, CA 타입인 경우 (PT_VO_VOD_VERSION 테이블 조회)
	public List<HashMap> getVerInfo();

		
	
	// 구매가능목록 리스트 조회 (MakeList)
	public List<GetNSListsResponseVO> getPurchableList(GetNSListsRequestVO requestVO);

	// 구매가능목록 리스트 조회 - CATID = VC일 경우 (MakeList)
	public List<GetNSListsResponseVO> getVcPurchableList(GetNSListsRequestVO requestVO);
	
	// 구매가능목록 리스트 조회 - CATID = CA일 경우 (MakeList)
	public List<GetNSListsResponseVO> getCaPurchableList(GetNSListsRequestVO requestVO);
	
	// VOD URL 리스트 조회 (MakeList)
	public List<UrlListVO> getVodUrlList(GetNSListsRequestVO requestVO);

	// 컨텐츠 정보 조회 (MakeList)
	public List<HashMap> getContentInfo(GetNSListsRequestVO requestVO);
	
	// 이미지 파일명 조회 (MakeList)
	public List<StillImageVO> getImageFileName(GetNSListsRequestVO requestVO);

	// 시리즈 정보 조회 (MakeList)
	public List<String> getSerInfo(GetNSListsRequestVO requestVO);

	// 마지막 앨범 정보 조회 (MakeList)
	public List<HashMap> getLastInfo(GetNSListsRequestVO requestVO);

	// 서브카테고리 유무 조회 (MakeList)
	public List<Integer> getExistSubCat(GetNSListsRequestVO requestVO);

	// 방영일 정보 조회 (MakeList)
	public List<String> getOnairDate(GetNSListsRequestVO requestVO);

	// 마지막 앨범 정보 조회2 (MakeList)
	public List<HashMap> getLastInfo2(GetNSListsRequestVO requestVO);

	// 상품 타입 정보 조회 (MakeList)
	public List<String> getProductType(GetNSListsRequestVO requestVO);
	
	// 앨범 포스터 조회
	public String getPosterImgFileName(GetNSListsResponseVO paramVO) throws Exception; 
	
}
