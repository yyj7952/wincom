package kr.co.wincom.imcs.api.getNSMultiConts;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


@Service
public class GetNSMultiContsServiceImpl implements GetNSMultiContsService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSMultiConts");
	
	@Autowired
	private GetNSMultiContsDao getNSMultiContsDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSContList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod997.pc)
	 */
	@Override
	public GetNSMultiContsResultVO getNSMultiConts(GetNSMultiContsRequestVO paramVO)	{
//		this.getNSContList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSMultiContsResponseVO> resultVO	= new ArrayList<GetNSMultiContsResponseVO>();
		GetNSMultiContsResponseVO tempVO			= new GetNSMultiContsResponseVO();
		GetNSMultiContsResponseVO linkTimeVO		= new GetNSMultiContsResponseVO();
		GetNSMultiContsResponseVO runTimeVO		= new GetNSMultiContsResponseVO();
		GetNSMultiContsResultVO	resultListVO	= new GetNSMultiContsResultVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		
		
		int result_set = 0;	
		int nAlbumCount = 0;
		String[] stAlbumList = null;
		String szAlbumId = "";
				
		try {
			
			if(result_set == 0 && !"".equals(paramVO.getMultiAlbumId())){												
				
				stAlbumList = paramVO.getMultiAlbumId().split(",");
								
				while(nAlbumCount < stAlbumList.length && nAlbumCount < 30){
					
					tp1 = System.currentTimeMillis();
					
					szAlbumId = stAlbumList[nAlbumCount];
					
					paramVO.setAlbumId(szAlbumId);
					
					tempVO = new GetNSMultiContsResponseVO();
					
					if("".equals(paramVO.getSaId()) || "".equals(paramVO.getStbMac())){
						tempVO.setAlbumId(szAlbumId);
						tempVO.setLinkTime("0");
						tempVO.setWatchYn("N");
					}else{
						tempVO = this.IsWatch(paramVO);
						
						result_set = paramVO.getResultSet();
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("시청 여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					
					if("Y".equals(tempVO.getWatchYn())){
						
						tp1 = System.currentTimeMillis();
						
						if("".equals(paramVO.getSaId()) || "".equals(paramVO.getStbMac())){
							tempVO.setLinkTime("0");
						}else{
							linkTimeVO = this.GetLinkTime(paramVO);
							
							tempVO.setLinkTime(linkTimeVO.getLinkTime());
							
							result_set = paramVO.getResultSet();
						}
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("링크타입 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					
					if(result_set != 0) break;
					
					tp1 = System.currentTimeMillis();
					
					runTimeVO = this.GetAlbumRuntime(paramVO);
					
					tempVO.setRuntime(runTimeVO.getRuntime());
					
					result_set = paramVO.getResultSet();
					
					if(result_set != 0) break;
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("런타입 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					
					if("".equals(szAlbumId)){
						nAlbumCount++;
						continue;
					}
					
					resultVO.add(tempVO);
					
					nAlbumCount++;
				}
				
			}
			
			
			resultListVO.setList(resultVO);
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID012) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	

	/**
	 * 시청 여부 조회
	 * @param paramVO
	 * @return
	 */
	public GetNSMultiContsResponseVO IsWatch(GetNSMultiContsRequestVO paramVO) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "nsvod012_001_20171214_001";
			
		List<String> list	= new ArrayList<String>();
		GetNSMultiContsResponseVO isWatchVO	= new GetNSMultiContsResponseVO();
		
		try {
			try{
				list  = getNSMultiContsDao.isWatch(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if (list == null || list.isEmpty()) {
				isWatchVO.setAlbumId(paramVO.getAlbumId());
				isWatchVO.setLinkTime("0");
				isWatchVO.setWatchYn("N");
			}else{
								
				isWatchVO.setAlbumId(paramVO.getAlbumId());
				isWatchVO.setWatchYn("Y");
			}
			
			paramVO.setResultSet(0);
					
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID012, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
		return isWatchVO;
	}
	
	/**
	 * 링크타입 정보 조회
	 * @param paramVO
	 * @return
	 */
	public GetNSMultiContsResponseVO GetLinkTime(GetNSMultiContsRequestVO paramVO) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "nsvod012_002_20171214_001";
			
		List<GetNSMultiContsResponseVO> list	= new ArrayList<GetNSMultiContsResponseVO>();
		GetNSMultiContsResponseVO getLinkTimeVO	= new GetNSMultiContsResponseVO();
		
		try {
			try{
				list  = getNSMultiContsDao.getLinkTime(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if (list == null || list.isEmpty()) {
				getLinkTimeVO.setAlbumId(paramVO.getAlbumId());
				getLinkTimeVO.setLinkTime("0");
			}else {
				getLinkTimeVO = list.get(0);
			}
			
			paramVO.setResultSet(0);
					
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID012, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
		return getLinkTimeVO;
	}

	
	/**
	 * 런타임 정보 조회
	 * @param paramVO
	 * @return
	 */
	public GetNSMultiContsResponseVO GetAlbumRuntime(GetNSMultiContsRequestVO paramVO) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "nsvod012_003_20171214_001";
			
		List<String> list	= new ArrayList<String>();
		GetNSMultiContsResponseVO runtimeVO	= new GetNSMultiContsResponseVO();
		
		try {
			try{
				list  = getNSMultiContsDao.getAlbumRuntime(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if (list != null && !list.isEmpty()) {
								
				runtimeVO.setRuntime(list.get(0));
			}
			
			paramVO.setResultSet(0);
					
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID012, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
		return runtimeVO;
	}

}
