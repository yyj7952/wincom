package kr.co.wincom.imcs.api.getNSChList;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSChListServiceImpl implements GetNSChListService {
	private Log imcsLogger = LogFactory.getLog("API_getNSChList");
	
	@Autowired
	private GetNSChListDao getNSChListDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSMakeChListService getNSMakeChListService;
	
//	@Autowired
//	private NosqlResultCache cache;
//	@Resource
//	public void setNosqlResultCache (NosqlResultCache nosqlResultCache) {
//		this.cache = nosqlResultCache;
//	}
	
//	public void getNSChList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	private static String regexIPv4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
	private static String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	private static String regexIPv4andIPv6 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	
	@Override
	public GetNSChListResultVO getNSChList(GetNSChListRequestVO paramVO){
//		this.getNSChList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSChListResultVO resultListVO = new GetNSChListResultVO();
		GetNSChListResponseVO tempVO = new GetNSChListResponseVO();
		List<GetNSChListResponseVO> tempListVO = new ArrayList<GetNSChListResponseVO>();
		ConcertInfoVO crtTempVO = new ConcertInfoVO();
		List<ConcertInfoVO> crtTempListVO = new ArrayList<ConcertInfoVO>();
		
		String LOCKFILE = "";
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID020.split("/")[1], imcsLog);
		
		//2018.08.23 폴더로 나누는 방법으로 변경
		String foldering_dir = String.format("p%sv%sf%sn%s", paramVO.getPooqYn(), paramVO.getHdtvViewGb(), paramVO.getFiveChYn(), paramVO.getSvcNode());
		LOCALPATH = String.format("%s/%s", LOCALPATH, foldering_dir);

		
		msg = "LOCALPATH : " + LOCALPATH;
		imcsLog.serviceLog(msg, methodName, methodLine);

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		
		String szChnlImgSvrip = "";
		
		try {
			szChnlImgSvrip	= commonService.getImgReplaceUrl2("img_chnl_server", "getNSChList");
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID020, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
		try{
			// 2019.10.04 - IPv6듀얼스택지원 : IPv6PrefixIP 정보 조회
			List<String> IPv6Prefix = commonService.getIPv6PrefixIP(ImcsConstants.NSAPI_PRO_ID090.split("/")[1], "L", imcsLog);
			paramVO.setPrefixInternet(IPv6Prefix.get(0));
			paramVO.setPrefixUplushdtv(IPv6Prefix.get(1));
						
			// 테스트 가입자 여부 조회
			long tp111	= System.currentTimeMillis();
			this.getTestSbc(paramVO);
			tp1	= System.currentTimeMillis();
			//System.out.println("검수 STB여부 조회: " + paramVO.getSaId() + " : " + String.valueOf((tp1 - tp111)));
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			// 가입 상품 조회
			this.getmProdId(paramVO);
			long tp11	= System.currentTimeMillis();
			//System.out.println("가입 상품 조회: " + paramVO.getSaId() + " : "  + String.valueOf((tp11 - tp1)));
			imcsLog.timeLog("가입 상품 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			
			String compFileName = "_t" + paramVO.getTestSbc()    + "_p"+paramVO.getPooqYn()   + "_m" + paramVO.getmProdId()
			                     +"_v" + paramVO.getHdtvViewGb() + "_f"+paramVO.getFiveChYn() + ".res";
			
			msg = "compFileName : " + compFileName;
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			
			File fLOCALPATH = new File(LOCALPATH);
			
			if(!fLOCALPATH.exists()){
				fLOCALPATH.mkdir();
			}
			
			String chkFileName = "";
			
			File[] files = fLOCALPATH.listFiles();
			
			List<String> list = new ArrayList<String>();
			
			
			try{
				if(files.length > 0){			
					for(int i = 0; i < files.length; i++){
						if(files[i].getName().toString().indexOf(compFileName) > -1){					
							msg = files[i].getName();
							
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							list.add(files[i].getName());
						}
					}
					if(list != null && list.size() > 0){
	//					List<String> list2 = new ArrayList<String>(list);
	//					Collections.reverse(list2);
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
				msg = " getNSMakeChLists Cache File Empty";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			msg = "chkFileName : " + chkFileName;
			
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			//List<String> list = Arrays.asList(files);
			
			
			
			if(list.size() > 4){
				msg = " [WARN] getNSMakeChList res file count [" + list.size() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			
			if(commonService.chkCacheFile(chkFileName, imcsLog) || list.size() == 0){
			
				msg = "getNSMakeChLists excute";
				imcsLog.serviceLog(msg, methodName, methodLine);
				getNSMakeChListService.getNSMakeChLists(paramVO);
				imcsLog.timeLog("getNSMakeChLists excute", String.valueOf((tp1 - tp_start)), methodName, methodLine);
				
//				list.add("");

				msg = " File [" + RESFILE + "] 리턴케이스0 ";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				long tt2 = System.currentTimeMillis();
				imcsLog.timeLog("리턴케이스1:", String.valueOf((tt2 - tp_start)), methodName, methodLine);
				
				if(list.size() == 0)
				{
					String result	= "1|FILE OPEN FAIL|";
					resultListVO.setResultHeader(result);
					return resultListVO;
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
						
						long tt1 = System.currentTimeMillis();
						String[] arrRowResult = null;
						
						for(int i = 0; i < arrResult.length; i++) {
							if(i == 0) continue;	// 첫번째는 헤더정보이기 때문에, 넘어간다.
							
							tempVO = new GetNSChListResponseVO();
							
							arrResult[i] = arrResult[i].replaceAll("img_chnl_server", szChnlImgSvrip);
						
							arrRowResult = arrResult[i].split(ImcsConstants.COLSEP_SPLIT);
							
							if(arrRowResult.length < 9) {
								crtTempVO = new ConcertInfoVO();
								crtTempVO.dataParcing2(arrRowResult);
							} else {
								tempVO.dataParcing(arrRowResult);
							}
							
							
							if(tempVO.getTimeAppYn().equals("Y"))
							{
								tempVO.setLiveVodIpv6Server1(ipv6_change(paramVO, tempVO.getLiveVodIpv6Server1(), "", tempVO.getLiveTimeServer1(), tempVO.getLiveServerNode1(), "N"));
								tempVO.setLiveVodIpv6Server2(ipv6_change(paramVO, tempVO.getLiveVodIpv6Server2(), "", tempVO.getLiveTimeServer2(), tempVO.getLiveServerNode2(), "N"));
								tempVO.setLiveVodIpv6Server3(ipv6_change(paramVO, tempVO.getLiveVodIpv6Server3(), "", tempVO.getLiveTimeServer3(), tempVO.getLiveServerNode3(), "N"));
							}
							
							if(arrRowResult.length < 9) {
								crtTempListVO.add(crtTempVO);
							} else {
								tempListVO.add(tempVO);
							}
						}
						long tt2 = System.currentTimeMillis();
						imcsLog.timeLog("파일치환:", String.valueOf((tt2 - tt1)), methodName, methodLine);
						
						
						msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						msg = " File [" + RESFILE + "] 리턴케이스1 ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						String resultHeader	= "0||";
						resultListVO.setResultHeader(resultHeader);
						resultListVO.setList(tempListVO);
						resultListVO.setCstList(crtTempListVO);
//						FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
						
					}else{
						msg = " File [" + RESFILE + "] checkResFile Failed ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						msg = " File [" + RESFILE + "] 리턴케이스2 ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						res.delete();
					}	
				}else{
					
					msg = " File [" + RESFILE + "] 리턴케이스3 ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					msg = " File [" + RESFILE + "] open Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}


			
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			// NosqlCacheType.HBASE_WR.ordinal(), NosqlCacheType.USERDB.ordinal() 이거는?
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID020) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 검수 STB 여부 조회
	 * @param paramVO
	 * @return
	 */
    public void getTestSbc(GetNSChListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<String> list   = new ArrayList<String>();
		
		try {
			list = getNSChListDao.getTestSbc(paramVO);
			
			if( list == null || list.isEmpty()){
				paramVO.setTestSbc("N");				
			} else {
				paramVO.setTestSbc(StringUtil.nullToSpace(list.get(0)));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			paramVO.setTestSbc("N");
		}
		
    }
    
    
    
    /**
	 * 가입 상품 조회
	 * @param paramVO
	 * @return
	 */
    public void getmProdId(GetNSChListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<String> list   = new ArrayList<String>();
		
		try {
			
			list = getNSChListDao.getmProdId(paramVO);
			
			if( list != null && !list.isEmpty()){
				paramVO.setmProdId(StringUtil.nullToSpace(list.get(0)));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
    }
	

    
	/**
	 * IPv4 -> IPv6로 변환
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	ipv6_change
	 */
	public String ipv6_change(GetNSChListRequestVO paramVO, String ipv6_ip, String ipv6_port, String ipv4_ip, String cdn_type, String fourd_replay) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		String szMsg = ""; 
		
		String result_ipv6 = "";
		String ipv4_protocol = "";
		String ipv4_port = "";
		String[] temp_ipv4_split = null;
		
		try{
			if(paramVO.getIpv6Flag().equals("Y"))
			{
				if(!ipv4_ip.equals("") && ipv6_ip.equals(""))
				{
					// 스트리밍 : case when :fourD_replay_yn = 'N' then 'http://'||B.stb_play_ip1||case when :node_group = 'Z' then '' else ':' || b.SUB_NODE_PORT1 end || '/' else 'rtsp://'||B.stb_play_ip1||':80/' end,
					// 다운로드 : 'http://'||B.stb_play_ip1||case when :node_group = 'Z' then '' else ':' || b.SUB_NODE_PORT1 end || '/',
					// AWS CDN은 도메인 방식이므로, 만약 IPv6컬럼에 값이 없을 경우에는 기존 IPv4의 도메인을 그대로 복사해준다.
					if(!cdn_type.equals("Z"))
					{
						temp_ipv4_split = ipv4_ip.split("/|:");
						for(int i = 0 ; i < temp_ipv4_split.length ; i++)
						{
							if(i == 0) ipv4_protocol = temp_ipv4_split[i];
							if(i == temp_ipv4_split.length - 1) ipv4_port = temp_ipv4_split[i];
								
							if(checkIp(temp_ipv4_split[i]) == 1)
							{
								String[] temp_ip = temp_ipv4_split[i].toString().split("\\.");
								for(int j = 0 ; j < temp_ip.length ; j++)
								{
									if(j != 0 && j % 2 == 0)
									{
										result_ipv6 = result_ipv6 + ":";
									}
									
									if(j == 0)
									{
//										result_ipv6 = Integer.toHexString(Integer.parseInt(temp_ip[j])).toUpperCase();
										result_ipv6 = String.format("%02X", Integer.parseInt(temp_ip[j]));
									}
									else
									{
//										result_ipv6 = result_ipv6 + Integer.toHexString(Integer.parseInt(temp_ip[j])).toUpperCase();
										result_ipv6 = result_ipv6 + String.format("%02X", Integer.parseInt(temp_ip[j]));
									}
								}
							}
						}
						
						if(cdn_type.equals("I"))
						{
	//						result_ipv6 = ipv4_protocol + "://" + paramVO.getPrefixInternet() + result_ipv6 + ":" + ipv4_port + "/";
							result_ipv6 = "[" + paramVO.getPrefixInternet() + result_ipv6 + "]";
						}
						else
						{
	//						result_ipv6 = ipv4_protocol + "://" + paramVO.getPrefixUplushdtv() + result_ipv6 + ":" + ipv4_port + "/";
							result_ipv6 = "[" + paramVO.getPrefixUplushdtv() + result_ipv6 + "]";
						}
					}
					else
					{
						result_ipv6 = ipv4_ip;
					}
				}
				else if(!ipv6_ip.equals(""))
				{
					if(fourd_replay.equals("N") || cdn_type.equals("Z"))
					{
	//					result_ipv6 = "http://" + ipv6_ip + ":" + ipv6_port + "/";
						result_ipv6 = "[" + ipv6_ip + "]";
					}
					else
					{
	//					result_ipv6 = "rtsp://" + ipv6_ip + ":80/";
						result_ipv6 = "[" + ipv6_ip + "]";
					}
				}
			}
			else
			{
				result_ipv6 = "";
			}
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] IPv4 to IPv6 convert fail (msg : " + e.getMessage() + ")";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}

		return result_ipv6;
	}
	
	public static int checkIp(String ipAddress) {
        Pattern pattern;
        int result = 0;
 
        pattern = Pattern.compile(regexIPv4andIPv6);
        if (ipAddress == null || pattern.matcher(ipAddress).matches() == false) {
//            System.out.println("유효하지 않은 IP 주소입니다.");
        	result = -1;
 
        } else {
            // IPv4
            pattern = Pattern.compile(regexIPv4);
            if (pattern.matcher(ipAddress).matches() == true) {
//                System.out.println("IPv4 주소입니다.");
            	result = 1;
            }
 
            // IPv6
            pattern = Pattern.compile(regexIPv6);
            if (pattern.matcher(ipAddress).matches() == true) {
//                System.out.println("IPv6 주소입니다.");
            	result = 2;
            }
        }
        return result;
    }
}
