package kr.co.wincom.imcs.api.setNSCHWatchTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetNSCHWatchTimeServiceImpl implements SetNSCHWatchTimeService {
	private Log imcsLogger = LogFactory.getLog("API_setNSCHWatchTime");
	
	@Autowired
	private CommonService commonService;
	
	
	@Override
	public SetNSCHWatchTimeResultVO setNSCHWatchTime(SetNSCHWatchTimeRequestVO paramVO){
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		SetNSCHWatchTimeResultVO resultVO = new SetNSCHWatchTimeResultVO();
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		imcsLog.debugLog(methodName + " service call");
		
		String flag		= "";
		String errMsg	= "";
		String msg		= "";
		int resultSet = 0;
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{			
			
			resultSet = makeCacheFile(paramVO);
			
			tp1 = System.currentTimeMillis();
			
			imcsLog.timeLog("유료 채널 시청 이력 파일 생성", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			if(resultSet == 0){
				resultVO.setFlag("0");
				resultVO.setErrMsg("시청 이력 저장에 성공하였습니다.");
			}else{
				resultVO.setFlag("1");
				resultVO.setErrMsg("시청 이력 저장에 실패하였습니다.");
				
				paramVO.setResultCode("20000001");				
			}
			
	    	
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;

			resultVO.setFlag("1");
			resultVO.setErrMsg("시청 이력 저장에 실패하였습니다.");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(flag, errMsg, "", resultVO);
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_SetNSCHWatchTime) + "]" + String.format("%-5s",  " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ " snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			resultVO.setResultCode(paramVO.getResultCode());
		}
		
		return resultVO;
	}	
	
	public Integer makeCacheFile(SetNSCHWatchTimeRequestVO paramVO) throws Exception {
	      StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
	      String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
	      String methodName = oStackTrace.getMethodName();
	      IMCSLog imcsLog   = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());   
	      String LOCALPATH = "";
	      String NASPATH = ""; 
	      String DATFILE = ""; // 파일생성 결로
	      String idolKeyFileName = ""; // 키파일명
	      Integer result_set = 0;
	      String msg = "";
	      
	      try {
	         

	         
	          
	         LOCALPATH = commonService.getCachePath("LOCAL", "idollive-watch", imcsLog);
	         NASPATH = commonService.getCachePath("NAS", "idollive-watch", imcsLog);
	         
	         
	         // 하위 폴더 경로 생성
	         String localDirName = String.format("%s/%s/%s", LOCALPATH, paramVO.getAlbumId(),
	               paramVO.getSaId().substring(paramVO.getSaId().length() - 4, paramVO.getSaId().length()));
	         String nasDirName = String.format("%s/%s/%s", NASPATH, paramVO.getAlbumId(),
	               paramVO.getSaId().substring(paramVO.getSaId().length() - 4, paramVO.getSaId().length()));
	         
	         LOCALPATH = localDirName;
	         NASPATH = nasDirName;
	         
	         File LOCAL_DIR = new File(LOCALPATH);
	         if(!LOCAL_DIR.exists()){
	            LOCAL_DIR.mkdirs();
	         }
	         
	         File NAS_DIR = new File(NASPATH);
	         if(!NAS_DIR.exists()){
	            NAS_DIR.mkdirs();
	         }
	         
	         idolKeyFileName = paramVO.getSaId() + "-" + paramVO.getStbMac() + "-" + paramVO.getAlbumId() + "-" 
	        		 			+ paramVO.getServiceId() + "-" + paramVO.getWatchDate() + "-" + paramVO.getRunTime() + "-"
	        		 			+ paramVO.getnWatchYn() + "-" + paramVO.getnSaId() + "-" + paramVO.getnStbMac() + "-" 
	        		 			+ paramVO.getAppType()
	        		 			+ ".dat";
	         
	         DATFILE = LOCALPATH + "/" + idolKeyFileName;
	         // 파일 쓰기
	         int nRetVal = FileUtil.fileWrite(DATFILE, "", false);
	         
	         if (nRetVal == 1) {
	            msg = " File [" + DATFILE + "] WRITE Finished";
	            imcsLog.serviceLog(msg, methodName, methodLine);
	         } else {
	        	result_set = -1;
	            msg = " File [" + DATFILE + "] WRITE Failed";	            
	            imcsLog.serviceLog(msg, methodName, methodLine);
	         }
	         
	         copyFile(LOCALPATH + "/" + idolKeyFileName , NASPATH + "/" + idolKeyFileName); // local 에서 nas 로 복사
	         
	      } catch (Exception e) {
	         result_set = -1;
	      }
	      
	      return result_set;
	   }
	   
	   
	   public void copyFile(String localFileName, String nasFileName) throws Exception{
	      try {
	         FileInputStream local = new FileInputStream(localFileName);
	         FileOutputStream nas = new FileOutputStream(nasFileName);

	         int data = 0;
	         while ((data = local.read()) != -1) {
	            nas.write(data);
	         }
	         nas.close();
	         local.close();

	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
}
