package kr.co.wincom.imcs.api.getNSNodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSNodeListServiceImpl implements GetNSNodeListService {
	private Log imcsLogger = LogFactory.getLog("API_getNSNodeList");
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSNodeList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	private static String regexIPv4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
	private static String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	private static String regexIPv4andIPv6 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	
	@Override
	public GetNSNodeListResultVO getNSNodeList(GetNSNodeListRequestVO paramVO){
//		this.getNSNodeList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSNodeListResultVO resultListVO = new GetNSNodeListResultVO();
		GetNSNodeListResponseVO tempVO = new GetNSNodeListResponseVO();
		
		//캐시 폴더 경로
		String LOCALPATH      = "";
		String NASPATH        = "";
		
		// 파일명 관련 파라미터
		String NAS_RESFILE		  = ""; //NAS res
		String NAS_RESFILE_VOD    = ""; //NAS res vod
		
		String LOCAL_RESFILE		  = ""; //LOCAL res
		String LOCAL_RESFILE_VOD    = ""; //LOCAL res vod
		
		String NAS_LOCKFILE		  = ""; //NAS lock
		String NAS_LOCKFILE_VOD   = ""; //NAS lock vod
		
		String LOCAL_LOCKFILE  = ""; //LOCAL lock
		String LOCAL_LOCKFILE_VOD   = ""; //LOCAL lock vod

		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID050.split("/")[1], imcsLog);
		NASPATH   = commonService.getCachePath("NAS", ImcsConstants.NSAPI_PRO_ID050.split("/")[1], imcsLog);
		
		NAS_RESFILE           = String.format("%s/getNSChNodeList.res", NASPATH); //NAS res 파일
		NAS_RESFILE_VOD       = String.format("%s/getNSVodNodeList.res", NASPATH); //NAS vod-res 파일
		NAS_LOCKFILE          = String.format("%s/getNSNodeList.lock", NASPATH); //NAS lock 파일
		NAS_LOCKFILE_VOD      = String.format("%s/getNSVodNodeList.lock", NASPATH); //NAS lock 파일
				
		LOCAL_RESFILE         = String.format("%s/getNSChNodeList.res", LOCALPATH); //Local res 파일
		LOCAL_RESFILE_VOD     = String.format("%s/getNSVodNodeList.res", LOCALPATH); //Local vod-res 파일
		LOCAL_LOCKFILE        = String.format("%s/getNSVodNodeList.lock", LOCALPATH); //Local vod-res 파일
		LOCAL_LOCKFILE_VOD    = String.format("%s/getNSVodNodeList.lock", LOCALPATH); //Local vod-res 파일
		
		//LOCAL
		File fLOCALRESFILE	   = new File(LOCAL_RESFILE);
		File fLOCALRESFILE_VOD = new File(LOCAL_RESFILE_VOD);
		File fLOCALLOCKFILE = new File(LOCAL_LOCKFILE);
		File fLOCALLOCKFILE_VOD = new File(LOCAL_LOCKFILE_VOD);
		
		String fname = "";
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		
		try{
			// 2019.10.04 - IPv6듀얼스택지원 : IPv6PrefixIP 정보 조회
			List<String> IPv6Prefix = commonService.getIPv6PrefixIP(ImcsConstants.NSAPI_PRO_ID090.split("/")[1], "L", imcsLog);
			paramVO.setPrefixInternet(IPv6Prefix.get(0));
			paramVO.setPrefixUplushdtv(IPv6Prefix.get(1));
						
			tp1	= System.currentTimeMillis();
			
			if (paramVO.getCallType().equals("C") || paramVO.getCallType().equals("A")) {
				fname = LOCAL_RESFILE;
				if (fLOCALLOCKFILE.exists()) {
					fname = fname + ".bak";
				}
				
				fLOCALRESFILE = new File(fname);
				if(fLOCALRESFILE.exists()) {
					String result = FileUtil.fileRead(fname, "UTF-8");

					if(!"".equals(result)) {
						String[] arrResult	= result.split(ImcsConstants.ROWSEP);
						
						result	= "";
						
						long tt1 = System.currentTimeMillis();
						String[] arrRowResult = null;
						
						for(int i = 0; i < arrResult.length; i++) {
							tempVO = new GetNSNodeListResponseVO();
							//arrResult[i] = arrResult[i].replaceAll("img_chnl_server", szChnlImgSvrip);
							arrRowResult = arrResult[i].split(ImcsConstants.COLSEP_SPLIT);
							tempVO.dataParcing(arrRowResult);
							tempVO.setIpv6LocalPlayIp(ipv6_change(paramVO, tempVO.getIpv6LocalPlayIp(), "", tempVO.getLocalPlayIp(), tempVO.getIpv6CdnType1(), "N"));
							tempVO.setIpv6NearPlayIp(ipv6_change(paramVO, tempVO.getIpv6NearPlayIp(), "", tempVO.getNearPlayIp(), tempVO.getIpv6CdnType2(), "N"));
							tempVO.setIpv6CenterPlayIp(ipv6_change(paramVO, tempVO.getIpv6CenterPlayIp(), "", tempVO.getCenterPlayIp(), tempVO.getIpv6CdnType3(), "N"));

							if(tempVO.getIpv6LocalServerType().equals("")) tempVO.setIpv6LocalServerType(tempVO.getLocalServerType());
							if(tempVO.getIpv6NearServerType().equals("")) tempVO.setIpv6NearServerType(tempVO.getNearServerType());
							if(tempVO.getIpv6CenterServerType().equals("")) tempVO.setIpv6CenterServerType(tempVO.getCenterServerType());						
							
							result	= result + tempVO.toString() + ImcsConstants.ROWSEP;
						}
						long tt2 = System.currentTimeMillis();
						imcsLog.timeLog("파일Fetch:", String.valueOf((tt2 - tt1)), methodName, methodLine);
						
						
						msg = " File [" + fname + "] rcvbuf... [" + fLOCALRESFILE.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						msg = " File [" + fname + "] 리턴케이스1 ";
						imcsLog.serviceLog(msg, methodName, methodLine);						
						
						resultListVO.setResult(result);
						
					}else{
						msg = " File [" + fname + "] checkResFile Failed ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						msg = " File [" + fname + "] 리턴케이스2 ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						fLOCALRESFILE.delete();
					}	
				}else{
					
					msg = " File [" + fname + "] 리턴케이스3 ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					msg = " File [" + fname + "] open Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}
			
			if (paramVO.getCallType().equals("V") || paramVO.getCallType().equals("A")) {
				fname = LOCAL_RESFILE_VOD;
				if (fLOCALLOCKFILE_VOD.exists()) {
					fname = fname + ".bak";
				}
				
				fLOCALRESFILE_VOD = new File(fname);
				if(fLOCALRESFILE_VOD.exists()) {
					String result = FileUtil.fileRead(fname, "UTF-8");

					if(!"".equals(result)) {
						String[] arrResult	= result.split(ImcsConstants.ROWSEP);
						
						result	= "";
						
						long tt1 = System.currentTimeMillis();
						String[] arrRowResult = null;
						
						for(int i = 0; i < arrResult.length; i++) {
							//arrResult[i] = arrResult[i].replaceAll("img_chnl_server", szChnlImgSvrip);
							tempVO = new GetNSNodeListResponseVO();
							arrRowResult = arrResult[i].split(ImcsConstants.COLSEP_SPLIT);
							tempVO.dataParcing(arrRowResult);
							tempVO.setIpv6LocalPlayIp(ipv6_change(paramVO, tempVO.getIpv6LocalPlayIp(), "", tempVO.getLocalPlayIp(), tempVO.getIpv6CdnType1(), "N"));
							tempVO.setIpv6NearPlayIp(ipv6_change(paramVO, tempVO.getIpv6NearPlayIp(), "", tempVO.getNearPlayIp(), tempVO.getIpv6CdnType2(), "N"));
							tempVO.setIpv6CenterPlayIp(ipv6_change(paramVO, tempVO.getIpv6CenterPlayIp(), "", tempVO.getCenterPlayIp(), tempVO.getIpv6CdnType3(), "N"));

							if(tempVO.getIpv6LocalServerType().equals("")) tempVO.setIpv6LocalServerType(tempVO.getLocalServerType());
							if(tempVO.getIpv6NearServerType().equals("")) tempVO.setIpv6NearServerType(tempVO.getNearServerType());
							if(tempVO.getIpv6CenterServerType().equals("")) tempVO.setIpv6CenterServerType(tempVO.getCenterServerType());						
							
							result	= result + tempVO.toString() + ImcsConstants.ROWSEP;
						}
						long tt2 = System.currentTimeMillis();
						imcsLog.timeLog("파일Fetch:", String.valueOf((tt2 - tt1)), methodName, methodLine);
						
						
						msg = " File [" + fname + "] rcvbuf... [" + fLOCALRESFILE.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						msg = " File [" + fname + "] 리턴케이스1 ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultListVO.setResult(resultListVO.getResult() + result);
						
					}else{
						msg = " File [" + fname + "] checkResFile Failed ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						msg = " File [" + fname + "] 리턴케이스2 ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						fLOCALRESFILE.delete();
					}	
				}else{
					
					msg = " File [" + fname + "] 리턴케이스3 ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					msg = " File [" + fname + "] open Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}			

			String resultHeader  = String.format("%s|%s|", "0", "");
			resultListVO.setResultHeader(resultHeader);
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , ie.getClass().getName() + ":" +  ie.getMessage());
			String resultHeader  = String.format("%s|%s|", "1", "FILE OPEN FAIL");
			resultListVO = new GetNSNodeListResultVO();
			resultListVO.setResultHeader(resultHeader);
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , e.getClass().getName() + ":" +  e.getMessage());
			String resultHeader  = String.format("%s|%s|", "1", "FILE OPEN FAIL");
			resultListVO = new GetNSNodeListResultVO();
			resultListVO.setResultHeader(resultHeader);
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID050) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
    
	/**
	 * IPv4 -> IPv6로 변환
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	ipv6_change
	 */
	public String ipv6_change(GetNSNodeListRequestVO paramVO, String ipv6_ip, String ipv6_port, String ipv4_ip, String cdn_type, String fourd_replay) throws Exception {
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
//									result_ipv6 = Integer.toHexString(Integer.parseInt(temp_ip[j])).toUpperCase();
									result_ipv6 = String.format("%02X", Integer.parseInt(temp_ip[j]));
								}
								else
								{
//									result_ipv6 = result_ipv6 + Integer.toHexString(Integer.parseInt(temp_ip[j])).toUpperCase();
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
