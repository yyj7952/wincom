package kr.co.wincom.imcs.api.getNSChList2;

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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.api.getNSChList.GetNSChListRequestVO;
import kr.co.wincom.imcs.api.getNSChList.GetNSChListResponseVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSChList2ServiceImpl implements GetNSChList2Service {
	private Log imcsLogger = LogFactory.getLog("API_getNSChList2");
	
	@Autowired
	private GetNSChList2Dao getNSChList2Dao;
	
	@Autowired
	private CommonService commonService;

	
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
	public GetNSChList2ResultVO getNSChList2(GetNSChList2RequestVO paramVO){
//		this.getNSChList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSChList2ResultVO resultListVO = new GetNSChList2ResultVO();
		GetNSChList2ResponseVO tempVO = new GetNSChList2ResponseVO();
		List<GetNSChList2ResponseVO> tempListVO = new ArrayList<GetNSChList2ResponseVO>();
		String VirtualChFlag = "";
		try {
			VirtualChFlag	= commonService.getVCFlag(ImcsConstants.NSAPI_PRO_ID020.split("/")[1]);	// ???????????? ??????
			paramVO.setVirtualChFlag(VirtualChFlag);
		} catch (Exception e) {
			System.out.println("GetConfigInfo fail");
		}
		
		String LOCKFILE = "";
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("COPY_LOCAL", ImcsConstants.NSAPI_PRO_ID020.split("/")[1], imcsLog);
		
		//2018.08.23 ????????? ????????? ???????????? ??????
		String foldering_dir = String.format("p%sv%sf%sn%s", paramVO.getPooqYn(), paramVO.getHdtvViewGb(), paramVO.getFiveChYn(), paramVO.getSvcNode());
		LOCALPATH = String.format("%s/%s", LOCALPATH, foldering_dir);

		
		msg = "LOCALPATH : " + LOCALPATH;
		imcsLog.serviceLog(msg, methodName, methodLine);


	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;

		String szChnlImgSvrip = "";
		String szCacheNodeCd = "";
		
		try {
			szChnlImgSvrip	= commonService.getImgReplaceUrl2("img_chnl_server", "getNSChList");
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID020, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
		try{
			// 2019.10.04 - IPv6?????????????????? : IPv6PrefixIP ?????? ??????
			List<String> IPv6Prefix = commonService.getIPv6PrefixIP(ImcsConstants.NSAPI_PRO_ID020.split("/")[1], "L", imcsLog);
			paramVO.setPrefixInternet(IPv6Prefix.get(0));
			paramVO.setPrefixUplushdtv(IPv6Prefix.get(1));
			
			
			// ????????? ????????? ?????? ??????
			long tp111	= System.currentTimeMillis();
			this.getTestSbc(paramVO);
			tp1	= System.currentTimeMillis();
			//System.out.println("?????? STB?????? ??????: " + paramVO.getSaId() + " : " + String.valueOf((tp1 - tp111)));
			imcsLog.timeLog("?????? STB?????? ??????", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			// ?????? ?????? ??????
			this.getmProdId(paramVO);
			long tp11	= System.currentTimeMillis();
			//System.out.println("?????? ?????? ??????: " + paramVO.getSaId() + " : "  + String.valueOf((tp11 - tp1)));
			imcsLog.timeLog("?????? ?????? ??????", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			
			String compFileName = "-t" + paramVO.getTestSbc()    + "-p"+paramVO.getPooqYn()   + "-m" + paramVO.getmProdId()
			                     +"-v" + paramVO.getHdtvViewGb() + "-f"+paramVO.getFiveChYn() + ".res";
			
			msg = "compFileName : " + compFileName;
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			
			File fLOCALPATH = new File(LOCALPATH);
			
			if(!fLOCALPATH.exists()){
				fLOCALPATH.mkdir();
			}
			
			String chkFileName = "";
			
			File[] files = fLOCALPATH.listFiles();
			
			try{
				if(files.length > 0){			
					for(int i = 0; i < files.length; i++){
						if(files[i].getName().toString().indexOf(compFileName) > -1){					
							msg = files[i].getName();
							
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							chkFileName = files[i].getName();
						}
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
	
			if(!chkFileName.equals("1")){

				RESFILE = LOCALPATH + "/" + chkFileName;
				
				File res = new File(RESFILE);
				
				if(res.exists()) {
					String result = FileUtil.fileRead(RESFILE, "UTF-8");
					
					
					
					if(!"".equals(result)) {
						String[] arrResult	= result.split(ImcsConstants.ROWSEP);
						
						result	= "";
						
						long tt1 = System.currentTimeMillis();
						String[] arrRowResult = null;
						
						for(int i = 0; i < arrResult.length; i++) {
							if(i == 0) continue;	// ???????????? ?????????????????? ?????????, ????????????.
							
							tempVO = new GetNSChList2ResponseVO();
							
							arrResult[i] = arrResult[i].replaceAll("img_chnl_server", szChnlImgSvrip);
							
							arrRowResult = arrResult[i].split(ImcsConstants.COLSEP_SPLIT);
							tempVO.dataParcing(arrRowResult);
							
							if(!VirtualChFlag.equals("0")) {
								if(tempVO.getVirtualType().length() != 0) {
									continue;
								}
							}
							
							if(tempVO.getTimeAppYn().equals("Y"))
							{
								tempVO.setLiveVodIpv6Server1(ipv6_change(paramVO, tempVO.getLiveVodIpv6Server1(), "", tempVO.getLiveTimeServer1(), tempVO.getLiveServerNode1(), "N"));
								tempVO.setLiveVodIpv6Server2(ipv6_change(paramVO, tempVO.getLiveVodIpv6Server2(), "", tempVO.getLiveTimeServer2(), tempVO.getLiveServerNode2(), "N"));
								tempVO.setLiveVodIpv6Server3(ipv6_change(paramVO, tempVO.getLiveVodIpv6Server3(), "", tempVO.getLiveTimeServer3(), tempVO.getLiveServerNode3(), "N"));
							}
							
							
							tempListVO.add(tempVO);
						}
						long tt2 = System.currentTimeMillis();
						imcsLog.timeLog("????????????:", String.valueOf((tt2 - tt1)), methodName, methodLine);
						
						
						msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						String resultHeader	= "0||";
						resultListVO.setResultHeader(resultHeader);
						resultListVO.setList(tempListVO);
//						FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
					}else{
						msg = " File [" + RESFILE + "] Resfile Read Failed";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						String resultHeader  = String.format("%s|%s|", "1", "FILE OPEN FAIL");
						resultListVO.setResultHeader(resultHeader);
					}	
				}else{
					msg = " File [" + RESFILE + "] Have No ResFile";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					String resultHeader  = String.format("%s|%s|", "1", "FILE OPEN FAIL");
					resultListVO.setResultHeader(resultHeader);
					return resultListVO;
				}
			} else {
				msg = " File [" + RESFILE + "] Open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				String resultHeader  = String.format("%s|%s|", "1", "FILE OPEN FAIL");
				resultListVO.setResultHeader(resultHeader);
				return resultListVO;
			}


			
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			// NosqlCacheType.HBASE_WR.ordinal(), NosqlCacheType.USERDB.ordinal() ??????????
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID020) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * ?????? STB ?????? ??????
	 * @param paramVO
	 * @return
	 */
    public void getTestSbc(GetNSChList2RequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<String> list   = new ArrayList<String>();
		
		try {
			list = getNSChList2Dao.getTestSbc(paramVO);
			
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
	 * ?????? ?????? ??????
	 * @param paramVO
	 * @return
	 */
    public void getmProdId(GetNSChList2RequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<String> list   = new ArrayList<String>();
		
		try {
			
			list = getNSChList2Dao.getmProdId(paramVO);
			
			if( list != null && !list.isEmpty()){
				paramVO.setmProdId(StringUtil.nullToSpace(list.get(0)));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
    }
    
    
    /**
	 * IPv4 -> IPv6??? ??????
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	ipv6_change
	 */
	public String ipv6_change(GetNSChList2RequestVO paramVO, String ipv6_ip, String ipv6_port, String ipv4_ip, String cdn_type, String fourd_replay) throws Exception {
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
					// ???????????? : case when :fourD_replay_yn = 'N' then 'http://'||B.stb_play_ip1||case when :node_group = 'Z' then '' else ':' || b.SUB_NODE_PORT1 end || '/' else 'rtsp://'||B.stb_play_ip1||':80/' end,
					// ???????????? : 'http://'||B.stb_play_ip1||case when :node_group = 'Z' then '' else ':' || b.SUB_NODE_PORT1 end || '/',
					// AWS CDN??? ????????? ???????????????, ?????? IPv6????????? ?????? ?????? ???????????? ?????? IPv4??? ???????????? ????????? ???????????????.
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
//            System.out.println("???????????? ?????? IP ???????????????.");
        	result = -1;
 
        } else {
            // IPv4
            pattern = Pattern.compile(regexIPv4);
            if (pattern.matcher(ipAddress).matches() == true) {
//                System.out.println("IPv4 ???????????????.");
            	result = 1;
            }
 
            // IPv6
            pattern = Pattern.compile(regexIPv6);
            if (pattern.matcher(ipAddress).matches() == true) {
//                System.out.println("IPv6 ???????????????.");
            	result = 2;
            }
        }
        return result;
    }
}
