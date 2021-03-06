package kr.co.wincom.imcs.api.getNSKidsWatch;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSKidsWatchService
{
	private final static String API_LOG_NAME = "000/getNSKidsWatch";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSKidsWatch");
	
	@Autowired
	private GetNSKidsWatchDao getNSKidsWatchDao;
	
	public GetNSKidsWatchResultVO getNSKidsWatch(GetNSKidsWatchRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("GetNSKidsWatch service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSKidsWatchResultVO resultVO = new GetNSKidsWatchResultVO();
		
		StringBuilder sb = new StringBuilder();
		String msg = "";		
		long tp1 = 0, tp2 = 0;
		
		HashMap<String, String> hm = null;
		HashMap<String, String> hmTypeAW = null;
		List<HashMap<String, String>> listBookInfo = null;
		List<HashMap<String, String>> listBookNmReadCnt = null;
		
		String idx_sa = paramVO.getSaId().substring(paramVO.getSaId().length() - 2);
		int p_idx_sa = Integer.parseInt(idx_sa) % 33;
		
		String cLastCheck = "";
		
		int watch_day = 0;
		int watch_week = 0;
		int watch_month = 0;
		int today_read_count = 0;
		int bookCount = 0;
		int total_book_count = 0;
		String freq_book = "";
		String freq_count = "";
		String last_book = "";
		String read_count = "";
		String read_rate1 = "";
		String read_rate2 = "";
		String read_sum_time = "";
		String tempReadRate = "";
		
		try
		{
			tp1 = System.currentTimeMillis();
			
			if(paramVO.getType().equals("A") == false && paramVO.getType().equals("B") == false
					&& paramVO.getType().equals("W") == false)
			{
				resultVO.setResult(String.format("1|?????????????????? : CALL_TYPE[%s]|\f", paramVO.getType()));
				throw new ImcsException();
			}
			
			// ???????????? ????????????
			hm = this.getNSKidsWatchDao.getNowDate();
			
			if(hm != null)
			{
				paramVO.setToday(hm.get("TODAY"));
				paramVO.setWeek(hm.get("WEEK"));
				paramVO.setMonth(hm.get("MONTH"));
			}
			
			imcsLog.serviceLog(String.format("init... [today:%s],[week:%s],[month:%s]",
					paramVO.getToday(), paramVO.getWeek(), paramVO.getMonth()), methodName, methodLine);
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("???????????? ????????????", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// ??????????????? ?????? ?????? ???????????? ??????
			if(paramVO.getType().equals("A") || paramVO.getType().equals("W"))
			{
				try {
					hmTypeAW = this.getNSKidsWatchDao.getType_A_W(paramVO);
				} catch(Exception ex) {
					resultVO.setResult(String.format("1|%s|\f", "???????????? ???????????? ??????"));
					throw ex;
				}
				
				watch_day = Integer.parseInt(hmTypeAW.get("WATCH_DAY"));
				watch_week = Integer.parseInt(hmTypeAW.get("WATCH_WEEK"));
				watch_month = Integer.parseInt(hmTypeAW.get("WATCH_MON"));
				today_read_count = Integer.parseInt(hmTypeAW.get("TODAY_READ_COUNT"));
			}
			
			tp1	= System.currentTimeMillis();
			
			imcsLog.timeLog("TYPE ?????? ?????? ???????????? ????????????", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			// ??????????????? ?????? ?????? ???????????? ??????
			if(paramVO.getType().equals("A") || paramVO.getType().equals("B"))
			{
				tp2	= System.currentTimeMillis();
				
				// ??? ????????? ?????????
				try {
					bookCount = this.getNSKidsWatchDao.getBookCount();
				} catch(Exception ex) {
					resultVO.setResult(String.format("1|%s|\f", "???????????? ????????? ??????"));
					throw ex;
				}
				
				total_book_count = bookCount;
				
				// 0?????? ????????? ????????? ???????????? ????????? ?????? ??????...
			    if(bookCount == 0 ) bookCount = 100;
			    
			    paramVO.setBookCount(bookCount);
			    paramVO.setP_idx_sa(p_idx_sa);
			    
			    imcsLog.timeLog("?????? ??? ????????? ????????????", String.valueOf(tp2 - tp1), methodName, methodLine);
			    
			    tp1	= System.currentTimeMillis();
			    
			    // ???????????? ?????? ????????????
			    try {
			    	listBookInfo = this.getNSKidsWatchDao.listBookManageInfo(paramVO);
				} catch(Exception ex) {
					resultVO.setResult(String.format("1|%s|\f", "???????????? ?????? ???????????? ??????"));
					throw ex;
				}
			    
			    for(int i = 0; i < listBookInfo.size(); i++)
			    {
			    	HashMap<String, String> hmBookInfo = listBookInfo.get(i);
			    	
			    	if(i > 2 && cLastCheck.equals("Y"))
			    		break;
			    	
			    	// ?????? ?????? ?????? ????????? ?????? ??? ????????? ???????????? ??????
			    	if(cLastCheck.equals("Y") == false && hmBookInfo.get("FREQ_YN").equals("Y"))
			    	{
			    		cLastCheck = "Y";
			    		freq_book = hmBookInfo.get("ALBUM_NAME");
			    		freq_count = hmBookInfo.get("READ_COUNT");
			    	}
			    	
			    	if(i == 0)
			    	{
			    		last_book = hmBookInfo.get("ALBUM_NAME");
			    		read_count = hmBookInfo.get("TOTAL_COUNT");
			    		read_rate1 = hmBookInfo.get("READ_RATE");
			    		read_sum_time = hmBookInfo.get("READ_SUM_TIME");
			    	}
			    	else if(i < 3)
			    	{
			    		last_book = String.format("%s\b%s", last_book, hmBookInfo.get("ALBUM_NAME"));
			    	}
			    }
			    
			    imcsLog.timeLog("???????????? ?????? ????????????", String.valueOf(tp1 - tp2), methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
		    
		    // I30 ?????? VERSION 3.0 ??????
		    freq_book = "";
		    freq_count = "";
		    last_book = "";
		    
		    try {
		    	listBookNmReadCnt = this.getNSKidsWatchDao.listVer3AlbumNmReadCnt(paramVO);
			} catch(Exception ex) {
				resultVO.setResult(String.format("1|%s|\f", "freq_query ??????"));
				throw ex;
			}
		    
		    for(int i = 0; i < listBookNmReadCnt.size(); i++)
		    {
		    	HashMap<String, String> hmBookCnt = listBookNmReadCnt.get(i);
		    	
		    	if(i == 0)
		    	{
		    		freq_book = hmBookCnt.get("ALBUM_NAME");
		    		freq_count = hmBookCnt.get("READ_COUNT");
		    	}
		    	else
		    	{
		    		freq_book = String.format("%s\b%s", freq_book, hmBookCnt.get("ALBUM_NAME"));
		    		freq_count = String.format("%s\b%s", freq_count, hmBookCnt.get("READ_COUNT"));
		    	}
		    }
		    
		    if(listBookNmReadCnt.size() == 0)
		    {
		    	freq_book = "";
	    		freq_count = "";
		    }
		    
		    imcsLog.timeLog("freq_query", String.valueOf(tp2 - tp1), methodName, methodLine);
		    
		    // I30 ?????? VERSION 3.0 ?????? ????????? ?????????
		    tp1	= System.currentTimeMillis();
		    
		    try {
		    	tempReadRate = this.getNSKidsWatchDao.getVer3BookReadRate(paramVO);
			} catch(Exception ex) {
				resultVO.setResult(String.format("1|%s|\f", "read_rate_query ??????"));
				throw ex;
			}
		    
		    if(StringUtil.isEmpty(tempReadRate))
		    	read_rate2 = "100";
		    else
		    	read_rate2 = tempReadRate;
		    
		    imcsLog.timeLog("read_rate_query", String.valueOf(tp1 - tp2), methodName, methodLine);
		    
		    sb.append("0||\f");
		    
		    // NSC
		    sb.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
		    		watch_day, 			watch_week, 		watch_month,
		    		read_count, 		read_rate1, 		freq_book,
		    		freq_count, 		today_read_count,	total_book_count,
		    		read_sum_time,		read_rate2
		    ));
		    
		    resultVO.setResult(sb.toString());
		}
		catch(ImcsException ce)
		{
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ce.getClass().getName() + ":" + ce.getMessage());
			
			paramVO.setResultCode("31000000");
		}
		catch(Exception e)
		{
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			
			paramVO.setResultCode("31000000");
		}
		finally
		{
			if(hm != null) hm.clear();
			if(hmTypeAW != null) hmTypeAW.clear();
			if(listBookInfo != null) listBookInfo.clear();
			if(listBookNmReadCnt != null) listBookNmReadCnt.clear();
			
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
}





























