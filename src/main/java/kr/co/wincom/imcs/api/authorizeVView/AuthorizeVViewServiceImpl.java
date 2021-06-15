package kr.co.wincom.imcs.api.authorizeVView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeVViewServiceImpl implements AuthorizeVViewService {
	private Log imcsLogger = LogFactory.getLog("API_authorizeVView");
	
	@Autowired
	private AuthorizeVViewDao authorizeVViewDao;
	
	@Autowired
	private CommonService commonService;

//	public void authorizeVView(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	int[] iPos_arr = new int[100];
	int iPos_var = 0;	
	
	int NORMAL_WIFI = 0;
	int NORMAL_LTE = 1;
	int NORMAL_5G_W = 2;
	int NORMAL_5G_L = 3;
	int NORMAL_4D_W = 4;
	int NORMAL_4D_L = 5;
	int NORMAL_AWS = 6;
	int NORMAL_MP_MUSIC_W = 7;
	int NORMAL_MP_MUSIC_L = 8;

	int SVC_R = 10;
	// 2020.02.04 - TV앱 공연 노드 로드밸런싱 추가
	int SVC_U = 20;
	
	private static String regexIPv4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
	private static String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	private static String regexIPv4andIPv6 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
    	
	@Override
	public AuthorizeVViewResultVO authorizeVView(AuthorizeVViewRequestVO paramVO){
//		this.authorizeVView(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];		
		String methodName = stackTraceElement.getMethodName();
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + "service call");
		
		AuthorizeVViewResultVO	resultListVO		= new AuthorizeVViewResultVO();
		AuthorizeVViewResponseVO tempVO				= new AuthorizeVViewResponseVO();
		List<AuthorizeVViewResponseVO> resultVO		= new ArrayList<AuthorizeVViewResponseVO>();
		
	    String msg				= "";
	    String szWatermarkYn	= "";
		String szCapUrl			= "";
		
	    Integer nResultSet = 0;
	    
		long tp1 = 0;
		long tp2 = 0;
		long tp4 = 0;
		long tp4_1 = 0;
		
		try{
			
			// 2019.10.04 - IPv6듀얼스택지원 : IPv6PrefixIP 정보 조회
			List<String> IPv6Prefix = commonService.getIPv6PrefixIP(ImcsConstants.NSAPI_PRO_ID090.split("/")[1], "L", imcsLog);
			paramVO.setPrefixInternet(IPv6Prefix.get(0));
			paramVO.setPrefixUplushdtv(IPv6Prefix.get(1));
						
			// PPV 여부 조회
			this.getPpvYn(paramVO);
			nResultSet = paramVO.getResult_set();

			if(nResultSet == 0){		// PPV 면 WATERMARK 조회
				szWatermarkYn	= this.getWatermarkCk(paramVO);
			}
			
			tp1	= System.currentTimeMillis();
			
			// 자막파일 조회
			if(nResultSet == 0){
				tempVO	= this.getSmiInfo(paramVO);				
				
				/* 다국어 자막 관련 로직		*/
				if( "Y".equals(paramVO.getDecPosYn()) && !"".equals(tempVO.getCapFileSize2()) && "Y".equals(tempVO.getCapFileEncryptYn()) ){
					
					tempVO.setCapFileName(tempVO.getCapFileName2());
					tempVO.setCapFileSize(tempVO.getCapFileSize2());
										
				}else{
					tempVO.setCapFileEncryptYn("N");
				}
				
				tempVO.setCapFileName(tempVO.getCapFileName() + "\b" + tempVO.getCapFileName());
			
				tp4	= System.currentTimeMillis();
				imcsLog.timeLog("자막파일 조회", String.valueOf(tp4 - tp1), methodName, methodLine);
			}
			
			if("Y".equals(szWatermarkYn) || "Y".equals(tempVO.getWatermarkYn()) ){
				tempVO.setWatermarkYn("Y");
			}else{
				tempVO.setWatermarkYn("N");
			}
			

			// 자막파일 url정보 조회
			nResultSet	= paramVO.getResult_set();
			if(nResultSet == 0){
				if( "Y".equals(tempVO.getSmiYn()) && "Y".equals(tempVO.getSmiImpYn()) ){
					try {
						szCapUrl		= commonService.getImgReplaceUrl2("cap_server", "authorizeVView");
					} catch(Exception e) {
//						imcsLog.failLog(ImcsConstants.API_PRO_ID177, "", null, "smi_svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
						paramVO.setResultCode("31000000");
						throw new ImcsException();
					}
					
					tp4_1	= System.currentTimeMillis();
					imcsLog.timeLog("자막파일 경로 조회", String.valueOf(tp4_1 - tp4), methodName, methodLine);
				}
			}

			nResultSet	= paramVO.getResult_set();
			tp2	= System.currentTimeMillis();
			
			if(nResultSet == 0){
				
				// 구간점프 이미지 조회
				try {
					tempVO.setThumbnailViewUrl(commonService.getImgReplaceUrl2("vod_zip_server", "authorizeVView"));
				} catch(Exception e) {
//					imcsLog.failLog(ImcsConstants.API_PRO_ID177, "", null, "thumbnail_svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
					
					//paramVO.setResultCode("31000000");
					throw new ImcsException();
				}
			
				
				//썸네일 정보 가져오기
				this.getThumnailInfo(paramVO, tempVO);
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("구간점프 이미지 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
				
			}
			
			resultListVO.setM3u8list(this.getVodM3u8Search(paramVO, tempVO));
			
			if( "Y".equals(tempVO.getSmiYn()) && "Y".equals(tempVO.getSmiImpYn()) ){
				tempVO.setCapUrl(szCapUrl + "\b" + tempVO.getVodServer1());
			}
			else
			{
				//2019.07.22 - 암호화 자막 정보도 SMI_YN이 N일 경우에는 응답값으로 내려주지 않는다.
				tempVO.setCapUrl("\b");
				tempVO.setCapFileName("\b");
				tempVO.setCapFileSize("");
				tempVO.setCapFileEncryptYn("N");
			}
						
			if(paramVO.getResult_set() == 0) {
				tempVO.setFlag("0");
				tempVO.setTicketId(paramVO.getSaId() + paramVO.getAlbumId());
				tempVO.setErrCode("");
				tempVO.setLinkTime("0");
				tempVO.setOnetimekey("");
				
			} else {
				tempVO	= new AuthorizeVViewResponseVO();
				tempVO.setFlag("1");
				tempVO.setErrCode("99");
				tempVO.setWatermarkYn(szWatermarkYn);
			}
			
			// 2019.09.16 - IPv6듀얼스택 지원 : ipv6지원하는 단말이 아닐 경우에는 ipv6관련 정보를 전부 null로 전달한다.
	        if(paramVO.getIpv6Flag().equals("N"))
	        {
	        	tempVO.setVodIpv6Server1("");
	        	tempVO.setVodIpv6Server2("");
	        	tempVO.setVodIpv6Server3("");
	        	tempVO.setVodIpv6Server1Type("");
	        	tempVO.setVodIpv6Server2Type("");
	        	tempVO.setVodIpv6Server3Type("");
	        }
			
			resultVO.add(0, tempVO);
			resultListVO.setList(resultVO);
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
//			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID177) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]" + " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	


	/**
	 * 구간정보 이미지 조회
	 * @param paramVO
	 * @return
	 */
	public void getThumnailInfo(AuthorizeVViewRequestVO paramVO, AuthorizeVViewResponseVO tempVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod177_009_20171214_001";
		List<StillImageVO> list	= new ArrayList<StillImageVO>();
		
		try {
			
			try {
				list = authorizeVViewDao.getThumnailInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				for(int i = 0 ; i < list.size() ; i++)
				{
					if(list.get(i).getPosterType().equals("Z"))
					{
						tempVO.setTimeInfo(list.get(i).getTimeInfo());
						tempVO.setThumbnailViewFileName(list.get(i).getImgFileName());
					}
					else if(list.get(i).getPosterType().equals("X"))
					{
						tempVO.setThumbnailViewFileName6s(list.get(i).getImgFileName());
					}						
				}
			}

			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID177, sqlId, cache, "thumbnail_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
	}
	
	
	/**
	 * M3U8정보 조회
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	getVodM3u8Search
	 */
	public List<AuthorizePlayIpVO> getVodM3u8Search(AuthorizeVViewRequestVO paramVO, AuthorizeVViewResponseVO tempVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		List<M3u8ProfileVO> vodM3u8List = new ArrayList<M3u8ProfileVO>();
		List<AuthorizePlayIpVO> authorizePlayIpList = new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO authorizePlayIp;
		
		// 이전에 조회한 node_group일 경우에는 CDN IP를 재사용하기 위해 변수값으로 제어한다.
		int i_dup_chk = 0;
		
		try {
			
				try{
					vodM3u8List = authorizeVViewDao.getVodProfile(paramVO);
				}catch(DataAccessException e){
					//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
				
				if( vodM3u8List == null || vodM3u8List.isEmpty()){
					paramVO.setResult_set(-1);
				} else {
					for(int m3u8_cnt = 0 ; m3u8_cnt < vodM3u8List.size() ; m3u8_cnt++)
					{
						i_dup_chk = 0;
						authorizePlayIp = null;
						authorizePlayIp = new AuthorizePlayIpVO();
						// 2019.04.02 - 일반노드 배포인데, MP화질 O / VR X / 공연 O일 경우에는 온누리넷만 배포하므로 별도로 처리한다. (WIFI의 경우 H, LTE의 경우 E로 처리)
						if(vodM3u8List.get(m3u8_cnt).getNodeGroup().equals("N")  && (vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("P") || vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("N")) && tempVO.getVrYn().equals("N") && tempVO.getMusicYn().equals("Y"))
						{
							vodM3u8List.get(m3u8_cnt).setNodeGroup("H");
						}

						// 2019.04.30 - 일반노드 배포인데, MN화질일 경우에는 온누리넷만 배포하므로 별도로 처리한다. (WIFI의 경우 H, LTE의 경우 E로 처리) - 5G 2차 고음질 어셋
						if(vodM3u8List.get(m3u8_cnt).getNodeGroup().equals("N") && vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("N"))
						{
							vodM3u8List.get(m3u8_cnt).setNodeGroup("H");							
						}
						
						authorizePlayIpList.add(m3u8_cnt, authorizePlayIp);
						authorizePlayIpList.get(m3u8_cnt).setNodeGroup(vodM3u8List.get(m3u8_cnt).getNodeGroup());
						
						// CDN IP 정보 조회 중 이전에 조회한 NODE GROUP 과 같을 경우에는 DB를 여러번 조회할 필요 없이 이전에 구한 CDN IP를 그대로 사용한다.
						for(int cdn_chk = 0 ; cdn_chk < m3u8_cnt ; cdn_chk++)
						{
							if(authorizePlayIpList.get(m3u8_cnt).getNodeGroup().equals(authorizePlayIpList.get(cdn_chk).getNodeGroup()))
							{
								authorizePlayIpList.get(m3u8_cnt).setNodeGroup(authorizePlayIpList.get(cdn_chk).getNodeGroup());
								authorizePlayIpList.get(m3u8_cnt).setServerPlayIp1(authorizePlayIpList.get(cdn_chk).getServerPlayIp1());
								authorizePlayIpList.get(m3u8_cnt).setServerPlayIp2(authorizePlayIpList.get(cdn_chk).getServerPlayIp2());
								authorizePlayIpList.get(m3u8_cnt).setServerPlayIp3(authorizePlayIpList.get(cdn_chk).getServerPlayIp3());
								authorizePlayIpList.get(m3u8_cnt).setServerType1(authorizePlayIpList.get(cdn_chk).getServerType1());
								authorizePlayIpList.get(m3u8_cnt).setServerType2(authorizePlayIpList.get(cdn_chk).getServerType2());
								authorizePlayIpList.get(m3u8_cnt).setServerType3(authorizePlayIpList.get(cdn_chk).getServerType3());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6Node1(authorizePlayIpList.get(cdn_chk).getServerIpv6Node1());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6Node2(authorizePlayIpList.get(cdn_chk).getServerIpv6Node2());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6Node3(authorizePlayIpList.get(cdn_chk).getServerIpv6Node3());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6PlayIp1(authorizePlayIpList.get(cdn_chk).getServerIpv6PlayIp1());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6PlayIp2(authorizePlayIpList.get(cdn_chk).getServerIpv6PlayIp2());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6PlayIp3(authorizePlayIpList.get(cdn_chk).getServerIpv6PlayIp3());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6Type1(authorizePlayIpList.get(cdn_chk).getServerIpv6Type1());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6Type2(authorizePlayIpList.get(cdn_chk).getServerIpv6Type2());
								authorizePlayIpList.get(m3u8_cnt).setServerIpv6Type3(authorizePlayIpList.get(cdn_chk).getServerIpv6Type3());
								i_dup_chk = 1;
								break;
							}
						}
						
						if(i_dup_chk == 1)
						{
							continue;
						}
						
						authorizePlayIpList.set(m3u8_cnt, getCdnInfo(authorizePlayIpList.get(m3u8_cnt), paramVO));
					}
					
					for(int m3u8_cnt = 0 ; m3u8_cnt < vodM3u8List.size() ; m3u8_cnt++)
					{
						authorizePlayIpList.get(m3u8_cnt).setM3u8Type(vodM3u8List.get(m3u8_cnt).getM3u8Type());
						authorizePlayIpList.get(m3u8_cnt).setCastisM3u8(vodM3u8List.get(m3u8_cnt).getCastisM3u8());
						authorizePlayIpList.get(m3u8_cnt).setOnnuriM3u8(vodM3u8List.get(m3u8_cnt).getOnnuriM3u8());
						
						if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("H") || vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("L"))
						{
							// 2019.02.16 - CastIs local IP Set
							if(authorizePlayIpList.get(m3u8_cnt).getServerType1().equals("1"))
							{
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHL(vodM3u8List.get(m3u8_cnt).getCastisM3u8());
								}
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLL(vodM3u8List.get(m3u8_cnt).getCastisM3u8());
								}
							}
							// 2019.02.16 - OnNet local IP Set
							else
							{
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHL(vodM3u8List.get(m3u8_cnt).getOnnuriM3u8());
								}
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLL(vodM3u8List.get(m3u8_cnt).getOnnuriM3u8());
								}
							}

							// 2019.02.16 - CastIs Near IP Set							
							if(authorizePlayIpList.get(m3u8_cnt).getServerType2().equals("1"))
							{
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHN(vodM3u8List.get(m3u8_cnt).getCastisM3u8());
								}
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLN(vodM3u8List.get(m3u8_cnt).getCastisM3u8());
								}
							}
							// 2019.02.16 - OnNet Near IP Set
							else
							{
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHN(vodM3u8List.get(m3u8_cnt).getOnnuriM3u8());
								}
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLN(vodM3u8List.get(m3u8_cnt).getOnnuriM3u8());
								}
							}

							// 2019.02.16 - CastIs Center IP Set
							if(authorizePlayIpList.get(m3u8_cnt).getServerType3().equals("1"))
							{
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHC(vodM3u8List.get(m3u8_cnt).getCastisM3u8());
								}
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLC(vodM3u8List.get(m3u8_cnt).getCastisM3u8());
								}
							}
							// 2019.02.16 - OnNet Center IP Set
							else
							{
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHC(vodM3u8List.get(m3u8_cnt).getOnnuriM3u8());
								}
								if(vodM3u8List.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLC(vodM3u8List.get(m3u8_cnt).getOnnuriM3u8());
								}
							}
							tempVO.setVodServer1(authorizePlayIpList.get(m3u8_cnt).getServerPlayIp1());
							tempVO.setVodServer2(authorizePlayIpList.get(m3u8_cnt).getServerPlayIp2());
							tempVO.setVodServer3(authorizePlayIpList.get(m3u8_cnt).getServerPlayIp3());
							tempVO.setVodServer1Type(authorizePlayIpList.get(m3u8_cnt).getServerType1());
							tempVO.setVodServer2Type(authorizePlayIpList.get(m3u8_cnt).getServerType2());
							tempVO.setVodServer3Type(authorizePlayIpList.get(m3u8_cnt).getServerType3());
							tempVO.setServerIpv6Node1(authorizePlayIpList.get(m3u8_cnt).getServerIpv6Node1());
							tempVO.setServerIpv6Node2(authorizePlayIpList.get(m3u8_cnt).getServerIpv6Node2());
							tempVO.setServerIpv6Node3(authorizePlayIpList.get(m3u8_cnt).getServerIpv6Node3());
							tempVO.setVodIpv6Server1(authorizePlayIpList.get(m3u8_cnt).getServerIpv6PlayIp1());
							tempVO.setVodIpv6Server2(authorizePlayIpList.get(m3u8_cnt).getServerIpv6PlayIp2());
							tempVO.setVodIpv6Server3(authorizePlayIpList.get(m3u8_cnt).getServerIpv6PlayIp3());
							tempVO.setVodIpv6Server1Type(authorizePlayIpList.get(m3u8_cnt).getServerIpv6Type1());
							tempVO.setVodIpv6Server2Type(authorizePlayIpList.get(m3u8_cnt).getServerIpv6Type2());
							tempVO.setVodIpv6Server3Type(authorizePlayIpList.get(m3u8_cnt).getServerIpv6Type3());
							
							continue;
						}
					}
				}
				
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return authorizePlayIpList;
	}
	
	
	/**
	 * CDN INFO 조회 로직 모음
	 * @param	AuthorizeNSViewRequestVO
	 * @throws Exception 
	 * @result 	void
	 */
	public AuthorizePlayIpVO getCdnInfo(AuthorizePlayIpVO PlayIpVO, AuthorizeVViewRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		String szNodeCd	= ""; 
		AuthorizePlayIpVO lstCdnInfo = new AuthorizePlayIpVO();				
		
		try {
			
			// 2020.11.11 - 운영팀 테스트용으로 T BASE_CD 허용 (황영환 책임님 요청)
			// 				5G / 4D / HEVC / AWS 모두 다 T로 동작하도록 함
			if(!PlayIpVO.getNodeGroup().equals("N"))
			{
				if(PlayIpVO.getNodeGroup().equals("F"))
				{
					if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("W"))
					{
						iPos_var = NORMAL_5G_W;
						PlayIpVO.setTmpNodeGroup("F");
					}
					else if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("L"))
					{
						iPos_var = NORMAL_5G_L;
						PlayIpVO.setTmpNodeGroup("V");
					}
					else if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("T"))
					{
						PlayIpVO.setTmpNodeGroup("T");
					}
				}
				else if(PlayIpVO.getNodeGroup().equals("D"))
				{
					if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("W"))
					{
						iPos_var = NORMAL_4D_W;
						PlayIpVO.setTmpNodeGroup("D");
					}
					else if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("L"))
					{
						iPos_var = NORMAL_4D_L;
						PlayIpVO.setTmpNodeGroup("R");
					}
					else if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("T"))
					{
						PlayIpVO.setTmpNodeGroup("T");
					}
				}
				else if(PlayIpVO.getNodeGroup().equals("Z"))
				{
					if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("T"))
					{
						PlayIpVO.setTmpNodeGroup("T");
					}
					else
					{
						iPos_var = NORMAL_AWS;
						PlayIpVO.setTmpNodeGroup("Z");
					}					
				}
				else if(PlayIpVO.getNodeGroup().equals("H"))
				{
					// 2019.04.02 - 일반노드 배포인데, MP화질 O / VR X / 공연 O일 경우에는 온누리넷만 배포하므로 별도로 처리한다. (WIFI의 경우 H, LTE의 경우 E로 처리)
					// 2019.05.13 - MN화질도 온누리넷만 배포
					if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("W"))
					{
						iPos_var = NORMAL_MP_MUSIC_W;
						PlayIpVO.setTmpNodeGroup("H");
					}
					else if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("L"))
					{
						iPos_var = NORMAL_MP_MUSIC_L;
						PlayIpVO.setTmpNodeGroup("E");
					}
					else if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("T"))
					{
						PlayIpVO.setTmpNodeGroup("T");
					}
				}
			}
			else
			{
				if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("W"))
				{
					iPos_var = NORMAL_WIFI;
					PlayIpVO.setTmpNodeGroup("W");
				}
				else if(paramVO.getBaseCd() != null && paramVO.getBaseCd().substring(0,1).equals("T"))
				{
					PlayIpVO.setTmpNodeGroup("T");
				}
				else
				{
					iPos_var = NORMAL_LTE;
					PlayIpVO.setTmpNodeGroup("L");
				}
			}
			
			switch(paramVO.getSvcNode())
			{
				case "N":
					break;
				case "R":
					iPos_var = iPos_var + SVC_R;
					PlayIpVO.setTmpNodeGroup("A" + PlayIpVO.getTmpNodeGroup());
					break;
				// 2020.02.04 - TV앱 공연 노드 로드밸런싱 추가
				case "U":
					iPos_var = iPos_var + SVC_U;
					PlayIpVO.setTmpNodeGroup("U" + PlayIpVO.getTmpNodeGroup());
					break;
				default:
					break;
			}
			
			try{

				if( "Y".equals(paramVO.getBaseGb()) ) {
					
					/* WI-FI 사용자의 요청에 따라 SUB_NODE_CD값을 순차적으로 응답하기 위하여 모든 노드에 대하여
					 * 현재 iPos값에 해당하는 순번의 노드값 반환(방송운영팀 요청에 의한 개발)			*/
					// 20200825 - LTE도 로드밸런싱을 한다.
					if(!PlayIpVO.getTmpNodeGroup().equals("T"))
					{
						szNodeCd = this.getNodeCdLB(paramVO, PlayIpVO);
					}
								
					// 지역노드 정보 조회
					// 20200825 - LTE도 로드밸런싱을 하기 위해 LTE만 위해 따로 조회하던 로직을 주석 처리
					if( "".equals(szNodeCd) && PlayIpVO.getTmpNodeGroup().equals("T")) {
						PlayIpVO.setBaseCondi(paramVO.getBaseCd());
						szNodeCd = this.getNodeCd(PlayIpVO, paramVO);
					}
					
					// 위 조회 결과 미존재 시 baseCd 한자리로 재조회
					if( "".equals(szNodeCd)) {
						PlayIpVO.setBaseCondi(PlayIpVO.getTmpNodeGroup());
						szNodeCd = this.getNodeCd(PlayIpVO, paramVO);
					}
					
					// 위 조회 결과 미존재 시 1234567890 하드 코딩 값으로 재조회
					if( "".equals(szNodeCd)) {
						PlayIpVO.setBaseCondi("1234567890");
						szNodeCd = this.getNodeCd(PlayIpVO, paramVO);
					}
					
					// 서버 리스트 정보 조회
					PlayIpVO.setNodeCd(szNodeCd);
					lstCdnInfo	= this.getVodServer1(paramVO, PlayIpVO);
					
				} else {
					lstCdnInfo	= this.getVodServer2(paramVO, PlayIpVO);
				}
			
				
				// 위 CDN 정보 조회 결과 없을 경우 기지국 코드를  1234567890으로 하드코딩 후 재조회
				if(lstCdnInfo == null) {
					if( "Y".equals(paramVO.getBaseGb()) ) {
						lstCdnInfo	= this.getVodServer3(paramVO, PlayIpVO);
					}
					
					if( "N".equals(paramVO.getBaseGb()) ) {
						lstCdnInfo	= this.getVodServer4(paramVO, PlayIpVO);
					}
				}
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			

		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
				
		lstCdnInfo.setNodeGroup(PlayIpVO.getNodeGroup());
		
		lstCdnInfo.setServerIpv6PlayIp1(ipv6_change(paramVO, lstCdnInfo.getServerIpv6PlayIp1(), lstCdnInfo.getServerIpv6Port(), lstCdnInfo.getServerPlayIp1(), lstCdnInfo.getServerIpv6Node1(), "N"));
		lstCdnInfo.setServerIpv6PlayIp2(ipv6_change(paramVO, lstCdnInfo.getServerIpv6PlayIp2(), lstCdnInfo.getServerIpv6Port(), lstCdnInfo.getServerPlayIp2(), lstCdnInfo.getServerIpv6Node2(), "N"));
		lstCdnInfo.setServerIpv6PlayIp3(ipv6_change(paramVO, lstCdnInfo.getServerIpv6PlayIp3(), lstCdnInfo.getServerIpv6Port(), lstCdnInfo.getServerPlayIp3(), lstCdnInfo.getServerIpv6Node3(), "N"));
		
		if(paramVO.getIpv6Flag().equals("Y"))
		{
			if(lstCdnInfo.getServerIpv6Type1().equals("")) lstCdnInfo.setServerIpv6Type1(lstCdnInfo.getServerType1());
			if(lstCdnInfo.getServerIpv6Type2().equals("")) lstCdnInfo.setServerIpv6Type2(lstCdnInfo.getServerType2());
			if(lstCdnInfo.getServerIpv6Type3().equals("")) lstCdnInfo.setServerIpv6Type3(lstCdnInfo.getServerType3());
		}
		else
		{
			lstCdnInfo.setServerIpv6Type1("");
			lstCdnInfo.setServerIpv6Type2("");
			lstCdnInfo.setServerIpv6Type3("");
		}
		
		return lstCdnInfo;
	}

	
	
	
	/**
	 *  PPV 여부 조회
	 *  @param	
	 *  @result	
	 */
	public void getPpvYn(AuthorizeVViewRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		String sqlId	= "lgvod177_001_20171214_001";
		int querySize = 0;
		List<Integer> list   = null;
		Integer resultVO = null;
		
		try {
			
			try{
				list = authorizeVViewDao.getPpvYn(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
			} else {
				querySize = list.size();
				resultVO = list.get(0);
				paramVO.setPpvYn(resultVO);
				
				if(resultVO > 0){
					paramVO.setResult_set(-1);
				}
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID177, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
	}
	
	
	/**
	 * 워터마크 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getWatermarkCk(AuthorizeVViewRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId			=  "lgvod177_002_20171214_001";
		String szWatermarkYn	= "";
		List<String> list	= new ArrayList<String>();
		
		try {
			
			try{
				list = authorizeVViewDao.getWatermarkCk(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szWatermarkYn	= list.get(0);
			}
		
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return szWatermarkYn;
    }
	
	
	
	
	/**
	 * 자막정보 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public AuthorizeVViewResponseVO getSmiInfo(AuthorizeVViewRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod177_003_20171214_001";
		List<AuthorizeVViewResponseVO> list   = new ArrayList<AuthorizeVViewResponseVO>();
		AuthorizeVViewResponseVO resultVO = new AuthorizeVViewResponseVO();
		
		try {
			try{
				list = authorizeVViewDao.getSmiInfo(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = list.get(0);
			}
			
			
		} catch (Exception e) {
			
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID177, sqlId, cache, "smi_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			paramVO.setResult_set(-1);
		}
		
		return resultVO;
    }
	
	
	
	/**
	 * 노드 정보 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCd(AuthorizePlayIpVO playIpVO, AuthorizeVViewRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId	=  "lgvod177_004_20171214_001";
    	String szNodeCd	= "";
		List<String> list   = new ArrayList<String>();
		
		try {
			
			try{
				list = authorizeVViewDao.getNodeCd(playIpVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			
			if( list != null && !list.isEmpty()){
				szNodeCd	= list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		}
		
    	return szNodeCd;
    }
	
	
	/**
	 * CDN 정보 조회1 (지역노드 정보가 존재할 경우)
	 * @param paramVO
	 * @return
	 */
	public AuthorizePlayIpVO getVodServer1(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) {
		IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod177_005_20171214_001";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		
		try {
			
			try {
				list = authorizeVViewDao.getVodServer1(playIpVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				returnVO = new AuthorizePlayIpVO();
				querySize	= list.size();
				returnVO = list.get(0);
			}

			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID177, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
			paramVO.setResult_set(-1);			
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID177, sqlId, cache, "svr_play_ip:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return returnVO;
	}
	
	
	
	
	
	/**
	 * 시청일 경우 CDN 정보 조회 (지역노드 정보가 미존재할 경우)
	 * @param paramVO
	 * @return
	 */
	public AuthorizePlayIpVO getVodServer2(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod177_006_20171214_001";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		
		try {
			
			try {
				playIpVO.setSaId(paramVO.getSaId());
				playIpVO.setStbMac(paramVO.getStbMac());
				list = authorizeVViewDao.getVodServer2(playIpVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				returnVO = new AuthorizePlayIpVO();
				querySize	= list.size();
				returnVO = list.get(0);
			}

			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID177, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
			paramVO.setResult_set(-1);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID177, sqlId, cache, "svr_play_ip:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return returnVO;
	}




	
	
	/**
	 * 시청일 경우 CDN 정보 조회
	 * @param paramVO
	 * @return
	 */
	public AuthorizePlayIpVO getVodServer3(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod177_007_20171214_001";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		
		try {
			
			try {
				list = authorizeVViewDao.getVodServer3(playIpVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			

			if (list != null && !list.isEmpty()) {
				returnVO = new AuthorizePlayIpVO();
				querySize	= list.size();
				returnVO = list.get(0);
			}else{
//				imcsLog.failLog(ImcsConstants.API_PRO_ID177, sqlId, cache, "svr_play_ip:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
			}

			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID177, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
			paramVO.setResult_set(-1);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID177, sqlId, cache, "svr_play_ip:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return returnVO;
	}

	
	
	public AuthorizePlayIpVO getVodServer4(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO playIpVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod177_008_20171214_001";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		
		try {
			
			try {
				list = authorizeVViewDao.getVodServer4(playIpVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				returnVO = new AuthorizePlayIpVO();
				querySize	= list.size();
				returnVO = list.get(0);
			}else{
//				imcsLog.failLog(ImcsConstants.API_PRO_ID177, sqlId, cache, "svr_play_ip:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
			}

			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID177, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
			paramVO.setResult_set(-1);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID177, sqlId, cache, "svr_play_ip:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return returnVO;
	}

	/**
	 * 노드 정보 조회(와이파이)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCdLB(AuthorizeVViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId	=  "lgvod177_010_20171214_001";
    	String szNodeCd	= "";
		List<String> list   = new ArrayList<String>();
		
		try {
			try{
				if(PlayIpVO.getTmpNodeGroup().length() == 1){
					list = authorizeVViewDao.getNodeCdLoadBalancing1(PlayIpVO);
				}
				else if(PlayIpVO.getTmpNodeGroup().length() == 2){
					list = authorizeVViewDao.getNodeCdLoadBalancing2(PlayIpVO);
				}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			/* 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
			 * PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
			 * 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
			 */
			if(iPos_arr[iPos_var] >= list.size()){
				iPos_arr[iPos_var] = 0;
			}
			
			if( list != null && !list.isEmpty()){				
				szNodeCd	= list.get(iPos_arr[iPos_var]);
				iPos_arr[iPos_var]++;
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		}
		
    	return szNodeCd;
    }
	
	/**
	 * IPv4 -> IPv6로 변환
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	ipv6_change
	 */
	public String ipv6_change(AuthorizeVViewRequestVO paramVO, String ipv6_ip, String ipv6_port, String ipv4_ip, String cdn_type, String fourd_replay) throws Exception {
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
							result_ipv6 = ipv4_protocol + "://[" + paramVO.getPrefixInternet() + result_ipv6 + "]:" + ipv4_port + "/";
						}
						else
						{
							result_ipv6 = ipv4_protocol + "://[" + paramVO.getPrefixUplushdtv() + result_ipv6 + "]:" + ipv4_port + "/";
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
						result_ipv6 = "http://[" + ipv6_ip + "]:" + ipv6_port + "/";
					}
					else
					{
						result_ipv6 = "rtsp://[" + ipv6_ip + "]:80/";
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