package kr.co.wincom.imcs.api.getNSMultiView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSMultiViewServiceImpl implements GetNSMultiViewService {
	private Log imcsLogger = LogFactory.getLog("API_getNSMultiView");
	
	@Autowired
	private GetNSMultiViewDao getNSMultiViewDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSMultiView(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSMultiViewResultVO getNSMultiView(GetNSMultiViewRequestVO paramVO){
//		this.getNSMultiView(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		GetNSMultiViewResponseVO tempVO = new GetNSMultiViewResponseVO();
	    List<GetNSMultiViewResponseVO> resultVO = new ArrayList<GetNSMultiViewResponseVO>();
	    List<GetNSMultiViewResponseVO> returnVO = new ArrayList<GetNSMultiViewResponseVO>();
	    GetNSMultiViewResultVO resultListVO = new GetNSMultiViewResultVO();
	    
		String msg	= "";
		
		int nMainCnt = 0;

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		
		try{
			
			String szThmImgSvrIp = "";		// 썸네일 이미지 조회
			String szImgSvrIp = "";
			
			try {
				szThmImgSvrIp = commonService.getIpInfo("snap_server", ImcsConstants.API_PRO_ID034.split("/")[1]);		// 썸네일 이미지 조회
				szImgSvrIp = commonService.getIpInfo("img_chnl_server", ImcsConstants.API_PRO_ID034.split("/")[1]);
			} catch (Exception e) {
				msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID034) + "] sts[" + ImcsConstants.LOG_MSG2 + "] msg[" + String.format("%-21s", "svr_ip:" + ImcsConstants.RCV_MSG1 + "]");
				imcsLog.serviceLog(msg, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
		    
		    // 멀티뷰 채널정보 조회
		    resultVO =  this.getNSMultiViewList(paramVO);
	    	
	    	if(resultVO != null && !resultVO.isEmpty()){
	    		nMainCnt = resultVO.size();
	    	}
	    	
	    	tp2	= System.currentTimeMillis();
			imcsLog.timeLog("멀티뷰 프리셋", String.valueOf(tp2 - tp1), methodName, methodLine);
	    	    	
	    	for(int i = 0; i < nMainCnt; i++){
	    		tempVO = resultVO.get(i);
	    		tempVO.setThmUrl(szThmImgSvrIp);
	    		tempVO.setImgUrl(szImgSvrIp);
	    		
	    		// 인입 파라미터가 P가 아니고 조회된 파라미터가 N이 아니면 출력
	    		if("P".equals(paramVO.getPooqYn()) && "N".equals(tempVO.getPooqYn())) {} 
	    		else 	
	    			returnVO.add(tempVO);	
	    	}
	    	
	    	tp3	= System.currentTimeMillis();
			imcsLog.timeLog("멀티뷰 프리셋 Fetch", String.valueOf(tp3 - tp2), methodName, methodLine);
			
			resultListVO.setList(returnVO);
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			ie.setList(resultListVO);
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 CurationException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID034) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
					+ " [DB:" + nUserDB + "]"; 
		imcsLog.serviceLog(msg, methodName, methodLine);

		}
		
		return resultListVO;
	}
    
    /**
     * Nscreen 멀티뷰 채널정보 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
    public List<GetNSMultiViewResponseVO> getNSMultiViewList(GetNSMultiViewRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod034_010_20171214_001";
		
		int querySize = 0;
		
		List<GetNSMultiViewResponseVO> list   = null;
		
		try {
			try{
				list = getNSMultiViewDao.getNSMultiView(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID034, sqlId, cache, "multiview_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				querySize = list.size();
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID034, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID034, sqlId, cache, "multiview_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			//throw new ImcsException(e);
		}
		
    	return list;
    }
}
