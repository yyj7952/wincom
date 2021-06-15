package kr.co.wincom.imcs.api.getNSContInfo;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.common.vo.ComConcertInfoVO;

@Repository
@SuppressWarnings("rawtypes")
public interface GetNSContInfoDao {
	
	// 부가세 요율 정보 가져오기
	public String getVatRate();
	
	// 검수 STB여부 조회
	public List<HashMap> getTestSbc(GetNSContInfoRequestVO requestVO);

	// 카테고리 유효성 체크 및 편성 여부 체크
	public List<HashMap> getChkCate(GetNSContInfoRequestVO requestVO);
	public List<HashMap> getChkCate2(GetNSContInfoRequestVO requestVO);
	
	//컨텐츠 정보 가져오기
	public List<AlbumInfo> getContInfoList(GetNSContInfoRequestVO requestVO);
	
	//큐시트 정보가져오기
	public List<HashMap> getCueSheetInfo1(AlbumInfo albumInfo);
	public List<HashMap> getCueSheetInfo2(AlbumInfo albumInfo);
	
	//장르 정보가져오기
	public List<HashMap> getGenreInfoList(AlbumInfo albumInfo);
	
	//다운로드 체크
	public List<String> getDownCheck(AlbumInfo albumInfo);

	//OST 정보 가져오기
	public List<HashMap> getOSTInfo(AlbumInfo albumInfo);
	
	//이미지 정보 가져오기
	public List<HashMap> getImgInfo(AlbumInfo albumInfo);

	public HashMap getAlbumTypeCheck(GetNSContInfoRequestVO requestVO);
	public String getOmniviewAlbumId1(GetNSContInfoRequestVO requestVO);
	public String getOmniviewAlbumId2(GetNSContInfoRequestVO requestVO);
	
	// CINE21 평균 별점, 리뷰 수
	public HashMap<String, String> getCinePointCnt(AlbumInfo albumInfo);
	
	//아이돌 라이브 유료콘서트
	public ComConcertInfoVO getConsertInfo(AlbumInfo albumInfo);
	
}
