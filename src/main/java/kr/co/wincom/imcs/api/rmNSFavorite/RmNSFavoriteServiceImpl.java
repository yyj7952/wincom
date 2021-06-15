package kr.co.wincom.imcs.api.rmNSFavorite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.rmFXAllWatchHis.RmFXAllWatchHisDao2;
import kr.co.wincom2.imcs.api.rmNSFavorite.RmNSFavoriteDao2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class RmNSFavoriteServiceImpl implements RmNSFavoriteService
{
	private Log imcsLogger		= LogFactory.getLog("API_rmNSFavorite");
	
	@Autowired
	private RmNSFavoriteDao rmNSFavoriteDao;

	@Autowired
	private RmNSFavoriteDao2 rmNSFavoriteDao2;
	
	@Autowired
	private NoSQLRedisDao	noSQLRedisDao;
	
//	public void rmNSFavorite(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-06-21
	 * 찜목록 삭제 (lgvod349.pc)
	 */
	@Override
	public RmNSFavoriteResultVO rmNSFavorite(RmNSFavoriteRequestVO paramVO)	{
//		this.rmNSFavorite(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String resFlag		= "";
		String resErrMsg	= "";
		
		RmNSFavoriteResultVO	resultListVO	= new RmNSFavoriteResultVO();
		
		String szMsg	= "";
		int nRetVal		= -99;
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		//20190522 권형도 내재화 수정
		paramVO.setIvMimsChk("1");
		
		try {
			tp1 = System.currentTimeMillis();
			
			if(paramVO.getContsId() != null && paramVO.getContsId().trim().length() != 0) {
				resultListVO.setContentsId(paramVO.getContsId());
			}
			
			for(int i = 0; i < paramVO.getContentsList().size(); i++) {
				// CONTENT_ID 가져오기 - CONTENT_ID는 M으로 시작해서 \b 이전까지
				paramVO.setContentId(paramVO.getContentsList().get(i));
				
				if( !"".equals(paramVO.getContentId()))		resultListVO.setContentsId(paramVO.getContentId());
				
				//2019.01.30 - IPTV/모바일 DB분리 대응 : DB lock으로 인해 수정하는 거긴하지만, DB lock을 없애기 위해 update문을 삭제 한다.
				// 해당 ContendID보다 큰 인덱스 -1 처리
				//nRetVal = this.uptFavoriteIndex(paramVO);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("찜목록 순번 Update", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				// 삭제 처리
				//if(nRetVal >= 0) {
				//	nRetVal	= this.rmNSFavoriteInfo(paramVO);
				//}
				
				nRetVal	= this.rmNSFavoriteInfo(paramVO);
				
				if (paramVO.getIvMimsChk().equals("1")) {
					nRetVal	= this.rmNSFavoriteInfo2(paramVO);
				}
				
				if (nRetVal >= 0) {
					this.rmNSFavoriteInfoDao2(paramVO);
				}
				
				tp1 = System.currentTimeMillis();
				imcsLog.timeLog("찜목록 삭제", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				if(nRetVal <= 0 ){
					break;
				}
			}
		
			if(nRetVal >= 0){
				resultListVO.setFlag("0");
				resultListVO.setErrMsg("찜목록 삭제되었습니다.");
			} else {
				resultListVO.setFlag("1");
				resultListVO.setErrMsg("찜목록 삭제가 실패하였습니다.");
				paramVO.setResultCode("20000001");
				
				throw new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg(), "", resultListVO);
			}
			
		} catch(ImcsException ie) {
			ie.setFlag(ie.getFlag());
			ie.setMessage(ie.getMessage());
			ie.setList(ie.getList());
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			resultListVO.setFlag("1");
			resultListVO.setErrMsg("찜목록 삭제가 실패하였습니다.");
			paramVO.setResultCode("20000001");
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg());
		} finally{
			//resultListVO.setFlag(resFlag);
			//resultListVO.setErrMsg(resErrMsg);
			resultListVO.setResultCode(paramVO.getResultCode());
			
			szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "]" 
					 + " sts[" + ImcsConstants.LOG_MSG3 + "]" + String.format("%-21s", " snd[FLAG=" + resultListVO.getFlag() + ":MESSAGE=" + resultListVO.getErrMsg() + "|]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(szMsg, methodName, methodLine);			
			
		}
				
		return resultListVO;
	}
	
	


	/**
	 * 찜목록 삭제
	 * @param 	RmNSFavoriteRequestVO paramVO
	 * @return  Integer
	 **/
	public Integer rmNSFavoriteInfo(RmNSFavoriteRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String sqlId	= "lgvod349_d01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;

		try {
			try {
				querySize = rmNSFavoriteDao.rmNSFavoriteInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

			//imcsLog.dbLog(ImcsConstants.API_PRO_ID349, sqlId, cache, querySize, methodName, methodLine);

			if(querySize == 0) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] tables Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				//imcsLog.failLog(ImcsConstants.API_PRO_ID349, "", cache, "fail info:no data found", methodName, methodLine);
			} else {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] tables Failed at ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID349, "", cache, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}

		return querySize;
	}

	public Integer rmNSFavoriteInfo2(RmNSFavoriteRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String sqlId	= "lgvod349_d01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;

		try {
			try {
				querySize = rmNSFavoriteDao.rmNSFavoriteInfo2(paramVO);
				System.out.println("#####################################:" + querySize);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

			//imcsLog.dbLog(ImcsConstants.API_PRO_ID349, sqlId, cache, querySize, methodName, methodLine);

			if(querySize == 0) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] tables Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				//imcsLog.failLog(ImcsConstants.API_PRO_ID349, "", cache, "fail info:no data found", methodName, methodLine);
			} else {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] tables Failed at ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID349, "", cache, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}

		return querySize;
	}
	
	public Integer rmNSFavoriteInfoDao2(RmNSFavoriteRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String sqlId	= "lgvod349_d01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;

		try {
			try {
				querySize = rmNSFavoriteDao2.rmNSFavoriteInfo(paramVO);
				System.out.println("#####################################2:" + querySize);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

			//imcsLog.dbLog(ImcsConstants.API_PRO_ID349, sqlId, cache, querySize, methodName, methodLine);

			if(querySize == 0) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] tables Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				//imcsLog.failLog(ImcsConstants.API_PRO_ID349, "", cache, "fail info:no data found", methodName, methodLine);
			} else {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] delete [PT_VO_FAVORITE] tables Failed at ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID349, "", cache, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}

		return querySize;
	}	


	/**
	 * 찜목록 인덱스 수정
	 * @param 	RmNSFavoriteRequestVO paramVO
	 * @return  Integer
	 **/
	public Integer uptFavoriteIndex(RmNSFavoriteRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String sqlId	= "lgvod349_u01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;

		try {
			try {
				querySize = rmNSFavoriteDao.uptFavoriteIndex(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID349, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] update [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}

		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] update [PT_VO_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID349) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ String.format("%-20s", " msg[fail info:" + cache.getLastException().getErrorMessage() + "]");	
			//imcsLog.serviceLog(szMsg, methodName, methodLine);
		}

		return querySize;
	}
	
}
