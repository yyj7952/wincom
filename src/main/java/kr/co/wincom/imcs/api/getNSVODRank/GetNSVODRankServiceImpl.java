package kr.co.wincom.imcs.api.getNSVODRank;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
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
public class GetNSVODRankServiceImpl implements GetNSVODRankService {
	private Log imcsLogger = LogFactory.getLog("API_getNSVODRank");
	
	@Autowired
	private GetNSVODRankDao getNSPresentDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSVODRank(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSVODRankResultVO getNSVODRank(GetNSVODRankRequestVO paramVO){
//		this.getNSVODRank(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSVODRankResultVO resultListVO = new GetNSVODRankResultVO();
		GetNSVODRankResponseVO tempVO = new GetNSVODRankResponseVO();
		List<GetNSVODRankResponseVO> resultVO = new ArrayList<GetNSVODRankResponseVO>();
		List<GetNSVODRankResponseVO> returnVO = new ArrayList<GetNSVODRankResponseVO>();
		
		String LOCKFILE = "";
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID993.split("/")[1], imcsLog);
		
		int nMainCnt	= 0;
		int nSubCnt		= 0;
		int nWaitCnt	= 0;
		int nPageNo		= 0;
		int nPageCnt	= 0;
		int nStartNo	= 0;
		int nEndNo		= 0;
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		
		try{
			/*if(paramVO.getPageNo().equals("0") && paramVO.getPageCnt().equals("0")){
				paramVO.setPageNo("A");
				paramVO.setPageCnt("A");
				
			}else{			
				if(paramVO.getPageNo().equals("") || paramVO.getPageNo()== null){
					paramVO.setPageNo("1");
				}				
			
				if(paramVO.getPageCnt().equals("") || paramVO.getPageCnt()== null){
					paramVO.setPageCnt("1");
				}
			}*/
			
			if(paramVO.getPageNo().equals("0")){ 
				paramVO.setPageNo("A");
			}
			
			if(paramVO.getPageCnt().equals("0")){
				paramVO.setPageCnt("A");
			}
				
			
			if(paramVO.getPageNo().equals("") || paramVO.getPageNo()== null){
				paramVO.setPageNo("1");
			}				
		
			if(paramVO.getPageCnt().equals("") || paramVO.getPageCnt()== null){
				paramVO.setPageCnt("1");
			}
			
			
			
			// 페이징 관련 파라미터 정리
			
			
			if (!"A".equals(paramVO.getPageNo()) && !"A".equals(paramVO.getPageCnt())) {
				
				nPageNo		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
				nPageCnt	= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
				
				nStartNo = (nPageNo * nPageCnt) - (nPageCnt - 1) - 1;

				nEndNo   = (nPageNo * nPageCnt);			
			}
						
			// 검수 STB 여부 조회
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			this.getTestSbc(paramVO);
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("가입자정보 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);
			
			String szImgSvrIp	= "";		// 이미지 서버 IP
			String szImgSvrUrl	= "";		// 이미지 서버 URL
			
			try {
				szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID993.split("/")[1]);			// 이미지서버 IP 조회
				szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID993.split("/")[1]);		// 이미지서버 URL 조회
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID993, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");

				throw new ImcsException();
			}
			
			paramVO.setImgUrl(szImgSvrUrl);
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("서버 IP 조회", String.valueOf((tp1 - tp2)), methodName, methodLine);
			
			LOCKFILE	= LOCALPATH + "/getNSVODRank_d"+paramVO.getDefinFlag()+"_y"+paramVO.getYouthYn()+"_q"+paramVO.getQuickDisYn()
					+"_v"+paramVO.getViewFlag2()+"_o"+paramVO.getGenreOne()+"_t"+paramVO.getGenreTwo()+".lock";
			
			RESFILE		= LOCALPATH + "/getNSVODRank_d"+paramVO.getDefinFlag()+"_y"+paramVO.getYouthYn()+"_q"+paramVO.getQuickDisYn()
					+"_v"+paramVO.getViewFlag2()+"_o"+paramVO.getGenreOne()+"_t"+paramVO.getGenreTwo()+".res";
			
			File res = new File(RESFILE);
						

			// 파일이 존재하면 읽고 아니면 lock 파일 로직 실행
			if(res.exists()) {
				String result = "";
 				
				String read_result = FileUtil.fileRead(RESFILE, "UTF-8");
				
				if (!"A".equals(paramVO.getPageNo()) && !"A".equals(paramVO.getPageCnt())) {
					
					String[] arrResult	= read_result.split(ImcsConstants.ROWSEP+ImcsConstants.ROWSEP);
					result	= "";
					
					for(int i = 0; i < arrResult.length; i++) {
						if(i >= nStartNo && i < nEndNo) {
							if (i == nEndNo-1) { //마지막 라인인 경우
								result	= result + arrResult[i] + ImcsConstants.ROWSEP;
							} else {
								result	= result + arrResult[i] + ImcsConstants.ROWSEP + ImcsConstants.ROWSEP;
							}
						}
					}
				}else{
					String[] arrResult	= read_result.split(ImcsConstants.ROWSEP + ImcsConstants.ROWSEP);
					result	= "";
					
					for(int i = 0; i < arrResult.length; i++) {
						if (i == arrResult.length-1) { //마지막 라인인 경우
							result	= result + arrResult[i] + ImcsConstants.ROWSEP;
						} else {
							result	= result + arrResult[i] + ImcsConstants.ROWSEP + ImcsConstants.ROWSEP;
						}
					}
				}
				
				if(!"".equals(StringUtil.nullToSpace(read_result))) {
					msg = " File [" + RESFILE + "] Read cache rcvbuf... [" + res.length() + "] bytes ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultListVO.setResult(result);
					//FileUtil.unlock(LOCKFILE, imcsLog);
					return resultListVO;
				}
				
				
			} else {
				msg = " File [" + RESFILE + "] open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				while(FileUtil.lock(LOCKFILE, imcsLog)){
					Thread.sleep(1000);
					nWaitCnt++;
					
					msg = " queryWaitCheck Sleep [" + nWaitCnt + "] sec";
					imcsLog.serviceLog(msg, methodName, methodLine);
	
					if(nWaitCnt >= 5){
						msg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID993 + "] sts[    0] msg["+ 
								"Creating Cache File...]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						paramVO.setResultCode("21000000");
						
						throw new ImcsException();
					}
					
					if(res.exists()) {
						String result = FileUtil.fileRead(RESFILE, "UTF-8");
						
						if (!"A".equals(paramVO.getPageNo()) && !"A".equals(paramVO.getPageCnt())) {
							String[] arrResult	= result.split(ImcsConstants.ROWSEP+ImcsConstants.ROWSEP);
							result	= "";
							
							for(int i = 0; i < arrResult.length; i++) {
								if(i >= nStartNo && i < nEndNo) {
									if (i == nEndNo-1) { //마지막 라인인 경우
										result	= result + arrResult[i] + ImcsConstants.ROWSEP;
									} else {
										result	= result + arrResult[i] + ImcsConstants.ROWSEP + ImcsConstants.ROWSEP;
									}
								}
							}
						}else{
							String[] arrResult	= result.split(ImcsConstants.ROWSEP+ImcsConstants.ROWSEP);
							result	= "";
							
							for(int i = 0; i < arrResult.length; i++) {
								if (i == arrResult.length-1) { //마지막 라인인 경우
									result	= result + arrResult[i] + ImcsConstants.ROWSEP;
								} else {
									result	= result + arrResult[i] + ImcsConstants.ROWSEP + ImcsConstants.ROWSEP;
								}
							}
						}
						
						if(!"".equals(StringUtil.nullToSpace(result))) {
							msg = " File [" + RESFILE + "] rcvbuf [" + res.length() + "] bytes ";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultListVO.setResult(result);
							FileUtil.unlock(LOCKFILE, imcsLog);
							return resultListVO;
						}
					}
				}
				
				// 추천 VOD 목록 조회
				resultVO = this.getNSVODRankList(paramVO);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("추천VOD 목록 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);
				
				if(resultVO != null){
					nMainCnt = resultVO.size();
				}
				
				for(int i = 0; i < nMainCnt; i++){
					tempVO = resultVO.get(i);
					
					if(nSubCnt > 19)	break;
					
					if("Y".equals(tempVO.getSeriesYn())){
						// 시리즈 카테고리 명 조회
						paramVO.setContsId(tempVO.getContsId());
						String szSerCatId = this.getCategoryId(paramVO);
						tempVO.setSerCatId(szSerCatId);
					}
					
					// 위에서 nSubCnt > 19 조건으로 break했는데.. 해당 로직이 의미가 있는지 모르겠음
					if (( "Y".equals(tempVO.getSeriesYn()) || "Y".equals(tempVO.getRelationYn()) ) && nSubCnt > 19 ) {
						continue;
					} else if("Y".equals(tempVO.getRecommendYn()) && nSubCnt > 19 ) {
						break;
					} else {
						nSubCnt++;
						
						// 서버IP
						if(!"".equals(tempVO.getWideImageUrl()))	tempVO.setImgUrl(tempVO.getWideImageUrl());
						else										tempVO.setImgUrl(szImgSvrIp);
						
						// 5.1ch여부
						if("DOLBY 5.1".equals(tempVO.getAudioType())){
							tempVO.setIs51ch("Y");
						}
						
						if("Y".equals(tempVO.getHdcontent()) || "S".equals(tempVO.getHdcontent())){
							tempVO.setIsHd("Y");
						}else if("N".equals(tempVO.getHdcontent())){
							tempVO.setIsHd("N");
						}
						
						tempVO.setWideImageUrl(szImgSvrUrl);
						
						/*if (!"A".equals(paramVO.getPageNo()) && !"A".equals(paramVO.getPageCnt())) {
							if(i >= nStartNo && i < nEndNo)
								returnVO.add(tempVO);
						} else*/
							returnVO.add(tempVO);
					}
				}
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("추천VOD 목록 FETCH", String.valueOf((tp1 - tp2)), methodName, methodLine);

				resultListVO.setList(returnVO);
			    
			    // 파일 쓰기
	 			int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
	 			
	 			if(nRetVal == 1) {
	 				msg = "File [" + RESFILE + "] WRITE [" + res.length() + "] bytes Finished";
	 				imcsLog.serviceLog(msg, methodName, methodLine);
	 			} else {
	 				msg = "File [" + RESFILE + "] WRITE failed";
	 				imcsLog.serviceLog(msg, methodName, methodLine);		
	 			}
	 			
	 			FileUtil.unlock(LOCKFILE, imcsLog);
	 			
	 			res = new File(RESFILE);
	 			
	 			if(res.exists()) {
	 				String result = "";
	 				
					String read_result = FileUtil.fileRead(RESFILE, "UTF-8");
					
					if (!"A".equals(paramVO.getPageNo()) && !"A".equals(paramVO.getPageCnt())) {
						result	= "";
						String[] arrResult	= read_result.split(ImcsConstants.ROWSEP+ImcsConstants.ROWSEP);
						
						for(int i = 0; i < arrResult.length; i++) {
							if(i >= nStartNo && i < nEndNo) {
								if (i == nEndNo-1) { //마지막 라인인 경우
									result	= result + arrResult[i] + ImcsConstants.ROWSEP;
								} else {
									result	= result + arrResult[i] + ImcsConstants.ROWSEP + ImcsConstants.ROWSEP;
								}
							}
								
						}
					}else{
						result	= "";
						String[] arrResult	= read_result.split(ImcsConstants.ROWSEP+ImcsConstants.ROWSEP);
						
						for(int i = 0; i < arrResult.length; i++) {
							if (i == arrResult.length-1) { //마지막 라인인 경우
								result	= result + arrResult[i] + ImcsConstants.ROWSEP;
							} else {
								result	= result + arrResult[i] + ImcsConstants.ROWSEP + ImcsConstants.ROWSEP;
							}
						}
					}
					
					if(!"".equals(StringUtil.nullToSpace(read_result))) {
						msg = " File [" + RESFILE + "] Read cache rcvbuf... [" + res.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultListVO.setResult(result);
						//FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
					}
					
					
				} else {
					msg = " File [" + RESFILE + "] open Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}
 			
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			// NosqlCacheType.HBASE_WR.ordinal(), NosqlCacheType.USERDB.ordinal() 이거는?
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID993) + "] sts[end  ] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID993) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}
		
		return resultListVO;
	}

	
	
	
	
	
	
	
	/**
	 * 검수 STB 여부 조회
	 * @param vo
	 * @throws Exception
	 */
	public void getTestSbc(GetNSVODRankRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod993_001_20171214_001";
		
		List<String> list   = new ArrayList<String>();
		int querySize = 0;
		
		try {
			try{
				list = getNSPresentDao.getTestSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			if( list == null || list.isEmpty()){
				paramVO.setViewFlag2("V");
			} else {
				paramVO.setViewFlag2(StringUtil.nullToSpace(list.get(0)));
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
				//imcsLog.dbLog2(ImcsConstants.API_PRO_ID993, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			//throw new ImcsException();
		}
    }
	
	
	/**
	 * 추천VOD목록(Best VOD) 조회 
	 * @param	paramVO
	 * @return	List<GetNSVODRankResponseVO>
	 */
	public List<GetNSVODRankResponseVO> getNSVODRankList(GetNSVODRankRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId = "";
    	if( "A".equals(paramVO.getGenreOne()) || "A".equals(paramVO.getGenreTwo())) {
    		sqlId =  "lgvod993_011_20171214_001";
    	} else {
    		sqlId =  "lgvod993_012_20171214_001";
    	}
		
		int querySize = 0;
		
		List<GetNSVODRankResponseVO> list   = new ArrayList<GetNSVODRankResponseVO>();
		
		try {
			try{
				list = getNSPresentDao.getNSVODRankList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list == null || list.isEmpty()){
				//imcsLog.failLog(ImcsConstants.API_PRO_ID993, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
			} else {
				querySize = list.size();
			}
			
			try{
				//imcsLog.dbLog2(ImcsConstants.API_PRO_ID993, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID993, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return list;
    }
	
	
	
	/**
	 * 시리즈 카테고리명 조회
	 * @param	GetNSVODRankRequestVO
	 * @return	String
	 */
	public String getCategoryId(GetNSVODRankRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		

		String sqlId = "lgvod993_013_20171214_001";
		String szCatId = "";
		
		List<String> list   = new ArrayList<String>();
		int querySize = 0;
		
		try {
			try{
				list = getNSPresentDao.getCategoryId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if( list != null && !list.isEmpty()){
				szCatId = StringUtil.nullToSpace(list.get(0));
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
				//imcsLog.dbLog2(ImcsConstants.API_PRO_ID993, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return szCatId;
    }
	
	
}
