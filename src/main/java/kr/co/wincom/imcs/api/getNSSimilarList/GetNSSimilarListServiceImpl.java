package kr.co.wincom.imcs.api.getNSSimilarList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSSimilarListServiceImpl implements GetNSSimilarListService {
	private Log imcsLogger = LogFactory.getLog("API_getNSSimilarList");
	
	@Autowired
	private GetNSSimilarListDao getNSSimilarListDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSSimilarList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSSimilarListResultVO getNSSimilarList(GetNSSimilarListRequestVO paramVO){
//		this.getNSSimilarList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodName = stackTraceElement.getMethodName();
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		imcsLog.debugLog(methodName+" service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;		

		List<GetNSSimilarListResponseVO> resultVO	= new ArrayList<GetNSSimilarListResponseVO>();
		GetNSSimilarListResponseVO tempVO		= new GetNSSimilarListResponseVO();
		GetNSSimilarListResultVO resultListVO	= new GetNSSimilarListResultVO();
		
		long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		
		String msg	= "";
	    
		try{
			String szImgSvrIp	= "";		// 이미지 서버 IP
			String szImgSvrUrl	= "";		// 이미지 서버 URL
			
			try {
				szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID985.split("/")[1]);			// 이미지서버 IP 조회
				szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID985.split("/")[1]);		// 이미지서버 URL 조회
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("서버IP값 조회", String.valueOf(tp1 - tp_start), methodName, methodLine);
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID985, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}
			
			

		    // 검수 STB여부 및 VIEW FLAG 조회
			try {
				this.testSbc(paramVO);
			} catch (Exception e) {
				paramVO.setTestSbc("N");
				paramVO.setViewFlag2("V");
			}
		    		    
		    tp2	= System.currentTimeMillis();
			imcsLog.timeLog("view_flag 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		    
		    
		    tp1	= System.currentTimeMillis();		    	
		    
		    // 비슷한 영화 조회
		    GetNSSimilarMovieVO movieVO =  this.getNSSimilarMovie(paramVO);
		    
		    tp2	= System.currentTimeMillis();
			imcsLog.timeLog("비슷한영화 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			if(movieVO == null) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID985, "", null, "cont_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
			}
			
	    	String[] lst_album = new String[10];
	    	
	    	
	    	List<ComWatchaVO> WatchaList = new ArrayList<ComWatchaVO>();
	    	
	    	if(movieVO != null){
	    		lst_album[0] = movieVO.getSimilarMovie01();
		    	lst_album[1] = movieVO.getSimilarMovie02();
		    	lst_album[2] = movieVO.getSimilarMovie03();
		    	lst_album[3] = movieVO.getSimilarMovie04();
		    	lst_album[4] = movieVO.getSimilarMovie05();
		    	lst_album[5] = movieVO.getSimilarMovie06();
		    	lst_album[6] = movieVO.getSimilarMovie07();
		    	lst_album[7] = movieVO.getSimilarMovie08();
		    	lst_album[8] = movieVO.getSimilarMovie09();
		    	lst_album[9] = movieVO.getSimilarMovie10();
		    	
		    	ComWatchaVO watchaInfoVO = new ComWatchaVO();
		    	
		    	for(int i = 0; i < 10; i++){
    		  		
		    		String szSimilarAlbumId = StringUtil.nullToSpace(lst_album[i]);
		    		
		    		if(i == 9){
		    			resultListVO.setContentsId(szSimilarAlbumId);
		    		}		    		
		    		
		    		// 왓챠 정보 조회
		    		if(szSimilarAlbumId != null && !"".equals(szSimilarAlbumId)){
			    		paramVO.setSimilarAlbumId(szSimilarAlbumId);
			    		tempVO = this.getSimilarInfo(paramVO);
			    		
			    		if(tempVO != null) {
			    			tempVO.setImgUrl(szImgSvrIp);
				    		tempVO.setImgSvcUrl(szImgSvrUrl);
				    		resultListVO.setContentsId(szSimilarAlbumId);
				    		
			    			watchaInfoVO = this.getWatchaInfo(paramVO);
			    			
			    			if(watchaInfoVO == null){
			    				watchaInfoVO = new ComWatchaVO();
			    			}
			    			
			    			resultVO.add(tempVO);
				    		WatchaList.add(watchaInfoVO);
			    		}
		    		}
		    	}
	    	}
	    	
	    	resultListVO.setList(resultVO);
	    	resultListVO.setWatchaList(WatchaList);
		}catch(ImcsException ie) {
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID985) + "] result[" + String.format("%-5s", resultListVO.toString()) + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID985) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}
		
		return resultListVO;
	}

	
    
    
	/**
     * 비슷한영화의 상세 정보 조회
     * @param	GetNSSimilarListRequestVO
     * @return	GetNSSimilarMovieVO
     */
    public GetNSSimilarListResponseVO getSimilarInfo(GetNSSimilarListRequestVO paramVO) throws Exception{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		CateInfoVO categoryVO = new CateInfoVO();
		
		
		String szProductType	= "";
		String szPrice			= "";
		String szCatName		= "";
		String szSeriesYn		= "";
		
		long tp_start = System.currentTimeMillis();

		
		// 상품타입 조회
		szProductType = this.getProductType(paramVO);
		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		
		if("0".equals(szProductType))		szPrice = "N";
		else								szPrice = "Y";

		
		// 카테고리명 조회
		categoryVO = this.getCategory(paramVO);
		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		if(categoryVO != null){
			szCatName	= StringUtil.nullToSpace(categoryVO.getCategoryName());
			szSeriesYn	= StringUtil.nullToSpace(categoryVO.getSeriesYn());
		}
		

		// 컨텐츠 정보 조회 (비슷한 영화의)
		List<GetNSSimilarListResponseVO> lSimilarList = new ArrayList<GetNSSimilarListResponseVO>();
		GetNSSimilarListResponseVO similarListVO = null;
		
		lSimilarList = this.getSimilar(paramVO);
		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		if(lSimilarList != null && lSimilarList.size() > 0){
			similarListVO =	lSimilarList.get(0); 
					
			similarListVO.setContsId(paramVO.getSimilarAlbumId());
			similarListVO.setPrice(szPrice);
			similarListVO.setCatName(szCatName);
			similarListVO.setSeriesYn(szSeriesYn);
			
			if("".equals(similarListVO.getSeriesDesc()) ){
				similarListVO.setSeriesYn("N");
			}else{
				similarListVO.setSeriesYn("Y");
			}
		} else {
			return similarListVO;
		}
		
		long tp_end = System.currentTimeMillis();
		imcsLog.timeLog("컨텐츠 상세 조회", String.valueOf(tp_end - tp_start), methodName, methodLine);
	
		return similarListVO;
	}
    
    
	
	
	/**
	 * 검수 STB여부 및 VIEW FLAG 조회
	 * @param	GetNSSimilarListRequestVO
	 * @result	void
	 */
    public void testSbc(GetNSSimilarListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
		String sqlId =  "lgvod985_001_20171214_001";
				
		int querySize = 0;
		
		List<GetNSSimilarListRequestVO> list   = new ArrayList<GetNSSimilarListRequestVO>();
		GetNSSimilarListRequestVO resultVO = null;
		
		try {
			
			try{
				resultVO = getNSSimilarListDao.testSbc(paramVO);
				list.add(resultVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()) {
				querySize = 0;
				resultVO = new GetNSSimilarListRequestVO();
				
				paramVO.setTestSbc("N");
				paramVO.setViewFlag2("V");
			} else {
				querySize = list.size();
				resultVO = (GetNSSimilarListRequestVO)list.get(0);
				
				paramVO.setTestSbc(resultVO.getTestSbc());
				paramVO.setViewFlag2(resultVO.getViewFlag2());
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID985, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} 
    }
    
    
    

    /**
     * 비슷한영화 조회
     * @param	GetNSSimilarListRequestVO
     * @return	GetNSSimilarMovieVO
     */
    public GetNSSimilarMovieVO getNSSimilarMovie(GetNSSimilarListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod985_002_20171214_001";
		int querySize = 0;
		
		List<GetNSSimilarMovieVO> list   = new ArrayList<GetNSSimilarMovieVO>();
		GetNSSimilarMovieVO resultVO = null;
		
		try {
			try{
				list  = getNSSimilarListDao.getNSSimilarMovie(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				resultVO = null;
				
				//imcsLog.failLog(ImcsConstants.API_PRO_ID985, sqlId, cache, "conts_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				
			} else {
				querySize = list.size();
				resultVO = (GetNSSimilarMovieVO)list.get(0);
			}

			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID985, sqlId, cache, querySize, methodName, methodLine);
			} catch(Exception e){}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID985, sqlId, cache, "conts_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return resultVO;
    }
    
    
    
    /**
     * 왓챠정보 조회
     * @param paramVO
     * @return
     */
    public ComWatchaVO getWatchaInfo(GetNSSimilarListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod985_s04_20171214_001";
		
		List<ComWatchaVO> list   = new ArrayList<ComWatchaVO>();
		ComWatchaVO resultVO = null;
				
		try {
			try{
				list  = getNSSimilarListDao.getWatchaInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				resultVO = new ComWatchaVO();
			} else {
				resultVO = (ComWatchaVO)list.get(0);
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID985, sqlId, cache, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
		
		
    	return resultVO;
    }
    
    
    /**
     * 상품타입 조회
     * @param	GetNSSimilarListRequestVO
     * @return	GetNSSimilarListResponseVO
     */
    public String getProductType(GetNSSimilarListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod985_s01_20171214_001";
    	String szProductType	= "";
    	
		int querySize = 0;
		
		List<String> list   = new ArrayList<String>();
		
		try {
			try{
				list  = getNSSimilarListDao.getProductType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if( list == null || list.isEmpty()){
				querySize		= 0;
			} else {
				querySize		= list.size();
				szProductType	= StringUtil.nullToSpace(list.get(0));
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID985, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID985, sqlId, cache, "conts_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return szProductType;
    }
    
    
    /**
     * 카테고리명 조회
     * @param	GetNSSimilarListRequestVO
     * @return	GetNSSimilarListCategoryVO
     */
    public CateInfoVO getCategory(GetNSSimilarListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod985_s02_20171214_001";
		
		List<CateInfoVO> list   = new ArrayList<CateInfoVO>();
		CateInfoVO resultVO = null;
		
		try {
			try{
				list  = getNSSimilarListDao.getCategory(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				resultVO = new CateInfoVO();
			} else {
				resultVO = (CateInfoVO)list.get(0);
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID985, sqlId, cache, "genre_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return resultVO;
    }
    
    
    
    
    /**
     * 비슷한 영화 컨텐츠 상세 정보 조회
     * @param	GetNSSimilarListRequestVO
     * @return	GetNSSimilarListResponseVO
     */
    public List<GetNSSimilarListResponseVO> getSimilar(GetNSSimilarListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod985_s03_20171214_001";
		
		List<GetNSSimilarListResponseVO> list   = new ArrayList<GetNSSimilarListResponseVO>();
		
		try {
			try{
				list  = getNSSimilarListDao.getSimilar(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID985, sqlId, cache, "conts_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return list;
    }
    

    
}
