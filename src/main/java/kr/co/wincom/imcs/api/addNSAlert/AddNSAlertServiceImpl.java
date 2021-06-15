package kr.co.wincom.imcs.api.addNSAlert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.authorizeNView.AuthorizeNViewRequestVO;
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
public class AddNSAlertServiceImpl implements AddNSAlertService
{
	private Log imcsLogger		= LogFactory.getLog("API_addNSAlert");
	
	@Autowired
	private AddNSAlertDao addNSAlertDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void AddNSAlert(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-06-21
	 * 알람받기 등록 (lgvod379.pc)
	 */
	@Override
	public AddNSAlertResultVO AddNSAlert(AddNSAlertRequestVO paramVO)	{
//		this.AddNSAlert(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		AddNSAlertResultVO	resultListVO	= new AddNSAlertResultVO();
		
		String resFlag		= "";
		String resErrMsg	= "";
		String resErrCode	= "";
		String resultCode	= "";
		
		String msg			= "";
		
		int nRetVal		= 0;
		long tp1, tp2	= 0;
		
		HashMap<String, String> reservedInfo	= new HashMap<String, String>();
		
		try {
			tp1	= System.currentTimeMillis();
			
			if( !"".equals(paramVO.getContsId())){
				resultListVO.setContentsId(paramVO.getContsId());
				resultListVO.setContentsName(paramVO.getContsName());
			}
			
			
			// 알람받기 중복체크
			Integer nDupChk = 0;
			nDupChk = addNSAlertDao.getAlertDupChk(paramVO);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("알람받기 중복 Check", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			if(nDupChk == null || nDupChk.equals(""))			// 마이바티스의 경우 결과가 0인경우에도 null을 리턴해줌.
				nDupChk	= 0;
			
			if(nDupChk == 0) {		// 중복이 아니면
				
				String result_set = "0";
				
				// 알람받기 인덱스 조회
				Integer nFavorIdx = 0;
				try {
					nFavorIdx = addNSAlertDao.getAlertIndex(paramVO.getSaId());
				} catch (Exception e) {
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID661) + "] sts[    0] msg[" + String.format("%-20s", "alert_idx:" + ImcsConstants.RCV_MSG2) + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultCode = "40000000";
					
					result_set = "-1";
				}	
				
				// 찜목록 갯수 조회 (최대 갯수 체크 목적)
				Integer nAlertCnt	= 0;
				
				if("0".equals(result_set)){
										
					try {
						nAlertCnt = addNSAlertDao.getAlertCount(paramVO.getSaId());
					} catch (Exception e) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID661) + "] sts[    0] msg[" + String.format("%-20s", "alert_cnt:" + ImcsConstants.RCV_MSG2) + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultCode = "40000000";
					}				
					
					if(nAlertCnt == null || nAlertCnt.equals("")){
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID661) + "] sts[    0] msg[" + String.format("%-20s", "alert_cnt:" + ImcsConstants.RCV_MSG3) + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						nAlertCnt	= 0;
					}
					
					paramVO.setAlertIdx(String.valueOf(nFavorIdx));
				}
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("알람받기 순번 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				
				if(nAlertCnt <= 59) {
					reservedInfo = this.getReservedInfo(paramVO);
					//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("예약 앨범 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					
					if(reservedInfo != null){
						paramVO.setPreviewFlag(reservedInfo.get("PREVIEW_FLAG"));
						paramVO.setReservedPrice(reservedInfo.get("RESERVED_PRICE"));
						paramVO.setReservedDate(reservedInfo.get("RESERVED_DATE"));
						
						nRetVal = this.addNSAlertInfo(paramVO);
					} else {
						nRetVal = -1;
					}
					
					if(nRetVal < 0 ) {
						resFlag		= "1";
						resErrCode	= "03";
						resErrMsg	= "알림받기 등록이 실패하였습니다.";
						resultCode	= "20000003";
					} else {
						resFlag		= "0";
						resErrCode	= "00";
						resErrMsg	= "알림받기에 등록되었습니다.";
					}
					
					tp1	= System.currentTimeMillis();
					imcsLog.timeLog("알람받기 등록", String.valueOf(tp1 - tp2), methodName, methodLine);
					
				} else {			// 찜목록 최대 갯수 초과시 에러
					resFlag		= "1";
					resErrCode	= "02";
					resErrMsg	= "더 이상 알림받기에 추가하실 수 없습니다.";
					resultCode	= "20000002";
				}
				
			} else {				// 찜목록 중복 에러
				resFlag		= "1";
				resErrCode	= "01";
				resErrMsg	= "이미 알림받기에 등록된 상품입니다.";
				resultCode	= "20000001";
			}

		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			resFlag		= "1";
			resErrCode	= "03"; 
			resErrMsg	= "알림받기 등록이 실패하였습니다.";
			resultCode	= "20000003";
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			resFlag		= "1";
			resErrCode	= "03";
			resErrMsg	= "알림받기 등록이 실패하였습니다.";
			resultCode	= "20000003";
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setFlag(resFlag);
			resultListVO.setErrMsg(resErrMsg);
			resultListVO.setErrCode(resErrCode);
			resultListVO.setResultCode(resultCode);
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID661) + "] sts[" + ImcsConstants.LOG_MSG3 + "]"
					+ " snd[FLAG=" + resFlag + "|MESSAGE=" + resErrMsg + "|]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID661) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
			
		}
		
		return resultListVO;
	}
	
	/**
	 * 알람받기 등록
	 * @param 	GetNSPurchasedVO paramVO
	 * @return  int
	 **/
	public int addNSAlertInfo(AddNSAlertRequestVO paramVO) {
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();

		String sqlId	= "lgvod661_i01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;
		try {
			
			try {
				querySize =  addNSAlertDao.addNSAlertInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID661, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID661) + "] insert [PT_VO_ALERT] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			querySize	 = -1;
			
//			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID661) + "] insert [PT_VO_ALERT] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
//			imcsLog.failLog(ImcsConstants.API_PRO_ID661, sqlId, cache, "fail_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return querySize;
	}

	/**
	 * 예약 앨범 정보 조회
	 * @param 	GetNSPurchasedVO paramVO
	 * @return  int
	 **/
	@SuppressWarnings({"rawtypes", "unchecked"})
	public HashMap<String, String> getReservedInfo(AddNSAlertRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod664_s01_20171214_001";

		List<HashMap<String, String>> list	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String> result	= new HashMap<String, String>();
		
		try {
			
			try {
				for(HashMap item : addNSAlertDao.getReservedInfo(paramVO.getContsId())) {
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					list.add(item);
				}
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID661, sqlId, cache, "still_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				result	= list.get(0);
			}

			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID661, sqlId, cache, "still_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return result;
	}
}
