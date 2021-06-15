package kr.co.wincom.imcs.api.getNSChPGM;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GetNSChPGMServiceImpl implements GetNSChPGMService {
	private Log imcsLogger = LogFactory.getLog("API_getNSChPGM");
	
	@Autowired
	private GetNSChPGMDao getNSChPGMDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSMakeChPGMService getNSMakeChPGMService;
	
//	public void getNSChPGM(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSChPGMResultVO getNSChPGM(GetNSChPGMRequestVO paramVO){
//		this.getNSChPGM(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		GetNSChPGMResultVO resultListVO = new GetNSChPGMResultVO();
		GetNSChPGMResponseVO tempVO = new GetNSChPGMResponseVO();
		List<GetNSChPGMResponseVO> resultVO = new ArrayList<GetNSChPGMResponseVO>();
		
		String LOCKFILE = "";
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID021.split("/")[1], imcsLog);
		
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
			VirtualChFlag	= commonService.getVCFlag(ImcsConstants.NSAPI_PRO_ID021.split("/")[1]);	// 가상채널 여부
		} catch (Exception e) {
			System.out.println("GetConfigInfo fail");
		}
		
		try {
			szThmImgSvrIp	= commonService.getImgReplaceUrl2("snap_server","getNSChPGM");
			szImgSvrUrl	= commonService.getImgReplaceUrl2("img_resize_server","getNSChPGM");
			szStilImgSvrUrl	= commonService.getImgReplaceUrl2("img_still_server","getNSChPGM");
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID021, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}

	    
		List<StillImageVO> lstImageInfo	= null;		// 이미지 파일명 조회 VO
		StillImageVO imageVO = null;
		GetNSChPGMResponseVO lstPrinfo	= null;		// 가상채널정보 VO
		
		try{
					
			//테스트 가입자 여부 조회
			this.getTestSbc(paramVO);
			
			tp1	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			String compFileName = "_3"+paramVO.getRequestTime()+"_4"+paramVO.getHdtvViewGb()+"_5"+paramVO.getPooqYn()+
			                      "_6"+paramVO.getCallFlag()+"_T"+paramVO.getTestSbc() + ".res";
			
			//하위 폴더 경로 생성
			String dirName = String.format("%s/4%s5%s6%sT%s", LOCALPATH, 
					paramVO.getHdtvViewGb(), paramVO.getPooqYn(), paramVO.getCallFlag(), paramVO.getTestSbc());
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
				msg = " getNSChPGM Cache File Empty";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			if(list.size() > 4){
				msg = " [WARN] getNSChPGM res file count [" + list.size() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			if(commonService.chkCacheFile(chkFileName, imcsLog) || list.size() == 0){
				
				msg = "getNSMakeVSIs excute";
				imcsLog.serviceLog(msg, methodName, methodLine);
				getNSMakeChPGMService.getNSMakeChPGMs(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				
				if (list.size() == 0) {//20210202 응답값 미전달 현상 개선
					chkFileName = "";
					DateFormat df = new SimpleDateFormat("yyyyMMddHH");
					Date date = df.parse(paramVO.getRequestTime());
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					String rTime = "";
					
					if(paramVO.getHdtvViewGb().equals("G")) {
						cal.add(Calendar.HOUR_OF_DAY, -24);
						rTime = df.format(cal.getTime());
					} else if (paramVO.getHdtvViewGb().equals("M")) {
						cal.add(Calendar.HOUR_OF_DAY, -3);
						rTime = df.format(cal.getTime());
					} else {
						cal.add(Calendar.HOUR_OF_DAY, -1);
						rTime = df.format(cal.getTime());
					}
					compFileName = "_3"+rTime+"_4"+paramVO.getHdtvViewGb()+"_5"+paramVO.getPooqYn()+
		                      "_6"+paramVO.getCallFlag()+"_T"+paramVO.getTestSbc() + ".res";

					list = new ArrayList<String>();	
					try
					{
						if(files.length > 0){			
							for(int i = 0; i < files.length; i++){
								if(files[i].getName().toString().indexOf(compFileName) > -1){
									msg = " 이전 파일 찾기 : " + files[i].getName();						
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
						msg = " getNSChPGM Cache File Empty";
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					
					if (list.size() == 0) {
						String result	= "1|CACHE FILE OPEN FAIL|";
						resultListVO.setResultHeader(result);
						return resultListVO;
					}
					
				}

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
					
					//결과 헤더 붙이기
					String resultHeader  = String.format("%s|%s|", "1", "CACHE FILE OPEN FAIL");
					resultListVO.setResultHeader(resultHeader);
				}
			} else {
				//결과 헤더 붙이기
				String resultHeader  = String.format("%s|%s|", "1", "CACHE FILE OPEN FAIL");
				resultListVO.setResultHeader(resultHeader);
			}
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , ie.getClass().getName() + ":" +  ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , e.getClass().getName() + ":" +  e.getMessage());
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			

			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID021) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
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
    public void getTestSbc(GetNSChPGMRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<String> list   = null;
				
		try {
			
			list = getNSChPGMDao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
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
    public List<GetNSChPGMResponseVO> getNSChPGMList(GetNSChPGMRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();

		List<GetNSChPGMResponseVO> rtnList = null;
		try {
			
			// 쿼리 자체가 너무 복잡하여 3가지 케이스로 나눔
//			if("A".equals(paramVO.getEpgSdate()) && "A".equals(paramVO.getEpgEdate())) {		// 편성스케줄 시작/종료 일자 (YYYYMMDD)
//				if("A".equals(paramVO.getTcIn()) && "A".equals(paramVO.getTcOut())) {			// 편성스케줄 시작/종료 시간 (24시간)
//					rtnList = getNSChPGMDao.getNSChPGMList1(paramVO);		// NO_PAGING (5일까지 데이터를 모두 조회)
//				} else {
//					rtnList = getNSChPGMDao.getNSChPGMList2(paramVO);
//				}
//			} else {
//				rtnList = getNSChPGMDao.getNSChPGMList3(paramVO);
//			}
			rtnList = getNSChPGMDao.getNSChPGMList(paramVO);
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID021, "", null, "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			paramVO.setResultCode("40000000");
			
			//imcsLog.errorLog(ImcsConstants.NSAPI_PRO_ID021, "ss", "aa", "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			
			throw new ImcsException();
		}
		
		if(rtnList == null || rtnList.isEmpty()){
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID021, "", null, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			paramVO.setResultCode("21000000");
		}
		
    	return rtnList;
    }
       
    
    
    /**
	 * 이미지 정보 조회
	 * @param paramVO,vo
	 * @return
	 */
    public List<StillImageVO> getImgUrl(GetNSChPGMRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod031_003_20171214_001";
		
		List<StillImageVO> list   = new ArrayList<StillImageVO>();
		
		try {
			try{
				list = getNSChPGMDao.getImgUrl(paramVO);
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
    public String getImgFileName(GetNSChPGMRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId =  "lgvod031_004_20171214_001";
		String szImgFileName = "";
		
		List<String> list   = null;
		
		try {
			
			try{
				list = getNSChPGMDao.getImgFileName(paramVO);
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
    public GetNSChPGMResponseVO getPrInfo(GetNSChPGMRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod031_005_20171214_001";

		List<GetNSChPGMResponseVO> list   = null;
		GetNSChPGMResponseVO resultVO = null;
		
		try {
			try{
				list = getNSChPGMDao.getPrInfo(paramVO);
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
