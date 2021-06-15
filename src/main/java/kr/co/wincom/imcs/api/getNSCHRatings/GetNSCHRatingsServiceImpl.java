package kr.co.wincom.imcs.api.getNSCHRatings;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
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
public class GetNSCHRatingsServiceImpl implements GetNSCHRatingsService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSCHRatings");
	
	@Autowired
	private GetNSCHRatingsDao getNSCHRatingsDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSCHRatings(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSCHRatingsResultVO getNSCHRatings(GetNSCHRatingsRequestVO paramVO){
//		this.getNSCHRatings(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);

		List<GetNSCHRatingsResponseVO> resultVO = new ArrayList<GetNSCHRatingsResponseVO>();
		GetNSCHRatingsResponseVO tempVO = new GetNSCHRatingsResponseVO();
		GetNSCHRatingsResultVO resultListVO = new GetNSCHRatingsResultVO();
		
		String msg	= "";
		String szThmImgSvrIp	= "";		// 썸네일 이미지 서버 IP
		String szChnlImgSvrIp	= "";		// 채널 이미지 서버 IP
		
		int nMainCnt = 0;
		long tp1, tp2 = 0;
	    
		try{
			tp1	= System.currentTimeMillis();
			
			// 가입자 정보 조회
			this.testSbc(paramVO);
			
			// 기지국 정보 조회			
			// 입력받은 BASE_CD를 이용하여 기지국정보 조회
			List<ComNodeVO> nodeInfoVO = new ArrayList<ComNodeVO>();
			paramVO.setBaseCondi(paramVO.getBaseCd());
			nodeInfoVO = this.getNode(paramVO);
		    
		    if(nodeInfoVO != null && nodeInfoVO.size() > 0){
		    	nMainCnt = nodeInfoVO.size();
		    } else {
		    	// 입력받은 BASE_CD의 앞1자리를 이용하여 기지국정보 조회
		    	paramVO.setBaseCondi(paramVO.getBaseOneCd());
		    	nodeInfoVO = this.getNode(paramVO);
		    	
		    	if(nodeInfoVO != null && nodeInfoVO.size() > 0){
			    	nMainCnt = nodeInfoVO.size();
			    } else {
			    	// BASE_CD(1234567890)를 이용하여 기지국정보 조회
			    	paramVO.setBaseCondi("1234567890");
			    	nodeInfoVO = this.getNode(paramVO);
			    	
			    	if(nodeInfoVO != null && nodeInfoVO.size() > 0){
				    	nMainCnt = nodeInfoVO.size();
				    }
			    }
		    }
		    
	    	if(nMainCnt > 0) {
	    		paramVO.setNodeCd(nodeInfoVO.get(0).getNodeCd());
		    	paramVO.setRbaseCode(nodeInfoVO.get(0).getrBaseCode());	
	    	}
			

		    tp2	= System.currentTimeMillis();
		    imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		    
		    msg = " [파라메타확인] c_test_sbc[" + paramVO.getTestSbc() + "] c_nsc_type[" + paramVO.getNscType() + "] c_base_gb[" + paramVO.getBaseGb() + "] c_base_cd[" + paramVO.getBaseCd()
		    		+ "] rd1.c_node_cd[" + paramVO.getNodeCd()+ "] c_pooq_yn[" + paramVO.getPooqYn()+"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		    try {
		    	szThmImgSvrIp = commonService.getIpInfo("snap_server", ImcsConstants.API_PRO_ID982.split("/")[1]);		// 썸네일 이미지 조회
		    	szChnlImgSvrIp = commonService.getIpInfo("img_chnl_server", ImcsConstants.API_PRO_ID982.split("/")[1]);		// 썸네일 이미지 조회
		    	
		    	
		    } catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID982, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				throw new ImcsException();
		    }

			
			// 채널 리스트 조회 (pooq_yn에 대한 분기 존재)
			tp1 = System.currentTimeMillis();
			resultVO =  this.getNSChnlList(paramVO);
	    	
	    	nMainCnt = 0;
	    	
	    	if(resultVO != null){
	    		nMainCnt = resultVO.size();
	    	}
	    	    	
	    	for(int i = 0; i < nMainCnt; i++){
	    		tempVO = resultVO.get(i);
	    		paramVO.setChnlCd("CH" + tempVO.getServiceId());
	    	
	    	    // 프로그램명 조회
	    		tempVO.setProName(StringUtil.nullToSpace(this.getProName(paramVO)));
	    		
			    tempVO.setLiveServer1("http://"+tempVO.getLiveIp1()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer2("http://"+tempVO.getLiveIp2()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer3("http://"+tempVO.getLiveIp3()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer4("http://"+tempVO.getLiveIp4()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer5("http://"+tempVO.getLiveIp5()+":"+tempVO.getLivePort()+"/");
			    tempVO.setLiveServer6("http://"+tempVO.getLiveIp6()+":"+tempVO.getLivePort()+"/");
			    
			    tempVO.setThmUrl(szThmImgSvrIp);
		    	tempVO.setImgUrl(szChnlImgSvrIp);
	    				    
			    resultVO.set(i, tempVO);
	    	}
		    		    
		    resultListVO.setList(resultVO);
		    msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID982) + "] select[" + nMainCnt +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
		    
		    tp2	= System.currentTimeMillis();
		    imcsLog.timeLog("채널 정보 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine);  

		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());

			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID982) + "] sts[" +ImcsConstants.LOG_MSG3 + "] msg[" + ImcsConstants.RCV_MSG5 + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID982) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
    
	
	/**
	 * 	검수 STB 여부 조회
	 * 	@param	GetNSCHRatingsRequestVO
	 * 	@return	void
	 */
    public void testSbc(GetNSCHRatingsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId =  "lgvod982_001_20171214_001";
		
		List<String> list   = null;
		
		try {
			try{
				list  = getNSCHRatingsDao.testSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				paramVO.setTestSbc("N");
			} else {
				paramVO.setTestSbc(StringUtil.nullToSpace(list.get(0)));
			}
			
		} catch (Exception e) {
			paramVO.setTestSbc("N");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
    
    
    /**
     *  전체 채널정보
     *  @param	GetNSCHRatingsRequestVO
     *  @return	List<GetNSCHRatingsResponseVO>
     */
    public List<GetNSCHRatingsResponseVO> getNSChnlList(GetNSCHRatingsRequestVO paramVO) throws Exception{
//    	long t1 = System.currentTimeMillis();
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId = "lgvod982_003_20171214_001";
    	
    	if( "P".equals(paramVO.getPooqYn()) ){
    		sqlId =  "lgvod982_003_20171214_001";
    	}else if( "N".equals(paramVO.getPooqYn()) ){
    		sqlId =  "lgvod982_004_20171214_001";
    	}else{
    		sqlId =  "lgvod982_005_20171214_001";
    	}
		
		List<GetNSCHRatingsResponseVO> list   = new ArrayList<GetNSCHRatingsResponseVO>();
		
		
		try {
			try{
				list = getNSCHRatingsDao.getNSChnlList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID982, sqlId, cache, "svr_play_ip:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
//    	long t2 = System.currentTimeMillis();
//    	System.out.println("####################getNSChnlList (끝)####################: " + ( t2 - t1 )/1000.0  );
		
    	return list;
    }
    
    
    
    /**
     *  기지국 정보 조회
     *  @param	GetNSCHRatingsRequestVO
     *  @return	List<GetNSCHRatingsRequestVO>
     */
    public List<ComNodeVO> getNode(GetNSCHRatingsRequestVO paramVO) throws Exception{
//    	long t1 = System.currentTimeMillis();
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId =  "lgvod982_002_20180723_001";
		
		List<ComNodeVO> list   = null;
		
		try {
			try{
				list = getNSCHRatingsDao.getNode(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			if( list == null || list.isEmpty()){
				list = null;
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
//    	long t2 = System.currentTimeMillis();
//    	System.out.println("####################getNode (끝)####################: " + ( t2 - t1 )/1000.0  );
    	return list;
    }
    
    
    
    /**
     * 프로그램명 조회
     * @param paramVO
     * @return
     */
    public String getProName(GetNSCHRatingsRequestVO paramVO) throws Exception{
//    	long t1 = System.currentTimeMillis();
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId 		= "lgvod982_006_20150126_001";
    	String szProName	= "";
		
		List<String> list   = null;
		
		int querySize	= 0;
		
		try {
			try{
				list	= getNSCHRatingsDao.getProName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szProName	= list.get(0);
				querySize	= list.size();
			}

		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID982, sqlId, cache, "svr_play_ip:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
//    	long t2 = System.currentTimeMillis();
//    	System.out.println("####################getProName (끝)####################: " + ( t2 - t1 )/1000.0  );
		return szProName;
    }
    
}
