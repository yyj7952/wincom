package kr.co.wincom.imcs.api.getNSSeriesList;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.getNSLists.GetNSListsRequestVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComSeriesInfoVO;

@Repository
public interface GetNSSeriesListDao {
	
	// 검수 STB여부 조회
	public List<String> getTestSbc(GetNSSeriesListRequestVO requestVO);

	// 부가세 요율 정보 가져오기
	public String getVatRate();
	
	// 카테고리 상세정보 조회
	public HashMap<String, String> getCateInfo1(GetNSSeriesListRequestVO paramVO);
	public HashMap<String, String> getCateInfo2(GetNSSeriesListRequestVO paramVO);
	
	// 마지막 시청회차 가져오기
	public HashMap<String, String> getLastWatchNo(GetNSSeriesListRequestVO paramVO);
	
	// 마지막 시청회차 가져오기 (프로야구 AWS)
	public HashMap<String, String> getLastWatchNoAws(GetNSSeriesListRequestVO paramVO);
	
	// 컨텐츠 편성 정보 가져오기
	public List<GetNSSeriesListResponseVO> getContSchedList1(GetNSSeriesListRequestVO paramVO);
	public List<GetNSSeriesListResponseVO> getContSchedList2(GetNSSeriesListRequestVO paramVO);
	// 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 PPS의 경우 카테고리 정보를 조회하지 않는다. (PT_LA_GROUP_RELATION으로 정보 제공 필요)
	public List<GetNSSeriesListResponseVO> getContSchedList3(GetNSSeriesListRequestVO paramVO);
	// 마지막 시청회차 가져오기 (프로야구 AWS)
	public List<GetNSSeriesListResponseVO> getContSchedListAWS(GetNSSeriesListRequestVO paramVO);
	
	//스틸이미지 가져오기
	public List<String> getStillImage(GetNSSeriesListResponseVO item);
	
	//다운로드 여부가져오기
	public String getDownFlag(GetNSSeriesListResponseVO item);
	
	public List<GetNSSeriesListResponseVO> getNSSeriesList(GetNSSeriesListRequestVO requestVO);
	
	public List<ComSeriesInfoVO> getSeriesInfo(GetNSSeriesListRequestVO requestVO);

	// 데이터프리 정보 조회
	public List<ComDataFreeVO> getDatafreeInfo(ComDataFreeVO requestVO);
}
