package kr.co.wincom.imcs.api.getNSVPSI;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Async
public class GetNSMakeVPSIServiceImpl implements GetNSMakeVPSIService {
	private Log imcsLogger = LogFactory.getLog("API_getNSVPSI");
	
	@Autowired
	private GetNSVPSIDao getNSVPSIDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSMakeVPSIs(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;	
	
	private String apiInfo	= "";
	
	@Override
	public GetNSVPSIResultVO getNSMakeVPSIs(GetNSVPSIRequestVO paramVO)	{
//		this.getNSMakeVPSIs(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		GetNSVPSIResultVO	resultListVO	= new GetNSVPSIResultVO();
		this.apiInfo	= ImcsConstants.API_PRO_ID031_w;
		
		resultListVO	= this.getNSMakeVPSI(paramVO, apiInfo);
		return resultListVO;
	}
	
	public GetNSVPSIResultVO getNSMakeVPSI(GetNSVPSIRequestVO paramVO, String apiInfo)	{
		//this.getNSMakeVPSIs(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
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
		
		// 파일명 관련 파라미터
		String RESFILE		= "";
		String NASFILE		= "";
		String LOCKFILE		= "";
		String LOCALLOCKFILE		= "";
		
		String szThmImgSvrIp	= "snap_server";		// 썸네일 이미지 서버 IP
		String szImgSvrUrl		= "img_resize_server";		// 이미지 서버 URL
		String szStilImgSvrUrl	= "img_still_server";		// 스틸 이미지 서버 URL
		String VirtualChFlag	= "";		// 가상채널 여부
		
		String LOCALPATH = "";
		String NASPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID031.split("/")[1], imcsLog);
		NASPATH = commonService.getCachePath("NAS", ImcsConstants.API_PRO_ID031.split("/")[1], imcsLog);
		
		int nMainCnt = 0;
		int nWaitCnt = 0;
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
	    
		List<StillImageVO> lstImageInfo	= null;		// 이미지 파일명 조회 VO
		StillImageVO imageVO = null;
		GetNSVPSIResponseVO lstPrinfo	= null;		// 가상채널정보 VO
		
		try{
			
			File fNASFILE	= null;
			File fRESFILE	= null;
			
			File fLOCKFILE	= null;
			File fLOCALLOCKFILE	= null;
			
			String szMsg			= "";
			
			String current_time = commonService.getSysdate();
			
			//2018.08.20 폴더로 나누는 방법으로 변경
			String nasDirName = String.format("%s/t%sn%sy%sp%sv%s", NASPATH, paramVO.getTestSbc(), paramVO.getNscType(), paramVO.getYouthYn(), 
					paramVO.getPooqYn(), paramVO.getHdtvViewGb());
			NASPATH = nasDirName;
			File NAS_DIR = new File(NASPATH);
			if(!NAS_DIR.exists()){
				NAS_DIR.mkdir();
			}
						
			//테스트 가입자 여부 조회
//			this.getTestSbc(paramVO);
//			tp1	= System.currentTimeMillis();
//			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			String compFileName = "_t"+paramVO.getTestSbc()+"_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_t"+paramVO.getTcOut()
					+"_p"+paramVO.getPooqYn()+"_v"+paramVO.getHdtvViewGb()+"_s"+paramVO.getServiceId()+"_d"+paramVO.getEpgSdate()+"_e"+paramVO.getEpgEdate();
			
			//2018.08.20 폴더로 나누는 방법으로 변경
			String locaDirName = String.format("%s/t%sn%sy%sp%sv%s", LOCALPATH, paramVO.getTestSbc(), paramVO.getNscType(), paramVO.getYouthYn(), 
					paramVO.getPooqYn(), paramVO.getHdtvViewGb());
			LOCALPATH = locaDirName;
			
			File LOCAL_DIR = new File(LOCALPATH);
			if(!LOCAL_DIR.exists()){
				LOCAL_DIR.mkdir();
			}
			
			String chkFileName = "";
			
			File[] files = NAS_DIR.listFiles();
			
			
			List<String> list = new ArrayList<String>();
			

			try{
				if(files.length > 0){	
					
					for(int i = 0; i < files.length; i++){
						if(files[i].getName().toString().indexOf(compFileName) > -1){
							list.add(files[i].getName());
						}
					}
					
					if(list != null && list.size() > 0){
	//					List<String> list2 = new ArrayList<String>(list);
	//					Collections.reverse(list2);
	//					
	//					chkFileName = list2.get(0);
						
						Collections.sort(list, new Comparator<String>(){
							public int compare(String obj1, String obj2){
								return obj1.compareToIgnoreCase(obj2);
							}
						});
						
						Collections.reverse(list);
						
						chkFileName = list.get(0);
					}else{
						chkFileName = "2";
					}
				}else{
					
					chkFileName = "2";
				}
			}catch(NullPointerException e)
			{
				szMsg = " getNSMakeVPSI Cache File Empty";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
			
			if(!commonService.chkCacheFile(chkFileName, imcsLog)){
				
				String cacheFileName = "";
				String[] arrlockFileName = list.get(0).split("\\.");
				
				for(int i = 0; i < arrlockFileName.length -1; i++){
					cacheFileName += arrlockFileName[i];
				}
				
				//String[] cacheFileName = list.get(0).split("\\.");
				if(list.get(0).indexOf(".lock") > -1){					
					LOCKFILE = NASPATH + "/" + list.get(0);
					LOCALLOCKFILE = LOCALPATH + "/" + list.get(0);
					NASFILE = NASPATH + "/" + cacheFileName + ".res";
					RESFILE = LOCALPATH + "/" + cacheFileName + ".res";
				}else{					
					LOCKFILE = NASPATH + "/" + cacheFileName + ".lock";
					LOCALLOCKFILE = LOCALPATH + "/" + cacheFileName + ".lock";
					NASFILE = NASPATH + "/" + list.get(0);
					RESFILE = LOCALPATH + "/" + list.get(0);
				}
				
				
				
				if(NAS_DIR.exists()){
					fRESFILE	= new File(RESFILE);
					fLOCALLOCKFILE   = new File(LOCALLOCKFILE);
					fNASFILE	= new File(NASFILE);
					fLOCKFILE   = new File(LOCKFILE);
					
					if(fNASFILE.exists()) {	
						try {
							String[] szCommand = {"/bin/sh", "-c", "cp " + NASFILE + " " + RESFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + NASFILE + "] copy [" + RESFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String result = FileUtil.fileRead(NASFILE, "UTF-8");
							resultListVO.setResult(result);
							
							FileUtil.unlock(LOCKFILE, imcsLog);
							
							delCacheFile(LOCALPATH, compFileName, paramVO);
							
							
							
				            return resultListVO;
						} catch(Exception e) {
							szMsg = " cp cache error!!! ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}/* else if(fLOCKFILE.exists()){
						int nWaitCnt	= 0;
						
						//while(FileUtil.lock(LOCKFILE, imcsLog)){
						while(fLOCKFILE.exists()){
							nWaitCnt++;
							
							if(nWaitCnt >= 5) {
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID032_w + "]";
								imcsLog.serviceLog(szMsg, methodName, methodLine);

					            return resultListVO;
							}
							
							if(fNASFILE.exists()) {
								imcsLog.serviceLog(" File [" + NASFILE + "] exist ", methodName, methodLine);
								
								try {
									String[] szCommand = {"/bin/sh", "-c", "chmod 666 " + NASFILE};
									Process p = Runtime.getRuntime().exec(szCommand);
									
									szMsg = " File [" + NASFILE + "] chmod 666";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								} catch (Exception e) {
									szMsg = " cache chmod 666 error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
								
								try {
									String[] szCommand2 = {"/bin/sh", "-c", "cp " + NASFILE + " " + RESFILE};
									Process p = Runtime.getRuntime().exec(szCommand2);
								
									szMsg = " File [" + NASFILE + "] copy [" + RESFILE + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								} catch (Exception e) {
									szMsg = " cp cache error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
								
								
								String result = FileUtil.fileRead(NASFILE, "UTF-8");
								resultListVO.setResult(result);
								
								FileUtil.unlock(LOCKFILE, imcsLog);
								
								delCacheFile(NASPATH, compFileName);	
								
								delCacheFile(LOCALPATH, compFileName);	
								
					            return resultListVO;
							} else {
								imcsLog.serviceLog("File [" + NASFILE + "] not exist", methodName, methodLine);
							}
						}
					}
					
					if(fRESFILE.exists() && fRESFILE.length() == 0){
						fRESFILE.delete();
					}
					
					if(fRESFILE.exists() && !fLOCALLOCKFILE.exists()){
						try {
							String[] szCommand = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String result = FileUtil.fileRead(RESFILE, "UTF-8");
							resultListVO.setResult(result);
							
							FileUtil.unlock(LOCALLOCKFILE, imcsLog);
							
							delCacheFile(NASPATH, compFileName);							
							
				            return resultListVO;
						} catch(Exception e) {
							szMsg = " cp cache error!!! ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}else if(!fRESFILE.exists() && !fLOCALLOCKFILE.exists()){
						int nWaitCnt	= 0;
						
						while(FileUtil.lock(LOCALLOCKFILE, imcsLog)){
							nWaitCnt++;
							
							if(nWaitCnt >= 5) {
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID032_w + "]";
								imcsLog.serviceLog(szMsg, methodName, methodLine);

					            return resultListVO;
							}
							
							if(fRESFILE.exists()) {
								imcsLog.serviceLog(" File [" + RESFILE + "] exist ", methodName, methodLine);
								
								try {
									String[] szCommand = {"/bin/sh", "-c", "chmod 666 " + RESFILE};
									Process p = Runtime.getRuntime().exec(szCommand);
									
									szMsg = " File [" + RESFILE + "] chmod 666";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								} catch (Exception e) {
									szMsg = " cache chmod 666 error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
								
								try {
									String[] szCommand2 = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
									Process p = Runtime.getRuntime().exec(szCommand2);
								
									szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								} catch (Exception e) {
									szMsg = " cp cache error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
								
								
								String result = FileUtil.fileRead(RESFILE, "UTF-8");
								resultListVO.setResult(result);
								
								FileUtil.unlock(LOCALLOCKFILE, imcsLog);
								
								delCacheFile(LOCALPATH, compFileName);	
								
								delCacheFile(NASPATH, compFileName);	
								
					            return resultListVO;
							} else {
								imcsLog.serviceLog("File [" + RESFILE + "] not exist", methodName, methodLine);
							}
						}
					}*/
				}
			}else{
				szMsg = " NAS File : NO FILE";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				
				LOCKFILE = NASPATH + "/getNSVPSI_" + current_time + compFileName + ".lock";
				
				LOCALLOCKFILE = LOCALPATH + "/getNSVPSI_" + current_time + compFileName + ".lock";
				
				NASFILE = NASPATH + "/getNSVPSI_" + current_time + compFileName + ".res";
				
				RESFILE = LOCALPATH + "/getNSVPSI_" + current_time + compFileName + ".res";
				
				/*FileUtil.lock(LOCKFILE, imcsLog);
				
				szMsg = " LOCKFILE";
				imcsLog.serviceLog(szMsg, methodName, methodLine);*/
			}
			
			fRESFILE	= new File(RESFILE);
			fLOCALLOCKFILE   = new File(LOCALLOCKFILE);
			fNASFILE	= new File(NASFILE);
			fLOCKFILE   = new File(LOCKFILE);
			
			boolean procStatus = false;
			
			if(NAS_DIR.exists()){
				if(!FileUtil.lock(LOCKFILE, imcsLog)){
					if(!fLOCKFILE.exists()){
						szMsg = " queryLock Fail, [NAS lock file make fail] " + LOCKFILE + " query execution itself";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						
						szMsg = " lock File write fail [" + LOCKFILE + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						
						procStatus = false;
					}
				}else{
					szMsg = " lock File exist [" + LOCKFILE + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);	
					procStatus = true;
				}
			}else{
				szMsg = " queryLock Fail, [NAS Directory Not Exist] " + NASPATH + " query execution itself";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				procStatus = false;
			}
			
			
			if(!FileUtil.lock(LOCALLOCKFILE, imcsLog)){
				if(!fLOCALLOCKFILE.exists()){
					szMsg = " lock File write fail [" + LOCALLOCKFILE + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					procStatus = false;
				}else{
					szMsg = " lock File exist [" + LOCALLOCKFILE + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);	
					
					procStatus = true;
				}
			}else{
				szMsg = " lock File exist [" + LOCALLOCKFILE + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);	
				
				procStatus = false;
			}

			if(procStatus){	
				
				try {
					VirtualChFlag	= commonService.getVCFlag(ImcsConstants.API_PRO_ID031.split("/")[1]);	// 가상채널 여부
				} catch (Exception e) {
					System.out.println("GetConfigInfo fail");
				}
				
				paramVO.setVirtualChFlag(VirtualChFlag);
				
				
				/**
				 * EPG 데이터 누락 방지 체크 
				 * PT_LP_PRGSCHEDULE_STD TABLE TUNCATE 시 데이터 누락으로 인한 sleep 1초 5회 체크 
				 * **/
				int sleep_cnt = 0;
				
				int chk_cnt = 0;
				
				while(true){
					if(sleep_cnt == 5)
			    	{
						szMsg = " PT_LP_PRGSCHEDULE_STD no data found (sleep end)";
						imcsLog.serviceLog(szMsg, methodName, methodLine);	
						
						FileUtil.unlock(LOCALLOCKFILE, imcsLog);
						
						FileUtil.unlock(LOCKFILE, imcsLog);
						
						break;
			    	}
					
					chk_cnt = this.selectPRGSCHEDULE(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if(chk_cnt == 1){
						break;
					}else{
						szMsg = " PT_LP_PRGSCHEDULE_STD no data found (sleep 1 sec)";
						imcsLog.serviceLog(szMsg, methodName, methodLine);	
						Thread.sleep(1000);
						sleep_cnt++;
					}					
				}					
				
				
				if(chk_cnt == 1){
					System.out.println("가상채널 EPG전체 스케줄정보 조회!!");
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
					
					szMsg = " nRetVal : " + nRetVal;
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					if(nRetVal == 1) {
						fRESFILE = new File(RESFILE);
						
						if(fRESFILE.length() != 0 ){
						
							msg = " File [" + RESFILE + "] WRITE [" + fRESFILE.length() + "] bytes Finished";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							try {
								String[] szCommand = {"/bin/sh", "-c", "chmod 666 " + RESFILE};
								Process p = Runtime.getRuntime().exec(szCommand);
								
								szMsg = " File [" + RESFILE + "] chmod 666";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
								
							} catch (Exception e) {
								szMsg = " cache chmod 666 error!!!";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
							}
							
							if(NAS_DIR.exists()){
								try {
									String[] szCommand2 = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
									Process p = Runtime.getRuntime().exec(szCommand2);
								
									szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
									
									delCacheFile(NASPATH, compFileName, paramVO);
									
								} catch (Exception e) {
									szMsg = " cp cache error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
							}
						}else{
							szMsg = " cache's size 0, cache delete and return";
							imcsLog.serviceLog(szMsg, methodName, methodLine);	
							
							fRESFILE.delete();
						}
						
											
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog(" [getNSMakeVPSI]cache_time", String.valueOf(tp2 - tp1), methodName, methodLine);
						
					} else {
						msg = " File [" + RESFILE + "] WRITE failed";
						imcsLog.serviceLog(msg, methodName, methodLine);		
					}
					
					FileUtil.unlock(LOCALLOCKFILE, imcsLog);
					
					FileUtil.unlock(LOCKFILE, imcsLog);
					
					delCacheFile(LOCALPATH, compFileName, paramVO);	
				}
			}		
    
			
			//stamp_today	// 사용안함
		    //c_current		// 사용안함
		    						
			/*LOCKFILE	= LOCALPATH + "/getNSVPSI_t"+paramVO.getTestSbc()+"_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_t"+paramVO.getTcOut()
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
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = null;
				
		try {

			list = getNSVPSIDao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSaId(paramVO.getSaId());
//			rowKeys.setSqlId(sqlId);
//			rowKeys.setStbMac(paramVO.getStbMac());
//			
//			checkKey.addVersionTuple("PT_VO_CUSTOM_ID", paramVO.getSaId());
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVPSIRequestVO requestVO = (GetNSVPSIRequestVO)param.get(0);
//						List<String> rtnList = getNSVPSIDao.getTestSbc(requestVO);
//						
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			if( list == null || list.isEmpty()) {
				paramVO.setTestSbc("N");				
			} else {
				paramVO.setTestSbc(list.get(0));
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			paramVO.setTestSbc("N");
		}
    }
    
    
    public Integer selectPRGSCHEDULE(GetNSVPSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	int chk_cnt = 0;
    	
    	try{
    		chk_cnt = getNSVPSIDao.selectPRGSCHEDULE();
    		
    		if(chk_cnt == 0){
    			String szMsg = " PT_LP_PRGSCHEDULE_STD table query (no data found)";
    			imcsLog.serviceLog(szMsg, methodName, methodLine);	
    		}
    		
    	}catch(Exception e){
    		String szMsg = " PT_LP_PRGSCHEDULE_STD table query error";
			imcsLog.serviceLog(szMsg, methodName, methodLine);	
    	}finally{
    		return chk_cnt;
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
					//System.out.println("##case1");
				} else {
					rtnList = getNSVPSIDao.getNSVPSIList2(paramVO);
					//System.out.println("##case2:" + paramVO.getHdtvViewGb());
					//System.out.println("##case2");
				}
			} else {
				rtnList = getNSVPSIDao.getNSVPSIList3(paramVO);
				//System.out.println("##case3");
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
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<StillImageVO> list   = new ArrayList<StillImageVO>();
		
		try {

			list = getNSVPSIDao.getImgUrl(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getVirtualId());
//			checkKey.addVersionTuple("PT_LA_ALBUM_IMG", paramVO.getVirtualId());
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<StillImageVO>() {
//				@Override
//				public List<StillImageVO> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVPSIRequestVO requestVO = (GetNSVPSIRequestVO)param.get(0);
//						List<StillImageVO> rtnList = getNSVPSIDao.getImgUrl(requestVO);
//						
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<StillImageVO> getReturnType() {
//					return StillImageVO.class;
//				}
//			}, binds, rowKeys, checkKey);
//						
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
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
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = null;
		
		try {

			list = getNSVPSIDao.getImgFileName(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getVirtualId());
//			checkKey.addVersionTuple("PT_LA_ALBUM_MST",paramVO.getVirtualId());
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVPSIRequestVO requestVO = (GetNSVPSIRequestVO)param.get(0);
//						List<String> rtnList = getNSVPSIDao.getImgFileName(requestVO);
//						
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			if( list != null && !list.isEmpty()){
				szImgFileName	= StringUtil.nullToSpace(list.get(0));
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
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

//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<GetNSVPSIResponseVO> list   = null;
		GetNSVPSIResponseVO resultVO = null;
		
		try {

			list = getNSVPSIDao.getPrInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getVirtualId());
//			checkKey.addVersionTuple("PT_LA_ALBUM_MST", paramVO.getVirtualId());
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<GetNSVPSIResponseVO>() {
//				@Override
//				public List<GetNSVPSIResponseVO> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVPSIRequestVO requestVO = (GetNSVPSIRequestVO)param.get(0);
//						List<GetNSVPSIResponseVO> rtnList = getNSVPSIDao.getPrInfo(requestVO);
//						
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//					
//				}
//				
//				@Override
//				public Class<GetNSVPSIResponseVO> getReturnType() {
//					return GetNSVPSIResponseVO.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			if( list != null && !list.isEmpty()){
				resultVO  = list.get(0);
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return resultVO;
    }
    
    
    public void delCacheFile(String file_path, String compValue, GetNSVPSIRequestVO paramVO){
		
		String szMsg = "";
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		try{
		
			File dirFile = new File(file_path);
			
			File[] files = dirFile.listFiles();
			
			List<String> list = new ArrayList<String>();
			
			if(files.length > 0){
				for(int i = 0; i < files.length; i++){
					if(files[i].getName().toString().indexOf(compValue) > -1){						
						list.add(files[i].getName());
					}
				}
				if(list != null){
					Collections.sort(list, new Comparator<String>(){
						public int compare(String obj1, String obj2){
							return obj1.compareToIgnoreCase(obj2);
						}
					});
					
					Collections.reverse(list);
					
					if(list.size() > 3){
						String del_file_path = file_path + "/" + list.get(list.size() -1);
						
						File del_file = new File(del_file_path);
						if(del_file.delete()){
							szMsg = " File delete [rm " + file_path + "/" + list.get(list.size() -1) + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
						}else{	
							imcsLog.errorLog(methodName + "-E","delCacheFile : File delete ERROR [rm " + file_path + "/" + list.get(list.size() -1) + "]");
						}
					}
				}
			}
		}catch(Exception e){
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
	}

@Override
public GetNSVPSIResultVO getNSVPSI(GetNSVPSIRequestVO paramVO) {
	// TODO Auto-generated method stub
	return null;
}
    
}
