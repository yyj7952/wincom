package kr.co.wincom.imcs.api.getNSLists;

import java.util.HashMap;
import java.util.WeakHashMap;


public interface GetNSListsService {
	// GetNSLists API
	public GetNSListsResultVO getNSLists(GetNSListsRequestVO requestVO) throws Exception;
	
	// GetNSList 내 파일 읽기 공통 모듈 (너무 많아서 만듬)
	public GetNSListsResultVO getFileList(String RESFILE, int nStartNo, int nEndNo, String img_server, String img_resize_server, String img_cat_server, GetNSListsRequestVO requestVO) throws Exception;
	
	// 파일명 생성 (파일명이 너무 길어 별도 메소드 생성
	public String getFileName(GetNSListsRequestVO paramVO, String szVerInfo, String szI20VerInfo) throws Exception;
	
	// VC인 경우의 VERSION LIST를 조회한다
	public HashMap<String, String> getVerInfo(GetNSListsRequestVO paramVO) throws Exception;
	
	// VERSION LIST를 조회한다
	public HashMap<String, String> getI20VerInfo(GetNSListsRequestVO paramVO) throws Exception;
	
	// 부모 카테고리 ID 조회
	public String getParentCatId(GetNSListsRequestVO paramVO) throws Exception;
	
	// 카테고리 상세정보 조회
	public void getCateInfo(GetNSListsRequestVO paramVO) throws Exception;
	
	// 검수 STB여부 조회
	public String getTestSbc(GetNSListsRequestVO paramVO) throws Exception;
	
}
