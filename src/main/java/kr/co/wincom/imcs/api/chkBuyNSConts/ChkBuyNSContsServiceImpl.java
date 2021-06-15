package kr.co.wincom.imcs.api.chkBuyNSConts;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import kr.co.wincom.imcs.common.NosqlCacheType;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.DateUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class ChkBuyNSContsServiceImpl implements ChkBuyNSContsService {
	private Log imcsLogger = LogFactory.getLog("API_chkBuyNSConts");
	
	@Autowired
	private ChkBuyNSContsDao chkBuyNSContsDao;

//	public void chkBuyNSConts(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public ChkBuyNSContsResultVO chkBuyNSConts(ChkBuyNSContsRequestVO paramVO){
//		this.chkBuyNSConts(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		DateUtil dateUtil = new DateUtil();
		
		ChkBuyNSContsResultVO resultVO = new ChkBuyNSContsResultVO();
		
		String msg = "";
		String szBuyingDate	= "";	    
	    Integer resultSet	= -1;
	    boolean isSuccess = false;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;
		
		try{
			// 상품타입 조회
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine);
			
			// loop를 한번 돌려서 하나라도 걸리면 break
			for(int i = 0; i < 1; i++)
			{
				// 상품타입 조회
				resultSet = this.chkBuyContsProdType(paramVO);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("상품타입 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(resultSet == 0)
				{
					isSuccess = true;
					szBuyingDate = String.valueOf(dateUtil.getCustomTodayFormat("yyyyMMddHHmmss"));
					break;
				}
				
				// PPV/PPS 구매 여부 확인
				szBuyingDate = this.chkBuyContsContsBuy(paramVO);
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("PPV/PPS 구매 여부 확인", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				if(StringUtils.isNotBlank(szBuyingDate))
				{
					isSuccess = true;
					break;
				}
				
				// 구매형 상품 존재여부
				resultSet = this.getBuyConts(paramVO);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("구매형 상품 존재여부", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(resultSet > 0)
				{
					isSuccess = true;
					szBuyingDate = String.valueOf(dateUtil.getCustomTodayFormat("yyyyMMddHHmmss"));
					break;
				}
				
				// 데이터 프리 구매 여부 확인
				szBuyingDate = this.getDataFreeBuy(paramVO);
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("데이터 프리 구매 여부 확인", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				if(StringUtils.isNotBlank(szBuyingDate))
				{
					isSuccess = true;
					break;
				}
				
				// 엔스크린(NSCREEN) 페어링 여부 확인
				HashMap<String, String> hm = this.getNscreenFairing(paramVO);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("엔스크린(NSCREEN) 페어링 여부 확인", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(hm != null && hm.size() > 0)
				{
					paramVO.setnSaId(hm.get("N_SA_ID"));
					paramVO.setnStbMac(hm.get("N_STB_MAC"));
					
					// 엔스크린(NSCREEN) 페어링 가입자로 구매 여부 확인
					hm = this.getNscreenFairingBuy(paramVO);
					
					tp1	= System.currentTimeMillis();
					imcsLog.timeLog("엔스크린(NSCREEN) 페어링 가입자로 구매 여부 확인", String.valueOf(tp1 - tp2), methodName, methodLine);
					
					int buyCnt = Integer.parseInt(hm.get("BUY_CNT"));
					
					if(buyCnt > 0)
					{
						szBuyingDate = hm.get("BUYING_DATE");
						
						isSuccess = true;
						break;
					}
					
					
					String chkCat = this.chkCategory(paramVO);
					
					List<Integer> list = null;
							
					if(chkCat.equals("1")) {
						list = this.kidProductCd(paramVO);
					} else {
						// 엔스크린(NSCREEN) 페어링 가입자로 가입 여부 확인
						list = this.getNscreenFairingJoin(paramVO);
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("엔스크린(NSCREEN) 페어링 가입자로 가입 여부 확인", String.valueOf(tp2 - tp1), methodName, methodLine);
					
					if(list != null && list.size() > 0)
					{
						isSuccess = true;
						szBuyingDate = String.valueOf(dateUtil.getCustomTodayFormat("yyyyMMddHHmmss"));
						break;
					}					
				}
			}
			
			if(isSuccess == true)
			{
				resultVO.setFlag("Y");
				resultVO.setBuyingDate(szBuyingDate);
			}
			else
			{
				resultVO.setFlag("N");
				resultVO.setErrCode("2");
			}
			
		}catch(ImcsException ie) {
			resultVO.setFlag("N");
			resultVO.setErrCode("3");
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID259) + "] return[" + resultVO.getFlag() +"|" + resultVO.getBuyingDate() + "|" + resultVO.getErrCode() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID259) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultVO;
	}
	
	/**
	 * 상품 타입 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private int chkBuyContsProdType(ChkBuyNSContsRequestVO paramVO) throws Exception
	{		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod259_001_20171214_001";
    	
		int querySize = 0;
		int resultSet = -1;
		
		List<Integer> list   = null;
		
		try
		{
			try{
				list = chkBuyNSContsDao.chkBuyContsProdType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list == null || list.isEmpty())
			{
				paramVO.setResultSet(-1);
				resultSet = -1;
				querySize = 0;
			}
			else
			{
				querySize = list.size();
				
				if(list.get(0) > 0)
				{
					resultSet = 0;
				}
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID259, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
		}
		catch(Exception e)
		{
			paramVO.setResultSet(-1);
			resultSet = -1;
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return resultSet;
	}
	
	/**
	 * 4. PPV/PPS 구매 여부 확인
	 * - 예약구매(시청하지 않은 상태)인 경우 EXPIRED_DATE = NULL
	 * - 예약구매는 PPV만 지원하고 있음.
	 * - 시청유효기간에 해당하는 컨텐츠 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private String chkBuyContsContsBuy(ChkBuyNSContsRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		String szMsg = "";
		String buyingDate = null;
		
		try
		{
			buyingDate = this.chkBuyNSContsDao.chkBuyContsContsBuy(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
		}
		catch(Exception e)
		{
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID259) + "] sts[" + ImcsConstants.LOG_MSG2 + "] "
					+ "msg[chkBuyConts:" + ImcsConstants.RCV_MSG2 + "]";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return buyingDate;
	}
	
	/**
	 * 구매형 상품 존재 여부
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private int getBuyConts(ChkBuyNSContsRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		String szMsg = "";
		int cnt = 0;
		
		try
		{
			cnt = this.chkBuyNSContsDao.getBuyConts(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
		}
		catch(Exception e)
		{
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID259) + "] sts[" + ImcsConstants.LOG_MSG2 + "] "
					+ "msg[chkBuyConts:" + ImcsConstants.RCV_MSG2 + "]";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return cnt;
	}
	
	/**
	 * 데이터 프리 구매 여부 확인
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private String getDataFreeBuy(ChkBuyNSContsRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		String szMsg = "";
		String buyingDate = null;
		
		try
		{
			buyingDate = this.chkBuyNSContsDao.getDataFreeBuy(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
		}
		catch(Exception e)
		{
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID259) + "] sts[" + ImcsConstants.LOG_MSG2 + "] "
					+ "msg[chkBuyConts:" + ImcsConstants.RCV_MSG2 + "]";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return buyingDate;
	}
	
	/**
	 * 엔스크린(NSCREEN) 페어링 여부 확인
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, String> getNscreenFairing(ChkBuyNSContsRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		String szMsg = "";
		HashMap<String, String> hm = null;
		
		try
		{
			hm = this.chkBuyNSContsDao.getNscreenFairing(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
		}
		catch(Exception e)
		{
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID259) + "] sts[" + ImcsConstants.LOG_MSG2 + "] "
					+ "msg[chkBuyConts:" + ImcsConstants.RCV_MSG2 + "]";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return hm;
	}
	
	/**
	 * 엔스크린(NSCREEN) 페어링 가입자로 구매 여부 확인
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, String> getNscreenFairingBuy(ChkBuyNSContsRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		String szMsg = "";
		HashMap<String, String> hm = null;
		
		try
		{
			hm = this.chkBuyNSContsDao.getNscreenFairingBuy(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
		}
		catch(Exception e)
		{
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID259) + "] sts[" + ImcsConstants.LOG_MSG2 + "] "
					+ "msg[chkBuyConts:" + ImcsConstants.RCV_MSG2 + "]";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return hm;
	}
	
	/**
	 * 엔스크린(NSCREEN) 페어링 가입자로 가입 여부 확인
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private List<Integer> getNscreenFairingJoin(ChkBuyNSContsRequestVO paramVO) throws Exception
	{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod259_002_20171214_001";
    	
		int querySize = 0;
		
		List<Integer> list = null;
		
		try
		{
			try{
				list = chkBuyNSContsDao.getNscreenFairingJoin(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list == null || list.isEmpty())
			{
				paramVO.setResultSet(-1);
				querySize = 0;
			}
			else
			{
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID259, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
		}
		catch(Exception e)
		{
			paramVO.setResultSet(-1);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
	}
	
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private String chkCategory(ChkBuyNSContsRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String chkCate = "";
		String szMsg = "";
		
		try
		{
			try {
				chkCate = chkBuyNSContsDao.chkCategory(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(chkCate == null)
				{
					chkCate = "0";
				}
				
				
			} catch (DataAccessException e) {
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		}
		catch(Exception e)
		{
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			paramVO.setResultCode("21000000");
			throw new ImcsException();
		}
		
		return chkCate;
	}
	
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private List<Integer> kidProductCd(ChkBuyNSContsRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		List<Integer> list = null;
		String szMsg = "";
		
		try
		{
			try {
				list = chkBuyNSContsDao.kidProductCd(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(list == null || list.isEmpty())
				{
					paramVO.setResultSet(-1);
				}
				
			} catch (DataAccessException e) {
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		}
		catch(Exception e)
		{
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			paramVO.setResultCode("21000000");
			throw new ImcsException();
		}
		
		return list;
	}
}

