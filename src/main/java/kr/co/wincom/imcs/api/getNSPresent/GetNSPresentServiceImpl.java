package kr.co.wincom.imcs.api.getNSPresent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSPresentServiceImpl implements GetNSPresentService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSPresent");
	
	@Autowired
	private GetNSPresentDao getNSPresentDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSPresent(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSPresentResultVO getNSPresent(GetNSPresentRequestVO paramVO){
//		this.getNSPresent(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		GetNSPresentResultVO resultVO = new GetNSPresentResultVO();
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String msg	= "";
		
		int nMainCnt	= 0;

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;	
		
		try{
			// 이미지 서버 IP/URL 조회
			//String szImgSvrIp	= "";		// 이미지 서버 IP
			String szImgSvrUrl	= "";		// 이미지 서버 URL
		    
		    try {
		    //	szImgSvrIp	= commonService.getIpInfo("img_server");			// 이미지서버 IP 조회
		    	szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID312.split("/")[1]);		// 이미지서버 URL 조회
			} catch (Exception e) {
				paramVO.setResultCode("31000000");
			}
		    
		    tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
		    
		    
		    // 오늘 날짜/시간 조회
		    String current_date = "";
		    try {
		    	current_date = commonService.getSysdate();
			} catch (Exception e) {
				imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
				throw new ImcsException();
			}
		    paramVO.setCurrentDate(current_date);
		    
		    
		    // 받은/보낸 선물함 조회
		    List<GetNSPresentResponseVO> lstPresentList =  this.getNSPresentList(paramVO);
		    GetNSPresentResponseVO presentList = new GetNSPresentResponseVO();
		    
	    	if( "G".equals(paramVO.getPresentGb()) )   	msg	= "받은 선물함 조회";
	    	else 										msg	= "보낸 선물함 조회";
	    	
	    	tp2	= System.currentTimeMillis();
	    	imcsLog.timeLog(msg, String.valueOf(tp2 - tp1), methodName, methodLine); 
	    	
	    	
	    	if(lstPresentList != null){
	    		nMainCnt = lstPresentList.size();
	    	}
	    	
	    	
	    	for(int i = 0; i < nMainCnt; i++){
	    		presentList = lstPresentList.get(i);
	    		
	    		if("".equals(presentList.getRealPresentPrice())){
	    			
	    			presentList.setRealPresentPrice( Integer.toString( Integer.parseInt(presentList.getPresentPrice()) + ( Integer.parseInt(presentList.getPresentPrice()) *10/100 ) ) );
	    			
	    		}
	    		
	    		presentList.setImgUrl(szImgSvrUrl);
	    		
	    		//통계 로그용
	    		if( i == nMainCnt - 1 && !"".equals(presentList.getContsName()) ){
	    			resultVO.setContentsId(presentList.getAlbumId());
	    			resultVO.setContentsName(presentList.getContsName());
	    		}
	    		
	    		// 선물 상세 정보 조회
	    		paramVO.setAlbumId(presentList.getAlbumId());
    			GetNSPresentDtlVO presentDtlVO = this.getNSPresentDtl(paramVO);
    			
    			presentList.setSeriesNo(presentDtlVO.getSeriesNo());
    			presentList.setHdcontent(presentDtlVO.getHdcontent());
    			presentList.setPrInfo(presentDtlVO.getPrInfo());
    			
    			// HD 여부 저장
    			if("Y".equals(presentDtlVO.getHdcontent()) || "S".equals(presentDtlVO.getHdcontent()) ) {
    				presentList.setIsHd("Y");
    			}else if( "N".equals(presentDtlVO.getHdcontent()) ){
    				presentList.setIsHd("N");
    			}
    			
    			// 카테고리 ID 리스트 조회
    			String szCatId	= "";
    			szCatId	= this.getNSCatId(paramVO);
	    		presentList.setCatIdArr(szCatId);
    			
    			
    			// 이미지 파일명 조회
    			String szStillImage	= "";
    			szStillImage = this.getStillImage(paramVO);
    			presentList.setImgFileName(szStillImage);
    			
    			lstPresentList.set(i, presentList);
	    	}
	    	
	    	tp3	= System.currentTimeMillis();
			imcsLog.timeLog("선물함 정보 Fetch", String.valueOf(tp3 - tp2), methodName, methodLine); 
		   	    		    
		    resultVO.setList(lstPresentList);
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());

			throw new ImcsException();
		} finally {			
			resultVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID312) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	
	/**
	 * 받은/보낸 선물함 조회
	 * @param	GetNSPresentRequestVO
	 * @result	List<GetNSPresentResponseVO>
	 */
    public List<GetNSPresentResponseVO> getNSPresentList(GetNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName	= oStackTrace.getMethodName();
    	
		String sqlId	= "";
		int querySize = 0;
		
		if("A".equals(paramVO.getPageNo()) || "A".equals(paramVO.getPageCnt())){
	    	if("G".equals(paramVO.getPresentGb())) 		sqlId = "lgvod312_001_20171214_001";	// 받은 선물함 조회, 페이징X
	    	else								   		sqlId = "lgvod312_011_20171214_001";	// 보낸 선물함 조회, 페이징X
	    } else {
	    	if("G".equals(paramVO.getPresentGb()))		sqlId = "lgvod312_002_20171214_001";	// 받은 선물함 조회, 페이징O
	    	else										sqlId = "lgvod312_012_20171214_001";	// 보낸 선물함 조회, 페이징O
	    }
		
		List<GetNSPresentResponseVO> list   = new ArrayList<GetNSPresentResponseVO>();
		
		try {
			try{
				list = getNSPresentDao.getNSPresent(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				
				paramVO.setResultCode("21000000");
				//imcsLog.failLog(ImcsConstants.API_PRO_ID312, sqlId, null, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				querySize = list.size();
			}
						
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID312, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID312, sqlId, null, "favor_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return list;
    }
    
    
    
    /**
     * 선물 상세 정보 조회
     * @param paramVO
     * @return GetNSPresentDtlVO
     * @throws Exception
     */
    public GetNSPresentDtlVO getNSPresentDtl(GetNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod312_003_20171214_001";
		
		List<GetNSPresentDtlVO> list   = new ArrayList<GetNSPresentDtlVO>();
		GetNSPresentDtlVO resultVO = new GetNSPresentDtlVO();
		
		try {
			try{
				list = getNSPresentDao.getNSPresentDtl(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO	= list.get(0);
			}
			
		} catch (Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID312, sqlId, cache, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return resultVO;
    }
    
    
    /**
     * 카테고리 ID 리스트 조회
     * @param	GetNSPresentRequestVO
     * @result	String
     */
    public String getNSCatId(GetNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId	= "lgvod312_004_20171214_001";
    	String szCatId	= "";
    	
		int querySize = 0;
		
		List<String> list   = new ArrayList<String>();
		
		try {
			try{
				list = getNSPresentDao.getNSCatId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			

			if( list == null ||list.isEmpty()){
				querySize = 0;
			} else {
				querySize = list.size();
				
				for(int i = 0; i < querySize; i++) {
					if(i == 0)	szCatId	= StringUtil.nullToSpace(list.get(i));	
					else		szCatId	= szCatId + "\b" + StringUtil.nullToSpace(list.get(i));
				}
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return szCatId;
    }
    
    
    
    
    /**
     * 이미지 파일명 조회
     * @param
     * @result
     */
    public String getStillImage(GetNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId 		=  "lgvod312_005_20171214_001";
		String szImgName	= "";
		
		List<String> list   = new ArrayList<String>();
		
		try {
			try{
				list = getNSPresentDao.getStillImage(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szImgName	= list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return szImgName;
    }

}
