package kr.co.wincom.imcs.api.addFXFavorite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
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
public class AddFXFavoriteServiceImpl implements AddFXFavoriteService {
	private Log imcsLogger		= LogFactory.getLog("API_addFXFavorite");
	
	@Autowired
	private AddFXFavoriteDao addFXFavoriteDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void addFXFavorite(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-06-21
	 * 찜목록 등록 (lgvod379.pc)
	 */
	@Override
	public AddFXFavoriteResultVO addFXFavorite(AddFXFavoriteRequestVO paramVO)	{
//		this.addFXFavorite(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);

		String resFlag		= "";
		String resErrCode	= "";
		String resErrMsg	= "";
		String szMsg		= "";
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		AddFXFavoriteResultVO	resultListVO	= new AddFXFavoriteResultVO();
		
		int nRetVal			= 0;
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		long tp_start		= paramVO.getTp_start();

		
		try {
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine);
			
			// 중복체크
			Integer nDupChk = 0;
			nDupChk = this.getFavorDupChk(paramVO);
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("찜목록 중복 Check", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			if(nDupChk == null || nDupChk.equals(""))			// 마이바티스의 경우 결과가 0인경우에도 null을 리턴해줌.
				nDupChk	= 0;
			
			
			if(nDupChk == 0) {		// 중복이 아니면
				
				Integer nFavorCnt	= 0;
				//2019.03.19 - MIMS 찜 정책은 유플릭스의 경우 모든 스크린에서 등록된 찜을 노출하므로, 유플릭스만 확인하지 않고, 모든 스크린에 등록된 찜 정보로 확인한다.
				nFavorCnt = this.getFavorIdx(paramVO);
				
				tp1 = System.currentTimeMillis();
				imcsLog.timeLog("찜목록 순번 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				if(nFavorCnt == null || nFavorCnt.equals(""))
					nFavorCnt	= 0;
				
				if(nFavorCnt > 59) return resultListVO;
				
				if(nFavorCnt <= 59) {
					nRetVal = this.addFavorInfo(paramVO);
					
					if(nRetVal < 0 ) {
						resFlag		= "1";
						resErrCode	= "03";
						resErrMsg	= "찜목록 등록이 실패하였습니다.";
					} else {
						resFlag		= "0";
						resErrCode	= "00";
						resErrMsg	= "찜목록에 등록되었습니다.";
					}
				} else {			// 찜목록 최대 갯수 초과시 에러
					resFlag		= "1";
					resErrCode	= "02";
					resErrMsg	= "더 이상 찜목록에 추가하실 수 없습니다.";
				}
				
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("찜목록 등록", String.valueOf(tp2 - tp1), methodName, methodLine);
			} else {				// 찜목록 중복 에러
				resFlag		= "1";
				resErrCode	= "01";
				resErrMsg	= "이미 찜목록에 등록된 상품입니다.";
			}
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("COMMIT || ROLLBACK", String.valueOf(tp2 - tp1), methodName, methodLine);
		} catch(ImcsException ie) {
			resFlag		= "1";
			resErrCode	= "03";
			resErrMsg	= "찜목록 등록이 실패하였습니다.";
			
			ie.setFlag(resFlag);
			ie.setErrorCode(resErrCode);
			ie.setMessage(resErrMsg);
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			resFlag		= "1";
			resErrCode	= "03";
			resErrMsg	= "찜목록 등록이 실패하였습니다.";
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(resFlag, resErrMsg, resErrCode);
		} finally{
			
			resultListVO.setFlag(resFlag);
			resultListVO.setErrCode(resErrCode);
			resultListVO.setErrMsg(resErrMsg);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID090) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(szMsg, methodName, methodLine);			
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 찜목록 개수 조회
	 * @param 	AddFXFavoriteRequestVO paramVO
	 * @return  int
	 */
	public Integer getFavorCnt(AddFXFavoriteRequestVO paramVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();

		String sqlId	= "fxvod090_003_20171214_001";
		String szMsg	= "";

		List<Integer> list   = new ArrayList<Integer>();
		Integer resultVO	= 0;
		int querySize	= 0;

		try {
			
			try{
				list = addFXFavoriteDao.getFavorCnt(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if(list == null || list.size() == 0) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID090) + "] sts[    0] msg[" + String.format("%-20s", "favor_cnt:" + ImcsConstants.RCV_MSG3) + "]";
			} else {
				resultVO	= list.get(0);
				
				if(resultVO == null)	resultVO = 0;
			}

			try{
//				imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID090, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			

		} catch (Exception e) {
//			if(cache.getLastException() != null) {
//				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID090) + "]"
//					+ " msg[" + String.format("%-20s", cache.getLastException().getErrorCode() + ":" + cache.getLastException().getErrorMessage() + "]");
//			} else {
//				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID090) + "] msg[" + String.format("%-20s", "    0:" + ImcsConstants.RCV_MSG2) + "]"; 
//			}
			
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}

		return resultVO;
	}
	
	
	/**
	 * 찜목록 개수 조회
	 * @param 	AddFXFavoriteRequestVO paramVO
	 * @return  int
	 */
	public Integer getFavorIdx(AddFXFavoriteRequestVO paramVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();

		String sqlId	= "fxvod090_002_20171214_001";



		List<Integer> list   = new ArrayList<Integer>();
		Integer resultVO	= 0;
		int querySize	= 0;

		try {
			try{
				list = addFXFavoriteDao.getFavorIdx(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}	
			
			if(list != null && list.size() > 0) {
				resultVO	= list.get(0);
				
				if(resultVO == null)	resultVO = 0;
			}

			try{
//				imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID090, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){
				resultVO = 999;
			}

		} catch (Exception e) {
			resultVO = 999;
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID090, sqlId, cache, "favor_idx:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return resultVO;
	}

	
	
	/**
	 * 찜목록 중복 체크
	 * @param 	AddFXFavoriteRequestVO paramVO
	 * @return  int
	 */
	public Integer getFavorDupChk(AddFXFavoriteRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();

		String sqlId	= "fxvod090_001_20171214_001";
		
		List<Integer> list   = new ArrayList<Integer>();
		Integer resultVO	= 0;
		int querySize	= 0;

		try {
			try{
				list = addFXFavoriteDao.getFavorDupChk(paramVO);
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if(list != null && list.size() > 0) {
				resultVO	= list.get(0);
				
				if(resultVO == null)	resultVO = 0;
			}

			try{
//				imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID090, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;

		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return resultVO;
	}

	/**
	 * 찜목록 등록
	 * @param 	AddFXFavoriteRequestVO paramVO
	 * @return  int
	 **/
	public Integer addFavorInfo(AddFXFavoriteRequestVO paramVO) throws Exception {
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();

		String sqlId	= "fxvod090_i01_20171214_001";
		String szMsg	= "";		

		int querySize	= 0;
		try {
//			checkKey.addVersionTuple("PT_VO_FAVORITE", paramVO.getSaId());
//			binds.add(paramVO);
//
//			querySize = cache.updateWithCacheVersion(new VersionUpdateExcutor() {
//
//				@Override
//				public int execute(List<Object> parameters) throws SQLException {
//					try {
//						AddFXFavoriteRequestVO param = (AddFXFavoriteRequestVO) parameters.get(0);
//						return addFXFavoriteDao.addFavorInfo(param);
//					} catch (DataAccessException e) {
//						// DB관련 Exception발생 시 getLastException을 받기위해
//						throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
//					}
//				}
//			}, binds, checkKey);
			
			try {
				querySize =  addFXFavoriteDao.addFavorInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}
			
			
			try {
//				imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID090, sqlId, cache, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID090) + "] insert [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}			

		} catch (Exception e) {
			querySize	= -1;
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID090) + "] insert [PT_VO_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID090, sqlId, cache, "fail_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return querySize;
	}

}
