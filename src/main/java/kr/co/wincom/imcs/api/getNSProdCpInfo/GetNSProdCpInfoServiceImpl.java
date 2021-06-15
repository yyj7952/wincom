package kr.co.wincom.imcs.api.getNSProdCpInfo;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSProdCpInfoServiceImpl implements GetNSProdCpInfoService {
	private Log imcsLogger = LogFactory.getLog("API_getNSProdCpInfo");
	
	@Autowired
	private GetNSProdCpInfoDao getNSProdCpInfoDao;
	
//	public void getNSProdCpInfo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSProdCpInfoResultVO getNSProdCpInfo(GetNSProdCpInfoRequestVO paramVO){
//		this.getNSProdCpInfo(paramVO.getSaId(), "", paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), "", paramVO.getPid());	
		
		GetNSProdCpInfoResultVO resultVO = new GetNSProdCpInfoResultVO();
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();

		imcsLog.debugLog(methodName + " service call");
		
		String msg	= "";
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		
		try{
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
		    
		    ComCpnVO cpnInfoVO = new ComCpnVO();
	    	
	    	try {
	    		cpnInfoVO =  getNSProdCpInfoDao.getNSProdCpInfoList(paramVO);
	    		
			} catch (Exception e) {
				resultVO.setResultCode("41000000");
				//imcsLog.failLog(ImcsConstants.API_PRO_ID014, "", null, "prodCp_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				
				throw new ImcsException();		
			}
	    	
	    	tp2	= System.currentTimeMillis();
			imcsLog.timeLog("상품쿠폰 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			
			if(cpnInfoVO == null){
	    		cpnInfoVO = new ComCpnVO();
	    		resultVO.setResultCode("21000000");
	    	}
		    
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("상품쿠폰 정보 FETCH", String.valueOf(tp1 - tp2), methodName, methodLine); 

		    resultVO.setCpnInfoVO(cpnInfoVO);
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());

			throw new ImcsException();
		} finally{
			resultVO.setResultCode(paramVO.getResultCode());
			
			// NosqlCacheType.HBASE_WR.ordinal(), NosqlCacheType.USERDB.ordinal() 이거는?
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID014) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultVO;
	}
    
    
}
