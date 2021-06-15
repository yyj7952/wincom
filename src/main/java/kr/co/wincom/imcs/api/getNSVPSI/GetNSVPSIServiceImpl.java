package kr.co.wincom.imcs.api.getNSVPSI;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import kr.co.wincom.imcs.api.getNSVSI.GetNSMakeVSIService;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GetNSVPSIServiceImpl implements GetNSVPSIService {
	private Log imcsLogger = LogFactory.getLog("API_getNSVPSI");
	
	@Autowired
	private GetNSVPSIDao getNSVPSIDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSMakeVPSIService getNSMakeVPSIService;
	
//	public void getNSVPSI(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSVPSIResultVO getNSVPSI(GetNSVPSIRequestVO paramVO){
//		this.getNSVPSI(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSVPSIResultVO resultListVO = new GetNSVPSIResultVO();
		GetNSVPSIResponseVO tempVO = new GetNSVPSIResponseVO();
		List<GetNSVPSIResponseVO> resultVO = new ArrayList<GetNSVPSIResponseVO>();
		
		String LOCKFILE = "";
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID031.split("/")[1], imcsLog);
		
		int nMainCnt = 0;
		int nWaitCnt = 0;
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		
		String szThmImgSvrIp	= "";		// 썸네일 이미지 서버 IP
		String szImgSvrUrl		= "";		// 이미지 서버 URL
		String szStilImgSvrUrl	= "";		// 스틸 이미지 서버 URL
		String VirtualChFlag	= "";		// 가상채널 여부
		
		try {
			VirtualChFlag	= commonService.getVCFlag(ImcsConstants.API_PRO_ID031.split("/")[1]);	// 가상채널 여부
		} catch (Exception e) {
			System.out.println("GetConfigInfo fail");
		}
		
		try {
			szThmImgSvrIp	= commonService.getImgReplaceUrl2("snap_server", "getNSVPSI");
			szImgSvrUrl	= commonService.getImgReplaceUrl2("img_resize_server", "getNSVPSI");
			szStilImgSvrUrl	= commonService.getImgReplaceUrl2("img_still_server", "getNSVPSI");
		} catch(Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID031, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}

	    
		List<StillImageVO> lstImageInfo	= null;		// 이미지 파일명 조회 VO
		StillImageVO imageVO = null;
		GetNSVPSIResponseVO lstPrinfo	= null;		// 가상채널정보 VO
		
		try{
						
			//테스트 가입자 여부 조회
			this.getTestSbc(paramVO);
			
			tp1	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			String compFileName = "_t"+paramVO.getTestSbc()+"_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_t"+paramVO.getTcOut()
			+"_p"+paramVO.getPooqYn()+"_v"+paramVO.getHdtvViewGb()+"_s"+paramVO.getServiceId()+"_d"+paramVO.getEpgSdate()+"_e"+paramVO.getEpgEdate() + ".res";
			
			//하위 폴더 경로 생성
			String dirName = String.format("%s/t%sn%sy%sp%sv%s", LOCALPATH, paramVO.getTestSbc(), paramVO.getNscType(), paramVO.getYouthYn(), 
					paramVO.getPooqYn(), paramVO.getHdtvViewGb());
			LOCALPATH = dirName;
			File fLOCALPATH = new File(LOCALPATH);
			//System.out.println("####### dirName ########: " + LOCALPATH);
			
			String chkFileName = "";
			
			File[] files = fLOCALPATH.listFiles();
			
			List<String> list = new ArrayList<String>();	
			try
			{
				if(files.length > 0){			
					for(int i = 0; i < files.length; i++){
						if(files[i].getName().toString().indexOf(compFileName) > -1){
							msg = files[i].getName();						
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							list.add(files[i].getName());
						}
					}
					if(list != null && list.size() > 0){
						Collections.sort(list, new Comparator<String>(){
							public int compare(String obj1, String obj2){
								return obj1.compareToIgnoreCase(obj2);
							}
						});
						
						Collections.reverse(list);
						
						chkFileName = list.get(0);
					}else{
						chkFileName = "1";
					}
				}else{
					
					chkFileName = "1";
				}
			}catch(NullPointerException e)
			{
				msg = " getNSVPSI Cache File Empty";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			if(list.size() > 4){
				msg = " [WARN] getNSVPSI res file count [" + list.size() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			if(commonService.chkCacheFile(chkFileName, imcsLog) || list.size() == 0){
				//System.out.println("getNSMakeVPSIs로 진입!!");
				msg = "getNSMakeVPSIs excute";
				imcsLog.serviceLog(msg, methodName, methodLine);
				getNSMakeVPSIService.getNSMakeVPSIs(paramVO);
			}
			
			if(!chkFileName.equals("1")){
				String lockFileName = "";
				String[] arrlockFileName = list.get(0).split("\\.");
				
				for(int i = 0; i < arrlockFileName.length -1; i++){
					lockFileName += arrlockFileName[i];
				}
				
				RESFILE = LOCALPATH + "/" + list.get(0);
				
				LOCKFILE = LOCALPATH + "/" + lockFileName + ".lock";
				
				File lock = new File(LOCKFILE);
				
				if(lock.exists()){
					msg = " [WARN] File [" + list.get(1) + "] 이전 cache return ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					RESFILE = LOCALPATH + "/" + list.get(1);
					
				}
				
				File res = new File(RESFILE);
				
				if(res.exists()) {
					String result = FileUtil.fileRead(RESFILE, "UTF-8");
					
					if(!"".equals(result)) {
						String[] arrResult	= result.split(ImcsConstants.ROWSEP);
						
						result	= "";
						
						for(int i = 0; i < arrResult.length; i++) {
							arrResult[i] = arrResult[i].replaceAll("snap_server", szThmImgSvrIp);	
							arrResult[i] = arrResult[i].replaceAll("img_resize_server", szImgSvrUrl);		
							arrResult[i] = arrResult[i].replaceAll("img_still_server", szStilImgSvrUrl);		
							result	= result + arrResult[i] + ImcsConstants.ROWSEP;
						}
						
						msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);

						resultListVO.setResult(result);
//						FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
						
					}else{
						msg = " File [" + RESFILE + "] checkResFile Failed ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						res.delete();
					}	
				}else{
					msg = " File [" + RESFILE + "] open Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}
					
			
			/*String szThmImgSvrIp	= "";		// 썸네일 이미지 서버 IP
			String szImgSvrUrl		= "";		// 이미지 서버 URL
			String szStilImgSvrUrl	= "";		// 스틸 이미지 서버 URL
			String VirtualChFlag	= "";		// 가상채널 여부
			
			try {
				VirtualChFlag	= commonService.getVCFlag(ImcsConstants.API_PRO_ID031.split("/")[1]);	// 가상채널 여부
			} catch (Exception e) {
				System.out.println("GetConfigInfo fail");
			}
			
			paramVO.setVirtualChFlag(VirtualChFlag);
			
		    		    		    
		    try {
		    	szThmImgSvrIp	= commonService.getIpInfo("snap_server", ImcsConstants.API_PRO_ID031.split("/")[1]);		// 썸네일 이미지 조회
		    	szImgSvrUrl		= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID031.split("/")[1]);	// 이미지서버 URL 조회
		    	szStilImgSvrUrl	= commonService.getIpInfo("img_still_server", ImcsConstants.API_PRO_ID031.split("/")[1]);	// 스틸 이미지서버 URL 조회
		    	
			} catch (Exception e) {
				imcsLog.failLog(ImcsConstants.API_PRO_ID031, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}
			
			//stamp_today	// 사용안함
		    //c_current		// 사용안함
		    						
			LOCKFILE	= LOCALPATH + "/getNSVPSI_t"+paramVO.getTestSbc()+"_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_t"+paramVO.getTcOut()
					+"_p"+paramVO.getPooqYn()+"_v"+paramVO.getHdtvViewGb()+"_s"+paramVO.getServiceId()+"_d"+paramVO.getEpgSdate()+"_e"+paramVO.getEpgEdate()+".lock";
			RESFILE		= LOCALPATH + "/getNSVPSI_t"+paramVO.getTestSbc()+"_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_t"+paramVO.getTcOut()
					+"_p"+paramVO.getPooqYn()+"_v"+paramVO.getHdtvViewGb()+"_s"+paramVO.getServiceId()+"_d"+paramVO.getEpgSdate()+"_e"+paramVO.getEpgEdate()+".res";
			
			File res = new File(RESFILE);
			
			// 파일이 존재하면 읽고 아니면 lock 파일 로직 실행
			if(res.exists()) {
				String result = FileUtil.fileRead(RESFILE, "UTF-8");
				
				if(!"".equals(result)) {
					msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				resultListVO.setResult(result);
				FileUtil.unlock(LOCKFILE, imcsLog);
				return resultListVO;		
			} else {
				msg = " File [" + RESFILE + "] open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				while(FileUtil.lock(LOCKFILE, imcsLog)){
					Thread.sleep(1000);
					nWaitCnt++;
					
					msg = " queryWaitCheck Sleep [" + nWaitCnt + "] sec";
					imcsLog.serviceLog(msg, methodName, methodLine);
	
					if(nWaitCnt >= 5){
						msg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID031 + "] sts[    0] msg["+ 
								String.format("%-17s", "par_yn:" + ImcsConstants.RCV_MSG2 +"]");
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						paramVO.setResultCode("21000000");
						throw new ImcsException();
					}
					
					if(res.exists()) {
						String result = FileUtil.fileRead(RESFILE, "UTF-8");
						
						if(!"".equals(result)) {
							msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
						
						resultListVO.setResult(result);
						FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
					}
				}
			}
			
			//Nscreen 가상채널 EPG전체 스케줄정보 조회
			resultVO = this.getNSVPSIList(paramVO);

			if(resultVO != null && !resultVO.isEmpty()){
				nMainCnt = resultVO.size();
			}
			
			tp2	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("스케줄 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);

			String szTempVirtualId	= "";
			String szImgFileName	= "";
			
			for(int i = 0; i < nMainCnt; i++) {
				tempVO = resultVO.get(i);
				
				// VIRTUAL_ID 가 M으로 시작하면
				if(!"".equals(tempVO.getVirtualId()) && tempVO.getVirtualId().length() > 0)
					szTempVirtualId	= tempVO.getVirtualId().substring(0, 1);
				else
					szTempVirtualId	= "";
				
				
				if( "M".equals(szTempVirtualId) ){
					paramVO.setVirtualId(tempVO.getVirtualId());
					
					// 이미지 정보 조회
					lstImageInfo = this.getImgUrl(paramVO);
					
					if(lstImageInfo != null && !lstImageInfo.isEmpty()){
						imageVO = lstImageInfo.get(0);

						//tempVO.setImgUrl(imageVO.getImgUrl());
						tempVO.setImgFileName(imageVO.getImgFileName());
						
						// 2017.03.15 - 이미지 NAS 이중화하면서 PT_VO_IMGSRV_LIST 테이블 외에 다른 테이블에서 조회하던 URL 정보를 PT_VO_IMGSRV_LIST 테이블로 통일 시킴
						tempVO.setImgUrl(szStilImgSvrUrl);
						
					} else {
						tempVO.setImgUrl(szImgSvrUrl);
						
						// 이미지 파일 정보 조회
						szImgFileName = this.getImgFileName(paramVO);
						
						if(!"".equals(szImgFileName) && szImgFileName.length() > 0){
							tempVO.setImgFileName(szImgFileName);
						}
						
					}
					
					
					// 가상 채널 정보 조회
					lstPrinfo = this.getPrInfo(paramVO);
					
					if(lstPrinfo != null){
						tempVO.setPrInfo(lstPrinfo.getPrInfo());
						tempVO.setRuntime(lstPrinfo.getRuntime());
					}
					
					
				}else{
					tempVO.setImgUrl(szThmImgSvrIp);
				}
				
				if("N".equals(tempVO.getImageYn())){
					tempVO.setImgUrl("");
					tempVO.setImgFileName("");
				}
				
				resultVO.set(i, tempVO);
			}
			
			tp3	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("스케줄 정보 Fetch", String.valueOf((tp3 - tp2)), methodName, methodLine);
			resultListVO.setList(resultVO);

			
			// 파일 쓰기
			int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
			
			if(nRetVal == 1) {
				msg = "File [" + RESFILE + "] WRITE [" + res.length() + "] bytes Finished";
				imcsLog.serviceLog(msg, methodName, methodLine);
			} else {
				msg = "File [" + RESFILE + "] WRITE failed";
				imcsLog.serviceLog(msg, methodName, methodLine);		
			}
			
			FileUtil.unlock(LOCKFILE, imcsLog);
			
			// 파일이 존재하면 읽고 아니면 lock 파일 로직 실행
			if(res.exists()) {
				String result = FileUtil.fileRead(RESFILE, "UTF-8");
				
				if(!"".equals(result)) {
					msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				resultListVO.setResult(result);
				FileUtil.unlock(LOCKFILE, imcsLog);
				return resultListVO;		
			} else {
				msg = " File [" + RESFILE + "] open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}*/
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , ie.getClass().getName() + ":" +  ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , e.getClass().getName() + ":" +  e.getMessage());
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID031) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID031) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 테스트 가입자 여부 조회
	 * @param paramVO
	 * @return
	 */
    public void getTestSbc(GetNSVPSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod031_001_20171214_001";
		
		List<String> list   = null;
				
		try {
			try{
				list = getNSVPSIDao.getTestSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if( list == null || list.isEmpty()) {
				paramVO.setTestSbc("N");				
			} else {
				paramVO.setTestSbc(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			paramVO.setTestSbc("N");
		}
    }
    
    
    
    /**
	 * Nscreen 가상채널 EPG전체 스케줄정보 조회
	 * @param paramVO
	 * @return
	 */
    public List<GetNSVPSIResponseVO> getNSVPSIList(GetNSVPSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();

		List<GetNSVPSIResponseVO> rtnList = null;
		try {
			
			// 쿼리 자체가 너무 복잡하여 3가지 케이스로 나눔
			if("A".equals(paramVO.getEpgSdate()) && "A".equals(paramVO.getEpgEdate())) {		// 편성스케줄 시작/종료 일자 (YYYYMMDD)
				if("A".equals(paramVO.getTcIn()) && "A".equals(paramVO.getTcOut())) {			// 편성스케줄 시작/종료 시간 (24시간)
					rtnList = getNSVPSIDao.getNSVPSIList1(paramVO);		// NO_PAGING (5일까지 데이터를 모두 조회)
				} else {
					rtnList = getNSVPSIDao.getNSVPSIList2(paramVO);
				}
			} else {
				rtnList = getNSVPSIDao.getNSVPSIList3(paramVO);
			}
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
		} catch (Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID031, "", null, "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			paramVO.setResultCode("40000000");
			
			//imcsLog.errorLog(ImcsConstants.API_PRO_ID031, "ss", "aa", "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			
			throw new ImcsException();
		}
		
		if(rtnList == null || rtnList.isEmpty()){
			//imcsLog.failLog(ImcsConstants.API_PRO_ID031, "", null, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			paramVO.setResultCode("21000000");
		}
		
    	return rtnList;
    }
       
    
    
    /**
	 * 이미지 정보 조회
	 * @param paramVO,vo
	 * @return
	 */
    public List<StillImageVO> getImgUrl(GetNSVPSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod031_003_20171214_001";
		
		List<StillImageVO> list   = new ArrayList<StillImageVO>();
		
		try {
			try{
				list = getNSVPSIDao.getImgUrl(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
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
    
    
    /**
   	 * 이미지 파일 정보 조회
   	 * @param paramVO
   	 * @return
   	 */
    public String getImgFileName(GetNSVPSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId =  "lgvod031_004_20171214_001";
		String szImgFileName = "";
		
		List<String> list   = null;
		
		try {
			try{
				list = getNSVPSIDao.getImgFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szImgFileName	= StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return szImgFileName;
    }
    
    
    
    /**
   	 * 가상 채널 정보 조회
   	 * @param paramVO,vo
   	 * @return
   	 */
    public GetNSVPSIResponseVO getPrInfo(GetNSVPSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod031_005_20171214_001";

		List<GetNSVPSIResponseVO> list   = null;
		GetNSVPSIResponseVO resultVO = null;
		
		try {
			try{
				list = getNSVPSIDao.getPrInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO  = list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return resultVO;
    }
    
}
