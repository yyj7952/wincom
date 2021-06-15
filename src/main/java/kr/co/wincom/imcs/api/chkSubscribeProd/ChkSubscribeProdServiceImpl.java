package kr.co.wincom.imcs.api.chkSubscribeProd;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class ChkSubscribeProdServiceImpl implements ChkSubscribeProdService {
	private Log imcsLogger = LogFactory.getLog("API_chkSubscribeProd");
	
	@Autowired
	private ChkSubscribeProdDao chkSubscribeProdDao;
	@Autowired
	private CommonService commonService;
	
	HashMap<String, String> mResult = new HashMap<String, String>();
	
	@Override
	public ChkSubscribeProdResultVO chkSubscribeProd(ChkSubscribeProdRequestVO paramVO){
//		this.chkSubscribeProd(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		ChkSubscribeProdResultVO tempVO = new ChkSubscribeProdResultVO();
		ChkSubscribeProdResultVO resultVO = new ChkSubscribeProdResultVO();
		List<ComSbcVO> lstCustomerInfo	= null;
		Integer messageSet	= 99;
		String msg = "";	
		String flag 	= "";
		String errCode	= "";
		String errMsg	= "";
	    Integer resultSet	= 0;
	    
	    
	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;
		
		try{
			// 상품타입 조회
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine);
			
			
			try {
	    		lstCustomerInfo = this.getCustomerInfo(paramVO);
	    		String szStatusFlag	= "";
				String szYnVodOpen	= "";
				
				if(lstCustomerInfo != null && lstCustomerInfo.size() > 0){
					szStatusFlag	= StringUtil.nullToSpace(lstCustomerInfo.get(0).getStatusFlag());
    				szYnVodOpen		= StringUtil.nullToSpace(lstCustomerInfo.get(0).getYnVodOpen());
    				
    				/* 사용여부 체크 */
    				if(!"Y".equals(szStatusFlag)){
    					resultSet = -1;
    					messageSet = 11;
    				}
    				
    				if("N".equals(szYnVodOpen)){
    					resultSet = -1;
    					messageSet = 12;
    				}
    				
				} else {
    				resultSet	= -1;
    				messageSet = 10;
    			}
				
				
			} catch (Exception e) {
				resultSet	= -1;
				messageSet = 10;
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("가입자 상태, 개통여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			tp1 = System.currentTimeMillis();
			if(resultSet == 0) {
				try {
					tempVO = this.getBuyContsProdType(paramVO);
					if(tempVO == null) {
						resultSet	= -1;
						messageSet = 13;
					} else {
						if(tempVO.getProdId().length() > 0) {
							resultVO = tempVO;
							resultSet	= 2;
						}
					}
				} catch (Exception e) {
					resultSet	= -1;
					messageSet = 13;
				}
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("동일 SVOD 가입여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			
			/* 리턴값을 지정하여 리턴처리 */
		    if(resultSet == 0){
		    	resultVO.setFlag("0");
		    }else if(resultSet == 2){
		    	resultVO.setFlag("2");
		    }else{
		       	resultVO.setFlag("1");
		    }
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG5;
			tp1	= System.currentTimeMillis();
			mResult = commonService.getErrorMsg(messageSet);
			
			flag	= "1";
			
			if(mResult != null) {
				errCode	= mResult.get("ERR_CODE");
				errMsg	= mResult.get("ERR_MSG");
			} else {
				errCode	= "99";
				errMsg	= "incorrect failed !!!";
			}
			resultVO.setFlag(flag);
			resultVO.setErrCode(errCode);
			resultVO.setErrMsg(errMsg);
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;

			resultVO.setFlag("1");
			resultVO.setErrCode("99");
			resultVO.setErrMsg("incorrect failed !!!");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(resultVO.getFlag(), resultVO.getErrCode(), resultVO.getErrMsg());
		} finally{
			
			if(resultSet == -1){
				mResult = commonService.getErrorMsg(messageSet);
				
				flag	= "1";
				
				if(mResult != null) {
					errCode	= mResult.get("ERR_CODE");
					errMsg	= mResult.get("ERR_MSG");
				} else {
					errCode	= "99";
					errMsg	= "incorrect failed !!!";
				}
				
				resultVO.setFlag(flag);
				resultVO.setErrCode(errCode);
				resultVO.setErrMsg(errMsg);
				
				String szMsg = "";
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_SUBSCRIBEPROD) + "] msg[" + resultVO.getErrMsg() + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_SUBSCRIBEPROD) + "] return[" + resultVO.getFlag() +"|" + resultVO.getErrMsg() + "|" + resultVO.getProdId() + "|" + resultVO.getProdName() + "|" + resultVO.getBuyingDate() + "|" + resultVO.getErrCode() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_SUBSCRIBEPROD) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultVO;
	}
	
	 /**
     *	가입자 상태, 개통여부 조회
     * 	@param paramVO
     *	@return
     */
    public List<ComSbcVO> getCustomerInfo(ChkSubscribeProdRequestVO paramVO){
    	IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);

    	String msg				= "";
		List<ComSbcVO> list   = new ArrayList<ComSbcVO>();
		try {
			
			try{
				try
	        	{
					list = chkSubscribeProdDao.getSbcInfo(paramVO);
	        	}catch(Exception e)
	        	{
	        		msg	= "[getSbcInfo Error : " + e.getMessage() + "]"; 								
					imcsLog.serviceLog(msg, methodName, methodLine);
	        	}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return list;
    }
	
    /**
     *	동일 SVOD 가입여부 조회
     * 	@param paramVO
     *	@return
     */
    public ChkSubscribeProdResultVO getBuyContsProdType(ChkSubscribeProdRequestVO paramVO){
    	IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);

    	String msg				= "";
		List<ChkSubscribeProdResultVO> list   = new ArrayList<ChkSubscribeProdResultVO>();
		ChkSubscribeProdResultVO result = new ChkSubscribeProdResultVO();
		try {
			
			try{
				try
	        	{
					list = chkSubscribeProdDao.getBuyContsProdType(paramVO);
					
					if( list != null && !list.isEmpty()){
						result = list.get(0);
					}
					
	        	}catch(Exception e)
	        	{
	        		result = null;
	        		msg	= "[getBuyContsProdType Error : " + e.getMessage() + "]"; 								
					imcsLog.serviceLog(msg, methodName, methodLine);
	        	}
			}catch(DataAccessException e){
				result = null;
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return result;
    }
}

