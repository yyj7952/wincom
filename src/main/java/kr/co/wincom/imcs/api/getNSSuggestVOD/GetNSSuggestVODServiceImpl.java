package kr.co.wincom.imcs.api.getNSSuggestVOD;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSSuggestVODServiceImpl implements GetNSSuggestVODService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSSuggestVOD");
	
	@Autowired
	private GetNSSuggestVODDao getNSSuggestVODDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSSuggestVOD(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSSuggestVODResultVO getNSSuggestVOD(GetNSSuggestVODRequestVO paramVO) {
//		this.getNSSuggestVOD(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();	
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		GetNSSuggestVODResultVO resultListVO	= new GetNSSuggestVODResultVO();
		GetNSSuggestVODResponseVO tempVO		= new GetNSSuggestVODResponseVO();
		List<GetNSSuggestVODResponseVO> resultVO = new ArrayList<GetNSSuggestVODResponseVO>();
		List<GetNSSuggestVODResponseVO> returnVO = new ArrayList<GetNSSuggestVODResponseVO>();
		
		String msg	= "";
		
		int nMainCnt	= 0;
		int nResultCnt	= 0;

	    long tp_start = paramVO.getTp_start();
		long tp1, tp2, tp3 = 0;
	    
		try{
			// 테스트 가입자 여부 조회
			GetNSSuggestVODRequestVO testsbc = this.getTestSbc(paramVO);
			paramVO.setViewFlag2(testsbc.getViewFlag2());
			paramVO.setTestSbc(testsbc.getTestSbc());
			
			String szImgSvrIp	= "";		// 이미지 서버 IP
			String szImgSvrUrl	= "";		// 이미지 서버 URL
		 
		    try {
		    	szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID899.split("/")[1]);			// 이미지서버 IP 조회
		    	szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID899.split("/")[1]);		// 이미지서버 URL 조회		    	
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID899, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");

				throw new ImcsException();
			}
		    
		   /* try {		// 사용안함
		    * 
		    	String tImgSvrip1 = "";
		    	String tImgSvrip2 = "";
		    	String tImgSvrip3 = "";
		    
		    	ImageServerVO imgVO = commonService.getImgNodeIp(paramVO.getSaId(), paramVO.getStbMac());
		    	
		    	if(imgVO != null){
		    		tImgSvrip1 = imgVO.gettImgsvrip1();
		    		tImgSvrip2 = imgVO.gettImgsvrip2();
		    		tImgSvrip3 = imgVO.gettImgsvrip3();
		    	}
			} catch (Exception e) {
				msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID899) + "] sts[" + ImcsConstants.LOG_MSG2 + "] msg[" + String.format("%-21s", "node_ip:" + ImcsConstants.RCV_MSG1 + "]");
				imcsLog.serviceLog(msg, methodName, methodLine);
				paramVO.setResultCode("31000000");
				throw new ImcsException();
			}*/
		    
		    //사용안함
		    //paramVO.setImgUrl(szImgSvrUrl);
		    
		    tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
		    
			
			// 랭킹 조회
		    if("".equals(paramVO.getAlbumId()))		this.getRanking1(paramVO);		// 랭킹 조회(앨범 ID X)
		    else									this.getRanking2(paramVO);    	// 랭킹 조회(앨범 ID O)
			
			
			// 추천 VOD 조회
			resultVO = this.getNSSuggestVODList(paramVO);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			
			
			if(resultVO != null){
				nMainCnt = resultVO.size();
			}
			
			for(int i = 0; i < nMainCnt; i++){
				tempVO = resultVO.get(i);
				
				if(i == nMainCnt-1 || nResultCnt == 9) {
					resultListVO.setContentsId(tempVO.getContsId());
					resultListVO.setContentsName(tempVO.getContsName());
					resultListVO.setCatId(tempVO.getCatId());
					resultListVO.setCatName(tempVO.getCatName());
				}
				
				List<GetNSSuggestVODResponseVO> resultDtlVO = new ArrayList<GetNSSuggestVODResponseVO>();
				GetNSSuggestVODResponseVO tempDtlVO = new GetNSSuggestVODResponseVO();
				
				int iSubCount = 0;
				
				paramVO.setCatId(tempVO.getCatId());
				paramVO.setAlbumId(tempVO.getContsId());
				
				// 추천 VOD 상세조회
				resultDtlVO = this.getNSSuggestVODDtl(paramVO);
				
				
				if(resultDtlVO != null){
					iSubCount = resultDtlVO.size();
				}
				
				if ( iSubCount == 0 ) continue;
				
				if( iSubCount > 0 ){
					tempDtlVO = resultDtlVO.get(0);
					
					tempVO.setContsName(tempDtlVO.getContsName());
					tempVO.setOnairDate(tempDtlVO.getOnairDate());
					tempVO.setSeriesDesc(tempDtlVO.getSeriesDesc());
					tempVO.setRealHDYn(tempDtlVO.getRealHDYn());
					tempVO.setChaNum(tempDtlVO.getChaNum());
					tempVO.setIsNew(tempDtlVO.getIsNew());
					tempVO.setSortNo(tempDtlVO.getSortNo());
					tempVO.setProductType(tempDtlVO.getProductType());
					tempVO.setImgFileName(tempDtlVO.getImgFileName());
					tempVO.setSuggestedPriceYn(tempDtlVO.getSuggestedPriceYn());
					tempVO.setPrInfo(tempDtlVO.getPrInfo());
					tempVO.setRunTime(tempDtlVO.getRunTime());
					tempVO.setTerrYn(tempDtlVO.getTerrYn());
					tempVO.setIs51ch(tempDtlVO.getIs51ch());
					tempVO.setHdContent(tempDtlVO.getHdContent());
					tempVO.setIs3d(tempDtlVO.getIs3d());
					tempVO.setIsCaption(tempDtlVO.getIsCaption());
					tempVO.setPoint(tempDtlVO.getPoint());					
					tempVO.setSerCatId(tempDtlVO.getSerCatId());
					tempVO.setSuggestedPrice(tempDtlVO.getSuggestedPrice());
					tempVO.setCatId(tempDtlVO.getCatId());
					
					if("".equals(tempVO.getSerCatId())){
						tempVO.setSeriesYn("N");
		                // 2018.07.13 시리즈 카테고리에 편성된 정보가 없을 시에는 단편 카테고리ID를 넘겨줘서, MIMS 구매 오류가 안나도록 한다.
		                // (MIMS에서는 카테고리ID가 없을 경우에 필수 파라미터 에러를 낸다.
						tempVO.setSerCatId(tempVO.getCatId());
					}else{
						tempVO.setSeriesYn("Y");
					}
				}
				
				/* 서버IP */
				tempVO.setImgUrl(szImgSvrIp);
				tempVO.setPosterImgUrl(szImgSvrUrl);
				
				//tempVO.setSubCnt(0);		// 사용안함
				
				// DOLBY 51, 여부 설정
				if("DOLBY 5.1".equals(tempVO.getIs51ch()))	tempVO.setIs51ch("Y");
				else										tempVO.setIs51ch("N");
								
				// 금액 설정				// 사용안함
				/*if("0".equals(tempVO.getProductType()))		tempVO.setPrice("N");
				else										tempVO.setPrice(tempVO.getSuggestedPriceYn());*/
				
				// HD여부 설정
				if("Y".equals(tempVO.getHdContent()) || "S".equals(tempVO.getHdContent())){
					tempVO.setIsHd("Y");
				}else if( "N".equals(tempVO.getHdContent()) ){
					tempVO.setIsHd("N");
				}
				
				// 유무료 설정
				if("Y".equals(tempVO.getSuggestedPriceYn())){
					tempVO.setAlbumBillFlag("Y");
				}
				
				returnVO.add(tempVO);
				
				nResultCnt ++;

				if ( nResultCnt >= 10 ) break;
				
			}
			
			tp3	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠정보 FETCH", String.valueOf(tp3 - tp2), methodName, methodLine); 
			
		    resultListVO.setList(returnVO);
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
			
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID899) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	
	
	/**
	 * 테스트 가입자 여부 조회
	 * @param paramVO
	 * @return
	 */
	public GetNSSuggestVODRequestVO getTestSbc(GetNSSuggestVODRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod899_010_20180713_001";
    	
		int querySize = 0;
		
		//List<String> list   = new ArrayList<String>();
		List<GetNSSuggestVODRequestVO> list   = new ArrayList<GetNSSuggestVODRequestVO>();
		GetNSSuggestVODRequestVO ret = new GetNSSuggestVODRequestVO(); 
		
		try {
			try{
				list = getNSSuggestVODDao.getTestSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				ret.setViewFlag2("V");
				ret.setTestSbc("N");
			} else {
				querySize = list.size();
				ret = list.get(0);
			}
			
			try{
				//imcsLog.dbLog2(ImcsConstants.API_PRO_ID899, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return ret;
    }
	
	
	
	
	
	/**
	 * 랭킹 조회(앨범 ID X)
	 * @param paramVO
	 * @return
	 */
	public void getRanking1(GetNSSuggestVODRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod899_001_20171214_001";

		List<GetNSSuggestVODRequestVO> list   = new ArrayList<GetNSSuggestVODRequestVO>();
		GetNSSuggestVODRequestVO resultVO = null;
		int querySize = 0;
		
		try {
			try{
				list = getNSSuggestVODDao.getRanking1(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = list.get(list.size() - 1);
				
				paramVO.setRankingStart(resultVO.getRankingStart());
				paramVO.setRankingEnd(resultVO.getRankingEnd());

				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
				//imcsLog.dbLog2(ImcsConstants.API_PRO_ID899, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
	
	
	
	
	
	/**
	 * 랭킹 조회(앨범 ID O)
	 * @param paramVO
	 * @return
	 */
	public void getRanking2(GetNSSuggestVODRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod899_002_20171214_001";

		List<GetNSSuggestVODRequestVO> list   = new ArrayList<GetNSSuggestVODRequestVO>();
		GetNSSuggestVODRequestVO resultVO = null;
		int querySize = 0;
		
		try {
			try{
				list = getNSSuggestVODDao.getRanking2(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = list.get(0);
				
				paramVO.setRankingStart(resultVO.getRankingStart());
				paramVO.setRankingEnd(resultVO.getRankingEnd());

				querySize = list.size();
			}else{
				this.getRanking3(paramVO);
			}
			
			//C에서 주석처리된 로그
			try{
				//imcsLog.dbLog2(ImcsConstants.API_PRO_ID899, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
	
	/**
	 * 랭킹 조회(VOD추천정보)
	 * @param paramVO
	 * @return
	 */
	public void getRanking3(GetNSSuggestVODRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod899_005_20171214_001";

		List<GetNSSuggestVODRequestVO> list   = new ArrayList<GetNSSuggestVODRequestVO>();
		GetNSSuggestVODRequestVO resultVO = null;
		int querySize = 0;
		
		String msg = "";
		
		try {
			try{
				list = getNSSuggestVODDao.getRanking3(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			if( list != null && !list.isEmpty()){
				resultVO = list.get(0);
				
				paramVO.setRankingStart(resultVO.getRankingStart());
				paramVO.setRankingEnd(resultVO.getRankingEnd());
				
				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID899) + "] NOT MATCH GENRE SMALL INFO [LARGE:" + resultVO.getGenreLarge() +", MID:" + resultVO.getGenreMid() +", SMALL:" + resultVO.getGenreSmall() +"]";
				imcsLog.serviceLog(msg, methodName, methodLine);

				querySize = list.size();
			}else{
				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID899) + "] NOT MATCH GENRE INFO [LARGE:" + paramVO.getGenreLarge() +", MID:" + paramVO.getGenreMid() +", SMALL:" + paramVO.getGenreSmall() +"]";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			//C에서 주석처리된 로그
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID899, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
	
	
	
	/**
	 * 추천 VOD 조회
	 * @param paramVO
	 * @return
	 */
	public List<GetNSSuggestVODResponseVO> getNSSuggestVODList(GetNSSuggestVODRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    			
		String sqlId = "lgvod899_003_20171214_001";
		int querySize = 0;
		
		List<GetNSSuggestVODResponseVO> list   = new ArrayList<GetNSSuggestVODResponseVO>();
		
		try {
			try{
				list = getNSSuggestVODDao.getNSSuggestVODList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				querySize = 0;
				//imcsLog.failLog(ImcsConstants.API_PRO_ID899, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				
				paramVO.setResultCode("21000000");
			} else {
				querySize = list.size();
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID899, sqlId, cache, querySize, methodName, methodLine);
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID899, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
	
	
	
	
	
	/**
	 * 추천 VOD 상세조회
	 * @param paramVO
	 * @return
	 */
	public List<GetNSSuggestVODResponseVO> getNSSuggestVODDtl(GetNSSuggestVODRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
    	String sqlId =  "lgvod899_004_20171214_001";
		
		List<GetNSSuggestVODResponseVO> list   = new ArrayList<GetNSSuggestVODResponseVO>();
		int querySize = 0;
		
		try {
			try{
				list = getNSSuggestVODDao.getNSSuggestVODDtl(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			//C에서 주석처리된 로그
			try{
				querySize = list.size();
				//imcsLog.dbLog2(ImcsConstants.API_PRO_ID899, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return list;
    }
}
