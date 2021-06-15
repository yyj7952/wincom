package kr.co.wincom.imcs.api.setNSPoint;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class SetNSPointServiceImpl implements SetNSPointService {
	private Log imcsLogger = LogFactory.getLog("API_setNSPoint");
	
	@Autowired
	private SetNSPointDao setNSPointDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void setNSPoint(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public SetNSPointResultVO setNSPoint(SetNSPointRequestVO paramVO){
//		this.setNSPoint(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		SetNSPointResultVO resultVO = new SetNSPointResultVO();
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String flag		= "";
		String errMsg	= "";
		String msg		= "";
		
	    int nPointChk = 0;
	    
	    Integer nResultSet = 0;
	    Integer result = 0;
	    
		long tp1, tp2, tp3 = 0;
	    
		try{
			tp1 = System.currentTimeMillis();
			
			// 기존에 작성된 평점정보가 있으면
//			try {
//				nPointChk = this.getPointChk(paramVO);
//				
//				if(nPointChk > 0)	result = this.updatePoint(paramVO);		// 평점 정보 수정
//				else				result = this.insertPoint(paramVO);		// 평점 정보 등록
//				
//				if(result == 1)		nResultSet = 0;
//				else				nResultSet = -1;
//			} catch(Exception e){
//				nResultSet = -1;
//			}
			
			
			// 앨범ID가 존재 여부 조회
			String szAdiAlbumId	= "";
//			try {
//				szAdiAlbumId = setNSPointDao.getAlbumId(paramVO);
//				
//				if( szAdiAlbumId == null || "".equals(szAdiAlbumId)) {		// 앨범 평점 정보 미존 재시 INSERT
//					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] select [PT_LA_CONTENTS_POINT_NSC] table[0] nodata";
//					imcsLog.serviceLog(msg, methodName, methodLine);
//					
//					try {
//						result = setNSPointDao.insertAlbPoint(paramVO);
//						
//						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] insert [PT_LA_CONTENTS_POINT_NSC] table[" + result + "] records Success at";
//						imcsLog.serviceLog(msg, methodName, methodLine);
//						
//					} catch (Exception e) {
//						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] update [PT_LA_CONTENTS_POINT_NSC] table Failed at";
//						imcsLog.serviceLog(msg, methodName, methodLine);
//						
//						nResultSet = -1;
//						
//					}
//				} else {							// 앨범 평점 정보 미존 재시 UPDATE
//					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] select [PT_LA_CONTENTS_POINT_NSC] table[1] records Success at";
//					imcsLog.serviceLog(msg, methodName, methodLine);
//					
//					try {
//						result = setNSPointDao.updateAlbPoint(paramVO);
//						
//						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] update [PT_LA_CONTENTS_POINT_NSC] table[" + result + "] records Success at";
//						imcsLog.serviceLog(msg, methodName, methodLine);
//						
//					} catch (Exception e) {
//						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] update [PT_LA_CONTENTS_POINT_NSC] table Failed at";
//						imcsLog.serviceLog(msg, methodName, methodLine);
//						
//						nResultSet = -1;
//					}			
//				}
//			} catch (Exception e) {
//				nResultSet	= -1;
//		        
//				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] select [PT_LA_CONTENTS_POINT_NSC] table[    0] records Failed at";
//				imcsLog.serviceLog(msg, methodName, methodLine);
//				
//				// SQLERR(__FILE__, __LINE__);
//			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("평점 설정", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
			if(nResultSet == 0){
				resultVO.setFlag("0");
				resultVO.setErrMsg("평점이 적용되었습니다.");
				
				tp3 = System.currentTimeMillis();
				imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf(tp3 - tp2), methodName, methodLine);
			}else{
				resultVO.setFlag("1");
				resultVO.setErrMsg("평점 적용이 실패하였습니다.");
				
				tp3 = System.currentTimeMillis();
				imcsLog.timeLog("EXEC SQL ROLLBACK", String.valueOf(tp3 - tp2), methodName, methodLine);
				
				throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), "");
			}
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			
			flag = ie.getFlag();
			errMsg = ie.getMessage();
			
			resultVO.setFlag(flag);
			resultVO.setErrMsg(errMsg);
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			
			flag	= "1";
			errMsg	= "평점 적용이 실패하였습니다.";	
			
			resultVO.setFlag(flag);
			resultVO.setErrMsg(errMsg);
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, errMsg, "");
			
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "]" + String.format("%-5s",  " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ " snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
    
	
//	/**
//	 * 평점 존재 여부 조회
//	 * @param 	SetNSPointRequestVO
//	 * @return	Integer
//	 */
//    public Integer getPointChk(SetNSPointRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
//		
//    	String sqlId = "lgvod609_001_20171214_001";
//    	int querySize = 0;
//    			
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<Integer> list   = null;
//		Integer nPointChk = 0;
//		
//		try {
//			rowKeys.setSaId(paramVO.getSaId());
//			rowKeys.setStbMac(paramVO.getStbMac());
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getContsId());
//			
//			checkKey.addVersionTuple("PT_VO_CUSTOM_POINT", paramVO.getSaId());
//			
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<Integer>() {
//				@Override
//				public List<Integer> execute(List<Object> param) throws SQLException {
//					try{
//						SetNSPointRequestVO requestVO = (SetNSPointRequestVO)param.get(0);
//						List<Integer> rtnList  = setNSPointDao.pointChk(requestVO);
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<Integer> getReturnType() {
//					return Integer.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			if( list == null || list.isEmpty()){
//				nPointChk = 0;
//			} else {
//				nPointChk = (Integer)list.get(0);
//				querySize = list.size();
//			}
//						
//			//C에서 주석 처리된 로그
//			try {
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID358, sqlId, cache, querySize, methodName, methodLine);
//			} catch (Exception e) {}
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
//			//throw new ImcsException(e);
//		}
//		
//    	return nPointChk;    	
//    }
	/**
	 * 평점 존재 여부 조회
	 * @param 	SetNSPointRequestVO
	 * @return	Integer
	 */
    public Integer getPointChk(SetNSPointRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		List<Integer> list   = null;
		Integer nPointChk = 0;
		String msg = "";
		
	try {
			try{
				list  = setNSPointDao.pointChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				nPointChk = 0;
				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] select [PT_LA_CUSTOM_POINT_NSC] table[0] nodata";
			} else {
				nPointChk = (Integer)list.get(0);
				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] select [PT_LA_CUSTOM_POINT_NSC] table[" + nPointChk + "] nodata";
			}
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			//throw new ImcsException(e);
		}
		
    	return nPointChk;    	
    }
    
    
    
    
//    /** 
//     * 평점 정보 수정
//     * @param	SetNSPointRequestVO
//     * @return	Integer
//     */
//    public Integer updatePoint(SetNSPointRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
//		String methodName = oStackTrace.getMethodName();
//    	
//    	String sqlId = "lgvod609_u01_20171214_001";
//    	String szMsg = "";
//
//		int querySize = 0;
//		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//    	try {
//			checkKey.addVersionTuple("PT_VO_CUSTOM_POINT", paramVO.getSaId());
//			binds.add(paramVO);
//			
//			querySize = cache.updateWithCacheVersion(new VersionUpdateExcutor() {
//				@Override
//				public int execute(List<Object> parameters) throws SQLException {
//					try{
//						SetNSPointRequestVO newInput = (SetNSPointRequestVO)parameters.get(0);
//						return setNSPointDao.updatePoint(newInput);
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//			}, binds, checkKey);
//						
//			// HABSE 버전 업 쿼리이므로 ORA_HBS COUNT + 1
//			//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
//			
//			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID609, sqlId, cache, querySize, methodName, methodLine);
//
//				if( querySize == 0 ){
//					imcsLog.failLog(ImcsConstants.API_PRO_ID609, "", cache, "fail info:no data found", methodName, methodLine);
//				} else {
//					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] update [PT_VO_CUSTOM_POINT] table[" + querySize + "] records Success at";
//					imcsLog.serviceLog(szMsg, methodName, methodLine);
//				}
//			} catch (Exception e) {}
//			
//		} catch (Exception e) {
//			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] update [PT_VO_CUSTOM_POINT] table Failed at";
//			imcsLog.serviceLog(szMsg, methodName, methodLine);
//			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID609, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//		}
//		
//		return querySize;
//	}
    
    
    /** 
     * 평점 정보 수정
     * @param	SetNSPointRequestVO
     * @return	Integer
     */
    public Integer updatePoint(SetNSPointRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String Msg = "";
		int querySize = 0;
		
    	try {
			try{
				querySize = setNSPointDao.updatePoint(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}						
			
			try {
				if( querySize == 0 ){
					Msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] update [PT_VO_CUSTOM_POINT] table[" + querySize + "] records Failed at";
				} else {
					Msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] update [PT_VO_CUSTOM_POINT] table[" + querySize + "] records Success at";
				}
				imcsLog.serviceLog(Msg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			Msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] update [PT_VO_CUSTOM_POINT] table Failed at";
			imcsLog.serviceLog(Msg, methodName, methodLine);
		}
		
		return querySize;
	}
    
    
//    /**
//     * 평점 정보 등록
//     * @param	SetNSPointRequestVO
//     * @return	Integer
//     */
//    public Integer insertPoint(SetNSPointRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
//		String methodName = oStackTrace.getMethodName();
//    	
//    	String sqlId = "lgvod609_i01_20171214_001";
//    	String szMsg = "";
//    	
//		int querySize = 0;
//		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//    	try {
//			checkKey.addVersionTuple("PT_VO_CUSTOM_POINT", paramVO.getSaId());
//			binds.add(paramVO);
//			
//			querySize = cache.updateWithCacheVersion(new VersionUpdateExcutor() {
//				@Override
//				public int execute(List<Object> parameters) throws SQLException {
//					try{
//						SetNSPointRequestVO newInput = (SetNSPointRequestVO)parameters.get(0);
//						return setNSPointDao.insertPoint(newInput);
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//			}, binds, checkKey);
//			
//			// HABSE 버전 업 쿼리이므로 ORA_HBS COUNT + 1
//			//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
//			
//			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID609, sqlId, cache, querySize, methodName, methodLine);
//
//				if( querySize == 0 ){
//					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] insert [PT_VO_CUSTOM_POINT] table Failed at";
//					imcsLog.serviceLog(szMsg, methodName, methodLine);
//					
//					imcsLog.failLog(ImcsConstants.API_PRO_ID609, "", cache, "fail info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
//				} else {
//					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] insert [PT_VO_CUSTOM_POINT] table[" + querySize + "] records Success at";
//					imcsLog.serviceLog(szMsg, methodName, methodLine);
//				}
//			} catch (Exception e) {}
//			
//		} catch (Exception e) {
//			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] insert [PT_VO_CUSTOM_POINT] table Failed at";
//			imcsLog.serviceLog(szMsg, methodName, methodLine);
//			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID609, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//		}
//		
//		return querySize;
//	}
    
    
    /**
     * 평점 정보 등록
     * @param	SetNSPointRequestVO
     * @return	Integer
     */
    public Integer insertPoint(SetNSPointRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	String Msg = "";
    	
		int querySize = 0;
		
    	try {
			try{
				querySize = setNSPointDao.insertPoint(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				if( querySize == 0 ){
					Msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] insert [PT_VO_CUSTOM_POINT] table[" + querySize + "] records Failed at";
					imcsLog.serviceLog(Msg, methodName, methodLine);
				} else {
					Msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] insert [PT_VO_CUSTOM_POINT] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(Msg, methodName, methodLine);
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			Msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID609) + "] insert [PT_VO_CUSTOM_POINT] table Failed at";
			imcsLog.serviceLog(Msg, methodName, methodLine);
		}
		
		return querySize;
	}
}