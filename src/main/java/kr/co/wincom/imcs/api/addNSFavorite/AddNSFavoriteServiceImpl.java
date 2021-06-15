package kr.co.wincom.imcs.api.addNSFavorite;

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
public class AddNSFavoriteServiceImpl implements AddNSFavoriteService {
	private Log imcsLogger		= LogFactory.getLog("API_addNSFavorite");
	
	@Autowired
	private AddNSFavoriteDao addNSFavoriteDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void addNSFavorite(String szSaId, String szStbMac, String szPid){
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
	public AddNSFavoriteResultVO addNSFavorite(AddNSFavoriteRequestVO paramVO)	{
//		this.addNSFavorite(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
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
		
		AddNSFavoriteResultVO	resultListVO	= new AddNSFavoriteResultVO();
		
		int nRetVal			= 0;
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		try {
			tp1 = System.currentTimeMillis();
			
			if(paramVO.getContsId() != null && paramVO.getContsId().trim().length() != 0) {
				resultListVO.setContentsId(paramVO.getContsId());
				resultListVO.setContentsName(paramVO.getContsName());
			}
			
			
			// 중복체크
			Integer nDupChk = 0;
			nDupChk = addNSFavoriteDao.getFavoriteDupChk(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("찜목록 중복 Check", String.valueOf(tp2 - tp1), methodName, methodLine);

			
			if(nDupChk == null || nDupChk.equals(""))			// 마이바티스의 경우 결과가 0인경우에도 null을 리턴해줌.
				nDupChk	= 0;
			
			
			if(nDupChk == 0) {		// 중복이 아니면
				
				// 찜목록 인덱스 조회
				/*Integer nFavorIdx = 0;
				try {
					nFavorIdx = addNSFavoriteDao.getFavoriteIndex(paramVO.getSaId());
					paramVO.setFavIdx(String.valueOf(nFavorIdx));
				} catch (Exception e) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID379) + "] sts[    0] msg[" + String.format("%-20s", "favor_idx:" + ImcsConstants.RCV_MSG2) + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
				
				
				tp1 = System.currentTimeMillis();
				imcsLog.timeLog("찜목록 순번 조회", String.valueOf(tp1 - tp2), methodName, methodLine);*/
				
				
				// 찜목록 갯수 조회 (최대 갯수 체크 목적)
				Integer nFavorCnt	= 0;
				try {
					nFavorCnt = addNSFavoriteDao.getFavoriteCount(paramVO.getSaId());
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				} catch (Exception e) {					
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID379) + "] sts[    0] msg[" + String.format("%-20s", "favor_cnt:" + ImcsConstants.RCV_MSG2) + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					paramVO.setResultCode("40000000");
				}
				
				
				if(nFavorCnt == null || nFavorCnt.equals("")){
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID379) + "] sts[    0] msg[" + String.format("%-20s", "favor_cnt:" + ImcsConstants.RCV_MSG3) + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					nFavorCnt	= 0;
				}				
				
								
				if(nFavorCnt <= 59) {
					nRetVal = this.addNSFavoriteInfo(paramVO);
					
					if(nRetVal < 0 ) {
						resFlag		= "1";
						resErrCode	= "03";
						resErrMsg	= "찜목록에 등록이 실패하였습니다.";
						paramVO.setResultCode("20000003");
					} else {
						resFlag		= "0";
						resErrCode	= "";
						resErrMsg	= "찜목록에 등록되었습니다.";
						paramVO.setResultCode("20000000");
					}
				} else {			// 찜목록 최대 갯수 초과시 에러
					resFlag		= "1";
					resErrCode	= "02";
					resErrMsg	= "더 이상 찜목록에 추가하실 수 없습니다.";
					paramVO.setResultCode("20000002");
				}
				
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("찜목록 등록", String.valueOf(tp2 - tp1), methodName, methodLine);
			} else {				// 찜목록 중복 에러
				resFlag		= "1";
				resErrCode	= "01";
				resErrMsg	= "이미 찜목록에 등록된 상품입니다.";
				paramVO.setResultCode("20000001");
			}
			
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			resFlag		= "1";
			resErrCode	= "03";
			resErrMsg	= "찜목록에 등록이 실패하였습니다.";
			paramVO.setResultCode("20000003");
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			
			resFlag		= "1";
			resErrCode	= "03";
			resErrMsg	= "찜목록에 등록이 실패하였습니다.";
			paramVO.setResultCode("20000003");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setFlag(resFlag);
			resultListVO.setErrCode(resErrCode);
			resultListVO.setErrMsg(resErrMsg);
			resultListVO.setResultCode(paramVO.getResultCode());
			
			szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID379) + "]" 
					 + " sts[" + ImcsConstants.LOG_MSG3 + "]" + String.format("%-21s", " snd[FLAG=" + resultListVO.getFlag() + ":MESSAGE=" + resultListVO.getErrMsg() + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID379) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	/**
	 * 찜목록 등록
	 * @param 	GetNSPurchasedVO paramVO
	 * @return  int
	 **/
	public Integer addNSFavoriteInfo(AddNSFavoriteRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();

		String sqlId	= "lgvod379_i01_20171214_001";
		String szMsg	= "";
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		int querySize	= 0;
		try {
			try {
				querySize = addNSFavoriteDao.addNSFavoriteInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}
			

			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID379, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID379) + "] insert [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}			

		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID379) + "] insert [PT_VO_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID379, sqlId, cache, "fail_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return querySize;
	}

}
