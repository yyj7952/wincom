package kr.co.wincom.imcs.api.getNSMakeNodeList;

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
public class GetNSMakeNodeListServiceImpl implements GetNSMakeNodeListService {
	private Log imcsLogger = LogFactory.getLog("API_getNSMakeNodeList");
	
	@Autowired
	private GetNSMakeNodeListDao getNSMakeNodeListDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSMakeNodeLists(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
	
	String SERVER_CONF = ImcsProperties.getProperty("filepath.server");
	Properties serverManager = GlobalCom.getPropertyFile(SERVER_CONF);
	private String sendVtsServerIPs = StringUtil.replaceNull(serverManager.getProperty("getNSMakeNodeLi_cvts_server_ip"),"http://123.140.17.253"); //Make요청 VTS 서버 IP
	private String sendinternalizationServerIPs = StringUtil.replaceNull(serverManager.getProperty("getNSMakeNodeLi_server_ip"),"http://123.140.17.253"); //Make요청 VTS 서버 IP

//	private IMCSLog imcsLog = null;	

	@SuppressWarnings("rawtypes")
	public GetNSMakeNodeListResultVO getNSMakeNodeList(GetNSMakeNodeListRequestVO paramVO)	{
//		this.getNSMakeNodeLists(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSMakeNodeListResultVO resultListVO = new GetNSMakeNodeListResultVO();
		List<GetNSMakeNodeListResponseVO> resultVO = new ArrayList<GetNSMakeNodeListResponseVO>();
		
		//캐시 폴더 경로
		String LOCALPATH      = "";
		String NASPATH        = "";
		String CVTSNASPATH        = "";

		// 파일명 관련 파라미터
		String NAS_RESFILE		  = ""; //NAS res
		String NAS_RESFILE_VOD    = ""; //NAS res vod
		
		String CVTS_NAS_RESFILE		  = ""; //NAS res
		String CVTS_NAS_RESFILE_VOD    = ""; //NAS res vod
		
		String LOCAL_RESFILE		  = ""; //LOCAL res
		String LOCAL_RESFILE_VOD    = ""; //LOCAL res vod
		
		String NAS_LOCKFILE		  = ""; //NAS lock
		
		String LOCAL_LOCKFILE  = ""; //LOCAL lock
		String LOCAL_LOCKFILE_VOD   = ""; //LOCAL lock vod
		
		
		int server_flag = 0; // 0 : 캐시 만드는 서버 , 1 : 캐시 복사하는 서버, 2 : C에서 호출받았을 때 캐시 생성 후 내재화 서버에만 복사하도록 호출
		int wait_cnt    = 0;
		int loop_cnt    = 0;
		
		LOCALPATH  = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID050.split("/")[1], imcsLog);
		NASPATH    = commonService.getCachePath("NAS", ImcsConstants.NSAPI_PRO_ID050.split("/")[1], imcsLog);
		CVTSNASPATH    = commonService.getCachePath("CVTSNAS", ImcsConstants.NSAPI_PRO_ID050.split("/")[1], imcsLog);
		
		NAS_LOCKFILE          = String.format("%s/getNSNodeList.lock", NASPATH); //NAS lock 파일
				
		NAS_RESFILE           = String.format("%s/getNSChNodeList.res", NASPATH); //NAS res 파일
		NAS_RESFILE_VOD       = String.format("%s/getNSVodNodeList.res", NASPATH); //NAS vod-res 파일
		CVTS_NAS_RESFILE           = String.format("%s/getNSChNodeList.res", CVTSNASPATH); //NAS res 파일
		CVTS_NAS_RESFILE_VOD       = String.format("%s/getNSVodNodeList.res", CVTSNASPATH); //NAS vod-res 파일
						
		LOCAL_RESFILE         = String.format("%s/getNSChNodeList.res", LOCALPATH); //Local res 파일
		LOCAL_RESFILE_VOD     = String.format("%s/getNSVodNodeList.res", LOCALPATH); //Local vod-res 파일
		LOCAL_LOCKFILE        = String.format("%s/getNSChNodeList.lock", LOCALPATH); //Local vod-res 파일
		LOCAL_LOCKFILE_VOD    = String.format("%s/getNSVodNodeList.lock", LOCALPATH); //Local vod-res 파일
		
		server_flag = Integer.parseInt(paramVO.getServerFlag());
		
		try{
			//NAS
			File fNASLOCKFILE	= null;

			//LOCAL
			File fLOCALRESFILE	= null;
			File fLOCALLOCKFILE	= null;
			
			String szMsg			= "";
			
			File NAS_DIR = new File(NASPATH);
			if(!NAS_DIR.exists()){
				NAS_DIR.mkdir();
			}

			File LOCAL_DIR = new File(LOCALPATH);
			if(!LOCAL_DIR.exists()){
				LOCAL_DIR.mkdir();
			}
			
			fNASLOCKFILE      = new File(NAS_LOCKFILE); //lfname_NAS (NAS lock 파일)
			
			if(fNASLOCKFILE.exists()) { // 1. Nas lock이 있으면
				while(true){
					if(wait_cnt == 5)
			    	{
						break;
			    	}
				
					if(NAS_DIR.exists()) { // 1. Nas lock이 없으면
						try {
							String[] szCommand = {"/bin/sh", "-c", "cp " + NAS_RESFILE + " " + LOCAL_RESFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + NAS_RESFILE + "] copy [" + LOCAL_RESFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String[] szCommand2 = {"/bin/sh", "-c", "cp " + NAS_RESFILE_VOD + " " + LOCAL_RESFILE_VOD};
							p = Runtime.getRuntime().exec(szCommand2);
							
							szMsg = " File [" + NAS_RESFILE_VOD + "] copy [" + LOCAL_RESFILE_VOD + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String result = FileUtil.fileRead(NAS_RESFILE, "UTF-8");
							resultListVO.setResult(result);
							
							FileUtil.unlock(NAS_LOCKFILE, imcsLog);
							
				            return resultListVO;
						} catch(Exception e) {
							szMsg = " cp cache error!!! ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}else{
						Thread.sleep(1000);
						wait_cnt++;
					}					
				}				
			}
			
			if (server_flag == 1 && NAS_DIR.exists()) {
				
				try {
					// 2. Nas에 cache가 있으면
					String[] szCommand = {"/bin/sh", "-c", "cp " + NAS_RESFILE + " " + LOCAL_RESFILE};
					Process p = Runtime.getRuntime().exec(szCommand);
					
					szMsg = " File [" + NAS_RESFILE + "] copy [" + LOCAL_RESFILE + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					String[] szCommand2 = {"/bin/sh", "-c", "cp " + NAS_RESFILE_VOD + " " + LOCAL_RESFILE_VOD};
					p = Runtime.getRuntime().exec(szCommand2);
					
					szMsg = " File [" + NAS_RESFILE_VOD + "] copy [" + LOCAL_RESFILE_VOD + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
				} catch (Exception e) {
					szMsg = " cp cache error!!!";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}

			} else {
				// 3. Nas lock파일 생성
				if(FileUtil.lock(NAS_LOCKFILE, imcsLog)){
					if(!fNASLOCKFILE.exists()){
						szMsg = " lock File write fail [" + NAS_LOCKFILE + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						
					}else{
						szMsg = " lock File exist [" + NAS_LOCKFILE + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);	
						
					}
				}
				
				loop_cnt =0;
				String fname     = "";
				String lfname    = "";
				String fname_NAS = "";
				String fname_CVTS_NAS = "";
				
				while(true)
				{
					if (loop_cnt == 2) break; //루프빠져나감
					
					if (loop_cnt == 0) {
						fname  = LOCAL_RESFILE;
						lfname = LOCAL_LOCKFILE;
						fname_NAS = NAS_RESFILE;
						fname_CVTS_NAS = CVTS_NAS_RESFILE;
						
						fLOCALLOCKFILE = new File(lfname);
					} else {
						fname  = LOCAL_RESFILE_VOD;
						lfname = LOCAL_LOCKFILE_VOD;
						fname_NAS = NAS_RESFILE_VOD;
						fname_CVTS_NAS = CVTS_NAS_RESFILE_VOD;
						
						fLOCALLOCKFILE = new File(lfname);
					}
					
					fLOCALRESFILE	   = new File(fname);
					
					// 1. local lock이 있으면
					if (fLOCALLOCKFILE.exists()) {
						szMsg = " lock File exist [" + NAS_LOCKFILE + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						GetNSMakeNodeListResultVO _resultVO = new GetNSMakeNodeListResultVO();
						_resultVO.setResult("");
						return _resultVO;
					}
					
					// 2. local에 cache가 있으면
					if (fLOCALRESFILE.exists()) {
						try {
							String[] szCommand = {"/bin/sh", "-c", "cp " + fname + " " + fname+".bak"};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + fname + "] copy [" + fname + ".bak" + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							//delCacheFile(NASPATH, compFileName, paramVO);
							
						} catch (Exception e) {
							szMsg = " cp cache error!!!";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}
					
					// 3. local lock파일 생성
					if(!FileUtil.lock(lfname, imcsLog)){
						szMsg = " lock File creat [" + lfname + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);	
					}
					
					/* 실제 쿼리 변경 */
					if (loop_cnt == 0) {
						resultVO = getNSMakeNodeListDao.getNodeLists1(paramVO);
					} else {
						resultVO = getNSMakeNodeListDao.getNodeLists2(paramVO);
					}
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					for (GetNSMakeNodeListResponseVO node : resultVO) {
						if (node.getLoadbalancingYn().equals("N")) {
							node.setLoadbalancingSort("");
						}
						
						if (loop_cnt == 0) {
							node.setResultType("CHNL");
						} else {
							node.setResultType("VOD");
						}
					}
					
					loop_cnt++;
					
					resultListVO.setList(resultVO);
					// 파일 쓰기
					int nRetVal = FileUtil.fileWrite(fname, resultListVO.toString(), false);
					
					szMsg = " nRetVal : " + nRetVal;
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					if(nRetVal == 1) {
						fLOCALRESFILE = new File(fname);
						
						if(fLOCALRESFILE.length() != 0 ){
							msg = " File [" + fname + "] WRITE [" + fLOCALRESFILE.length() + "] bytes Finished";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							try {
								String[] szCommand = {"/bin/sh", "-c", "chmod 666 " + fname};
								Process p = Runtime.getRuntime().exec(szCommand);
								
								szMsg = " File [" + fname + "] chmod 666";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
								
							} catch (Exception e) {
								szMsg = " cache chmod 666 error!!!";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
							}
							
							if(NAS_DIR.exists()){
								try {
									//local파일을 NAS로 Copy
									String[] szCommand = {"/bin/sh", "-c", "cp " + fname + " " + fname_NAS};
									Process p = Runtime.getRuntime().exec(szCommand);
								
									szMsg = " File [" + fname + "] copy [" + fname_NAS + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
									//NAS파일에 권한주기 Start
									String[] szCommand2 = {"/bin/sh", "-c", "chmod 666 " + fname_NAS};
									p = Runtime.getRuntime().exec(szCommand2);
									
									szMsg = " File [" + fname_NAS + "] chmod 666";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									// chmod 777 fname_NAS
									//LOCAL파일에 권한주기 End
									
									if(server_flag == 0)
									{
										//local파일을 NAS로 Copy
										String[] szCommand_cvts = {"/bin/sh", "-c", "cp " + fname + " " + fname_CVTS_NAS};
										Process p_cvts = Runtime.getRuntime().exec(szCommand_cvts);
									
										szMsg = " File [" + fname + "] copy [" + fname_NAS + "]";
										imcsLog.serviceLog(szMsg, methodName, methodLine);
										
										//NAS파일에 권한주기 Start
										String[] szCommand2_cvts = {"/bin/sh", "-c", "chmod 666 " + fname_CVTS_NAS};
										p_cvts = Runtime.getRuntime().exec(szCommand2_cvts);
										
										szMsg = " File [" + fname_NAS + "] chmod 666";
										imcsLog.serviceLog(szMsg, methodName, methodLine);
										// chmod 777 fname_NAS
										//LOCAL파일에 권한주기 End
									}
									
								} catch (Exception e) {
									szMsg = " cp cache error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
							}
							
						} else {
							fLOCALRESFILE.delete();
						}
					}
				}
				
				FileUtil.unlock(LOCAL_LOCKFILE, imcsLog);
				FileUtil.unlock(LOCAL_LOCKFILE_VOD, imcsLog);				
				FileUtil.unlock(NAS_LOCKFILE, imcsLog);
			}
			
			//다른  VTS에 동일 캐시파일 생성 요청
			int httpOkChk = 0;
			try {
				if(server_flag == 0)
				{
					String[] getNSMakeNodeLi_cvts_ip = sendVtsServerIPs.split("[|]");
					
					RestTemplate restTemplate = new RestTemplate();
					String vtsUrl = "";
					String retStr = "";
					
					for (String _vtsurl : getNSMakeNodeLi_cvts_ip) {
						if (!StringUtil.isEmpty(_vtsurl)) {
							vtsUrl = "http://" + _vtsurl + "/servlets/CommSvl?CMD=getNSMakeNodeLi&PARAM=" + paramVO.getStringParam() + "IMCSCLIENT";						
							retStr = restTemplate.getForObject(vtsUrl, String.class);
							
							szMsg = " CVTS URL CALL [" + _vtsurl + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							if (retStr.equals("ERROR")) {
								httpOkChk = 1;
							}
						}
					}
				}
				
				if(server_flag != 1 && server_flag != -1)
				{
					String[] getNSMakeNodeLi_ip = sendinternalizationServerIPs.split("[|]");
					
					RestTemplate restTemplate = new RestTemplate();
					String vtsUrl = "";
					String retStr = "";
					
					for (String _vtsurl : getNSMakeNodeLi_ip) {
						if (!StringUtil.isEmpty(_vtsurl)) {
							vtsUrl = "http://" + _vtsurl + "/servlets/CommSvl?CMD=getNSMakeNodeLi&PARAM=" + paramVO.getStringParam() + "internalizationIMCSCLIENT";						
							retStr = restTemplate.getForObject(vtsUrl, String.class);
							
							szMsg = " internalization URL CALL [" + _vtsurl + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							if (retStr.equals("ERROR")) {
								httpOkChk = 1;
							}
						}
					}
				}
				
			} catch (Exception e) {
				
			} finally {
				
			}
			
			if (httpOkChk == 1) {
				GetNSMakeNodeListResultVO _resultVO = new GetNSMakeNodeListResultVO();
				_resultVO.setResult("ERROR");
				return _resultVO;
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
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID050) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		//return resultListVO;
		return new GetNSMakeNodeListResultVO(); 
	}
	
	
	
//	/**
//	 * 테스트 가입자 여부 조회
//	 * @param paramVO
//	 * @return
//	 */
//    public void getTestSbc(GetNSMakeNodeListRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		
//		List<String> list   = null;
//				
//		try {
//			
//			list = getNSMakeNodeListDao.getTestSbc(paramVO);
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
    
    public void delCacheFile(String file_path, String compValue, GetNSMakeNodeListRequestVO paramVO){
		
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
//    public List<HashMap> getVitualIdInfo(GetNSMakeNodeListResponseVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//    	
//		List<HashMap> list   = new ArrayList<HashMap>();
//		
//		try {
//			
//			list = getNSMakeNodeListDao.getVitualIdInfo(paramVO);
//						
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
//		}
//		
//		return list;
//    }

//	@Override
//	public GetNSMakeNodeListResultVO getNSMakeNodeList(GetNSMakeNodeListRequestVO paramVO) {
//		// TODO Auto-generated method stub
//		return null;
//	}
    
}
