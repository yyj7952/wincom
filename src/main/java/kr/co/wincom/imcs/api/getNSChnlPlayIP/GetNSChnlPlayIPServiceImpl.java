package kr.co.wincom.imcs.api.getNSChnlPlayIP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
public class GetNSChnlPlayIPServiceImpl implements GetNSChnlPlayIPService {
	private Log imcsLogger = LogFactory.getLog("API_getNSChnlPlayIP");
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSChnlPlayIP(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	int 	iPos = 0;		// 비포 WIFI 라운드로빈
	int 	iPos_F = 0; 	// 5G WIFI 라운드로빈
	int 	iPos_V = 0; 	// 5G LTE 라운드로빈
	int 	iPos_D = 0; 	// 4D Replay WIFI 라운드로빈
	int 	iPos_R = 0; 	// 4D Replay LTE 라운드로빈
	int 	iPos_AW = 0; 	// 프로야구 해외 WIFI 라운드로빈
	int 	iPos_AL = 0; 	// 프로야구 해외 LTE 라운드로빈
	int 	iPos_AF = 0; 	// 프로야구 해외 5G WIFI 라운드로빈
	int 	iPos_AV = 0; 	// 프로야구 해외 5G LTE 라운드로빈
	int 	iPos_AD = 0; 	// 프로야구 해외 4D Replay WIFI 라운드로빈
	int 	iPos_AR = 0; 	// 프로야구 해외 4D Replay LTE 라운드로빈
	int		iPos_TW = 0; 	// TV앱 WIFI 라운드로빈
	int		iPos_TL = 0;	// TV앱 LTE 라운드로빈
	int		iPos_TF = 0;	// TV앱 5G WIFI 라운드로빈
	int		iPos_TV = 0;	// TV앱 5G LTE 라운드로빈
	int		iPos_TD = 0;	// TV앱 4D Replay WIFI 라운드로빈
	int		iPos_TR = 0;	// TV앱 4D Replay LTE 라운드로빈
	int		iPos_MW = 0; 	// 뮤직,공연앱 WIFI 라운드로빈
	int		iPos_ML = 0;	// 뮤직,공연앱 LTE 라운드로빈
	int		iPos_MF = 0;	// 뮤직,공연앱 5G WIFI 라운드로빈
	int		iPos_MV = 0;	// 뮤직,공연앱 5G LTE 라운드로빈
	int		iPos_MD = 0;	// 뮤직,공연앱 4D Replay WIFI 라운드로빈
	int		iPos_MR = 0;	// 뮤직,공연앱 4D Replay LTE 라운드로빈
	// 2021.04.15 - 아이돌라이브 유료채널
	int		iPos_PW = 0;	// 실시간 유료채널 WIFI 라운드로빈
	int		iPos_PL = 0;	// 실시간 유료채널 LTE 라운드로빈
	int		iPos_PF = 0;	// 실시간 유료채널 5G WIFI 라운드로빈
	int		iPos_PV = 0;	// 실시간 유료채널 5G LTE 라운드로빈
	
	int		iPos_result = 0; // 최종적으로 줘야할 정보 순번
	int		iPos_result_5g = 0; // 최종적으로 줘야할 정보 순번
	int		iPos_result_4d = 0; // 최종적으로 줘야할 정보 순번
	
	private static String regexIPv4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
	private static String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	private static String regexIPv4andIPv6 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	
	@Override
	public GetNSChnlPlayIPResultVO getNSChnlPlayIP(GetNSChnlPlayIPRequestVO paramVO){
//		this.getNSChnlPlayIP(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSChnlPlayIPResultVO resultListVO = new GetNSChnlPlayIPResultVO();
		GetNSChnlPlayIPResponseVO tempVO = new GetNSChnlPlayIPResponseVO();
		
		//GetNSChnlPlayIPResponseVO lstChPlayIP = new GetNSChnlPlayIPResponseVO();
		GetNSChnlPlayIPResponseVO lstTmpChPlayIP = new GetNSChnlPlayIPResponseVO();
		GetNSChnlPlayIPResponseVO lst2ndChPlayIP = new GetNSChnlPlayIPResponseVO();
		GetNSChnlPlayIPResponseVO lst3rdChPlayIP = new GetNSChnlPlayIPResponseVO();
		
		
		List<GetNSChnlPlayIPResponseVO> resultVO = new ArrayList<GetNSChnlPlayIPResponseVO>();
		List<GetNSChnlPlayIPResponseVO> resultVO1 = new ArrayList<GetNSChnlPlayIPResponseVO>();
		List<GetNSChnlPlayIPResponseVO> resultVO2 = new ArrayList<GetNSChnlPlayIPResponseVO>();
		List<GetNSChnlPlayIPResponseVO> resultVO3 = new ArrayList<GetNSChnlPlayIPResponseVO>();

		
		//캐시 폴더 경로
		String LOCALPATH      = "";
		//String NASPATH        = "";

		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID050.split("/")[1], imcsLog);
		//NASPATH   = commonService.getCachePath("NAS", ImcsConstants.NSAPI_PRO_ID050.split("/")[1], imcsLog);
		
		// 파일명 관련 파라미터
		String LOCAL_RESFILE    = ""; //LOCAL res
		String LOCAL_LOCKFILE   = ""; //LOCAL lock
		LOCAL_RESFILE     = String.format("%s/getNSChNodeList.res", LOCALPATH); //Local res 파일
		LOCAL_LOCKFILE    = String.format("%s/getNSChNodeList.lock", LOCALPATH); //Local res 파일
		
		//LOCAL
		File fLOCALLOCKFILE = new File(LOCAL_LOCKFILE);
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
			
			fname = LOCAL_RESFILE;
			if (fLOCALLOCKFILE.exists()) {
				fname = fname + ".bak";
			}
			
			File fLOCALRESFILE = new File(fname);
			if(fLOCALRESFILE.exists()) {
				String result = FileUtil.fileRead(fname, "UTF-8");

				if(!"".equals(result)) {
					
					String[] arrResult	= result.split(ImcsConstants.ROWSEP);
					result	= "";
					int end_flag = 0;
					int tok_cnt = 0;
					int bakup_data_cnt = 0;
					int	load_balancing_cnt = 0;
					String load_balancing = "N";

					long tt1 = System.currentTimeMillis();
					
					resultVO1 = new ArrayList<GetNSChnlPlayIPResponseVO>();
					resultVO2 = new ArrayList<GetNSChnlPlayIPResponseVO>();
					resultVO3 = new ArrayList<GetNSChnlPlayIPResponseVO>();
					
					for (int i = 0; i < arrResult.length; i++) {
						String[] lineData	= arrResult[i].split("\\" + ImcsConstants.COLSEP);
						end_flag = 0;
						load_balancing = "N";
						int flag = 0;
						tempVO = new GetNSChnlPlayIPResponseVO();
						lstTmpChPlayIP = new GetNSChnlPlayIPResponseVO();
						lst2ndChPlayIP = new GetNSChnlPlayIPResponseVO();
						lst3rdChPlayIP = new GetNSChnlPlayIPResponseVO();
						
						
						for (int j = 1; j < lineData.length ; j++) {				
							switch (j) {
							case 1:
								tempVO.setFileSvcNode(lineData[1]);
								break;
							case 2:
								tempVO.setChkBaseGb3(lineData[2]);
								if(lineData[2].length()== 1){
									tempVO.setChkBaseGb1(tempVO.getChkBaseGb3().substring(0, 1));
									tempVO.setChkBaseGb2("0");
								}
								else if (lineData[2].length() == 2) {
									tempVO.setChkBaseGb2(tempVO.getChkBaseGb3().substring(1, 2));
								}
								else {
									tempVO.setChkBaseGb1("0");
									tempVO.setChkBaseGb2("0");
								}
								
								
								if(
									!(
									  paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()) ||
									  paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()) ||
									  (lineData[2].equals("N") && lineData[2].length() == 1)
									)
								)
								{
									end_flag = -1;
								}
								break;
							case 3:
								switch(paramVO.getSvcNode())
								{
									case "N":
										if(paramVO.getBaseCd().substring(0, 1).equals("W"))
										{
											if(!((paramVO.getBaseCd().substring(0, 1).equals(lineData[3].substring(0, 1)) && lineData[3].length() <= 3) || lineData[3].equals("1234567890")))
											{
												end_flag = -1;
											}
										}
										else
										{
											if(!(paramVO.getBaseCd().equals(lineData[3]) || lineData[3].equals("1234567890") || lineData[3].equals("L") || lineData[3].equals("T")))
											{
												end_flag = -1;
											}
										}
										break;
									default:
										if(!(lineData[3].length() <= 4 || lineData[3].equals("1234567890")))
										{
											end_flag = -1;
										}
										break;
								}
								if( !paramVO.getSvcNode().substring(0, 1).equals(tempVO.getFileSvcNode()) && !lineData[3].equals("1234567890"))
								{
									end_flag = -1;
								}
								break;
							case 4:
								if(lineData[4].substring(0, 1).equals("Y"))
								{
									load_balancing = "Y";
								}
								break;
							case 5:
								if(load_balancing.equals("Y"))
								{
									load_balancing_cnt++;
								}
								break;
							case 6:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setLocalPlayIp(lineData[6]);
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setLocalPlayIp(lineData[6]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setLocalPlayIp(lineData[6]);
								}
								break;
							case 7:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setNearPlayIp(lineData[7]);
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setNearPlayIp(lineData[7]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setNearPlayIp(lineData[7]);
								}
								break;
							case 8:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setCenterPlayIp(lineData[8]);
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setCenterPlayIp(lineData[8]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setCenterPlayIp(lineData[8]);
								}
								break;
							case 9:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setLocalServerType(lineData[9]);
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setLocalServerType(lineData[9]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setLocalServerType(lineData[9]);
								}
								break;
							case 10:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setNearServerType(lineData[10]);
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setNearServerType(lineData[10]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setNearServerType(lineData[10]);
								}
								break;
							case 11:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setCenterServerType(lineData[11]);
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setCenterServerType(lineData[11]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setCenterServerType(lineData[11]);
								}
								break;
							case 13:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setIpv6CdnType1(lineData[13]);
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setIpv6CdnType1(lineData[13]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setIpv6CdnType1(lineData[13]);
								}
								break;
							case 14:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setIpv6CdnType2(lineData[14]);
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setIpv6CdnType2(lineData[14]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setIpv6CdnType2(lineData[14]);
								}
								break;
							case 15:
								if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setIpv6CdnType3(lineData[15]);
									flag = 1;
								}
								else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setIpv6CdnType3(lineData[15]);
									end_flag = 1;
									bakup_data_cnt = 1;
									flag = 2;
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setIpv6CdnType3(lineData[15]);
									end_flag = 1;
									flag = 3;
								}
								break;
							case 16:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server1(lineData[16]);
									}
									else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server1(lineData[16]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server1(lineData[16]);
									}
								}
								break;
							case 17:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server2(lineData[17]);
									}
									else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server2(lineData[17]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server2(lineData[17]);
									}
								}
								break;
							case 18:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server3(lineData[18]);
									}
									else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server3(lineData[18]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server3(lineData[18]);
									}
								}
							case 19:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server1Type(lineData[19]);
									}
									else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server1Type(lineData[19]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server1Type(lineData[19]);
									}
								}
								break;
							case 20:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server2Type(lineData[20]);
									}
									else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server2Type(lineData[20]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server2Type(lineData[20]);
									}
								}
								break;
							case 21:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server3Type(lineData[21]);
									}
									else if(paramVO.getBaseCd().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server3Type(lineData[21]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server3Type(lineData[21]);
									}
								}								
								break;
							default:
								break;
							}
							if(end_flag == -1)
							{
								break;
							}
						}
						if (end_flag == 0) {
							tok_cnt++;
						}
						
						if (flag == 1) {
							lstTmpChPlayIP.setResultType("CHCDN");
							resultVO1.add(lstTmpChPlayIP);
						} else if (flag == 2) {
							lst2ndChPlayIP.setResultType("CHCDN");
							resultVO2.add(lst2ndChPlayIP);
						} else if (flag == 3) {
							lst3rdChPlayIP.setResultType("CHCDN");
							resultVO3.add(lst3rdChPlayIP);			
						}
					}
					
					iPos_result = 1;
					if(load_balancing_cnt > 0)
					{
						switch(paramVO.getSvcNode())
						{
							case "N":
								if(paramVO.getBaseCdTmp().substring(0, 1).equals("W"))
								{
									iPos++;
									if(iPos > load_balancing_cnt)
									{
										iPos = 1;
									}
									iPos_result = iPos;
								}
								else
								{
									iPos_result = 1;
								}
								break;
							case "A":
								if(paramVO.getBaseCdTmp().substring(1, 2).equals("W"))
								{
									iPos_AW++;
									if(iPos_AW > load_balancing_cnt)
									{
										iPos_AW = 1;
									}
									iPos_result = iPos_AW;
								}
								else if(paramVO.getBaseCdTmp().substring(0, 1).equals("T"))
								{
									iPos_result = 1;
								}
								else
								{
									iPos_AL++;
									if(iPos_AL > load_balancing_cnt)
									{
										iPos_AL = 1;
									}
									iPos_result = iPos_AL;
								}
								break;
							case "T":
								if(paramVO.getBaseCdTmp().substring(1, 2).equals("W"))
								{
									iPos_TW++;
									if(iPos_TW > load_balancing_cnt)
									{
										iPos_TW = 1;
									}
									iPos_result = iPos_TW;
								}
								else if(paramVO.getBaseCdTmp().substring(1, 2).equals("L"))
								{
									iPos_TL++;
									if(iPos_TL > load_balancing_cnt)
									{
										iPos_TL = 1;
									}
									iPos_result = iPos_TL;
								}
								else if(paramVO.getBaseCdTmp().substring(0, 1).equals("T"))
								{
									iPos_result = 1;
								}
								else
								{
									iPos_TL++;
									if(iPos_TL > load_balancing_cnt)
									{
										iPos_TL = 1;
									}
									iPos_result = iPos_TL;
								}
								break;
							case "M":
								if(paramVO.getBaseCdTmp().substring(1, 2).equals("W"))
								{
									iPos_MW++;
									if(iPos_MW > load_balancing_cnt)
									{
										iPos_MW = 1;
									}
									iPos_result = iPos_MW;
								}
								else if(paramVO.getBaseCdTmp().substring(0, 1).equals("T"))
								{
									iPos_result = 1;
								}
								else
								{
									iPos_ML++;
									if(iPos_ML > load_balancing_cnt)
									{
										iPos_ML = 1;
									}
									iPos_result = iPos_ML;
								}
								break;
							// 2021.04.15 - 아이돌라이브 유료채널
							case "P":
								if(paramVO.getBaseCdTmp().substring(1, 2).equals("W"))
								{
									iPos_PW++;
									if(iPos_PW > load_balancing_cnt)
									{
										iPos_PW = 1;
									}
									iPos_result = iPos_PW;
								}
								else if(paramVO.getBaseCdTmp().substring(0, 1).equals("T"))
								{
									iPos_result = 1;
								}
								else
								{
									iPos_PL++;
									if(iPos_PL > load_balancing_cnt)
									{
										iPos_PL = 1;
									}
									iPos_result = iPos_PL;
								}
								break;
							default:
								break;
						}
					}
					if (tok_cnt > 0) {
						if(paramVO.getIpv6Flag().equals("Y"))
						{
							resultVO1.get(iPos_result-1).setVodIpv6Server1(ipv6_change(paramVO, resultVO1.get(iPos_result-1).getVodIpv6Server1(), "", resultVO1.get(iPos_result-1).getLocalPlayIp(), resultVO1.get(iPos_result-1).getIpv6CdnType1(), "N"));
							resultVO1.get(iPos_result-1).setVodIpv6Server2(ipv6_change(paramVO, resultVO1.get(iPos_result-1).getVodIpv6Server2(), "", resultVO1.get(iPos_result-1).getNearPlayIp(), resultVO1.get(iPos_result-1).getIpv6CdnType2(), "N"));
							resultVO1.get(iPos_result-1).setVodIpv6Server3(ipv6_change(paramVO, resultVO1.get(iPos_result-1).getVodIpv6Server3(), "", resultVO1.get(iPos_result-1).getCenterPlayIp(), resultVO1.get(iPos_result-1).getIpv6CdnType3(), "N"));
							
							if(resultVO1.get(iPos_result-1).getVodIpv6Server1Type().equals("")) resultVO1.get(iPos_result-1).setVodIpv6Server1Type(resultVO1.get(iPos_result-1).getLocalServerType());
							if(resultVO1.get(iPos_result-1).getVodIpv6Server2Type().equals("")) resultVO1.get(iPos_result-1).setVodIpv6Server2Type(resultVO1.get(iPos_result-1).getNearServerType());
							if(resultVO1.get(iPos_result-1).getVodIpv6Server3Type().equals("")) resultVO1.get(iPos_result-1).setVodIpv6Server3Type(resultVO1.get(iPos_result-1).getCenterServerType());
						}
						
						resultVO.add(resultVO1.get(iPos_result-1));
					} else if (tok_cnt == 0 && bakup_data_cnt == 1) {
						if(paramVO.getIpv6Flag().equals("Y"))
						{
							resultVO2.get(0).setVodIpv6Server1(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server1(), "", resultVO2.get(0).getLocalPlayIp(), resultVO2.get(0).getIpv6CdnType1(), "N"));
							resultVO2.get(0).setVodIpv6Server2(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server2(), "", resultVO2.get(0).getNearPlayIp(), resultVO2.get(0).getIpv6CdnType2(), "N"));
							resultVO2.get(0).setVodIpv6Server3(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server3(), "", resultVO2.get(0).getCenterPlayIp(), resultVO2.get(0).getIpv6CdnType3(), "N"));
							
							if(resultVO2.get(0).getVodIpv6Server1Type().equals("")) resultVO2.get(0).setVodIpv6Server1Type(resultVO2.get(0).getLocalServerType());
							if(resultVO2.get(0).getVodIpv6Server2Type().equals("")) resultVO2.get(0).setVodIpv6Server2Type(resultVO2.get(0).getNearServerType());
							if(resultVO2.get(0).getVodIpv6Server3Type().equals("")) resultVO2.get(0).setVodIpv6Server3Type(resultVO2.get(0).getCenterServerType());
						}
						
						resultVO.add(resultVO2.get(0));
					} else {
						if(paramVO.getIpv6Flag().equals("Y"))
						{
							resultVO3.get(0).setVodIpv6Server1(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server1(), "", resultVO3.get(0).getLocalPlayIp(), resultVO3.get(0).getIpv6CdnType1(), "N"));
							resultVO3.get(0).setVodIpv6Server2(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server2(), "", resultVO3.get(0).getNearPlayIp(), resultVO3.get(0).getIpv6CdnType2(), "N"));
							resultVO3.get(0).setVodIpv6Server3(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server3(), "", resultVO3.get(0).getCenterPlayIp(), resultVO3.get(0).getIpv6CdnType3(), "N"));
							
							if(resultVO3.get(0).getVodIpv6Server1Type().equals("")) resultVO3.get(0).setVodIpv6Server1Type(resultVO3.get(0).getLocalServerType());
							if(resultVO3.get(0).getVodIpv6Server2Type().equals("")) resultVO3.get(0).setVodIpv6Server2Type(resultVO3.get(0).getNearServerType());
							if(resultVO3.get(0).getVodIpv6Server3Type().equals("")) resultVO3.get(0).setVodIpv6Server3Type(resultVO3.get(0).getCenterServerType());
						}
						
						resultVO.add(resultVO3.get(0));						
					}
					
					tok_cnt = 0;
					load_balancing_cnt = 0;
					bakup_data_cnt = 0;
					resultVO1 = new ArrayList<GetNSChnlPlayIPResponseVO>();
					resultVO2 = new ArrayList<GetNSChnlPlayIPResponseVO>();
					resultVO3 = new ArrayList<GetNSChnlPlayIPResponseVO>();
					
					for (int i = 0; i < arrResult.length; i++) {
						String[] lineData	= arrResult[i].split("\\" + ImcsConstants.COLSEP);
						end_flag = 0;
						load_balancing = "N";
						int flag = 0;
						tempVO = new GetNSChnlPlayIPResponseVO();
						lstTmpChPlayIP = new GetNSChnlPlayIPResponseVO();
						lst2ndChPlayIP = new GetNSChnlPlayIPResponseVO();
						lst3rdChPlayIP = new GetNSChnlPlayIPResponseVO();
						
						
						for (int j = 1; j < lineData.length ; j++) {
								
							switch (j) {
							case 1:
								tempVO.setFileSvcNode(lineData[1]);
								break;
							case 2:
								tempVO.setChkBaseGb3(lineData[2]);
								if(lineData[2].length()== 1){
									tempVO.setChkBaseGb1(tempVO.getChkBaseGb3().substring(0, 1));
									tempVO.setChkBaseGb2("0");
								}
								else if (lineData[2].length() == 2) {
									tempVO.setChkBaseGb2(tempVO.getChkBaseGb3().substring(1, 2));
								}
								else {
									tempVO.setChkBaseGb1("0");
									tempVO.setChkBaseGb2("0");
								}
								
								if(
									!(
									  paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()) ||
									  paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()) ||
									  (lineData[2].equals("N") && lineData[2].length() == 1)
									)
								)
								{
									end_flag = -1;
								}
								break;
							case 3:
								switch(paramVO.getSvcNode().substring(0, 1))
								{
									case "N":
										if(lineData[3].length() == 0){
											end_flag = -1;
											break;
										}
										else if(paramVO.getBaseCd5g().substring(0, 1).equals("F"))
										{
											if(!((paramVO.getBaseCd5g().substring(0, 1).equals(lineData[3].substring(0, 1)) && (lineData[3].length() <= 3)) || lineData[3].equals("F") || lineData[3].equals("1234567890")))
											{
												end_flag = -1;
											}
										}
										else if(paramVO.getBaseCd5g().substring(0, 1).equals("V") || paramVO.getBaseCd5g().substring(0, 1).equals("T")) 
										{
											if(!((paramVO.getBaseCd5g().substring(0, 1).equals(lineData[3].substring(0, 1)) && (lineData[3].length() <= 3)) || lineData[3].equals("V") || lineData[3].equals("T") || (paramVO.getBaseCd5g().substring(0, 1).equals("T") && paramVO.getBaseCd5g().equals(lineData[3])) || lineData[3].equals("1234567890")))
											{
												end_flag = -1;
											}
										}
										else
										{
											if(!(paramVO.getBaseCd5g().equals(lineData[3]) || lineData[3].equals("1234567890")))
											{
												end_flag = -1;
											}
										}
										break;
									default:
										if(!(lineData[3].length() <= 4 || lineData[3].equals("1234567890")))
										{
											end_flag = -1;
										}
										break;
								}
								if( !paramVO.getSvcNode().substring(0, 1).equals(tempVO.getFileSvcNode()) && !lineData[3].equals("1234567890"))
								{
									end_flag = -1;
								}
								break;
							case 4:
								if(lineData[4].substring(0, 1).equals("Y"))
								{
									load_balancing = "Y";
								}
								break;
							case 5:
								if(load_balancing.equals("Y"))
								{
									load_balancing_cnt++;
								}
								break;
							case 6:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setLocalPlayIp(lineData[6]);
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setLocalPlayIp(lineData[6]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setLocalPlayIp(lineData[6]);
								}
								break;
							case 7:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setNearPlayIp(lineData[7]);
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setNearPlayIp(lineData[7]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setNearPlayIp(lineData[7]);
								}
								break;
							case 8:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setCenterPlayIp(lineData[8]);
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setCenterPlayIp(lineData[8]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setCenterPlayIp(lineData[8]);
								}
								break;
							case 9:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setLocalServerType(lineData[9]);
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setLocalServerType(lineData[9]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setLocalServerType(lineData[9]);
								}
								break;
							case 10:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setNearServerType(lineData[10]);
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setNearServerType(lineData[10]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setNearServerType(lineData[10]);
								}
								break;
							case 11:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setCenterServerType(lineData[11]);
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setCenterServerType(lineData[11]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setCenterServerType(lineData[11]);
								}
								break;
							case 13:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setIpv6CdnType1(lineData[13]);
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setIpv6CdnType1(lineData[13]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setIpv6CdnType1(lineData[13]);
								}
								break;
							case 14:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setIpv6CdnType2(lineData[14]);
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setIpv6CdnType2(lineData[14]);
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setIpv6CdnType2(lineData[14]);
								}
								break;
							case 15:
								if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
								{
									lstTmpChPlayIP.setIpv6CdnType3(lineData[15]);
									flag = 1;
								}
								else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
								{
									lst2ndChPlayIP.setIpv6CdnType3(lineData[15]);
									end_flag = 1;
									bakup_data_cnt = 1;
									flag = 2;
								}
								else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
								{
									lst3rdChPlayIP.setIpv6CdnType3(lineData[15]);
									end_flag = 1;
									flag = 3;
								}
								break;
							case 16:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server1(lineData[16]);
									}
									else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server1(lineData[16]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server1(lineData[16]);
									}
								}
								break;
							case 17:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server2(lineData[17]);
									}
									else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server2(lineData[17]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server2(lineData[17]);
									}
								}
								break;
							case 18:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server3(lineData[18]);
									}
									else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server3(lineData[18]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server3(lineData[18]);
									}
								}
							case 19:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server1Type(lineData[19]);
									}
									else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server1Type(lineData[19]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server1Type(lineData[19]);
									}
								}
								break;
							case 20:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server2Type(lineData[20]);
									}
									else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server2Type(lineData[20]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server2Type(lineData[20]);
									}
								}
								break;
							case 21:
								if(paramVO.getIpv6Flag().equals("Y"))
								{
									if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setVodIpv6Server3Type(lineData[21]);
									}
									else if(paramVO.getBaseCd5g().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setVodIpv6Server3Type(lineData[21]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setVodIpv6Server3Type(lineData[21]);
									}
								}								
								break;
							default:
								break;
							}
							if(end_flag == -1)
							{
								break;
							}
						}
						if (end_flag == 0) {
							tok_cnt++;
						}
						if (flag == 1) {
							lstTmpChPlayIP.setResultType("5GCDN");
							resultVO1.add(lstTmpChPlayIP);
						} else if (flag == 2) {
							lst2ndChPlayIP.setResultType("5GCDN");
							resultVO2.add(lst2ndChPlayIP);
						} else if (flag == 3) {
							lst3rdChPlayIP.setResultType("5GCDN");
							resultVO3.add(lst3rdChPlayIP);			
						}
					}
					
					iPos_result_5g = 1;
					if(load_balancing_cnt > 0)
					{
						switch(paramVO.getSvcNode())
						{
							case "N":
								if(paramVO.getBaseCdTmp5g().substring(0, 1).equals("F"))
								{
									iPos_F++;
									if(iPos_F > load_balancing_cnt)
									{
										iPos_F = 1;
									}
									iPos_result_5g = iPos_F;
								}
								else if(paramVO.getBaseCdTmp5g().substring(0, 1).equals("T"))
								{
									iPos_result_5g = 1;
								}
								else
								{
									iPos_V++;
									if(iPos_V > load_balancing_cnt)
									{
										iPos_V = 1;
									}
									iPos_result_5g = iPos_V;
								}
								break;
							case "A":
								if(paramVO.getBaseCdTmp5g().substring(1, 2).equals("F"))
								{
									iPos_AF++;
									if(iPos_AF > load_balancing_cnt)
									{
										iPos_AF = 1;
									}
									iPos_result_5g = iPos_AF;
								}
								else if(paramVO.getBaseCdTmp5g().substring(0, 1).equals("T"))
								{
									iPos_result_5g = 1;
								}
								else
								{
									iPos_AV++;
									if(iPos_AV > load_balancing_cnt)
									{
										iPos_AV = 1;
									}
									iPos_result_5g = iPos_AV;
								}
								break;
							case "T":
								if(paramVO.getBaseCdTmp5g().substring(1, 2).equals("F"))
								{
									iPos_TF++;
									if(iPos_TF > load_balancing_cnt)
									{
										iPos_TF = 1;
									}
									iPos_result_5g = iPos_TF;
								}
								else if(paramVO.getBaseCdTmp5g().substring(1, 2).equals("V"))
								{
									iPos_TV++;
									if(iPos_TV > load_balancing_cnt)
									{
										iPos_TV = 1;
									}
									iPos_result_5g = iPos_TV;
								}
								else if(paramVO.getBaseCdTmp5g().substring(0, 1).equals("T"))
								{
									iPos_result_5g = 1;
								}
								else
								{
									iPos_TV++;
									if(iPos_TV > load_balancing_cnt)
									{
										iPos_TV = 1;
									}
									iPos_result_5g = iPos_TV;
								}
								break;
							case "M":
								if(paramVO.getBaseCdTmp5g().substring(1, 2).equals("F"))
								{
									iPos_MF++;
									if(iPos_MF > load_balancing_cnt)
									{
										iPos_MF = 1;
									}
									iPos_result_5g = iPos_MF;
								}
								else if(paramVO.getBaseCdTmp5g().substring(0, 1).equals("T"))
								{
									iPos_result_5g = 1;
								}
								else
								{
									iPos_MV++;
									if(iPos_MV > load_balancing_cnt)
									{
										iPos_MV = 1;
									}
									iPos_result_5g = iPos_MV;
								}
								break;
							// 2021.04.15 - 아이돌라이브 유료채널
							case "P":
								if(paramVO.getBaseCdTmp5g().substring(1, 2).equals("F"))
								{
									iPos_PF++;
									if(iPos_PF > load_balancing_cnt)
									{
										iPos_PF = 1;
									}
									iPos_result_5g = iPos_PF;
								}
								else if(paramVO.getBaseCdTmp5g().substring(0, 1).equals("T"))
								{
									iPos_result_5g = 1;
								}
								else
								{
									iPos_PV++;
									if(iPos_PV > load_balancing_cnt)
									{
										iPos_PV = 1;
									}
									iPos_result_5g = iPos_PV;
								}
								break;
							default:
								break;
						}
					}
					
					if (tok_cnt > 0) {
						if(paramVO.getIpv6Flag().equals("Y"))
						{
							resultVO1.get(iPos_result_5g-1).setVodIpv6Server1(ipv6_change(paramVO, resultVO1.get(iPos_result_5g-1).getVodIpv6Server1(), "", resultVO1.get(iPos_result_5g-1).getLocalPlayIp(), resultVO1.get(iPos_result_5g-1).getIpv6CdnType1(), "N"));
							resultVO1.get(iPos_result_5g-1).setVodIpv6Server2(ipv6_change(paramVO, resultVO1.get(iPos_result_5g-1).getVodIpv6Server2(), "", resultVO1.get(iPos_result_5g-1).getNearPlayIp(), resultVO1.get(iPos_result_5g-1).getIpv6CdnType2(), "N"));
							resultVO1.get(iPos_result_5g-1).setVodIpv6Server3(ipv6_change(paramVO, resultVO1.get(iPos_result_5g-1).getVodIpv6Server3(), "", resultVO1.get(iPos_result_5g-1).getCenterPlayIp(), resultVO1.get(iPos_result_5g-1).getIpv6CdnType3(), "N"));
							
							if(resultVO1.get(iPos_result_5g-1).getVodIpv6Server1Type().equals("")) resultVO1.get(iPos_result_5g-1).setVodIpv6Server1Type(resultVO1.get(iPos_result_5g-1).getLocalServerType());
							if(resultVO1.get(iPos_result_5g-1).getVodIpv6Server2Type().equals("")) resultVO1.get(iPos_result_5g-1).setVodIpv6Server2Type(resultVO1.get(iPos_result_5g-1).getNearServerType());
							if(resultVO1.get(iPos_result_5g-1).getVodIpv6Server3Type().equals("")) resultVO1.get(iPos_result_5g-1).setVodIpv6Server3Type(resultVO1.get(iPos_result_5g-1).getCenterServerType());
						}
						
						resultVO.add(resultVO1.get(iPos_result_5g-1));
					} else if (tok_cnt == 0 && bakup_data_cnt == 1) {
						if(paramVO.getIpv6Flag().equals("Y"))
						{
							resultVO2.get(0).setVodIpv6Server1(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server1(), "", resultVO2.get(0).getLocalPlayIp(), resultVO2.get(0).getIpv6CdnType1(), "N"));
							resultVO2.get(0).setVodIpv6Server2(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server2(), "", resultVO2.get(0).getNearPlayIp(), resultVO2.get(0).getIpv6CdnType2(), "N"));
							resultVO2.get(0).setVodIpv6Server3(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server3(), "", resultVO2.get(0).getCenterPlayIp(), resultVO2.get(0).getIpv6CdnType3(), "N"));
							
							if(resultVO2.get(0).getVodIpv6Server1Type().equals("")) resultVO2.get(0).setVodIpv6Server1Type(resultVO2.get(0).getLocalServerType());
							if(resultVO2.get(0).getVodIpv6Server2Type().equals("")) resultVO2.get(0).setVodIpv6Server2Type(resultVO2.get(0).getNearServerType());
							if(resultVO2.get(0).getVodIpv6Server3Type().equals("")) resultVO2.get(0).setVodIpv6Server3Type(resultVO2.get(0).getCenterServerType());
						}
						
						resultVO.add(resultVO2.get(0));
					} else {
						if(paramVO.getIpv6Flag().equals("Y"))
						{
							resultVO3.get(0).setVodIpv6Server1(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server1(), "", resultVO3.get(0).getLocalPlayIp(), resultVO3.get(0).getIpv6CdnType1(), "N"));
							resultVO3.get(0).setVodIpv6Server2(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server2(), "", resultVO3.get(0).getNearPlayIp(), resultVO3.get(0).getIpv6CdnType2(), "N"));
							resultVO3.get(0).setVodIpv6Server3(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server3(), "", resultVO3.get(0).getCenterPlayIp(), resultVO3.get(0).getIpv6CdnType3(), "N"));
							
							if(resultVO3.get(0).getVodIpv6Server1Type().equals("")) resultVO3.get(0).setVodIpv6Server1Type(resultVO3.get(0).getLocalServerType());
							if(resultVO3.get(0).getVodIpv6Server2Type().equals("")) resultVO3.get(0).setVodIpv6Server2Type(resultVO3.get(0).getNearServerType());
							if(resultVO3.get(0).getVodIpv6Server3Type().equals("")) resultVO3.get(0).setVodIpv6Server3Type(resultVO3.get(0).getCenterServerType());
						}
						
						resultVO.add(resultVO3.get(0));						
					}
					
					if("D".equals(paramVO.getCallFlag())){
						
						tok_cnt = 0;
						load_balancing_cnt = 0;
						bakup_data_cnt = 0;
						resultVO1 = new ArrayList<GetNSChnlPlayIPResponseVO>();
						resultVO2 = new ArrayList<GetNSChnlPlayIPResponseVO>();
						resultVO3 = new ArrayList<GetNSChnlPlayIPResponseVO>();
						
						for (int i = 0; i < arrResult.length; i++) {
							String[] lineData	= arrResult[i].split("\\" + ImcsConstants.COLSEP);
							end_flag = 0;
							load_balancing = "N";
							int flag = 0;
							tempVO = new GetNSChnlPlayIPResponseVO();
							lstTmpChPlayIP = new GetNSChnlPlayIPResponseVO();
							lst2ndChPlayIP = new GetNSChnlPlayIPResponseVO();
							lst3rdChPlayIP = new GetNSChnlPlayIPResponseVO();
							 
							for (int j = 1; j < lineData.length ; j++) {

								switch (j) {
								case 1:
									tempVO.setFileSvcNode(lineData[1]);
									break;
								case 2:
									tempVO.setChkBaseGb3(lineData[2]);
									if(lineData[2].length()== 1){
										tempVO.setChkBaseGb1(tempVO.getChkBaseGb3().substring(0, 1));
										tempVO.setChkBaseGb2("0");
									}
									else if (lineData[2].length() == 2) {
										tempVO.setChkBaseGb2(tempVO.getChkBaseGb3().substring(1, 2));
									}
									else {
										tempVO.setChkBaseGb1("0");
										tempVO.setChkBaseGb2("0");
									}
									
									if(
										!(
										  paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()) ||
										  paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()) ||
										  (lineData[2].equals("N") && lineData[2].length() == 1)
										)
									)
									{
										end_flag = -1;
									}
									break;
								case 3:
									switch(paramVO.getSvcNode())
									{
										case "N":
											if(paramVO.getBaseCd4d().substring(0, 1).equals("D"))
											{
												if(!((paramVO.getBaseCd4d().substring(0, 1).equals(lineData[3].substring(0, 1)) && lineData[3].length() <= 3) || lineData[3].equals("D") || lineData[3].equals("1234567890")))
												{
													end_flag = -1;
												}
											}
											else if(paramVO.getBaseCd4d().substring(0, 1).equals("R")) 
											{
												if(!((paramVO.getBaseCd4d().substring(0, 1).equals(lineData[3].substring(0, 1)) && lineData[3].length() <= 3) || lineData[3].equals("R") || lineData[3].equals("1234567890")))
												{
													end_flag = -1;
												}
											}
											else
											{
												if(!(paramVO.getBaseCd4d().equals(lineData[3]) || lineData[3].equals("1234567890")))
												{
													end_flag = -1;
												}
											}
											break;
										default:
											if(!(lineData[3].length() <= 4 || lineData[3].equals("1234567890")))
											{
												end_flag = -1;
											}
											break;
									}
									if( !paramVO.getSvcNode().substring(0, 1).equals(tempVO.getFileSvcNode()) && !lineData[3].equals("1234567890"))
									{
										end_flag = -1;
									}
									break;
								case 4:
									if(lineData[4].substring(0, 1).equals("Y"))
									{
										load_balancing = "Y";
									}
									break;
								case 5:
									if(load_balancing.equals("Y"))
									{
										load_balancing_cnt++;
									}
									break;
								case 6:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setLocalPlayIp("rtsp://" + lineData[6] + ":80/");
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setLocalPlayIp("rtsp://" + lineData[6]+ ":80/");
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setLocalPlayIp("rtsp://" + lineData[6]+ ":80/");
									}
									break;
								case 7:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setNearPlayIp("rtsp://" + lineData[7]+ ":80/");
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setNearPlayIp("rtsp://" + lineData[7]+ ":80/");
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setNearPlayIp("rtsp://" + lineData[7]+ ":80/");
									}
									break;
								case 8:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setCenterPlayIp("rtsp://" + lineData[8]+ ":80/");
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setCenterPlayIp("rtsp://" + lineData[8]+ ":80/");
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setCenterPlayIp("rtsp://" + lineData[8]+ ":80/");
									}
									break;
								case 9:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setLocalServerType(lineData[9]);
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setLocalServerType(lineData[9]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setLocalServerType(lineData[9]);
									}
									break;
								case 10:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setNearServerType(lineData[10]);
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setNearServerType(lineData[10]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setNearServerType(lineData[10]);
									}
									break;
								case 11:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setCenterServerType(lineData[11]);
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setCenterServerType(lineData[11]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setCenterServerType(lineData[11]);
									}
									break;
								case 13:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setIpv6CdnType1(lineData[13]);
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setIpv6CdnType1(lineData[13]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setIpv6CdnType1(lineData[13]);
									}
									break;
								case 14:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setIpv6CdnType2(lineData[14]);
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setIpv6CdnType2(lineData[14]);
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setIpv6CdnType2(lineData[14]);
									}
									break;
								case 15:
									if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setIpv6CdnType3(lineData[15]);
										flag = 1;
									}
									else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
									{
										lst2ndChPlayIP.setIpv6CdnType3(lineData[15]);
										end_flag = 1;
										bakup_data_cnt = 1;
										flag = 2;
									}
									else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
									{
										lst3rdChPlayIP.setIpv6CdnType3(lineData[15]);
										end_flag = 1;
										flag = 3;
									}
									break;
								case 16:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server1(lineData[16]);
										}
										else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
										{
											lst2ndChPlayIP.setVodIpv6Server1(lineData[16]);
										}
										else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
										{
											lst3rdChPlayIP.setVodIpv6Server1(lineData[16]);
										}
									}
									break;
								case 17:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server2(lineData[17]);
										}
										else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
										{
											lst2ndChPlayIP.setVodIpv6Server2(lineData[17]);
										}
										else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
										{
											lst3rdChPlayIP.setVodIpv6Server2(lineData[17]);
										}
									}
									break;
								case 18:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server3(lineData[18]);
										}
										else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
										{
											lst2ndChPlayIP.setVodIpv6Server3(lineData[18]);
										}
										else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
										{
											lst3rdChPlayIP.setVodIpv6Server3(lineData[18]);
										}
									}
								case 19:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server1Type(lineData[19]);
										}
										else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
										{
											lst2ndChPlayIP.setVodIpv6Server1Type(lineData[19]);
										}
										else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
										{
											lst3rdChPlayIP.setVodIpv6Server1Type(lineData[19]);
										}
									}
									break;
								case 20:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server2Type(lineData[20]);
										}
										else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
										{
											lst2ndChPlayIP.setVodIpv6Server2Type(lineData[20]);
										}
										else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
										{
											lst3rdChPlayIP.setVodIpv6Server2Type(lineData[20]);
										}
									}
									break;
								case 21:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server3Type(lineData[21]);
										}
										else if(paramVO.getBaseCd4d().substring(0, 1).equals(tempVO.getChkBaseGb2()))
										{
											lst2ndChPlayIP.setVodIpv6Server3Type(lineData[21]);
										}
										else if(tempVO.getChkBaseGb3().equals("N") && tempVO.getChkBaseGb3().length() == 1)
										{
											lst3rdChPlayIP.setVodIpv6Server3Type(lineData[21]);
										}
									}								
									break;
								default:
									break;
								}
								if(end_flag == -1)
								{
									break;
								}
							}
							if (end_flag == 0) {
								tok_cnt++;
							}
							if (flag == 1) {
								lstTmpChPlayIP.setResultType("4DCDN");
								resultVO1.add(lstTmpChPlayIP);
							} else if (flag == 2) {
								lst2ndChPlayIP.setResultType("4DCDN");
								resultVO2.add(lst2ndChPlayIP);
							} else if (flag == 3) {
								lst3rdChPlayIP.setResultType("4DCDN");
								resultVO3.add(lst3rdChPlayIP);			
							}
						}
						
						iPos_result_4d = 1;
						if(load_balancing_cnt > 0)
						{
							switch(paramVO.getSvcNode())
							{
								case "N":
									if(paramVO.getBaseCdTmp4d().substring(0, 1).equals("D"))
									{
										iPos_D++;
										if(iPos_D > load_balancing_cnt)
										{
											iPos_D = 1;
										}
										iPos_result_4d = iPos_D;
									}
									else
									{
										iPos_R++;
										if(iPos_R > load_balancing_cnt)
										{
											iPos_R = 1;
										}
										iPos_result_4d = iPos_R;
									}
									break;
								case "A":
									if(paramVO.getBaseCdTmp5g().substring(0, 1).equals("F"))
									{
										iPos_AD++;
										if(iPos_AD > load_balancing_cnt)
										{
											iPos_AD = 1;
										}
										iPos_result_4d = iPos_AD;
									}
									else
									{
										iPos_AR++;
										if(iPos_AR > load_balancing_cnt)
										{
											iPos_AR = 1;
										}
										iPos_result_4d = iPos_AR;
									}
									break;
								case "T":
									if(paramVO.getBaseCdTmp().substring(0, 1).equals("F"))
									{
										iPos_TD++;
										if(iPos_TF > load_balancing_cnt)
										{
											iPos_TD = 1;
										}
										iPos_result_4d = iPos_TD;
									}
									else
									{
										iPos_TR++;
										if(iPos_TR > load_balancing_cnt)
										{
											iPos_TR = 1;
										}
										iPos_result_4d = iPos_TR;
									}
									break;
								case "M":
									if(paramVO.getBaseCdTmp5g().substring(0, 1).equals("F"))
									{
										iPos_MD++;
										if(iPos_MD > load_balancing_cnt)
										{
											iPos_MD = 1;
										}
										iPos_result_4d = iPos_MD;
									}
									else
									{
										iPos_MR++;
										if(iPos_MR > load_balancing_cnt)
										{
											iPos_MR = 1;
										}
										iPos_result_4d = iPos_MR;
									}
									break;
								default:
									break;
							}
						}

						if (tok_cnt > 0) {
							if(paramVO.getIpv6Flag().equals("Y"))
							{
								resultVO1.get(iPos_result_4d-1).setVodIpv6Server1(ipv6_change(paramVO, resultVO1.get(iPos_result_4d-1).getVodIpv6Server1(), "", resultVO1.get(iPos_result_4d-1).getLocalPlayIp(), resultVO1.get(iPos_result_4d-1).getIpv6CdnType1(), "Y"));
								resultVO1.get(iPos_result_4d-1).setVodIpv6Server2(ipv6_change(paramVO, resultVO1.get(iPos_result_4d-1).getVodIpv6Server2(), "", resultVO1.get(iPos_result_4d-1).getNearPlayIp(), resultVO1.get(iPos_result_4d-1).getIpv6CdnType2(), "Y"));
								resultVO1.get(iPos_result_4d-1).setVodIpv6Server3(ipv6_change(paramVO, resultVO1.get(iPos_result_4d-1).getVodIpv6Server3(), "", resultVO1.get(iPos_result_4d-1).getCenterPlayIp(), resultVO1.get(iPos_result_4d-1).getIpv6CdnType3(), "Y"));
								
								if(resultVO1.get(iPos_result_4d-1).getVodIpv6Server1Type().equals("")) resultVO1.get(iPos_result_4d-1).setVodIpv6Server1Type(resultVO1.get(iPos_result_4d-1).getLocalServerType());
								if(resultVO1.get(iPos_result_4d-1).getVodIpv6Server2Type().equals("")) resultVO1.get(iPos_result_4d-1).setVodIpv6Server2Type(resultVO1.get(iPos_result_4d-1).getNearServerType());
								if(resultVO1.get(iPos_result_4d-1).getVodIpv6Server3Type().equals("")) resultVO1.get(iPos_result_4d-1).setVodIpv6Server3Type(resultVO1.get(iPos_result_4d-1).getCenterServerType());
							}
							
							resultVO.add(resultVO1.get(iPos_result_4d-1));
						} else if (tok_cnt == 0 && bakup_data_cnt == 1) {
							if(paramVO.getIpv6Flag().equals("Y"))
							{
								resultVO2.get(0).setVodIpv6Server1(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server1(), "", resultVO2.get(0).getLocalPlayIp(), resultVO2.get(0).getIpv6CdnType1(), "Y"));
								resultVO2.get(0).setVodIpv6Server2(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server2(), "", resultVO2.get(0).getNearPlayIp(), resultVO2.get(0).getIpv6CdnType2(), "Y"));
								resultVO2.get(0).setVodIpv6Server3(ipv6_change(paramVO, resultVO2.get(0).getVodIpv6Server3(), "", resultVO2.get(0).getCenterPlayIp(), resultVO2.get(0).getIpv6CdnType3(), "Y"));
								
								if(resultVO2.get(0).getVodIpv6Server1Type().equals("")) resultVO2.get(0).setVodIpv6Server1Type(resultVO2.get(0).getLocalServerType());
								if(resultVO2.get(0).getVodIpv6Server2Type().equals("")) resultVO2.get(0).setVodIpv6Server2Type(resultVO2.get(0).getNearServerType());
								if(resultVO2.get(0).getVodIpv6Server3Type().equals("")) resultVO2.get(0).setVodIpv6Server3Type(resultVO2.get(0).getCenterServerType());
							}
							
							resultVO.add(resultVO2.get(0));
						} else {
							if(paramVO.getIpv6Flag().equals("Y"))
							{
								resultVO3.get(0).setVodIpv6Server1(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server1(), "", resultVO3.get(0).getLocalPlayIp(), resultVO3.get(0).getIpv6CdnType1(), "Y"));
								resultVO3.get(0).setVodIpv6Server2(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server2(), "", resultVO3.get(0).getNearPlayIp(), resultVO3.get(0).getIpv6CdnType2(), "Y"));
								resultVO3.get(0).setVodIpv6Server3(ipv6_change(paramVO, resultVO3.get(0).getVodIpv6Server3(), "", resultVO3.get(0).getCenterPlayIp(), resultVO3.get(0).getIpv6CdnType3(), "Y"));
								
								if(resultVO3.get(0).getVodIpv6Server1Type().equals("")) resultVO3.get(0).setVodIpv6Server1Type(resultVO3.get(0).getLocalServerType());
								if(resultVO3.get(0).getVodIpv6Server2Type().equals("")) resultVO3.get(0).setVodIpv6Server2Type(resultVO3.get(0).getNearServerType());
								if(resultVO3.get(0).getVodIpv6Server3Type().equals("")) resultVO3.get(0).setVodIpv6Server3Type(resultVO3.get(0).getCenterServerType());
							}
							
							resultVO.add(resultVO3.get(0));						
						}
						
					}
					// 2021.04.15 - 아이돌라이브 유료채널 (PC WEB or 모바일 WEB은 정보 제공)
					else if("P".equals(paramVO.getCallFlag()) || "B".equals(paramVO.getCallFlag())){
						
						tok_cnt = 0;
						load_balancing_cnt = 0;
						bakup_data_cnt = 0;
						resultVO1 = new ArrayList<GetNSChnlPlayIPResponseVO>();
						
						for (int i = 0; i < arrResult.length; i++) {
							String[] lineData	= arrResult[i].split("\\" + ImcsConstants.COLSEP);
							end_flag = 0;
							load_balancing = "N";
							int flag = 0;
							tempVO = new GetNSChnlPlayIPResponseVO();
							lstTmpChPlayIP = new GetNSChnlPlayIPResponseVO();
							 
							for (int j = 1; j < lineData.length ; j++) {

								switch (j) {
								case 1:
									tempVO.setFileSvcNode(lineData[1]);
									break;
								case 2:
									tempVO.setChkBaseGb3(lineData[2]);
									if(lineData[2].length()== 1){
										tempVO.setChkBaseGb1(tempVO.getChkBaseGb3().substring(0, 1));
									}
									
									if( !( paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()) ) )
									{
										end_flag = -1;
									}
									
									break;
								case 3:
									switch(paramVO.getSvcNode())
									{
										case "P":
											if(!((paramVO.getBaseCdpcweb().substring(0, 1).equals(lineData[3].substring(0, 1)) && lineData[3].length() == 2) || lineData[3].equals("P")))
											{
												end_flag = -1;
											}
											break;
										default:										
											break;
									}
									if( !paramVO.getSvcNode().substring(0, 1).equals(tempVO.getFileSvcNode()) )
									{
										end_flag = -1;
									}
									break;
								case 4:
									load_balancing = "N";									
									break;
								case 5:
									load_balancing_cnt = 0;
									break;
								case 6:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setLocalPlayIp(lineData[6]);
									}
									break;
								case 7:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setNearPlayIp(lineData[7]);
									}
									break;
								case 8:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setCenterPlayIp(lineData[8]);
									}
									break;
								case 9:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setLocalServerType(lineData[9]);
									}
									break;
								case 10:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setNearServerType(lineData[10]);
									}
									break;
								case 11:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setCenterServerType(lineData[11]);
									}
									break;
								case 13:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setIpv6CdnType1(lineData[13]);
									}
									break;
								case 14:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setIpv6CdnType2(lineData[14]);
									}
									break;
								case 15:
									if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
									{
										lstTmpChPlayIP.setIpv6CdnType3(lineData[15]);
										flag = 1;
									}
									break;
								case 16:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server1(lineData[16]);
										}
									}
									break;
								case 17:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server2(lineData[17]);
										}									
									}
									break;
								case 18:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server3(lineData[18]);
										}
									}
								case 19:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server1Type(lineData[19]);
										}										
									}
									break;
								case 20:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server2Type(lineData[20]);
										}									
									}
									break;
								case 21:
									if(paramVO.getIpv6Flag().equals("Y"))
									{
										if(paramVO.getBaseCdpcweb().substring(1, 2).equals(tempVO.getChkBaseGb1()))
										{
											lstTmpChPlayIP.setVodIpv6Server3Type(lineData[21]);
										}									
									}								
									break;
								default:
									break;
								}
								if(end_flag == -1)
								{
									break;
								}
							}
							if (end_flag == 0) {
								tok_cnt++;
							}
							if (flag == 1) {
								if("P".equals(paramVO.getCallFlag()))
								{
									lstTmpChPlayIP.setResultType("PCWEB");
								}
								else
								{
									lstTmpChPlayIP.setResultType("BBWEB");
								}
								resultVO1.add(lstTmpChPlayIP);
							}
						}
						

						if (tok_cnt > 0) {
							if(paramVO.getIpv6Flag().equals("Y"))
							{
								resultVO1.get(0).setVodIpv6Server1(ipv6_change(paramVO, resultVO1.get(0).getVodIpv6Server1(), "", resultVO1.get(0).getLocalPlayIp(), resultVO1.get(0).getIpv6CdnType1(), "Y"));
								resultVO1.get(0).setVodIpv6Server2(ipv6_change(paramVO, resultVO1.get(0).getVodIpv6Server2(), "", resultVO1.get(0).getNearPlayIp(), resultVO1.get(0).getIpv6CdnType2(), "Y"));
								resultVO1.get(0).setVodIpv6Server3(ipv6_change(paramVO, resultVO1.get(0).getVodIpv6Server3(), "", resultVO1.get(0).getCenterPlayIp(), resultVO1.get(0).getIpv6CdnType3(), "Y"));
								
								if(resultVO1.get(0).getVodIpv6Server1Type().equals("")) resultVO1.get(0).setVodIpv6Server1Type(resultVO1.get(0).getLocalServerType());
								if(resultVO1.get(0).getVodIpv6Server2Type().equals("")) resultVO1.get(0).setVodIpv6Server2Type(resultVO1.get(0).getNearServerType());
								if(resultVO1.get(0).getVodIpv6Server3Type().equals("")) resultVO1.get(0).setVodIpv6Server3Type(resultVO1.get(0).getCenterServerType());
							}
							
							resultVO.add(resultVO1.get(0));
						}
					}
					
					long tt2 = System.currentTimeMillis();
					imcsLog.timeLog("파일Fetch:", String.valueOf((tt2 - tt1)), methodName, methodLine);
					
					String resultHeader  = String.format("0||");
					resultListVO.setResultHeader(resultHeader);
					resultListVO.setList(resultVO);
					
					
				}else{
					msg = " File [" + fname + "] checkResFile Failed ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					isLastProcess = ImcsConstants.RCV_MSG6; 
				}	
			}else{
				msg = " File [" + fname + "] open failed ";
				imcsLog.serviceLog(msg, methodName, methodLine);
				isLastProcess = ImcsConstants.RCV_MSG6; 
				String resultHeader  = String.format("%s|%s|", "1", "FILE OPEN FAIL");
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
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID060) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
    
	/**
	 * IPv4 -> IPv6로 변환
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	ipv6_change
	 */
	public String ipv6_change(GetNSChnlPlayIPRequestVO paramVO, String ipv6_ip, String ipv6_port, String ipv4_ip, String cdn_type, String fourd_replay) throws Exception {
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
							if(fourd_replay.equals("N"))
							{
								result_ipv6 = "[" + paramVO.getPrefixInternet() + result_ipv6 + "]";
							}
							else
							{
								result_ipv6 = "rtsp://[" + paramVO.getPrefixInternet() + result_ipv6 + "]:80";
							}
						}
						else
						{
	//						result_ipv6 = ipv4_protocol + "://" + paramVO.getPrefixUplushdtv() + result_ipv6 + ":" + ipv4_port + "/";							
							if(fourd_replay.equals("N"))
							{
								result_ipv6 = "[" + paramVO.getPrefixUplushdtv() + result_ipv6 + "]";
							}
							else
							{
								result_ipv6 = "rtsp://[" + paramVO.getPrefixUplushdtv() + result_ipv6 + "]:80";
							}
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
						if(fourd_replay.equals("N"))
						{
							result_ipv6 = "[" + ipv6_ip + "]";
						}
						else
						{
							result_ipv6 = "rtsp://[" + ipv6_ip + "]:80";
						}
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
