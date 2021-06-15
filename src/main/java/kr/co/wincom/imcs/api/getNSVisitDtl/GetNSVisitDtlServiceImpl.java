package kr.co.wincom.imcs.api.getNSVisitDtl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSVisitDtlServiceImpl implements GetNSVisitDtlService {
	private Log imcsLogger = LogFactory.getLog("API_getNSVisitDtl");
	
	@Autowired
	private GetNSVisitDtlDao getNSVisitDtlDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSVisitDtl(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSVisitDtlResultVO getNSVisitDtl(GetNSVisitDtlRequestVO paramVO){
//		this.getNSVisitDtl(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];		
		String methodName = stackTraceElement.getMethodName();
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + "service call");
		
		GetNSVisitDtlResultVO resultListVO	= new GetNSVisitDtlResultVO();
		GetNSVisitDtlResponseVO tempVO	= new GetNSVisitDtlResponseVO();
		List<GetNSVisitDtlResponseVO> resultVO	= new ArrayList<GetNSVisitDtlResponseVO>();
		
	    String msg		= "";
	    
	    int nMainCnt = 0;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
	    
		try{
			String szImgSvrIp		= "";		// 이미지 서버 IP
		    String szImgCasheSvrIp	= "";		// 이미지 캐쉬서버 IP
		    String szImgResizeSvrIp	= "";		// 이미지 리사이즈서버 IP
		    
		    try {
		    	szImgSvrIp			= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID984.split("/")[1]);			// 이미지서버 IP 조회
		    	szImgCasheSvrIp		= commonService.getIpInfo("img_cachensc_server", ImcsConstants.API_PRO_ID984.split("/")[1]);	// 이미지 캐쉬서버 IP 조회
		    	szImgResizeSvrIp	= commonService.getIpInfo("img_create_server", ImcsConstants.API_PRO_ID984.split("/")[1]);		// 이미지 리사이즈 IP 조회
		    	
		    	tp1	= System.currentTimeMillis();
				imcsLog.timeLog("서버IP값 조회", String.valueOf(tp1 - tp_start), methodName, methodLine);
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID984, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}
		    
		   
		    if(paramVO.getCatId().trim().length() != 0){
		    	// 컨텐츠 정보 조회
		    	List<GetNSVisitDtlResponseVO> getNSVisitDtl =  this.getNSVisitDtlList(paramVO);
		    	
		    	tp2	= System.currentTimeMillis();
				imcsLog.timeLog("컨텐츠정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		    	
		    	if(getNSVisitDtl == null || getNSVisitDtl.size() == 0 || getNSVisitDtl.isEmpty()) {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID984, "", null, "cont_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
					paramVO.setResultCode("21000000");
				} else{
					nMainCnt	= getNSVisitDtl.size();
				}
		    	    	
		    	for(int i = 0; i < nMainCnt; i++){
		    		tempVO = getNSVisitDtl.get(i);
		    	
		    		String szImgFileName	= StringUtil.nullToSpace(tempVO.getImgFileName());
		    		
		    		tempVO.setImgUrl(szImgSvrIp + "\b" + szImgCasheSvrIp + "\b" + szImgResizeSvrIp);
		    		tempVO.setImgFileName(szImgFileName + "\b" + szImgFileName + "\b" + szImgFileName);
		    		tempVO.setDownYn("N");
	    			
		    		FmInfoVO getFminfo = this.getFminfo(paramVO);
	    			
	    			if(getFminfo != null){
	    				// paramVO.setFmYn(getFminfo.getFmYN());			// 사용안함
	    				// paramVO.setFmAssetId(getFminfo.getAdiProdId());	// 사용안함
	    				if(Integer.parseInt(StringUtil.nullToZero(getFminfo.getDownCnt())) > 0)
	    					tempVO.setDownYn("Y");
	    			}
	    			
	    			// i_cnk > 0 로직 필요 없어 제외
	    			
	    			// 트레일러 정보 조회
	    			List<ComTrailerVO> lTrilerInfo = new ArrayList<ComTrailerVO>();
	    			ComTrailerVO trilerVO = new ComTrailerVO();
	    			
	    			lTrilerInfo = this.getTrailerInfo(paramVO);
    				
    				if(lTrilerInfo != null && lTrilerInfo.size() > 0){
    					trilerVO = lTrilerInfo.get(0);
    					
    					tempVO.setVodServer1(trilerVO.getTrailerUrl1());
    					tempVO.setVodServer2(trilerVO.getTrailerUrl2());
    					tempVO.setVodServer3(trilerVO.getTrailerUrl3());
    					tempVO.setVodFileName1(trilerVO.getTrailerFileName1());
    					tempVO.setVodFileName2(trilerVO.getTrailerFileName1());
    					tempVO.setVodFileName3(trilerVO.getTrailerFileName1());
    					
    					// tempVO.setContentsId(trilerVO.getContentsId());		// 사용안함
    					// tempVO.setContentsName(trilerVO.getContentsName());	// 사용안함
	    			}	    			
	    			
    				
    				
	    			// 스틸이미지 정보 조회
	    			List<StillImageVO> lStillImage = new ArrayList<StillImageVO>();
	    			StillImageVO stillImageVO = new StillImageVO();
	    			String szStillFileName	= "";
	    			
	    			lStillImage = this.getStillImage(paramVO);
    				
    				if(lStillImage != null) {
	    				for(int j = 0; j < lStillImage.size(); j++){
	    					stillImageVO = lStillImage.get(j);
		    				
		    				if("N".equals(stillImageVO.getImgFlag())){		// 쿼리가 N으로 조회하는데 이 로직이 필요 있나?
		    					if(j == 0)	szStillFileName = stillImageVO.getImgFileName();
		    					else		szStillFileName = szStillFileName +"\b"+ stillImageVO.getImgFileName();
		    				}
	    				}
    				}
	    			
    				tempVO.setStillFileName(szStillFileName);
    				
    				String dataFreeBillyn = "";
    				
    				dataFreeBillyn = this.getDataFreeBillYn(paramVO);
    				
    				tempVO.setDataFreeBillFlag(dataFreeBillyn);
    				
    				resultVO.add(tempVO);
		    	}
		    }
		    resultListVO.setList(resultVO);
		    
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;

			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			ie.setList(resultListVO);
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID984) + "] result[" + String.format("%-5s", tempVO.toString()) + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			//msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID984) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]" + " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
			//imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
    
	/**
	 *  컨텐츠정보 조회
	 *  @param 	GetNSVisitDtlRequestVO
	 *  @result	List<GetNSVisitDtlResponseVO>
	 */
    public List<GetNSVisitDtlResponseVO> getNSVisitDtlList(GetNSVisitDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod984_001_20171214_001";
    	
		int querySize = 0;
		
		List<GetNSVisitDtlResponseVO> list   = new ArrayList<GetNSVisitDtlResponseVO>();
		
		try {
			try{
				list = getNSVisitDtlDao.getNSVisitDtl(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			
			}

			if( list == null || list.isEmpty()){
				querySize = 0;
				//imcsLog.failLog(ImcsConstants.API_PRO_ID984, "", cache, "cont_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				querySize = list.size();
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID984, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID984, sqlId, cache, "conts_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return list;
    }
    
    
    
    /** 
     *  트레일러 정보 조회
     *  @param	GetNSVisitDtlRequestVO
     *  @result	List<TrilerVO>
     */
    public List<ComTrailerVO> getTrailerInfo(GetNSVisitDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod984_004_20171214_001";
    	
		List<ComTrailerVO> list = new ArrayList<ComTrailerVO>();
		
		try {
			try{
				list = getNSVisitDtlDao.getTrailerInfo(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID984, sqlId, cache, "triler_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
		}
		
    	return list;
    }
    
    
    
    
    /**
     *  스틸이미지 정보 조회
     *  @param	GetNSVisitDtlRequestVO
     *  @result	List<StillImageVO>
     */
    public List<StillImageVO> getStillImage(GetNSVisitDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod984_005_20171214_001";
    	
		List<StillImageVO> list   = new ArrayList<StillImageVO>();
		
		try {
			try{
				list = getNSVisitDtlDao.getStillImage(paramVO);
			}catch(DataAccessException e){
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID984, sqlId, cache, "still_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
		}
		
    	return list;
    }
    
    
    
    
    /**
     *  Face-Match 준비여부 조회
     * 	GetNSVisitDtlRequestVO
     *  FmInfoVO
     */
    public FmInfoVO getFminfo(GetNSVisitDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod984_003_20171214_001";
		int querySize = 0;
		
		List<FmInfoVO> list	= new ArrayList<FmInfoVO>();
		FmInfoVO resultVO = new FmInfoVO();
		
		try {
			try{
				list = getNSVisitDtlDao.getFminfo(paramVO);
			} catch(DataAccessException e) {
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				querySize = 0;
				resultVO = new FmInfoVO();
			} else {
				querySize = list.size();
				resultVO = (FmInfoVO)list.get(0);
			}
						
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID984, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());			
		}
		
    	return resultVO;
    }
    
    /**
     *  데이터 프리 유/무료 여부 조회
     *  @param	GetNSVisitDtlRequestVO
     *  @result	String
     */
    public String getDataFreeBillYn(GetNSVisitDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod984_006_20171214_001";
    	
		List<String> list   = new ArrayList<String>();
		
		String dataFreeBill = "";
		
		try {
			try{
				list = getNSVisitDtlDao.getDataFreeBillYn(paramVO);
			}catch(DataAccessException e){
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if( list != null && !list.isEmpty()){
				dataFreeBill	= list.get(0);
			}else{
				dataFreeBill = "Y";
			}
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID984, sqlId, cache, "datafreebillyn_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
		}
		
    	return dataFreeBill;
    }
}
