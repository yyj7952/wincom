package kr.co.wincom.imcs.api.getNSSVODInfo;

import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSSVODInfoServiceImpl implements GetNSSVODInfoService {
	private Log imcsLogger = LogFactory.getLog("API_getNSSVODInfo");
	
	@Autowired
	private GetNSSVODInfoDao getNSSVODInfoDao;

//	public void getNSSVODInfo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSSVODInfoResultVO getNSSVODInfo(GetNSSVODInfoRequestVO paramVO){
//		this.getNSSVODInfo(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		GetNSSVODInfoResultVO resultListVO = new GetNSSVODInfoResultVO();
		List<GetNSSVODInfoResponseVO> resultVO	=  new ArrayList<GetNSSVODInfoResponseVO>();
    	GetNSSVODInfoResponseVO tempVO	= new GetNSSVODInfoResponseVO();
    	
		String msg	= "";
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
	    
		int nMainCnt = 0;
		
		try{
			try {
				// 채널코드 정보 조회
				String szChnlDvCd = getNSSVODInfoDao.getChnlInfo(paramVO);
				paramVO.setChnlDvCd(szChnlDvCd);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
				
				// 상품정보 조회
	    		resultVO = getNSSVODInfoDao.getNSSVODInfoList(paramVO);
	    		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	    		//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.REDIS_DB.ordinal()]++;
	    		
			} catch (Exception e) {
				resultListVO.setResultCode("40000000");
				//imcsLog.failLog(ImcsConstants.API_PRO_ID008, "", null, "prod_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);

				throw new ImcsException();		
			}
	    	
	    	tp1	= System.currentTimeMillis();
			imcsLog.timeLog("상품정보 조회", String.valueOf(tp1 - tp_start), methodName, methodLine);
	    	
	    	
	    	if(resultVO != null && !resultVO.isEmpty()){
	    		nMainCnt = resultVO.size();
	    	} else {
	    		resultListVO.setResultCode("21000000");
				//imcsLog.failLog(ImcsConstants.API_PRO_ID008, "", null, "prod_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
	    	}
	    	    	
	    	for(int i = 0; i < nMainCnt; i++){
	    		tempVO = resultVO.get(i);
	    		
	    		resultListVO.setProductId(tempVO.getSubProdId());
	    		resultListVO.setProductName(tempVO.getSubProdName());
	    		resultListVO.setProductPrice(tempVO.getSubProdPrice());
	    		//resultListVO.setCatName(tempVO.getCatName());
	    		resultListVO.setSubProdDesc(tempVO.getSubProdDesc());
	    		resultListVO.setuCubeProdId(tempVO.getuCubeProdId());
	    		
	    		paramVO.setSubProdId(tempVO.getSubProdId());
	    		
	    		Integer nDataCnt = 0;
	    		
	    		try {
	    			// 상품 존재 유무 조회 1, 2
	    			nDataCnt = getNSSVODInfoDao.chkProdInfo(paramVO);
	    			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	    			//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
	    		
	    			if(nDataCnt == null || nDataCnt == 0){
	    				nDataCnt = getNSSVODInfoDao.chkProdInfo2(paramVO);
	    				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	    				//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
	    				
	    				if(nDataCnt == null)	nDataCnt = 0;
	    			}
				} catch (Exception e) {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID008, "", null, "cus_prod_chk:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
					resultListVO.setResultCode("40000000");
				}
	    		
	    		
	    		if(nDataCnt == 1)	tempVO.setSubYn("0");
	    		else    			tempVO.setSubYn("1");
	    		
	    		resultVO.set(i, tempVO);
	    	}
		    
	    	tp2	= System.currentTimeMillis();
			imcsLog.timeLog("상품정보 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine);
	    	
			resultListVO.setList(resultVO);
		    
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID008) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	

}
