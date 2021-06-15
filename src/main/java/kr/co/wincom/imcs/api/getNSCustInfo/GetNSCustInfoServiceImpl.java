package kr.co.wincom.imcs.api.getNSCustInfo;


import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  GetNSCustInfoServiceImpl implements  GetNSCustInfoService {
	private Log imcsLogger = LogFactory.getLog("API_getNSCustInfo");
	
	@Autowired
	private GetNSCustInfoDao getNSCustInfoDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSCustInfo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	
	@Override
	public  GetNSCustInfoResultVO getNSCustInfo(GetNSCustInfoRequestVO paramVO){
//		this.getNSCustInfo(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSCustInfoResultVO resultVO = new GetNSCustInfoResultVO();
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String flag		= "";
		String errMsg	= "";
	    
	    String msg		= "";
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{
			String szCurrentDate = "";
			try {
				szCurrentDate = commonService.getSysdate();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	    	} catch (Exception e) {
	    		imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
				//throw new ImcsException(ImcsConstants.FAIL_CODE, e);
			}
			paramVO.setCurrentDate(szCurrentDate);
			
			String szCustomInfo = getNSCustInfoDao.getCustomInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			//if("".equals(szCustomInfo)) {
			if (StringUtil.isEmpty(szCustomInfo)) {
				resultVO.setFlag("0");
				resultVO.setTestSbc("N");
			}
			else {
				resultVO.setFlag("0");
				resultVO.setTestSbc(szCustomInfo);
			}
			
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			flag	= "1";
			errMsg	= "";		
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(flag, errMsg, "");
		} finally{
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID001) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
}
