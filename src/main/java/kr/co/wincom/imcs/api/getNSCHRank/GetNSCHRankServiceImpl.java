package kr.co.wincom.imcs.api.getNSCHRank;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSCHRankServiceImpl implements GetNSCHRankService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSCHRank");
	
	@Autowired
	private GetNSCHRankDao getNSCHRankDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSCHRank(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSCHRankResultVO getNSCHRank(GetNSCHRankRequestVO paramVO){
//		this.getNSCHRank(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		List<GetNSCHRankResponseVO> resultVO =  new ArrayList<GetNSCHRankResponseVO>();
		GetNSCHRankResponseVO tempVO = new GetNSCHRankResponseVO();
		GetNSCHRankResultVO resultListVO = new GetNSCHRankResultVO();
				
		String msg	= "";
		
		int nMainCnt = 0;

	    long tp_start = paramVO.getTp_start();
		long tp1, tp2, tp3 = 0;
		
		String szChnlImgSvrip = "";
		
		try {
			szChnlImgSvrip	= commonService.getIpInfo("img_chnl_server", ImcsConstants.API_PRO_ID649.split("/")[1]);
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID649, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
	    
		try{
			// 기지국 정보 조회
			nMainCnt = 0;
			//if( "Y".equals(paramVO.getBaseGb()) ){
				// 입력받은 BASE_CD를 이용하여 기지국정보 조회
				List<ComNodeVO> nodeInfoVO = new ArrayList<ComNodeVO>();
				nodeInfoVO = this.getNode(paramVO);
			    
			    if(nodeInfoVO != null && !nodeInfoVO.isEmpty()){
			    	nMainCnt = nodeInfoVO.size();
			    } else {
			    	// 입력받은 BASE_CD의 앞1자리를 이용하여 기지국정보 조회
			    	paramVO.setBaseCd(paramVO.getBaseOneCd());
			    	nodeInfoVO = this.getNode(paramVO);
			    	
			    	if(nodeInfoVO != null && !nodeInfoVO.isEmpty()){
				    	nMainCnt = nodeInfoVO.size();
				    } else {
				    	// BASE_CD(1234567890)를 이용하여 기지국정보 조회
				    	paramVO.setBaseCd("1234567890");
				    	nodeInfoVO = this.getNode(paramVO);
				    	
				    	if(nodeInfoVO != null && !nodeInfoVO.isEmpty()){
					    	nMainCnt = nodeInfoVO.size();
					    }
				    }
			    }
			    
		    	if(nMainCnt > 0) {
		    		paramVO.setNodeCd(nodeInfoVO.get(0).getNodeCd());
			    	paramVO.setRbaseCode(nodeInfoVO.get(0).getrBaseCode());	
		    	}
			//}
		    
		    // 가입자 정보 조회
		    this.getDongYn(paramVO);		// DONG_YN Set
		    this.testSbc(paramVO);
		    
		    tp1	= System.currentTimeMillis();
			imcsLog.timeLog("가입자 조회", String.valueOf(tp1 - tp_start), methodName, methodLine);
		    
			// 실시간 인기채널 프로그램 조회
	    	resultVO =  this.getNSCHRankList(paramVO);
	    	
	    	tp2	= System.currentTimeMillis();
			imcsLog.timeLog("실시간 인기채널 프로그램 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    	
	    	nMainCnt = 0;
	    	
	    	if(resultVO != null){
	    		nMainCnt = resultVO.size();
	    	}else{
//				imcsLog.failLog(ImcsConstants.API_PRO_ID649, "", null, "periode_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
	    	}
	    	    	
	    	for(int i = 0; i < nMainCnt; i++){
	    		tempVO = resultVO.get(i);
	    		
			    tempVO.setLiveServer1("http://"+tempVO.getLiveIp1()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer2("http://"+tempVO.getLiveIp2()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer3("http://"+tempVO.getLiveIp3()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer4("http://"+tempVO.getLiveIp4()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer5("http://"+tempVO.getLiveIp5()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer6("http://"+tempVO.getLiveIp6()+":"+tempVO.getLivePort()+"/");
			    
			    if(!"http:".equals(tempVO.getImgUrl())){
			    	tempVO.setImgUrl(szChnlImgSvrip);
			    }
	    	
			    resultVO.set(i, tempVO);
	    	}
		    
	    	tp3	= System.currentTimeMillis();
			imcsLog.timeLog("실시간 인기채널 프로그램 Fetch", String.valueOf(tp3 - tp2), methodName, methodLine);
		    
		    resultListVO.setList(resultVO);
		    
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID649) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	/** 
	 *  동정보 조회
	 *  @param	GetNSCHRankRequestVO
	 */
	public void getDongYn(GetNSCHRankRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId =  "lgvod649_002_20171214_001";
		
		List<String> list   = new ArrayList<String>();

		try {
			try{
				list = getNSCHRankDao.getDongYn(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				paramVO.setDongYn("N");
			} else {
				paramVO.setDongYn("Y");
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
	
	
	/**
	 *  가입자 정보 조회
	 *  @param	GetNSCHRankRequestVO
	 *  @result	void
	 */
    public void testSbc(GetNSCHRankRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod649_003_20171214_001";
				
		List<String> list   = new ArrayList<String>();
		
		try {
			try{
				list = getNSCHRankDao.testSbc(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				paramVO.setTestSbc("N");
			} else {
				paramVO.setTestSbc(StringUtil.nullToSpace(list.get(0)));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
    
    
    // 
    /**
     * 	실시간 인기채널 프로그램 조회
     * 	@param	GetNSCHRankRequestVO
     * 	@result	List<GetNSCHRankResponseVO>
     */
    public List<GetNSCHRankResponseVO> getNSCHRankList(GetNSCHRankRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "";
		
		int querySize = 0;
		
		List<GetNSCHRankResponseVO> list   = null;
		
		String sql_condi = "";
		String nsc_condi = "";
		
		try {
			try{
				list = getNSCHRankDao.getNSCHRankList(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				querySize = list.size();
			}else{
				paramVO.setResultCode("21000000");
//				imcsLog.failLog(ImcsConstants.API_PRO_ID649, sqlId, cache, "periode_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
						
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID649, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID649, sqlId, cache, "periode_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
		
    	return list;
    }
    
    
    /**
     * 	기지국정보 조회 (노드 정보 조회)
     * 	@param	GetNSCHRankRequestVO
     * 	@result	List<GetNSCHRankRequestVO>
     */
    public List<ComNodeVO> getNode(GetNSCHRankRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod649_001_20180723_001";
		
		List<ComNodeVO> list   = null;
		
		try {
			try{
				list = getNSCHRankDao.getNode(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return list;
    }
    
}
