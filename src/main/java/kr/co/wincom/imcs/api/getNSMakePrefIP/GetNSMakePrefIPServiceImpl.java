package kr.co.wincom.imcs.api.getNSMakePrefIP;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.property.ImcsProperties;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.GlobalCom;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class GetNSMakePrefIPServiceImpl implements GetNSMakePrefIPService {
	private Log imcsLogger = LogFactory.getLog("API_getNSMakePrefIP");
	
	@Autowired
	private GetNSMakePrefIPDao getNSMakePrefIPDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSMakePrefIPs(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
	
	String SERVER_CONF = ImcsProperties.getProperty("filepath.server");
	Properties serverManager = GlobalCom.getPropertyFile(SERVER_CONF);
	private String sendVtsServerIPs = StringUtil.replaceNull(serverManager.getProperty("getNSMakeNodeLi_cvts_server_ip"),"http://123.140.17.253"); //Make요청 VTS 서버 IP
	private String sendinternalizationServerIPs = StringUtil.replaceNull(serverManager.getProperty("getNSMakeNodeLi_server_ip"),"http://123.140.17.253"); //Make요청 VTS 서버 IP

//	private IMCSLog imcsLog = null;	

	@SuppressWarnings("rawtypes")
	public GetNSMakePrefIPResultVO getNSMakePrefIP(GetNSMakePrefIPRequestVO paramVO)	{
//		this.getNSMakePrefIPs(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSMakePrefIPResultVO resultListVO = new GetNSMakePrefIPResultVO();
		List<GetNSMakePrefIPResponseVO> resultVO = new ArrayList<GetNSMakePrefIPResponseVO>();
		
		//캐시 폴더 경로
		String LOCALPATH      = "";
		String NASPATH        = "";
		String CVTSNASPATH        = "";

		
		String LOCAL_RESFILE		  = ""; //LOCAL res
		String LOCAL_RESFILE_NEW	  = ""; //LOCAL new res
		String LOCAL_LOCKFILE  = ""; //LOCAL lock
		
		
		int server_flag = 0; // 0 : 캐시 만드는 서버 , 1 : 캐시 복사하는 서버, 2 : C에서 호출받았을 때 캐시 생성 후 내재화 서버에만 복사하도록 호출
		int wait_cnt    = 0;
		
		LOCALPATH  = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID090.split("/")[1], imcsLog);
		
		LOCAL_RESFILE         = String.format("%s/getNSPrefixIP.res", LOCALPATH); //Local res 파일
		LOCAL_RESFILE_NEW     = String.format("%s/getNSPrefixIP.res.new", LOCALPATH); //Local new res 파일
		LOCAL_LOCKFILE        = String.format("%s/getNSPrefixIP.lock", LOCALPATH); //Local vod-res 파일
		
		
		try{

			//LOCAL
			File fLOCALRESFILE_NEW	= null;
			File fLOCALLOCKFILE	= null;
			
			String szMsg			= "";

			String fname     = "";
			String lfname    = "";
			String fname_NEW = "";
			
			fname  = LOCAL_RESFILE;
			fname_NEW  = LOCAL_RESFILE_NEW;
			lfname = LOCAL_LOCKFILE;
			
			fLOCALLOCKFILE = new File(lfname);				
			fLOCALRESFILE_NEW  = new File(fname_NEW);
			
			// 1. local lock이 있으면
			if (fLOCALLOCKFILE.exists()) {
				szMsg = " lock File exist [" + fLOCALLOCKFILE + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				GetNSMakePrefIPResultVO _resultVO = new GetNSMakePrefIPResultVO();
				return _resultVO;
			}
			
			// 2. local lock파일 생성
			if(!FileUtil.lock(lfname, imcsLog)){
				szMsg = " lock File creat [" + lfname + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);	
			}
			
			
			/* 실제 쿼리 변경 */
			resultVO = getNSMakePrefIPDao.getPrefixIP(paramVO);
				
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			for (GetNSMakePrefIPResponseVO prefixIP : resultVO) {
				resultListVO.setList(resultVO);
			}
			
			// 파일 쓰기
			int nRetVal = FileUtil.fileWrite(fname_NEW, resultListVO.toString(), false);
			
			szMsg = " nRetVal : " + nRetVal;
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			if(nRetVal == 1) {
				fLOCALRESFILE_NEW = new File(fname_NEW);
				
				if(fLOCALRESFILE_NEW.length() != 0 ){
					msg = " File [" + fname_NEW + "] WRITE [" + fLOCALRESFILE_NEW.length() + "] bytes Finished";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					// 2. .new 파일을 .res파일로 복사
					if (fLOCALRESFILE_NEW.exists()) {
						try {
							String[] szCommand = {"/bin/sh", "-c", "cp " + fname_NEW + " " + fname};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + fname_NEW + "] copy [" + fname + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String[] szCommand2 = {"/bin/sh", "-c", "chmod 666 " + fname};
							p = Runtime.getRuntime().exec(szCommand2);
							
							szMsg = " File [" + fname + "] chmod 666";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
						} catch (Exception e) {
							szMsg = " cp or chmod 666 cache error!!!";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}
					
					// 신규 파일 삭제
//					FileUtil.unlock(LOCAL_RESFILE_NEW, imcsLog);
					
				} else {
					fLOCALRESFILE_NEW.delete();
				}
			}
		
			FileUtil.unlock(LOCAL_LOCKFILE, imcsLog);			
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , ie.getClass().getName() + ":" +  ie.getMessage());
			
			FileUtil.unlock(LOCAL_LOCKFILE, imcsLog);
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , e.getClass().getName() + ":" +  e.getMessage());
			
			FileUtil.unlock(LOCAL_LOCKFILE, imcsLog);
		} finally{
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID090) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		//return resultListVO;
		return new GetNSMakePrefIPResultVO(); 
	}
	
	
	
//	/**
//	 * 테스트 가입자 여부 조회
//	 * @param paramVO
//	 * @return
//	 */
//    public void getTestSbc(getNSMakePrefIPRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		
//		List<String> list   = null;
//				
//		try {
//			
//			list = getNSMakePrefIPDao.getTestSbc(paramVO);
//			
//			if( list == null || list.isEmpty()) {
//				paramVO.setTestSbc("N");				
//			} else {
//				paramVO.setTestSbc(list.get(0));
//			}
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
//			paramVO.setTestSbc("N");
//		}
//    }
    
    public void delCacheFile(String file_path, String compValue, GetNSMakePrefIPRequestVO paramVO){
		
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
    
	/**
	 * 옴니뷰 정보  조회
	 * @param paramVO
	 * @return List<HashMap>
	 */
//    public List<HashMap> getVitualIdInfo(getNSMakePrefIPResponseVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//    	
//		List<HashMap> list   = new ArrayList<HashMap>();
//		
//		try {
//			
//			list = getNSMakePrefIPDao.getVitualIdInfo(paramVO);
//						
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
//		}
//		
//		return list;
//    }

//	@Override
//	public getNSMakePrefIPResultVO getNSMakePrefIP(getNSMakePrefIPRequestVO paramVO) {
//		// TODO Auto-generated method stub
//		return null;
//	}
    
}
