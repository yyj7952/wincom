package kr.co.wincom.imcs.api.getNSCHLists;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComTestSbcVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSCHListsServiceImpl implements GetNSCHListsService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSCHLists");
	
	@Autowired
	private GetNSCHListsDao getNSCHListsDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSCHLists(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSCHListsResultVO getNSCHLists(GetNSCHListsRequestVO paramVO){
//		this.getNSCHLists(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSCHListsResponseVO> resultVO	= new ArrayList<GetNSCHListsResponseVO>();
		GetNSCHListsResponseVO tempVO		= new GetNSCHListsResponseVO();
		GetNSCHListsResultVO resultListVO	= new GetNSCHListsResultVO();

		String msg	= "";
		
		int nMainCnt = 0;

	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;
		
		String szPpmImgSvrip = "";
		
		try {
			szPpmImgSvrip	= commonService.getIpInfo("img_ppm_server", ImcsConstants.API_PRO_ID035.split("/")[1]);
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID035, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
		try{
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			// 기지국 정보 조회
			nMainCnt = 0;
			if( "Y".equals(paramVO.getBaseGb()) ){
				// 입력받은 BASE_CD를 이용하여 기지국정보 조회
				List<ComNodeVO> nodeInfoVO = new ArrayList<ComNodeVO>();
				paramVO.setBaseCondi(paramVO.getBaseCd());
				nodeInfoVO = this.getNode(paramVO);
			    
			    if(nodeInfoVO != null && !nodeInfoVO.isEmpty()){
			    	nMainCnt = nodeInfoVO.size();
			    } else {
			    	// 입력받은 BASE_CD의 앞1자리를 이용하여 기지국정보 조회
			    	paramVO.setBaseCondi(paramVO.getBaseOneCd());
			    	nodeInfoVO = this.getNode(paramVO);
			    	
			    	if(nodeInfoVO != null && !nodeInfoVO.isEmpty()){
				    	nMainCnt = nodeInfoVO.size();
				    } else {
				    	// BASE_CD(1234567890)를 이용하여 기지국정보 조회
				    	paramVO.setBaseCondi("1234567890");
				    	nodeInfoVO = this.getNode(paramVO);
				    	
				    	if(nodeInfoVO != null && !nodeInfoVO.isEmpty()){
					    	nMainCnt = nodeInfoVO.size();
					    }
				    }
			    }
			    
		    	if(nMainCnt > 0) {
		    		paramVO.setNodeCd(nodeInfoVO.get(0).getNodeCd());
			    	paramVO.setRbaseCode(nodeInfoVO.get(0).getrBaseCode());	
		    	} else {
		    		paramVO.setBaseGb("N");
		    	}
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("기지국정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 

			
			
			// 가입자관련정보 조회
	    	this.testSbc(paramVO);
		    
		    if( !"Y".equals(paramVO.getBaseGb()) ){
		    	this.setNodeHjd(paramVO);
		    }
		    
		    tp1	= System.currentTimeMillis();
			imcsLog.timeLog("가입자 정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine); 
			
			
		    
		    // 메인페이지 주요채널 프로그램 조회
		    resultVO =  this.getNSChnlLists(paramVO);
	    	
	    	nMainCnt = 0;
	    	
	    	if(resultVO != null){
	    		nMainCnt = resultVO.size();
	    	}else{
	    		paramVO.setResultCode("21000000");
//				imcsLog.failLog(ImcsConstants.API_PRO_ID035, "", null, "chnl_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
	    	}

	    	tp2	= System.currentTimeMillis();
	    	imcsLog.timeLog("메인페이지 주요채널 프로그램 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    	
	    	for(int i = 0; i < nMainCnt; i++){
	    		tempVO = resultVO.get(i);
	    		
			    tempVO.setLiveServer1("http://"+tempVO.getLiveIp1()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer2("http://"+tempVO.getLiveIp2()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer3("http://"+tempVO.getLiveIp3()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer4("http://"+tempVO.getLiveIp4()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer5("http://"+tempVO.getLiveIp5()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer6("http://"+tempVO.getLiveIp6()+":"+tempVO.getLivePort()+"/");
			    
			    if(!"http:".equals(tempVO.getImgUrl())){
			    	tempVO.setImgUrl(szPpmImgSvrip);
			    }
			
			    resultVO.set(i, tempVO);
	    	}
		    
	    	resultListVO.setList(resultVO);
	    	
	    	tp1	= System.currentTimeMillis();
			imcsLog.timeLog("메인페이지 주요채널 프로그램 Fetch", String.valueOf(tp1 - tp2), methodName, methodLine); 
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID035) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	/**
	 * 	기지국정보 조회
	 * 	@param	GetNSMusicListRequestVO
	 *  @result	List<GetNSCHListsRequestVO>
	 */
	public List<ComNodeVO> getNode(GetNSCHListsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId =  "lgvod035_001_20180723_001";
		
		List<ComNodeVO> list   = null;
		
		try {
			try{
				list = getNSCHListsDao.getNode(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return list;
    }
    

	/**
	 * 	검수 STB 여부 조회
	 * 	@param	GetNSMusicListRequestVO
	 * 	@result	
	 */
    public void testSbc(GetNSCHListsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
		String sqlId =  "lgvod035_002_20171214_001";
		
		List<ComTestSbcVO> list   = null;
		ComTestSbcVO resultVO = null;
		
		try {
			try{
				list = getNSCHListsDao.testSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if( list == null ||list.isEmpty()){
				paramVO.setTestSbc("N");
				paramVO.setHjdongNo("X");
			} else {
				resultVO = (ComTestSbcVO)list.get(0);
				
				paramVO.setTestSbc(resultVO.getTestSbc());
				paramVO.setHjdongNo(resultVO.getHjDongNo());
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			//throw new ImcsException();
		}
    }
    
    
    /**
     * 	행정동 정보 조회
     * 	@param	paramVO
     * 	@result	
     */
    public void setNodeHjd(GetNSCHListsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod035_003_20171214_001";
		
		List<String> list   = null;
		
		try {
			try{
				list = getNSCHListsDao.getNodeHjd(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				paramVO.setDongYn("N");
				paramVO.setHjdongNo("1234567890");
			} else {
				paramVO.setDongYn("Y");
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			//throw new ImcsException();
		}
    }
    
    
    /**
     *	메인페이지 주요 채널 정보 조회
     *	@param	GetNSMusicListRequestVO
     *	@result	List<GetNSCHListsResponseVO>
     */
    public List<GetNSCHListsResponseVO> getNSChnlLists(GetNSCHListsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod035_020_20180723_001";
		
		int querySize = 0;
		
		List<GetNSCHListsResponseVO> list   = null;
		
		try {
			try{
				list = getNSCHListsDao.getNSChnlLists(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				list = null;
				
//				imcsLog.failLog(ImcsConstants.API_PRO_ID035, sqlId, cache, "chnl_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
				
			} else 
			{
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID035, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4000" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("40000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID035, sqlId, cache, "chnl_list:" + cache.getLastException().getErrorMessage(), methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return list;
    }
}
