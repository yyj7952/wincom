package kr.co.wincom.imcs.api.getCopyCache;


import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.GlobalCom;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.property.ImcsProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class  GetCopyCacheServiceImpl implements  GetCopyCacheService {
	private Log imcsLogger = LogFactory.getLog("API_getCopyCache");
	
	@Autowired
	private GetCopyCacheDao getCopyCacheDao;
	
	@Autowired
	private CommonService commonService;

//	public void getCopyCache(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	String SERVER_CONF = ImcsProperties.getProperty("filepath.server");
	Properties serverManager = GlobalCom.getPropertyFile(SERVER_CONF);
//	private String svcNm = StringUtil.replaceNull(serverManager.getProperty("SVC_NM"),"");
	
	
	@Override
	public void getCopyCache(GetCopyCacheRequestVO paramVO){
//		this.getCopyCache(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		String getCopyCache_99server_yn = StringUtil.replaceNull(commonService.getServerProperties("getCopyCache_99server_yn"),"Y");
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getApiName(), paramVO.getVersionKey(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		GetCopyCacheResultVO resultVO = new GetCopyCacheResultVO();
		
		switch(getCopyCache_99server_yn)
		{
			case "Y":
				paramVO.setServer99("99");
				break;
			case "N":
				paramVO.setServer99("");
				break;
			default:
				paramVO.setServer99("99");
				break;
		}
		
		
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		String LOCALPATH   = "";
		String NASPATH = "";
		
		LOCALPATH = commonService.getCachePath("COPY_LOCAL", paramVO.getApiName(), imcsLog);
		NASPATH = commonService.getCachePath("COPY_NAS", paramVO.getApiName(), imcsLog);
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		long tp_start = paramVO.getTp_start();
	    long tp1 = 0;
		long tp2 = 0;
		String flag		= "";
		String errMsg	= "";
		
		try{
			
			if ((paramVO.getApiName().equals("getNSMnuList") || paramVO.getApiName().equals("getNSMnuListDtl")) && paramVO.getCacheMod().equals("M")) {
				
				LOCALPATH = commonService.getCachePath("COPY_LOCAL", "", imcsLog);
				NASPATH = commonService.getCachePath("COPY_NAS", paramVO.getApiName(), imcsLog);
				
				String nasTarGzDir = NASPATH + "/" + paramVO.getApiName() + "_" + paramVO.getCacheVer().substring(0, 8)+".tar.gz";
				String localTarGzDir = LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer().substring(0, 8)+".tar.gz";
				
				msg = " nasTarGzDir : " + nasTarGzDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
		    	msg = " localTarGzDir : " + localTarGzDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
				
				File nasTarGzFileDir = new File(nasTarGzDir);
				File localTarGzFileDir = new File(LOCALPATH);
				
				msg = " nasTarGzFileDir : " + nasTarGzFileDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
		    	msg = " localTarGzFileDir : " + localTarGzFileDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
				
				File localFileDir = new File(LOCALPATH + paramVO.getApiName());
				File localBakFileDir = new File(LOCALPATH + paramVO.getApiName() + "_bak");
				
				msg = " localFileDir : " + localFileDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
		    	msg = " localBakFileDir : " + localBakFileDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
				
				File localTarGz  = new File(localTarGzDir);
		    	//copyFile(nasTarGzDir, localTarGzDir, resultVO);
				
				if (localTarGz.exists()) {
					resultVO.setFlag("3");
					msg = localTarGzDir + ": overlap";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else {
					int copyFlag = fileCopybyLinux (nasTarGzDir, localTarGzDir);
					
					if (copyFlag == 0) {
						
						msg = " fileCopybyLinux success : " + copyFlag;
						imcsLog.serviceLog(msg, methodName, methodLine);
						copySuccessChk(nasTarGzDir, localTarGzDir, resultVO, paramVO);
					} else {
						resultVO.setFlag("1");
						msg = " fileCopybyLinux fail: " + copyFlag;
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
				}
		    	
		    	if(resultVO.getFlag().equals("0")){
					int tarflag = fileDecompressbyLinux(localTarGzDir, LOCALPATH);
					if(tarflag == 0) {
						msg = " fileDecompressbyLinux success : " + tarflag;
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					} else {
						resultVO.setFlag("1");
						msg = " fileDecompressbyLinux fail: " + tarflag;
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					
					if(paramVO.getTotFileCnt().equals("0")) {
						msg = " TotFileCnt : " + paramVO.getTotFileCnt();
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					} else {
						File ffRESFILE = new File(LOCALPATH + paramVO.getCacheVer().substring(0, 8));
						File []sfileList = ffRESFILE.listFiles();
						for(File stempFile : sfileList) {
							File[] fileList = stempFile.listFiles();
							
							msg = " fileList : " + stempFile.getPath();
					    	imcsLog.serviceLog(msg, methodName, methodLine);
					    	
							
							for(File tempFile : fileList) {
								String nasFileName=tempFile.getName();
								String path = tempFile.getParentFile().toString();
								
								String[] array = nasFileName.split("-");
								
								String localFileName = array[0];
								for(int i= 1; i < array.length ; i++) {
									if(i != 2) {
										localFileName = localFileName + "-" + array[i];
									}
								}
								
								
								File nasFileNameF = new File(path + "/" + nasFileName);
								File localFileNameF = new File(path + "/" + localFileName);
								
								if(!nasFileNameF.renameTo(localFileNameF)) {
									msg = nasFileNameF.getName() + "->" + localFileNameF.getName() + " file rename fail";
							    	imcsLog.serviceLog(msg, methodName, methodLine);
								}
							}
						}
					}
					
					File newLocalPath = new File(LOCALPATH + paramVO.getCacheVer().substring(0, 8));
					File oldLocalPath = new File(LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer().substring(0, 8));
					
					
					if(!localFileDir.renameTo(localBakFileDir)) {
						msg = localFileDir.getName() + "->" + localBakFileDir.getName() + " file rename fail";
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					} else {
						msg = localFileDir.getName() + "->" + localBakFileDir.getName() + " file rename success";
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					if(!newLocalPath.renameTo(localFileDir)) {
						msg = newLocalPath.getName() + "->" + localFileDir.getName() + " file rename fail";
						imcsLog.serviceLog(msg, methodName, methodLine);
					} else {
						msg = newLocalPath.getName() + "->" + localFileDir.getName() + " file rename success";
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					if(!localBakFileDir.renameTo(oldLocalPath)) {
						msg = localBakFileDir.getName() + "->" + oldLocalPath.getName() + " file rename fail";
						imcsLog.serviceLog(msg, methodName, methodLine);
					} else {
						msg = localBakFileDir.getName() + "->" + oldLocalPath.getName() + " file rename success";
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					}
		    	}
		    	
				if(localTarGz.exists() && !resultVO.getFlag().equals("3")) {
		    		localTarGz.delete();// tar.gz 파일 삭제
		    	}
				
			} else if ((paramVO.getApiName().equals("getNSMnuList") || paramVO.getApiName().equals("getNSMnuListDtl")) && paramVO.getCacheMod().equals("S")) {
				LOCALPATH = commonService.getCachePath("COPY_LOCAL", "", imcsLog);
				NASPATH = commonService.getCachePath("COPY_NAS", paramVO.getApiName(), imcsLog);
				
				String nasTarGzDir = NASPATH + "/service" + "/" + paramVO.getApiName() + "_" + paramVO.getCacheVer()+".tar.gz";
				String localTarGzDir = LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer() + ".tar.gz";
				ArrayList<String> verKeyList = new ArrayList<String>();;
				
				
				if (paramVO.getDelYn().equals("Y")) {// Y일 경우 해당 카테고리의
					
					msg = " delcategory : " + LOCALPATH + paramVO.getApiName() + "/" + paramVO.getVersionKey().substring(0, 2);
			    	imcsLog.serviceLog(msg, methodName, methodLine);
					
					int delFlag = delFlagFile(LOCALPATH + paramVO.getApiName() + "/" + paramVO.getVersionKey().substring(0, 2), paramVO);
					
					if(delFlag == 0) {
						msg = " delFlagFile success : " + delFlag;
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					} else {
						resultVO.setFlag("1");
						msg = " delFlagFile fail: " + delFlag;
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					if(delFlag == 0) {
						
						try {
							flag = getCopyCacheDao.getCacheChk(paramVO);
						} catch (DataAccessException e) {
							throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
						}

						paramVO.setVtsCrtVer(paramVO.getCacheVer());

						tp2 = System.currentTimeMillis();
						imcsLog.timeLog(paramVO.getApiName() + "_" + paramVO.getVersionKey() + " : category delete success ",
								String.valueOf(tp2 - tp1), methodName, methodLine);

						try {
							flag = getCopyCacheDao.getCacheChk(paramVO);
						} catch (DataAccessException e) {
							throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
						}

						if (flag == null || flag.length() == 0) {
							tp1 = System.currentTimeMillis();
							insertCacheVer(paramVO, resultVO);
							tp2 = System.currentTimeMillis();
							imcsLog.timeLog("insertCacheVer", String.valueOf(tp2 - tp1), "insertCacheVer", methodLine);
						} else {
							tp1 = System.currentTimeMillis();
							updateCacheVer(paramVO, resultVO);
							tp2 = System.currentTimeMillis();
							imcsLog.timeLog("updateCacheVer", String.valueOf(tp2 - tp1), "updateCacheVer", methodLine);
						}
					} else {
						imcsLog.timeLog(paramVO.getApiName() + "_" + paramVO.getVersionKey() + " : Category Delete fail ",
								String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					

					return;
				}
				
				
				
				msg = " nasTarGzDir : " + nasTarGzDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
		    	msg = " localTarGzDir : " + localTarGzDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
				
				
				File localTarGz  = new File(localTarGzDir);
		    	
				File localDir  = new File(LOCALPATH + paramVO.getApiName());
				
				if (!localDir.exists()) {//api캐시 폴더가 없다면 생성
					localDir.mkdirs();
				}
				
		    	//copyFile(nasTarGzDir, localTarGzDir, resultVO);
		    	
		    	
		    	if (localTarGz.exists()) {
					resultVO.setFlag("3");
					msg = localTarGzDir + " 이미 존재";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else {
					int copyFlag = fileCopybyLinux (nasTarGzDir, localTarGzDir);
					
					if (copyFlag == 0) {
						msg = " fileCopybyLinux success : " + copyFlag;
						imcsLog.serviceLog(msg, methodName, methodLine);
						copySuccessChk(nasTarGzDir, localTarGzDir, resultVO, paramVO);
					} else {
						resultVO.setFlag("1");
						msg = " fileCopybyLinux fail: " + copyFlag;
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
				}
		    	
		    	
		    	
		    	if(resultVO.getFlag().equals("0")){
		    		
		    		File serviceDir = new File(LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer());
			    	
			    	if (!serviceDir.exists()) {
			    		serviceDir.mkdirs();
					}
		    		
					int tarflag = fileDecompressbyLinux(localTarGzDir, LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer());
					if(tarflag == 0) {
						msg = " fileDecompressbyLinux success : " + tarflag;
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					} else {
						resultVO.setFlag("1");
						msg = " fileDecompressbyLinux fail: " + tarflag;
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					String newLocalPath = LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer() + "/" + paramVO.getCacheVer() + "/";
					String oldLocalPath = LOCALPATH + paramVO.getApiName() + "/";
					
					verKeyList = verKeyList(newLocalPath);
					
					if (verKeyList.size() > 0) {
						msg = " Category count: " + verKeyList.size();
				    	imcsLog.serviceLog(msg, methodName, methodLine);
						
				    	String newLocal = "";
				    	String oldLocal = "";
						// 파일 비교 후 삭제
						for (int z = 0; z < verKeyList.size(); z++) {
							newLocal = newLocalPath + verKeyList.get(z).substring(0, 2) + "/";
							oldLocal = oldLocalPath + verKeyList.get(z).substring(0, 2) + "/";
							deleteFile(newLocal, oldLocal, paramVO, verKeyList.get(z));
						}
						
					}
					
					//압축 푼 파일 덮어쓰기
					//copyFileFn(newLocalPath , oldLocalPath);
					
					
					//deleteFileFn(LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer());//해당 폴더 삭제
					
					if(resultVO.getFlag().equals("0")){
			    		int overWriteflag = fileOverWritebyLinux(newLocalPath, oldLocalPath);
						if(overWriteflag == 0) {
							msg = " fileOverWritebyLinux success : " + overWriteflag;
					    	imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							resultVO.setFlag("1");
							msg = " fileOverWritebyLinux fail: " + overWriteflag;
					    	imcsLog.serviceLog(msg, methodName, methodLine);
						}
			    	}
			    	
			    	if(resultVO.getFlag().equals("0")){
			    		int deleteflag = fileDeletebyLinux(LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer());
						if(deleteflag == 0) {
							msg = " fileDeletebyLinux success : " + deleteflag;
					    	imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							resultVO.setFlag("1");
							msg = " fileDeletebyLinux fail: " + deleteflag;
					    	imcsLog.serviceLog(msg, methodName, methodLine);
						}
			    	}
		    	}
				
		    	
				
		    	if(verKeyList.size() > 0) {
		    		for (int z = 0; z < verKeyList.size(); z++) {
						paramVO.setVersionKey(verKeyList.get(z));
						
						if(resultVO.getFlag().equals("0")) {
							paramVO.setCopyFlag("0");
							paramVO.setVtsCrtVer(paramVO.getCacheVer());
							
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog(paramVO.getCacheVer() + "_" +paramVO.getVersionKey() +" : 캐쉬파일 복사 완료 ", String.valueOf(tp2 - tp1), methodName, methodLine); 
							
							try{
								flag = getCopyCacheDao.getCacheChk(paramVO);
							}catch(DataAccessException e){
								throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
							}
							
							if(flag == null || flag.length() == 0) {
								tp1 = System.currentTimeMillis();
								insertCacheVer(paramVO,resultVO);
								tp2	= System.currentTimeMillis();
								imcsLog.timeLog("insertCacheVer", String.valueOf(tp2 - tp1), "insertCacheVer", methodLine); 
							}else {
								tp1 = System.currentTimeMillis();
								updateCacheVer(paramVO,resultVO);
								tp2	= System.currentTimeMillis();
								imcsLog.timeLog("updateCacheVer", String.valueOf(tp2 - tp1), "updateCacheVer", methodLine);
							}
						} else if (resultVO.getFlag().equals("1")) {
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("캐쉬파일 복사 fail", String.valueOf(tp2 - tp1), methodName, methodLine); 
							
							try{
								flag = getCopyCacheDao.getCacheChk(paramVO);
							}catch(DataAccessException e){
								throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
							}
							
							if(flag == null || flag.length() == 0) {
								paramVO.setVtsCrtVer(paramVO.getCacheVer());
								tp1 = System.currentTimeMillis();
								insertCacheVer(paramVO,resultVO);
								tp2	= System.currentTimeMillis();
								imcsLog.timeLog("insertCacheVer", String.valueOf(tp2 - tp1), "insertCacheVer", methodLine); 
							}else {
								tp1 = System.currentTimeMillis();
								updateCacheVer(paramVO,resultVO);
								tp2	= System.currentTimeMillis();
								imcsLog.timeLog("updateCacheVer", String.valueOf(tp2 - tp1), "updateCacheVer", methodLine);
							}
						}
					}
		    	}
		    	
		    	if(localTarGz.exists() && !resultVO.getFlag().equals("3")) {
		    		localTarGz.delete();// tar.gz 파일 삭제
		    	}
				
			} else if(paramVO.getApiName().equals("getNSChPGM") || paramVO.getApiName().equals("getNSChList")) {	
				LOCALPATH = commonService.getCachePath("COPY_LOCAL", "", imcsLog);
				NASPATH = commonService.getCachePath("COPY_NAS", paramVO.getApiName(), imcsLog);
				
				
				String nasTarGzDir = NASPATH + "/" + paramVO.getApiName() + "_TAR" + "/" + paramVO.getApiName() + "_" + paramVO.getCacheVer()+".tar.gz";
				String localTarGzDir = LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer()+".tar.gz";
				
				msg = " nasTarGzDir : " + nasTarGzDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
		    	msg = " localTarGzDir : " + localTarGzDir;
		    	imcsLog.serviceLog(msg, methodName, methodLine);
				
				File localTarGz  = new File(localTarGzDir);
		    	
				File localDir  = new File(LOCALPATH + paramVO.getApiName());
				
				if (!localDir.exists()) {//api캐시 폴더가 없다면 생성
					localDir.mkdirs();
				}
				
		    	//copyFile(nasTarGzDir, localTarGzDir, resultVO);
		    	
		    	if (localTarGz.exists()) {
					resultVO.setFlag("3");
					msg = localTarGzDir + " 이미 존재";
					imcsLog.serviceLog(msg, methodName, methodLine+"asdfasd");
				} else {
					int copyFlag = fileCopybyLinux (nasTarGzDir, localTarGzDir);
					
					if (copyFlag == 0) {
						msg = " fileCopybyLinux success : " + copyFlag;
						imcsLog.serviceLog(msg, methodName, methodLine);
						copySuccessChk(nasTarGzDir, localTarGzDir, resultVO, paramVO);
					} else {
						resultVO.setFlag("1");
						msg = " fileCopybyLinux fail: " + copyFlag;
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
				}
		    	
		    	
		    	
		    	
		    	if(resultVO.getFlag().equals("0")){
		    		
		    		File serviceDir = new File(LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer());
			    	
			    	if (!serviceDir.exists()) {
			    		serviceDir.mkdirs();
					}
		    		
			    	int tarflag = fileDecompressbyLinux(localTarGzDir, LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer() + "/");
					if(tarflag == 0) {
						msg = " fileDecompressbyLinux success : " + tarflag;
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					} else {
						resultVO.setFlag("1");
						msg = " fileDecompressbyLinux fail: " + tarflag;
				    	imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					String newLocalPath = LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer() + "/" + paramVO.getCacheVer() + "/";
					String oldLocalPath = LOCALPATH + paramVO.getApiName();
					
					//압축 푼 파일 덮어쓰기
					//copyFileFn(newLocalPath, oldLocalPath);
					msg = " newLocalPath : " + newLocalPath;
			    	imcsLog.serviceLog(msg, methodName, methodLine);
			    	msg = " oldLocalPath : " + oldLocalPath;
			    	imcsLog.serviceLog(msg, methodName, methodLine);
			    	
			    	if(resultVO.getFlag().equals("0")){
			    		int overWriteflag = fileOverWritebyLinux(newLocalPath, oldLocalPath);
						if(overWriteflag == 0) {
							msg = " fileOverWritebyLinux success : " + overWriteflag;
					    	imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							resultVO.setFlag("1");
							msg = " fileOverWritebyLinux fail: " + overWriteflag;
					    	imcsLog.serviceLog(msg, methodName, methodLine);
						}
			    	}
			    	
			    	if(resultVO.getFlag().equals("0")){
			    		int deleteflag = fileDeletebyLinux(LOCALPATH + paramVO.getApiName() + "_" + paramVO.getCacheVer());
						if(deleteflag == 0) {
							msg = " fileDeletebyLinux success : " + deleteflag;
					    	imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							resultVO.setFlag("1");
							msg = " fileDeletebyLinux fail: " + deleteflag;
					    	imcsLog.serviceLog(msg, methodName, methodLine);
						}
			    	}
					
					//deleteFileFn(newLocalPath);//해당 폴더 삭제
		    	}
				
				if(resultVO.getFlag().equals("0")) {
					paramVO.setCopyFlag("0");
					paramVO.setVtsCrtVer(paramVO.getCacheVer());
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog(paramVO.getCacheVer() + "_" +paramVO.getVersionKey() +" : 캐쉬파일 복사 완료 ", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					try{
						flag = getCopyCacheDao.getCacheChk(paramVO);
					}catch(DataAccessException e){
						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
					}
					
					if(flag == null || flag.length() == 0) {
						tp1 = System.currentTimeMillis();
						insertCacheVer(paramVO,resultVO);
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("insertCacheVer", String.valueOf(tp2 - tp1), "insertCacheVer", methodLine); 
					}else {
						tp1 = System.currentTimeMillis();
						updateCacheVer(paramVO,resultVO);
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("updateCacheVer", String.valueOf(tp2 - tp1), "updateCacheVer", methodLine);
					}
				} else if (resultVO.getFlag().equals("1")) {
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("캐쉬파일 복사 fail", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					try{
						flag = getCopyCacheDao.getCacheChk(paramVO);
					}catch(DataAccessException e){
						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
					}
					
					if(flag == null || flag.length() == 0) {
						paramVO.setVtsCrtVer(paramVO.getCacheVer());
						tp1 = System.currentTimeMillis();
						insertCacheVer(paramVO,resultVO);
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("insertCacheVer", String.valueOf(tp2 - tp1), "insertCacheVer", methodLine); 
					}else {
						tp1 = System.currentTimeMillis();
						updateCacheVer(paramVO,resultVO);
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("updateCacheVer", String.valueOf(tp2 - tp1), "updateCacheVer", methodLine);
					}
				}
				
				if(localTarGz.exists() && !resultVO.getFlag().equals("3")) {
		    		localTarGz.delete();// tar.gz 파일 삭제
		    	}
				
			}
			
			String szCurrentDate = "";
			try {
				szCurrentDate = commonService.getSysdate();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	    	} catch (Exception e) {
	    		imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
				//throw new ImcsException(ImcsConstants.FAIL_CODE, e);
			}
			
			//String szCustomInfo = getCopyCacheDao.getCustomInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			flag	= "1";
			errMsg	= "";		
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(flag, errMsg, "");
		} finally{
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_COPYCACHE_ID001) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		//return resultVO;
	} 

	
	public static void deleteFileFn(String path) {
		
		File file = new File(path); // 매개변수로 받은 경로를 파일객체선언 (/home/nation909/test 경로의 폴더를 지정함)
		try {
			
			File[] files = file.listFiles();  // 해당 폴더 안의 파일들을 files 변수에 담음
			if(files.length > 0) { // 파일, 폴더가 1개라도 있을경우 실행
				for (int i=0; i<files.length; i++) { // 개수만큼 루프
					if(files[i].isFile()) { // 파일일경우 해당파일 삭제
						files[i].delete();
					}
					else { // 폴더일경우 재귀함수로 해당폴더의 경로를 전달함
						deleteFileFn(files[i].getPath()); // 재귀함수
					}
					files[i].delete(); // 폴더일경우 재귀함수가 다돌고나서, 즉 폴더안의 파일이 다지워지고 나서 해당폴더를 삭제함 
				}
				file.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	
	public static void copyFileFn(String path, String copypath) {
		File file = new File(path); // 매개변수로 받은 경로를 파일객체선언
		File[] files = file.listFiles();  // 해당 폴더 안의 파일들을 files 변수에 담음
		
		try {
			if (files.length > 0) { // 파일, 폴더가 1개라도 있을경우 실행
				for (int i = 0; i < files.length; i++) { // 개수만큼 루프
					if (files[i].isFile()) { // 파일일경우 해당파일 복사

						try {
							FileInputStream newf = new FileInputStream(files[i].getPath());
							FileOutputStream oldf = new FileOutputStream(copypath + "/" + files[i].getName());

							int data = 0;
							while ((data = newf.read()) != -1) {
								oldf.write(data);
							}
							newf.close();
							oldf.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else { // 폴더일경우 재귀함수로 해당폴더를 생성, 경로를 전달함
						File oldpath = new File(copypath + "/" + files[i].getName());
						if (!oldpath.exists()) {
							oldpath.mkdirs();
						}
						copyFileFn(files[i].getPath(), copypath + "/" + files[i].getName()); // 재귀함수
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	
	public void deleteFile(String newDir, String oldDir, GetCopyCacheRequestVO paramVO, String verKey) throws Exception {
		IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getApiName(), paramVO.getVersionKey(), paramVO.getPid());
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		String msg = "";
		final String LOCALPATHNAME = paramVO.getApiName() + "-" + verKey;
		
		  
		try {
			
			File ffRESFILE = new File(newDir);
			File[] newFileList = ffRESFILE.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File files, String name) {
				return name.startsWith(LOCALPATHNAME);
				}
			});
			
			File aaRESFILE = new File(oldDir);
			File[] oldFileList = aaRESFILE.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File files, String name) {
				return name.startsWith(LOCALPATHNAME);
				}
			});
			
			String newFileName = "";
			String oldFileName = "";
			int flag = 0;
			
			
			if (oldFileList != null && oldFileList.length > 0) {
				for (File oldFile : oldFileList) {
					
					oldFileName = oldFile.getName();
					//String[] localArray = oldFileName.split("-");
					
					flag = 0;
					for (File nasFile : newFileList) {
						newFileName = nasFile.getName();
						if(oldFileName.equals(newFileName)) {
							flag = 1;
						}
					}

					if (flag == 0) {
						String localfile = oldFile.getAbsolutePath();
						int deleteflag = fileDelbyLinux(localfile);
						msg = localfile + " 삭제.";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						
						if(deleteflag == 0) {
							msg = localfile + " delete success.";
							imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							msg = localfile + " delete fail.";
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
					}
				}
			}
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage() + "," + methodName + "_getSysdate()");
		}
	}
	
	
	
	public int delFlagFile(String localDir, GetCopyCacheRequestVO paramVO) throws Exception {
		
	/*	final String LOCALPATHNAME = paramVO.getApiName() + "-" + paramVO.getVersionKey();
		
		try {
			File aaRESFILE = new File(localDir);
			File[] localFileList = aaRESFILE.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File files, String name) {
				return name.startsWith(LOCALPATHNAME);
				}
			});

			String localFileName = "";
			if (localFileList.length > 0) {
				for (File localFile : localFileList) {
					localFileName = localFile.getName();
					String[] localArray = localFileName.split("-");

					if (paramVO.getVersionKey().equals(localArray[1])) {
						localFile.delete();
						msg = localFileName + " 삭제.";
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
				}
			}

		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage() + "," + methodName + "_getSysdate()");
		}*/
		
		//find /data/cache2/getNSMnuList_20200500/XD -name "*XD008*.res" -delete\
		int flag = 0;
		try {
			// 파일 복사
			String[] szCommand = {"/bin/sh", "-c", "find " + localDir + " -name '*" + paramVO.getVersionKey() + "*.res' -delete", "echo $?"};
			Process process = Runtime.getRuntime().exec(szCommand);
			process.waitFor();
			flag = process.exitValue();
			process.destroy();
		} catch(IOException e) {
			flag = 1;
		}
		
		return flag;
	}
	
	public void copyFile(String nasFileName, String localFileName, GetCopyCacheResultVO resultVO) throws Exception{
		try {
			FileInputStream nas = new FileInputStream(nasFileName);
			FileOutputStream local = new FileOutputStream(localFileName);

			int data = 0;
			while ((data = nas.read()) != -1) {
				local.write(data);
			}
			nas.close();
			local.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copySuccessChk(String nasFileName, String localFileName, GetCopyCacheResultVO resultVO, GetCopyCacheRequestVO paramVO) throws Exception{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getApiName(), paramVO.getVersionKey(), paramVO.getPid());	
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		File nas = new File(nasFileName);
		File local = new File(localFileName);
		String msg	= "";

		if (nas.exists() && local.exists()) {
			long nasSize = nas.length();
			long localSize = local.length();
			System.out.println("nasSize : " + nasSize + " bytes : " + nas.getAbsoluteFile());
			System.out.println("localSize : " + localSize + " bytes : " + local.getAbsoluteFile());
			msg = "nasSize : " + nasSize + " bytes : " + nas.getAbsoluteFile();
			imcsLog.serviceLog(msg, methodName, methodLine);
			msg = "localSize : " + localSize + " bytes : " + local.getAbsoluteFile();
			imcsLog.serviceLog(msg, methodName, methodLine);

			if (nasSize != localSize) {
				resultVO.setFlag("1");
				System.err.println("캐시파일 생성 fail");
			}

		} else {
			System.err.println("파일이 없음...");

		}
	    
	}
	
	public ArrayList<String> verKeyList(String newDir) throws Exception{

		ArrayList<String> result = new ArrayList<String>();;
		try{
			File ffRESFILE = new File(newDir);
			File[] newFileList = ffRESFILE.listFiles();
			int flag = 0;
			
			if (newFileList.length > 0) {
				for(File stempFile : newFileList) {
					File[] fileList = stempFile.listFiles();
					
					if (fileList.length > 0) {
						
						for(File file : fileList) {
							String nasFileName = file.getName();
							String[] array = nasFileName.split("-");
							flag = 0;
							
							if(result.size() > 0) {
								for(int i = 0; i < result.size() ; i++) {
									if(result.get(i).equals(array[1])) {
										flag = 1;
									}
								}
							}
							
							if(flag == 0) {
								result.add(array[1]);
							}
							
						}
					}
				}
			}
		}	catch(DataAccessException e){
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}
	    return result;
	}
	
	public int updateCacheVer(GetCopyCacheRequestVO paramVO, GetCopyCacheResultVO resultVO) throws Exception{
		//버전정보  update 할부분
		int flag = 0;
		try{
			flag = getCopyCacheDao.updateCacheVersion(paramVO);
		}catch(DataAccessException e){
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}
		return flag;
	}
	
	public int insertCacheVer(GetCopyCacheRequestVO paramVO, GetCopyCacheResultVO resultVO) throws Exception{
		//버전정보 insert 할부분
		int flag = 0;
		try{
			flag = getCopyCacheDao.insertCacheVersion(paramVO);
		}catch(DataAccessException e){
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}
		return flag;
	}
	
	/////////////////////////////////////////////////////////////////////////////////리눅스 버전

	
	public int fileDecompressbyLinux(String fileName, String localPath) throws Exception{
		 Process process = null;
	      List<String> fileList = new ArrayList<String>();
	      BufferedReader in = null;
	     
		int flag = 0;
		try {
			// 파일 복사
			
			String[] szCommand = {"/bin/sh", "-c", "tar -xvzf " + fileName + " -C " + localPath};
			process = Runtime.getRuntime().exec(szCommand);
	         in = new BufferedReader(new InputStreamReader(process.getInputStream()));
	         // process.waitFor();
	          
	         String cl = null;
	         while((cl=in.readLine())!=null){
	            fileList.add("/" + cl);
	         }
	         in.close();
	         process.waitFor();
	         process.destroy();
		} catch(IOException e) {
			flag = 1;
			if(null != in) in.close();
	        if(null != process) process.destroy();
	        e.printStackTrace();
		}
		
		return flag;
	}
	
	public int fileCopybyLinux(String nasFileName, String localFileName) throws Exception{
		
		int flag = 0;
		try {
			// 파일 복사
			String[] szCommand = {"/bin/sh", "-c", "cp " + nasFileName + " " + localFileName, "echo $?"};
			Process process = Runtime.getRuntime().exec(szCommand);
			process.waitFor();
			flag = process.exitValue();
			process.destroy();
		} catch(IOException e) {
			flag = 1;
		}
		
		return flag;
	}

	public int fileOverWritebyLinux(String nasFileName, String localFileName) throws Exception {

		int flag = 0;
		try {
			// 파일 복사
			String[] szCommand = { "/bin/sh", "-c", "cp -rp " + nasFileName + "/* " + localFileName, "echo $?" };
			Process process = Runtime.getRuntime().exec(szCommand);
			process.waitFor();
			flag = process.exitValue();
			process.destroy();
		} catch (IOException e) {
			flag = 1;
		}
		return flag;
	}
	
	public int fileDeletebyLinux(String nasFileName) throws Exception{
		
		int flag = 0;
		try {
			// 파일 복사
			String[] szCommand = {"/bin/sh", "-c", "rm -r " + nasFileName, "echo $?"};
			Process process = Runtime.getRuntime().exec(szCommand);
			process.waitFor();
			flag = process.exitValue();
			process.destroy();
		} catch(IOException e) {
			flag = 1;
		}
		
		return flag;
	}
	
	public int fileDelbyLinux(String localFileName) throws Exception{
		
		int flag = 0;
		try {
			// 파일 복사
			String[] szCommand = {"/bin/sh", "-c", "rm " + localFileName, "echo $?"};
			Process process = Runtime.getRuntime().exec(szCommand);
			process.waitFor();
			flag = process.exitValue();
			process.destroy();
		} catch(IOException e) {
			flag = 1;
		}
		
		return flag;
	}

}
