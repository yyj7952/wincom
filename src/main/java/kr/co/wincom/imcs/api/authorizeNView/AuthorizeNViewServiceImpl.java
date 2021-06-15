package kr.co.wincom.imcs.api.authorizeNView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;
import kr.co.wincom.imcs.common.vo.M3u8AWSProfileVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.authorizeNView.AuthorizeNViewDao2;

@Service
public class AuthorizeNViewServiceImpl implements AuthorizeNViewService {
	private Log imcsLogger		= LogFactory.getLog("API_authorizeNView");
	
	@Autowired
	private AuthorizeNViewDao authorizeNViewDao;
	
	@Autowired
	private AuthorizeNViewDao2 authorizeNViewDao2;
	
	@Autowired
	private CommonService commonService;
	
//	public void authorizeNView(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	
	int iPos = 1;
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
	
	
	int iPos_AW = 1;
	int iPos_AL = 1;
	
	private static String regexIPv4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
	private static String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	private static String regexIPv4andIPv6 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	
	@Override
	public AuthorizeNViewResultVO authorizeNView(AuthorizeNViewRequestVO paramVO){
		
		// 2020.03.30 - 프로야구 하이라이트 VOD 정보 제공을 위해 별도 서버에 하이트라이트 시청정보를 저장하는 API를 호출하여 별도 DB에 데이터 적재
		String BaseBallServerUrl = StringUtil.replaceNull(commonService.getServerProperties("BaseBall_server_url"),"http://localhost:8070/highlightapi/");
		String BaseBallTimeOut = StringUtil.replaceNull(commonService.getServerProperties("BaseBall_authorize_timeout"),"2");
		String BaseBallServerUseYn = StringUtil.replaceNull(commonService.getServerProperties("BaseBall_server_use_yn"),"N");
		
		
		// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
		// -------------------------------------------------------------------------------------------------------------------
		String AWS_chk_app = StringUtil.replaceNull(commonService.getServerProperties("AWS_chk_app"),"P");
		String AWS_chk_web = StringUtil.replaceNull(commonService.getServerProperties("AWS_chk_web"),"C");
		String AWS_app = "N";
		String AWS_web = "N";
		
		String[] AWS_app_arr = AWS_chk_app.split(";");
		for(String str_aws : AWS_app_arr)
		{
			if(paramVO.getApplType().length() == 4)
			{
				if(paramVO.getApplType().substring(0, 1).equals(str_aws))
				{
					AWS_app = "Y";
					break;
				}
			}
		}
		
		if(AWS_app.equals("Y"))
		{
			String[] AWS_web_arr = AWS_chk_web.split(";");
			for(String str_aws : AWS_web_arr)
			{
				if(paramVO.getApplType().length() == 4)
				{
					if(paramVO.getApplType().substring(2, 3).equals(str_aws))
					{
						AWS_web = "Y";
						break;
					}
				}
			}
		}
		// -------------------------------------------------------------------------------------------------------------------
		
		
//		String NXLinkTimeServerUrl = StringUtil.replaceNull(commonService.getServerProperties("getNXLinkTime_server_ip"),"http://106.249.52.153");
//		String copyTimeOut = StringUtil.replaceNull(commonService.getServerProperties("getNXLinkTime_timeout"),"2");
		
//		this.authorizeNView(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodName = stackTraceElement.getMethodName();
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + "service call");		

		AuthorizeNViewResultVO	resultListVO	= new AuthorizeNViewResultVO();
		AuthorizeNViewResponseVO tempVO			= new AuthorizeNViewResponseVO();
		List<AuthorizeNViewResponseVO> resultVO	= new ArrayList<AuthorizeNViewResponseVO>();
		AuthorizePlayIpVO playIpListVO = new AuthorizePlayIpVO();
		
	    String msg				= "";
	    String szWatermarkYn	= "";
	    String szCapUrl			= "\b";
	    
	    int nMainCnt	= 0;
	    int nDataChk	= 0;
	    int nScreenCnt	= 0; // 엔스크린 가입여부 확인. 결과값이 0 이면 오류로 리턴.	
	    String szLinkTime = "0";
	    
	    Integer nResultSet	= 0;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		
		HashMap<String, Object> mbuyInfo		= null;
		List<HashMap> mThumnailInfo	= null;
		List<M3u8ProfileVO> m3u8Info        = null;
		HashMap<String, String> nScreenBuyChk	= null;
		List<ContTypeVO> lstProdInfo			= null;
		//List<ComCdnVO> lstCdnInfo	= null;
		AuthorizePlayIpVO lstCdnInfo	= null;		
		ComCpnVO cpnInfoVO 			= new ComCpnVO();
		
		ContTypeVO prodInfoVO		= null;
		FmInfoVO fmInfoVO 			= null;
		
		try{
			//파라미터 체크 (2018.07.26 권형도)
			Integer retParamValid = Request_Param_Valid(paramVO);
			if ( retParamValid < 0 ) {
				return resultListVO;
			}

			// 2019.10.04 - IPv6듀얼스택지원 : IPv6PrefixIP 정보 조회
			List<String> IPv6Prefix = commonService.getIPv6PrefixIP(ImcsConstants.NSAPI_PRO_ID090.split("/")[1], "L", imcsLog);
			paramVO.setPrefixInternet(IPv6Prefix.get(0));
			paramVO.setPrefixUplushdtv(IPv6Prefix.get(1));
			
			// 엔스크린 페어링 여부 확인(엔스크린 가입 여부 확인)
			if(paramVO.getnWatchYn().equals("Y"))
			{
				nScreenCnt = this.nScreenPairingChk(paramVO);
				
				if(nScreenCnt == 0)
				{
					paramVO.setResultCode("21000000");
					
					tempVO.setFlag("1"); // 성공여부 코드값
					tempVO.setErrCode("80"); // 오류 구분 코드. 80 : non pairing
					tempVO.setViewFlag(paramVO.getViewType());
					resultVO.add(0, tempVO);
					resultListVO.setList(resultVO);
				}
			}
			
			
			// N_WATCH_YN 이 N 일때 또는
			// N_WATCH_YN 이 Y 이고 엔스크린에 가입되어 있을 때
			// N_WATCH_YN 이 Y 이고 엔스크린에 가입이 안되어 있으면 중간에 오류로 리턴해줘야 하는데
			// Exception 을 발생시키면 공백을 리턴함.
			if(paramVO.getnWatchYn().equals("N") || (paramVO.getnWatchYn().equals("Y") && nScreenCnt > 0))
			{
				lstProdInfo = this.getProdInfo(paramVO);
				
				if(lstProdInfo.size()==0 && paramVO.getnWatchYn().equals("Y")) {
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[conts_type:]";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				if(lstProdInfo != null && lstProdInfo.size() > 0){
					nMainCnt = lstProdInfo.size();
				}
				
				for(int i = 0; i < nMainCnt; i++){
					
					prodInfoVO = lstProdInfo.get(i);
					
					paramVO.setContsType(prodInfoVO.getContsType());
					if(paramVO.getnWatchYn().equals("Y")) {
						paramVO.setNscreenYn(prodInfoVO.getNscreenYn());
					}
					
					List<String> datafreePriceList = null;
					
					 /* 데이터 프리 구매 여부 조회	*/
					try {
				        /*
				         * 2017.08.11 엔스크린(nScreen) - 엔스크린 권한이면 데이터프리 구매 여부를 체크할 필요가 없다.
				         * n_watch_yn이 Y일 때 함수 타는지는 확인 필요!
					     * 2018.05.15 - TV 앱 사용자의 경우 FVOD 시청만 가능하기 때문에 구매여부를 체크할 필요가 없다.
				         */
						datafreePriceList = authorizeNViewDao.getBuyDataFreeInfo(paramVO);
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] chkPaymentId [SELECT DATAFREE PT_VO_BUY] table ["+datafreePriceList.size()+"] records Success at";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
					} catch (Exception e) {
						//imcsLog.failLog(ImcsConstants.API_PRO_ID178, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
					}
					
					
					if(paramVO.getFreeFlag().equals("N") && paramVO.getnWatchYn().equals("N") && datafreePriceList != null && datafreePriceList.size() > 0 && !"".equals(datafreePriceList.get(0)) ){
						//nResultSet = 0;
						tp1	= System.currentTimeMillis();
						
						paramVO.setDatafreePrice(datafreePriceList.get(0));
						paramVO.setDatafreeBuyYn("Y");
						paramVO.setDatafreeWatchYn("Y");
						
						nDataChk = 1;
						
						if("1".equals(paramVO.getContsType())){	// 컨텐츠 타입이 PPV일 경우
							tp1	= System.currentTimeMillis();
							int nEventType	= this.getEventType(paramVO);
							
							if(nEventType != 0)		continue;
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("EVENT 타입 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
						
						paramVO.setProductId(paramVO.getAlbumId());			
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("DataFree 구매내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
											
					}else{
						//nResultSet = 1;
						
						paramVO.setDatafreeBuyYn("N");
						paramVO.setDatafreeWatchYn("N");
						

						if (paramVO.getnWatchYn().equals("Y")) {
							String[] nscArray = paramVO.getNscreenYn().split(";");
							String nscYn = nscArray[0];

							if (nscYn.equals("Y")) {
								String[] productArray = nscArray[1].split("/");
								 //단방향이 M일경우 시청불가
								if(nscArray[2].equals("M")){
									tempVO.setReplay4dYn("N");
									tempVO.setFlag("1");
									tempVO.setCapUrl("\b");
									tempVO.setCapFileName("\b");
									tempVO.setErrCode("60");
									tempVO.setWatermarkYn(szWatermarkYn);
									
									tempVO.setDatafreeWatchYn(paramVO.getDatafreeWatchYn());
									tempVO.setPresentYn(paramVO.getPresentYn());
									tempVO.setViewFlag(paramVO.getViewType());
									resultVO.add(0, tempVO);
									resultListVO.setCpnInfoVO(cpnInfoVO);
									resultListVO.setList(resultVO);
									
									paramVO.setResultCode("21000000");
									return resultListVO;
									}
								
								if (productArray[0].equals("0")) {
									nDataChk = 1;
									paramVO.setProductId(paramVO.getAlbumId());
									paramVO.setFvodFlag("0");
									msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] msg[FVOD 컨텐츠 입니다.]";
									imcsLog.serviceLog(msg, methodName, methodLine);
									break;
								}

							}
							if (nscYn.equals("N")) {

								tempVO.setReplay4dYn("N");
								tempVO.setFlag("1");
								tempVO.setCapUrl("\b");
								tempVO.setCapFileName("\b");
								tempVO.setErrCode("60");
								tempVO.setWatermarkYn(szWatermarkYn);
								
								tempVO.setDatafreeWatchYn(paramVO.getDatafreeWatchYn());
								tempVO.setPresentYn(paramVO.getPresentYn());
								tempVO.setViewFlag(paramVO.getViewType());
								resultVO.add(0, tempVO);
								resultListVO.setCpnInfoVO(cpnInfoVO);
								resultListVO.setList(resultVO);
								
								paramVO.setResultCode("21000000");
								return resultListVO;
								
							}
						}
						
						
						if("0".equals(paramVO.getContsType())){			// 컨텐츠 타입이 FVOD 일 경우							
							if(paramVO.getnWatchYn().equals("N"))
							{
								nDataChk = 1;
								paramVO.setProductId(paramVO.getAlbumId());
								
								break;
							}
						}
						// 컨텐츠 타입이 PPV일 경우
						// 2018.05.15 - TV 앱 사용자는 FVOD 컨텐츠 시청만 가능
						else if("1".equals(paramVO.getContsType()) && paramVO.getnWatchYn().equals("Y") == false && !paramVO.getFreeFlag().equals("Y")) {
							tp1	= System.currentTimeMillis();
							int nEventType	= this.getEventType(paramVO);
							
							if(nEventType != 0)		continue;
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("EVENT 타입 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
							
							
							if(nResultSet == 0) {
								// 구매 내역 조회
								tp1	= System.currentTimeMillis();
								
								try {
									
									mbuyInfo	= authorizeNViewDao.getBuyInfo(paramVO);
									if(mbuyInfo != null) {
										paramVO.setReservePrice(StringUtil.nullToSpace( (String) mbuyInfo.get("RESERVED_PRICE")));
										paramVO.setExpiredDate(StringUtil.nullToSpace( (String) mbuyInfo.get("EXPIRED_DATE")));
										paramVO.setExpiredDateUp(StringUtil.nullToSpace( (String) mbuyInfo.get("EXPIRED_DATE_TMP")));
										paramVO.setRefundYn(StringUtil.nullToSpace( (String) mbuyInfo.get("REFUND_YN")));
										
										msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] chkPaymentId [SELEC PT_VO_BUY] tables [" + mbuyInfo.size() + "] records Success at ";
										imcsLog.serviceLog(msg, methodName, methodLine);
									} else {
										nResultSet	= -1;
									}
								} catch(Exception e) {
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[" + ImcsConstants.LOG_MSG2 + "] msg[" + String.format("%-21s", ImcsConstants.RCV_MSG6 + "]");
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									nResultSet	= -1;
								}
								
								// 구매 내역이 없는 경우 받은 선물 여부 체크
								if(nResultSet != 0) {
									
									paramVO.setProductId(paramVO.getAlbumId());
									
									nDataChk = this.getPresentCnt(paramVO);
									
									if(nDataChk > 0) {
										nDataChk = 1;
										
										paramVO.setPresentYn("Y");
										paramVO.setProductId(paramVO.getAlbumId());
										
										nResultSet = 0;
										
										break;
									}
									
									continue;
								}
								
								tp2	= System.currentTimeMillis();
								imcsLog.timeLog("구매내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
							}
							
							// 예약 구매의 경우 유효기간을 체크하지 않는다.
							if("R".equals(paramVO.getRefundYn())){
								nDataChk	= 1;
								
								paramVO.setProductId(paramVO.getAlbumId());
								break;
							}
							
							nDataChk = this.chkBuyCont(paramVO);
							
							if(nDataChk > 0) {
								nDataChk	= 1;
								paramVO.setProductId(paramVO.getAlbumId());
								break;
							}
							
						// 컨텐츠 타입이 PVOD일 경우
						// 2018.05.15 - TV 앱 사용자는 FVOD 컨텐츠만 시청할 수 있다. c_free_flag == Y 이면 TV앱 사용자
						}else if("2".equals(paramVO.getContsType()) && !paramVO.getnWatchYn().equals("Y") && !paramVO.getFreeFlag().equals("Y")){
							nDataChk = this.chkBuyCont(paramVO);
							
							if (nDataChk > 0) {
								nDataChk = 1;
								paramVO.setProductId("");
								break;
							}
						}
					}
					
					if(!paramVO.getDatafreeUseYn().equals(paramVO.getDatafreeWatchYn())){
						
						/* 데이터 프리 여부와 실제 데이터 프리를 구매 하였는지에 대한 값을 비교하여 일치하지 않을 경우
						 * 데이터 프리 oneTimeKey를 생성하지 않는다.
						 */
						paramVO.setDatafreeWatchYn("N");
						
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[DF_WARN] DF_PRICE["+paramVO.getDatafreePrice()+"], DF_BUY_YN["+paramVO.getDatafreeBuyYn()+"], SAME_CTN["+paramVO.getSameCtn()+"]";
						imcsLog.serviceLog(msg, methodName, methodLine);					
						
						paramVO.setResultCode("21000999");
					}				
					
				}
				
				if(prodInfoVO != null) {
					resultListVO.setProductId(StringUtil.nullToSpace(prodInfoVO.getProductId()));
					resultListVO.setProductName(StringUtil.nullToSpace(prodInfoVO.getProductName()));
					resultListVO.setProductPrice(StringUtil.nullToSpace(paramVO.getReservePrice()));
					resultListVO.setContentsName(StringUtil.nullToSpace(prodInfoVO.getContsName()));
				}
				
			    // 2017.08.14 엔스크린(NSCREEN) - 쿼리 PLAN 확인 필요
			    // 선판매 체크 어떻게 할지 고민하기.
				if(paramVO.getnWatchYn().equals("Y") && !paramVO.getFvodFlag().equals("0"))
			    {
			    	// 20170817 엔스크린(NSCREEN) 구매 여부 체크
			    	if(!paramVO.getnBuyDate().equals("N"))
			    	{
						try
						{							
							nScreenBuyChk = authorizeNViewDao.getNScreenBuyChk(paramVO);
							
							if(nScreenBuyChk != null && nScreenBuyChk.size() > 0)
							{
								nDataChk = Integer.parseInt(nScreenBuyChk.get("DATA_CHK"));
								resultListVO.setProductId(StringUtil.nullToSpace(nScreenBuyChk.get("PRODUCT_ID")));
								paramVO.setProductId(StringUtil.nullToSpace(nScreenBuyChk.get("PRODUCT_ID")));
							}
							
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[lgvod178_025_20171128_001] rcv[ORACLE" + nScreenBuyChk.size() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);							
						} catch (Exception e) {
//							imcsLog.failLog(ImcsConstants.API_PRO_ID178, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
						}
			    	}
			    	
			    	if(nDataChk > 0) /* data가 있으면 */
			    	{
			    		nDataChk = 1;
			    	}
			    	else
			    	{
			    		if(paramVO.getnBuyDate().equals("N"))
			    		{
			    			// 20170817 엔스크린(NSCREEN) 가입 여부 체크
//			    			List<String> productCdList = this.nscreenSubscriptionChkProductCdList(paramVO);
							List<String> productCdList = new ArrayList<>();
			    			String chkCat = this.chkCategory(paramVO);
			    			
			    			if(chkCat.equals("1")) {
			    				productCdList = this.kidProductCd(paramVO);
			    			} else {
			    				productCdList = this.nscreenSubscriptionChkProductCdList(paramVO);
			    			}
			    			
			    			nDataChk = this.nscreenSubscriptionChk(productCdList, paramVO, resultListVO);
			    			
			    			if (nDataChk == 1) {
			    				HashMap<String, String> nNScreenAssetInfo = authorizeNViewDao.getNScreenAssetInfo(paramVO);
			    				
			    				if (nNScreenAssetInfo!=null) {
			    					paramVO.setNscreenGenreSmall(nNScreenAssetInfo.get("GENRE_SMALL"));
			    					paramVO.setSuggestedPrice(nNScreenAssetInfo.get("SUGGESTED_PRICE"));
			    					paramVO.setTerrYn(nNScreenAssetInfo.get("TERR_YN"));
			    					paramVO.setTerrPeriod(nNScreenAssetInfo.get("TERR_PERIOD"));
			    					paramVO.setTerrEddate(nNScreenAssetInfo.get("TERR_ED_DATE"));
			    					paramVO.setOnairDate(nNScreenAssetInfo.get("ONAIR_DATE"));
			    					paramVO.setPreviewPeriod(nNScreenAssetInfo.get("PREVIEW_PERIOD"));
			    					paramVO.setCpId(nNScreenAssetInfo.get("CP_ID"));
			    					
			    					// 2019.11.01 - VOD 정산 프로세스 개선 : 메타 테이블에 넣을 정보 Set
			    					paramVO.setAssetName(nNScreenAssetInfo.get("ASSET_NAME"));
			    					paramVO.setRatingCd(nNScreenAssetInfo.get("RATING_CD"));
			    					paramVO.setRunTime(nNScreenAssetInfo.get("RUN_TIME"));
			    					paramVO.setCpIdUflix(nNScreenAssetInfo.get("CP_ID_UFLIX"));			    				
			    					paramVO.setCpIdUflix(nNScreenAssetInfo.get("CP_ID_UFLIX"));
			    					paramVO.setSeriesNo(nNScreenAssetInfo.get("SERIES_NO"));
			    				}
			    			}
			    		}
			    	}
			    }

			    // 2018.05.15 - TV 앱 사용자의 경우 시청이력, 이어보기 여부를 조회하지 않는다. c_free_flag == Y 일 경우 TV앱 사용자
			    if(nDataChk == 1 && paramVO.getFreeFlag().equals("N"))
			    {
//					// 이어보기 시간정보 조회 (LinkTime)
//					tp1	= System.currentTimeMillis();
//					
//					// 2019.01.29 - 2월 19일 IPTV,모바일 DB 분리 대응
//					if (!paramVO.getnWatchYn().equals("Y")) {
//						//szLinkTime = StringUtil.nullToZero(authorizeNViewDao.getLinkTimeN(paramVO));
//						szLinkTime = authorizeNViewDao.getLinkTimeN(paramVO);
//						if (!StringUtil.isEmpty(szLinkTime) || szLinkTime != null) {
//							paramVO.setLinkChk("1");
//						} else {
//							szLinkTime = "0";
//						}
//						
//						tp2	= System.currentTimeMillis();
//						imcsLog.timeLog("SELETE PT_VO_SET_TIME_PTT_NSC", String.valueOf(tp2 - tp1), methodName, methodLine);
//					} else {
//						//2021.01.21 Seamless TF 3차
//						String NXLinkTimeServerFullUrl = String.format("%s/servlets/CommSvl?CMD=%s&PARAM=SA_ID=%s|STB_MAC=%s|ALBUM_ID=%s|S_SA_ID=%s|S_STB_MAC=%s|",
//								NXLinkTimeServerUrl, 
//								"getNXLinkTime",
//								paramVO.getSaId(), 
//								paramVO.getStbMac(),
//								paramVO.getAlbumId(),
//								paramVO.getnSaId(),
//								paramVO.getnStbMac());
//						
//						szLinkTime = urlCall(NXLinkTimeServerFullUrl, copyTimeOut, paramVO);//getNXLinkTime 이어보기 정보 가져오기
//						
//						tp2	= System.currentTimeMillis();
//						imcsLog.timeLog("getNXLinkTime 호출", String.valueOf(tp2 - tp1), methodName, methodLine);
//					}
					
					// 이어보기 시간정보 조회 (LinkTime)
					tp1	= System.currentTimeMillis();
					
					// 2019.01.29 - 2월 19일 IPTV,모바일 DB 분리 대응
					if (!paramVO.getnWatchYn().equals("Y")) {
						//szLinkTime = StringUtil.nullToZero(authorizeNViewDao.getLinkTimeN(paramVO));
						szLinkTime = authorizeNViewDao.getLinkTimeN(paramVO);
						if (!StringUtil.isEmpty(szLinkTime) || szLinkTime != null) {
							paramVO.setLinkChk("1");
						} else {
							szLinkTime = "0";
						}
					} else {
						szLinkTime = StringUtil.nullToZero(authorizeNViewDao.getLinkTime(paramVO));
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("SELETE PT_VO_SET_TIME_PTT_NSC", String.valueOf(tp2 - tp1), methodName, methodLine);
					
			    } else if (paramVO.getFreeFlag().equals("Y")) {
			    	paramVO.setLinkTime("0");
			    }
				imcsLog.timeLog("구매만료 여부check", String.valueOf(tp2 - tp_start), methodName, methodLine);
				
				
				if (nDataChk != 1){
					paramVO.setResultSet(-1);
//					paramVO.setLinkChk("1");
					
					if(paramVO.getnWatchYn().equals("Y") == false){
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[buy_info:"+ImcsConstants.RCV_MSG3+"]";
					}
					else if(paramVO.getnBuyDate().equals("N")) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[nscreen_subscription_info:"+ImcsConstants.RCV_MSG3+"]";
					}
					else {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[nscreen_buy_info:"+ImcsConstants.RCV_MSG3+"]";
					}
					
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else {// 데이터가 존재하면 
					nMainCnt = 0;
					
					// 워터마크 유무 체크
					szWatermarkYn	= this.getWatermarkChk(paramVO);
					
					// 자막파일관련 정보 조회
					tempVO	= this.getSmiInfo(paramVO);
					
					if(!tempVO.getReplay4dYn().equals("")) {
						paramVO.setReplay4dYn(tempVO.getReplay4dYn());
					}
					if(!tempVO.getReplay4dContValue().equals("")) {
						// 2020.02.28 - 프로야구 4D Replay 물리파일명 DB정보와 상관없이 확장자를 .4ds로 바꿨던 것을 .. 그냥 DB정보 그대로 주게 수정
//						tempVO.setReplay4dContValue(tempVO.getReplay4dContValue() + ".4ds");
					}
					
					if("D".equals(paramVO.getViewType()) && "Y".equals(tempVO.getReplay4dYn())){
						nResultSet	= -1;
					}
					
					/* 다국어 자막 관련 로직		*/
					if( "Y".equals(paramVO.getDecPosYn()) && !"".equals(tempVO.getCapFileName2()) && "Y".equals(tempVO.getCapFileEncryptYn()) ){
						
						/* 신 자막 제공
		            	 * 자막 이름과 자막 사이즈를 신 자막으로 제공*/
						
						tempVO.setCapFileName(tempVO.getCapFileName2());
						tempVO.setCapFileSize(tempVO.getCapFileSize2());
											
					}else{
						/* 구 자막 제공
		            	 * 자막 언어를 한국어와 영어에 한해서 제공하기 위하여 데이터 가공*/
						tempVO.setCapFileEncryptYn("N");					
							
						String szSmiLanguage = "";
						
						if( tempVO.getSmiLanguage().indexOf("한국어") >= 0){
							szSmiLanguage = "한국어;";
						}
						
						if( tempVO.getSmiLanguage().indexOf("영어") >= 0){
							szSmiLanguage = szSmiLanguage + "영어;";
						}
						
						if(!"".equals(szSmiLanguage)){
							tempVO.setSmiLanguage(szSmiLanguage);
						}
					}
					
					tempVO.setLinkTime(szLinkTime);
					tempVO.setCapFileName(tempVO.getCapFileName() + "\b" + tempVO.getCapFileName());
					
					if(paramVO.getViewType().equals("D"))
					{
						paramVO.setContentFileSize(tempVO.getContentFileSize());
					}
					
					tp1	= System.currentTimeMillis();
					imcsLog.timeLog("초기배포여부 및 자막파일 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
	
					// 워터마크 유무 저장 (카테고리나 컨텐츠 중 하나만 있어도 Y)
					if( "Y".equals(szWatermarkYn) || "Y".equals(tempVO.getWatermarkYn()) )
						tempVO.setWatermarkYn("Y");
					else
						tempVO.setWatermarkYn("N");
					
					
					if( "Y".equals(tempVO.getSmiYn()) && "Y".equals(tempVO.getSmiImpYn())) {
						tp1	= System.currentTimeMillis();
						imcsLog.timeLog("자막파일 경로 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
					}
					
					// 시청이력 등록
					nResultSet = paramVO.getResultSet();
					
					//2018.05.15 - TV 앱 사용자는 시청이력을 남기지 않는다. c_free_flag == Y 인 경우 TV 앱 사용자
					if(nResultSet == 0 && paramVO.getFreeFlag().equals("N")){
						String szProductId	= paramVO.getProductId();
						if( "".equals(paramVO.getProductId()) ) {
							try {
								szProductId	= authorizeNViewDao.getProductId(paramVO);
								szProductId	= StringUtil.nullToSpace(szProductId);
								paramVO.setProductId(szProductId);
							}catch(Exception e) {
								msg	= "[PT_VO_BUY_DETAIL_NSC SELECT SQL_ERROR]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								paramVO.setResultCode("41000000");
								
								nResultSet = -1;
							}
						}
						if("".equals(szProductId)) {
							msg	= "[PT_VO_BUY_DETAIL_NSC SELECT no data]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							paramVO.setResultCode("21000000");
							
							nResultSet	= -1;
						} 
						if(!paramVO.getnWatchYn().equals("Y")) {
							this.insertWatchHis(paramVO);
						} else {
							this.insertWatchHisNScreen(paramVO);
						}
						
						nResultSet = paramVO.getResultSet();
						
						
						if(nResultSet == 0){
							tp3	= System.currentTimeMillis();
							imcsLog.timeLog("시청이력 등록", String.valueOf(tp3 - tp2), methodName, methodLine);
						}
					}
					
					
					//구간점프 이미지 조회
					try {
						tempVO.setThumbnailViewUrl(commonService.getImgReplaceUrl2("vod_zip_server", "authorizeNView"));
					} catch(Exception e) {
//						imcsLog.failLog(ImcsConstants.API_PRO_ID178, "", null, "thumbnail_svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
						
						paramVO.setResultCode("31000000");
						throw new ImcsException();
					}
					
					// 구간점프 이미지 조회
					mThumnailInfo = this.getThumnailInfo(paramVO);
					
					if(mThumnailInfo != null) {
						for (int i=0; i < mThumnailInfo.size(); i++) {
							tempVO.setPosterType((String)mThumnailInfo.get(i).get("POSTER_TYPE"));
							if (tempVO.getPosterType().equals("Z")) {
								tempVO.setThumbnailViewFileName((String)mThumnailInfo.get(i).get("CONTENT_VALUE"));
								tempVO.setTimeInfo((String)mThumnailInfo.get(i).get("SECOND_VALUE"));
							} else if (tempVO.getPosterType().equals("X")) {
								tempVO.setImgFileName6s((String)mThumnailInfo.get(i).get("CONTENT_VALUE"));
							}
						}
						
					}
					
					tp1	= System.currentTimeMillis();
					imcsLog.timeLog("구간점프 이미지 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
				}
				
				nResultSet = paramVO.getResultSet();
				
				if (paramVO.getFreeFlag().equals("Y") && !paramVO.getContsType().equals("0")) {
					tempVO = new AuthorizeNViewResponseVO();
					// 2020.01.08 - 시청 권한 없고, 스트리밍 요청시 실패 응답값을 제대로 주기 위해 Default Set
					tempVO.setReplay4dYn("N");
					
					tempVO.setFlag("1");
					tempVO.setCapUrl("\b");
					tempVO.setCapFileName("\b");
					tempVO.setErrCode("60");
					tempVO.setWatermarkYn(szWatermarkYn);
					
					paramVO.setResultCode("21000000");
				} else if(nResultSet == 0 && nDataChk == 1) {
					tempVO.setFlag("0");
					
					if(paramVO.getnWatchYn().equals("Y") == false)
						tempVO.setTicketId(paramVO.getSaId() + paramVO.getAlbumId() + paramVO.getBuyingDate());
					else
					{
						if(paramVO.getnBuyDate().equals("N"))
							tempVO.setTicketId(paramVO.getnSaId() + paramVO.getAlbumId());
						else
							tempVO.setTicketId(paramVO.getnSaId() + paramVO.getAlbumId() + paramVO.getnBuyDate());
					}				
					
					// 다운로드 일경우만 리얼HD 파일명 리턴
					if( "D".equals(paramVO.getViewType()) ) {
						String szLiveHevcFileName	= this.getLiveHevcFileName(paramVO);
						
						if(!"".equals(szLiveHevcFileName)) {
							tempVO.setRealHdFileName(szLiveHevcFileName);
							paramVO.setBitRate("M9");
						}
						
						// 2019.02.28 - 다운로드 M9 5G 또는 AWS 노드를 찾는다.
						if(tempVO.getMusicYn().equals("Y") && tempVO.getVrYn().equals("Y")) {
							// 5G 노드를 찾는다.
							playIpListVO.setNodeGroup("F");
							lstCdnInfo	= this.getCdnInfo(playIpListVO, paramVO);
							
							tempVO.setRealHdServer1(lstCdnInfo.getServerPlayIp1());
							tempVO.setRealHdServer2(lstCdnInfo.getServerPlayIp2());
							tempVO.setRealHdServer3(lstCdnInfo.getServerPlayIp3());
							tempVO.setServerIpv6Node1(lstCdnInfo.getServerIpv6Node1());
							tempVO.setServerIpv6Node2(lstCdnInfo.getServerIpv6Node2());
							tempVO.setServerIpv6Node3(lstCdnInfo.getServerIpv6Node3());
							tempVO.setRealHdIpv6Server1(lstCdnInfo.getServerIpv6PlayIp1());
							tempVO.setRealHdIpv6Server2(lstCdnInfo.getServerIpv6PlayIp2());
							tempVO.setRealHdIpv6Server3(lstCdnInfo.getServerIpv6PlayIp3());
							
							if(!lstCdnInfo.getVodFileName().equals("")) {
								if(!paramVO.getBitRate().equals("M9")) {
								tempVO.setVodFileName(lstCdnInfo.getVodFileName());
								}
							}
							
						} else if (tempVO.getVrYn().equals("Y")) {
							// AWS(아마존) 노드를 찾는다
							playIpListVO.setNodeGroup("Z");
							lstCdnInfo	= this.getCdnInfo(playIpListVO, paramVO);
							
							tempVO.setRealHdServer1(lstCdnInfo.getServerPlayIp1());
							tempVO.setRealHdServer2(lstCdnInfo.getServerPlayIp2());
							tempVO.setRealHdServer3(lstCdnInfo.getServerPlayIp3());
							tempVO.setServerIpv6Node1(lstCdnInfo.getServerIpv6Node1());
							tempVO.setServerIpv6Node2(lstCdnInfo.getServerIpv6Node2());
							tempVO.setServerIpv6Node3(lstCdnInfo.getServerIpv6Node3());
							
							tempVO.setRealHdIpv6Server1(lstCdnInfo.getServerIpv6PlayIp1());
							tempVO.setRealHdIpv6Server2(lstCdnInfo.getServerIpv6PlayIp2());
							tempVO.setRealHdIpv6Server3(lstCdnInfo.getServerIpv6PlayIp3());
							
							if(!lstCdnInfo.getVodFileName().equals("")) {
								if(!paramVO.getBitRate().equals("M9")) {
								tempVO.setVodFileName(lstCdnInfo.getVodFileName());
								}
							}
						}
					}
					
					
					// 장르 정보 조회
					String szGenreInfo	= "";
					tp1	= System.currentTimeMillis();
					try {
						GenreInfoVO genreRetVO = this.getGenreInfo(paramVO);
						szGenreInfo = 
								StringUtil.nullToSpace(genreRetVO.getGenreLarge()) + "|" + 
								StringUtil.nullToSpace(genreRetVO.getGenreMid()) + "|" + 
								StringUtil.nullToSpace(genreRetVO.getGenreSmall());
						paramVO.setGenreInfo(szGenreInfo);
						paramVO.setSuggestedPrice(genreRetVO.getSuggestedPrice()); // 2018.07.27 권형도 (lgvod178.c 4818)
					} catch(Exception e) {
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("장르 정보 조회 실패", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					
					
					// 인앱은 쿠폰정보 조회 안함
			        //================================================
			        // 1. IOS 인 경우 쿠폰조회 제외
			        // 2. 받은 선물 시청인 경우 쿠폰조회 제외
			        //================================================
			        //2018.05.15 - TV 앱 사용자의 경우 FVOD 만 이용하기 때문에 쿠폰 조회를 하지 않는다. c_free_flag == Y 이면 TV 앱 사용자
					if( !"I".equals(paramVO.getAppStr()) && "N".equals(paramVO.getPresentYn())
							&& paramVO.getnWatchYn().equals("Y") == false && paramVO.getFreeFlag().equals("N")) {
						tp1	= System.currentTimeMillis();
						try {
							// 쿠폰 정보 조회
							paramVO.setProdType(paramVO.getContsType().substring(0, 1));
							cpnInfoVO	= this.getCpnInfo(paramVO);
							
							tp2	= System.currentTimeMillis();
							//imcsLog.timeLog("스탬프/쿠폰 정보 조회 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
						} catch(Exception e) {
							imcsLog.errorLog(e.toString());
							
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("스탬프/쿠폰 정보 조회 실패", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
					}
					
					
					//예약구매 후 최초 시청의 경우 유효기간 저장
			        //2018.05.15 - TV 앱 사용자의 경우 구매를 하지 않기 때문에 구매 정보 또한 UPDATE 하지 않는다. c_free_flag == Y 이면 TV 앱 사용자
					if( "R".equals(paramVO.getRefundYn()) && "0".equals(paramVO.getExpiredDate()) && paramVO.getFreeFlag().equals("N") ) {
						tp1	= System.currentTimeMillis();
						
						int rlt = 0;
						
						try {
							// setBuyContentExDate
							/*-----------------------------------------------------------------
							/ 예약구매 컨텐츠 최초 시청시, 구매정보 갱신
							------------------------------------------------------------------*/
							rlt = authorizeNViewDao.uptBuyContsExDate(paramVO);
							
						} catch(Exception e) {
							nResultSet	= -1;
							
							paramVO.setResultCode("40000000");
						}
						
						if(rlt == 0){
							
							nResultSet	= -1;
							
							paramVO.setResultCode("21000000");
						}
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("UPDATE 예약구매 컨텐츠유효기간 설정", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					
					
					// FACE-MATCH 정보 조회
					fmInfoVO	= this.getFmInfo(paramVO);
					
					if(fmInfoVO != null) {
						tempVO.setFmYn(StringUtil.nullToSpace(fmInfoVO.getFmYN()));
						tempVO.setAssetId(StringUtil.nullToSpace(fmInfoVO.getAdiProdId()));
					}
					
					tempVO.setFlag("0");
					
					String szAssetId	= "";
					szAssetId	= this.getAssetId(paramVO);
					
					if( !"".equals(szAssetId))	tempVO.setHevcYn("Y");
					
					if(!paramVO.getnWatchYn().equals("Y") && "Y".equals(paramVO.getDatafreeWatchYn())
							&& ( "Y".equals(paramVO.getSameCtn()) || "A".equals(paramVO.getSameCtn()) ) ){
						
						/* 데이터Free One Time Key 생성(AES256 암호화, BASE64 인코딩)
						 * 가입번호 7~10번째 자리(4byte) + Asset ID 5~10번째자리(6byte) + 현재시각
						 * */
						tp1	= System.currentTimeMillis();
						
						String pwd = "";
						
						pwd = this.getEnctyptKey(paramVO);
						
						String strOneTimeKey =commonService.createOneTimeKey(paramVO.getSaId(), paramVO.getAlbumId(), pwd, "V", "", imcsLog);
						
						//위 함수에 로그 포함
						/*msg = " origin [" + strTempMsg + "] AES256 Enc and Base64Encoding result [" + strOneTimeKey + "] key[" + pwd + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);*/
						
						if(strOneTimeKey != null && !"".equals(strOneTimeKey)){
							tempVO.setDatafreeOnetimeKey(strOneTimeKey);
						}
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("데이터 프리 OneTimeKey 생성", String.valueOf(tp2 - tp1), methodName, methodLine);
						
					}else{
						paramVO.setDatafreeWatchYn("N");
					}
					
					String seasonYn = "";
					
					/* 시즌 여부 조회	*/
					seasonYn = this.getSeasonInfo(paramVO);
					
					if("".equals(seasonYn)){
						tempVO.setSeasonYn("N");
					}else{
						tempVO.setSeasonYn("Y");
					}
					
					
					
					//2018.10.30 - HEVC1 ~ 이후 화질은 PT_LA_M3U8_INFO 테이블에서 조회를 한다.
					if(paramVO.getViewType().equals("S") && tempVO.getReplay4dYn().equals("N")) {
						tp1	= System.currentTimeMillis();
						resultListVO.setM3u8list(this.getVodM3u8Search(paramVO, tempVO, AWS_app, AWS_web));
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("M3U8 프로파일 및 CDN IP 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					} else if (paramVO.getViewType().equals("S") && tempVO.getReplay4dYn().equals("Y")) {
						// 2019.02.16 - 4D Replay CDN IP는 CDN구조체 배열의 마지막 항목으로 사용
						tp1	= System.currentTimeMillis();
						playIpListVO.setNodeGroup("D");
						lstCdnInfo = this.getCdnInfo(playIpListVO, paramVO);
						tempVO.setVodServer1(lstCdnInfo.getServerPlayIp1());
						tempVO.setVodServer2(lstCdnInfo.getServerPlayIp2());
						tempVO.setVodServer3(lstCdnInfo.getServerPlayIp3());
						tempVO.setVodServer1Type(lstCdnInfo.getServerType1());
						tempVO.setVodServer2Type(lstCdnInfo.getServerType2());
						tempVO.setVodServer3Type(lstCdnInfo.getServerType3());
						tempVO.setVodIpv6Server1(lstCdnInfo.getServerIpv6PlayIp1());
						tempVO.setVodIpv6Server2(lstCdnInfo.getServerIpv6PlayIp2());
						tempVO.setVodIpv6Server3(lstCdnInfo.getServerIpv6PlayIp3());
						tempVO.setVodIpv6Server1Type(lstCdnInfo.getServerIpv6Type1());
						tempVO.setVodIpv6Server2Type(lstCdnInfo.getServerIpv6Type2());
						tempVO.setVodIpv6Server3Type(lstCdnInfo.getServerIpv6Type3());
						tempVO.setRealHdIpv6Server1("");
						tempVO.setRealHdIpv6Server2("");
						tempVO.setRealHdIpv6Server3("");						
						
						if(tempVO.getServerIpv6Node1().equals("")) {
							tempVO.setServerIpv6Node1(lstCdnInfo.getServerIpv6Node1());
							tempVO.setServerIpv6Node2(lstCdnInfo.getServerIpv6Node2());
							tempVO.setServerIpv6Node3(lstCdnInfo.getServerIpv6Node3());
						}
						
						imcsLog.timeLog("4D Replay CDN IP 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					} else if (paramVO.getViewType().equals("D")) {
						if(paramVO.getDefinFlag().equals("1")) {
							paramVO.setBitRate("M1");	// 4M
						}
						if(paramVO.getDefinFlag().equals("2")) {
							paramVO.setBitRate("M2");	// 2M
						}
						if(paramVO.getDefinFlag().equals("")) {
							paramVO.setBitRate("M2");	// 2M
						}
						
						tp1	= System.currentTimeMillis();
						
						playIpListVO.setNodeGroup("N");
						lstCdnInfo	= this.getCdnInfo(playIpListVO, paramVO);
						
						tempVO.setVodServer1(lstCdnInfo.getServerPlayIp1());
						tempVO.setVodServer2(lstCdnInfo.getServerPlayIp2());
						tempVO.setVodServer3(lstCdnInfo.getServerPlayIp3());
						tempVO.setVodServer1Type(lstCdnInfo.getServerType1());
						tempVO.setVodServer2Type(lstCdnInfo.getServerType2());
						tempVO.setVodServer3Type(lstCdnInfo.getServerType3());
						tempVO.setVodIpv6Server1(lstCdnInfo.getServerIpv6PlayIp1());
						tempVO.setVodIpv6Server2(lstCdnInfo.getServerIpv6PlayIp2());
						tempVO.setVodIpv6Server3(lstCdnInfo.getServerIpv6PlayIp3());
						tempVO.setVodIpv6Server1Type(lstCdnInfo.getServerIpv6Type1());
						tempVO.setVodIpv6Server2Type(lstCdnInfo.getServerIpv6Type2());
						tempVO.setVodIpv6Server3Type(lstCdnInfo.getServerIpv6Type3());
						
						// 5G나 아마존이 프로파일이 없으면 기본M2의 노드 정보를 세팅한다.
						if(tempVO.getServerIpv6Node1().equals("")) {
							tempVO.setServerIpv6Node1(lstCdnInfo.getServerIpv6Node1());
							tempVO.setServerIpv6Node2(lstCdnInfo.getServerIpv6Node2());
							tempVO.setServerIpv6Node3(lstCdnInfo.getServerIpv6Node3());
						}
						
						if(!lstCdnInfo.getVodFileName().equals("")) {
							if(!paramVO.getBitRate().equals("M9")) {
							tempVO.setVodFileName(lstCdnInfo.getVodFileName());
							}
						}
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("DOWNLOAD CDN IP 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						
						// hevc cdn ip조회하는 것을 공통코드에서 다른 cdn ip 구해오는 것과 동일하게 변경(pt_lv_range_ip_info 통해)하면서 hevc파일이 있을 때에만 조회하게 추가
						// 4D_replay or AWS가 아닌 경우에만 조회
						// 다운로드일 경우에는 밑에서 일반노드 IP만 조회하기 때문에 별도 조회 필요
						if(!tempVO.getRealHdFileName().equals("") && !tempVO.getVrYn().equals("Y"))
						{
							tp1	= System.currentTimeMillis();
							playIpListVO.setNodeGroup("H");
							lstCdnInfo = this.getCdnInfo(playIpListVO, paramVO);
							tempVO.setRealHdServer1(lstCdnInfo.getServerPlayIp1());
							tempVO.setRealHdServer2(lstCdnInfo.getServerPlayIp2());
							tempVO.setRealHdServer3(lstCdnInfo.getServerPlayIp3());
							tempVO.setRealHdIpv6Server1(lstCdnInfo.getServerIpv6PlayIp1());
							tempVO.setRealHdIpv6Server2(lstCdnInfo.getServerIpv6PlayIp2());
							tempVO.setRealHdIpv6Server3(lstCdnInfo.getServerIpv6PlayIp3());
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("DOWNLOAD HEVC CDN IP 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
					}
				 
					if( "Y".equals(tempVO.getSmiYn()) && "Y".equals(tempVO.getSmiImpYn())) {
						try {
							szCapUrl = commonService.getImgReplaceUrl2("cap_server", "authorizeNView");
							tempVO.setCapUrl(szCapUrl + "\b" + tempVO.getVodServer1());
						} catch(Exception e) {
//							imcsLog.failLog(ImcsConstants.API_PRO_ID178, "", null, "smi_svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
							paramVO.setResultCode("31000000");
							
							throw new ImcsException();
						}
					}
					else
					{
						//2019.07.22 - 암호화 자막 정보 및 다국어자막어학여부도 SMI_YN이 N일 경우에는 응답값으로 내려주지 않는다.
						tempVO.setCapUrl("\b");
						tempVO.setCapFileName("\b");
						tempVO.setCapFileSize("");
						tempVO.setSmiLanguage("");
						tempVO.setCapFileEncryptYn("N");
						tempVO.setCapFileLanguageYn("N");
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
			        	tempVO.setRealHdIpv6Server1("");
			        	tempVO.setRealHdIpv6Server2("");
			        	tempVO.setRealHdIpv6Server3("");
			        }
			        
			        // 2020.03.30 - 프로야구 하이라이트 VOD 정보 제공을 위해 별도 서버에 하이트라이트 시청정보를 저장하는 API를 호출하여 통합DB가 아닌 별도 DB에 데이터 적재
			        if(paramVO.getApplType().substring(0, 1).equals("P") && BaseBallServerUseYn.equals("Y"))
			        {
			        	try
			        	{
					        //URL호출
					        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
							factory.setConnectTimeout(Integer.parseInt(BaseBallTimeOut));
							factory.setReadTimeout(Integer.parseInt(BaseBallTimeOut));
							
							RestTemplate restTemplate = new RestTemplate(factory);
//							RestTemplate restTemplate = new RestTemplate();
						
							//(예시) http://localhost:8888/highlightapi/setNSHitCount?파라미터
							String setNSHitCountServerFullUrl = String.format("%s/%s?SA_ID=%s&MAC_ADDR=%s&ALBUM_ID=%s&FLAG=%s&SCENE_TYPE=%s", 
									BaseBallServerUrl,
									"setNSHitCount",
									paramVO.getSaId(), 
									paramVO.getStbMac(),
									paramVO.getAlbumId(),
									"P",
									"H");

							ResponseEntity<String> responseEntity = restTemplate.getForEntity(setNSHitCountServerFullUrl, String.class, 25);
							
							JsonParser jsonParser = new JsonParser();
							JsonElement jsonElement = jsonParser.parse(responseEntity.getBody());
							
							String flag = jsonElement.getAsJsonObject().get("flag").toString();
							
							if(flag.equals("\"0\""))
							{
								msg	= "[BaseBall HighLight VOD Save Success]"; 								
								imcsLog.serviceLog(msg, methodName, methodLine);
							}
							else
							{
								String fail_message = jsonElement.getAsJsonObject().get("message").toString();
								msg	= "[BaseBall HighLight VOD Save Fail : " + fail_message + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
							}

			        	}catch(Exception e)
			        	{
//			        		msg	= "[BaseBall HighLight VOD Save Error : " + e.getMessage().substring(0, e.getMessage().length() > 20 ? 20 : e.getMessage().length()) + "]"; 								
			        		msg	= "[BaseBall HighLight VOD Save Pass]";
							imcsLog.serviceLog(msg, methodName, methodLine);
			        	}
			        }
			        
			} else {
				tempVO = new AuthorizeNViewResponseVO();
				// 2020.01.08 - 시청 권한 없고, 스트리밍 요청시 실패 응답값을 제대로 주기 위해 Default Set
				tempVO.setReplay4dYn("N");
				
				tempVO.setFlag("1");
				tempVO.setCapUrl("\b");
				tempVO.setCapFileName("\b");
				tempVO.setErrCode("60");
				tempVO.setWatermarkYn(szWatermarkYn);
				
				paramVO.setResultCode("21000000");
			}
				tempVO.setDatafreeWatchYn(paramVO.getDatafreeWatchYn());
				tempVO.setPresentYn(paramVO.getPresentYn());
				tempVO.setViewFlag(paramVO.getViewType());
				resultVO.add(0, tempVO);
				resultListVO.setCpnInfoVO(cpnInfoVO);
				resultListVO.setList(resultVO);
			}
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
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] result[" + resultListVO.toString() + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
			
//			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
//					+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
						
			//imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	

	

	/**
	 * 쿠폰 정보 조회 (SMARTUX 함수 이용)
	 * @param paramVO
	 * @return
	 */
	public ComCpnVO getCpnInfo(AuthorizeNViewRequestVO paramVO) throws Exception{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName	= oStackTrace.getMethodName();
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);
		
		long tp1, tp2	= 0;
		
		ComCpnVO rtnCpnInfoVO = new ComCpnVO();
		ComCpnVO cpnInfoVO = new ComCpnVO();
		
		String cpnInfo = "";
		
		String szMsg = " START smartux info: p_idx_sa[" + paramVO.getSaId() + "], c_album_id[" + paramVO.getAlbumId() + "],  product=["
				+ paramVO.getProdType() + "], screen_type[" + paramVO.getScreenType() + "], genre[" + paramVO.getGenreInfo() + "]";
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		imcsLog.serviceLog(szMsg, methodName, methodLine);
		
		// 발급가능쿠폰 정보 조회
		tp1	= System.currentTimeMillis();
		try{
			cpnInfo = "";
			
			cpnInfoVO	= authorizeNViewDao.getCpnPossibleList(paramVO);
			
			if(cpnInfoVO == null){
//				imcsLog.failLog(ImcsConstants.API_PRO_ID178, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}else{
				cpnInfo = cpnInfoVO.getCpnInfo();
			}
			
			if(cpnInfoVO != null && cpnInfoVO.getCpnInfo() != null && !"".equals(cpnInfoVO.getCpnInfo())) {
				rtnCpnInfoVO.setCpnInfo("CPN01" + ImcsConstants.COLSEP + cpnInfoVO.getCpnInfo() + ImcsConstants.COLSEP);
				
				// 쿠폰 존재 여부 조회 및 쿠폰 정보 입력
				this.insCpnInfo(paramVO, cpnInfoVO.getCpnInfo());
			}
			
		} catch(Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[" + ImcsConstants.LOG_MSG2 + "] " 
					+ String.format("%-22s", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG2) + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		tp2	= System.currentTimeMillis();
		imcsLog.timeLog("발급가능쿠폰(F_GET_CPN_COND_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		
		// 스탬프 정보 조회
		try{
			
			cpnInfo = "";
			cpnInfoVO	= authorizeNViewDao.getStmPossibleList(paramVO);
			
			if(cpnInfoVO == null){
//				imcsLog.failLog(ImcsConstants.API_PRO_ID178, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}else{
				cpnInfo = cpnInfoVO.getStmInfo();
			}
			
			if(cpnInfoVO != null && cpnInfoVO.getStmInfo() != null && !"".equals(cpnInfoVO.getStmInfo())) {
				rtnCpnInfoVO.setStmInfo("STP00" + ImcsConstants.COLSEP + cpnInfoVO.getStmInfo() + ImcsConstants.COLSEP);
			    
				// 쿠폰 존재 여부 조회 및 쿠폰 정보 입력
				this.insStmInfo(paramVO, cpnInfoVO.getStmInfo());
			} else {
				cpnInfoVO = new ComCpnVO();
			}
			
		    szMsg	= " SELECT smartux.F_GET_STM_COND_POSSIBLE =[" + cpnInfo + "]";
		    imcsLog.serviceLog(szMsg, methodName, methodLine);
		} catch(Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[" + ImcsConstants.LOG_MSG2 + "] " 
					+ String.format("%-22s", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG2) + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		tp1	= System.currentTimeMillis();
		imcsLog.timeLog("발급가능스탬프(F_GET_STM_COND_POSSIBLE)", String.valueOf(tp1 - tp2), methodName, methodLine);
		
		
		//if(!paramVO.getApplType().equals("E")) {
		if(!"E".equals(paramVO.getApplType().substring(0 ,1))) { //20190930 권형도
			// 사용 가능 쿠폰 정보 조회
			try{
				
				cpnInfo = "";
				cpnInfoVO	= authorizeNViewDao.getUseCpnPossibleList(paramVO);
				
				if(cpnInfoVO == null){
	//				imcsLog.failLog(ImcsConstants.API_PRO_ID178, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}else{
					cpnInfo = cpnInfoVO.getUseCpnInfo();
				}
				
				if(cpnInfoVO != null && cpnInfoVO.getUseCpnInfo() != null && !"".equals(cpnInfoVO.getUseCpnInfo())) {
					rtnCpnInfoVO.setUseCpnInfo("CPN02" + ImcsConstants.COLSEP + cpnInfoVO.getUseCpnInfo() + ImcsConstants.COLSEP);
				}
				
			} catch(Exception e) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[" + ImcsConstants.LOG_MSG2 + "] " 
						+ String.format("%-22s", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG2) + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
		}
		tp2	= System.currentTimeMillis();
		imcsLog.timeLog("사용가능스쿠폰(F_GET_CPN_USE_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);

		return rtnCpnInfoVO;
	}

	
	
	
	
	
	/**
	 * 스탬프 조회 후 스탬프 존재시 INSERT
	 * @param paramVO
	 * @return
	 */
	public int insStmInfo(AuthorizeNViewRequestVO paramVO, String szData) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		szData = StringUtil.nullToSpace(szData);
		String[] arrData	= null;
		String[] arrInfo	= null;
		
		int nDataCnt	= 1;
		int nCpnCnt		= 0;
		int nResult		= 0;

		String szMsg	= "";
		String szCpnId	= "";
		
		/*if("".equals(szData) || szData.indexOf(ImcsConstants.ARRSEP) < 0)
			return -1;
		
		arrData	= szData.split(ImcsConstants.ARRSEP);
		nDataCnt = arrData.length;
		
		for(int i = 0; i < nDataCnt; i++) {
			if("".equals(arrData) || arrData[i].indexOf("|") < 0)	
				return -1;*/
		if("".equals(szData) || szData.indexOf("|") < 0)
			return -1;
		
		arrInfo = szData.split("\\|");
		nDataCnt = arrInfo.length;
		for(int i=0; i<nDataCnt; i= i+4){
			
			szCpnId	= arrInfo[1];
			paramVO.setCpnId(szCpnId);
			
			// 쿠폰 존재여부 체크
			try {
				nCpnCnt	= authorizeNViewDao.getStmChk(paramVO);
			} catch(Exception e) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[    0] msg[" + String.format("%-21s", "coupon(mims)_info:" + ImcsConstants.RCV_MSG2 + "]");
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
			if(nCpnCnt > 0) {
				continue;
			}
			
			try {
				nResult = authorizeNViewDao.insStmInfo(paramVO);
				
				/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_CPM_STAMP_BOX_ACTION] COUPON_BOX_ACTION table [" + nResult + "] records Success at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);*/
			} catch(Exception e) { 	nResult	= -1;	}
			
			if(nResult < 1) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_CPM_STAMP_BOX_ACTION] COUPON_BOX_ACTION table Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				break;
			}
		}
		
		return nResult;
	}
	



	/**
	 * 쿠폰정보 조회 후 쿠폰 존재시 INSERT
	 * @param paramVO
	 * @return
	 */
	public int insCpnInfo(AuthorizeNViewRequestVO paramVO, String szData) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		szData = StringUtil.nullToSpace(szData);
		String[] arrData	= null;
		String[] arrInfo	= null;
		
		int nDataCnt	= 1;
		int nCpnCnt		= 0;
		int nResult		= 0;

		String szMsg	= "";
		String szCpnId	= "";
		/*
		if("".equals(szData) || szData.indexOf(ImcsConstants.ARRSEP) < 0)
			return -1;
		
		arrData	= szData.split(ImcsConstants.ARRSEP);
		nDataCnt = arrData.length;
		
		for(int i = 0; i < nDataCnt; i++) {
			if("".equals(arrData) || arrData[i].indexOf("|") < 0)	
				return -1;*/
		
		if("".equals(szData) || szData.indexOf("|") < 0)
			return -1;
		
		arrInfo = szData.split("\\|");
		nDataCnt = arrInfo.length;
		for(int i=0; i<nDataCnt; i= i+4){
			
			szCpnId	= arrInfo[1];
			paramVO.setCpnId(szCpnId);
			
			// 쿠폰 존재여부 체크
			try {
				nCpnCnt	= authorizeNViewDao.getCpnChk(paramVO);
			} catch(Exception e) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[    0] msg[" + String.format("%-21s", "coupon(mims)_info:" + ImcsConstants.RCV_MSG2 + "]");
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
			if(nCpnCnt > 0) {
				continue;
			}
			
			try {
				nResult = authorizeNViewDao.insCpnInfo(paramVO);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table [" + nResult + "] records Success at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) { 	nResult	= -1;	}
			
			if(nResult < 1) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				break;
			}
		}
		
		return nResult;
	}

	


	/**
	 * HEVC_YN을 체크하기 위하여 ASSET_ID 조회
	 * @param paramVO
	 * @return
	 */
	public String getAssetId(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
				
		String sqlId		= "lgvod178_030_20180525_001";
		String szAssetId	= "";

		List<String> list	= new ArrayList<String>();
		
		try {
			try {
				list = authorizeNViewDao.getAssetId(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				szAssetId	= StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return szAssetId;
	}
	
	
	
	
	/**
	 * Face-Match 준비여부 조회
	 * @param paramVO
	 * @return
	 */
	public FmInfoVO getFmInfo(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId	= "lgvod178_020_20180525_001";
		List<FmInfoVO> list	= new ArrayList<FmInfoVO>();
		FmInfoVO fmInfoVO = null;
		
		try {

			try {
				list = authorizeNViewDao.getFmInfo(paramVO);
	
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				fmInfoVO	= list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return fmInfoVO;
	}
	
	


	/**
	 * 장르 정보 조회
	 * @param paramVO
	 * @return
	 */
	public GenreInfoVO getGenreInfo(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId		= "lgvod178_p01_20180814_001";
		String szMsg		= "";
		String szGenreInfo	= "||";
		List<GenreInfoVO> list	= new ArrayList<GenreInfoVO>();
		GenreInfoVO genreVO		= new GenreInfoVO();
		
		try {
			
			try {
				list = authorizeNViewDao.getGenreInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty() && list.get(0) != null ) {
				genreVO	= list.get(0);
				//szGenreInfo	= StringUtil.nullToSpace(genreVO.getGenreLarge()) + "|" + StringUtil.nullToSpace(genreVO.getGenreMid())
				//		+ "|" + StringUtil.nullToSpace(genreVO.getGenreSmall());
			}
			
			try{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] select genre[" + szGenreInfo + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) {}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return genreVO;
	}
	
	
	
	/**
	 * CDN INFO 조회 로직 모음
	 * @param	AuthorizeNViewRequestVO
	 * @result 	CdnInfoVO
	 */
	public AuthorizePlayIpVO getCdnInfo(AuthorizePlayIpVO PlayIpVO, AuthorizeNViewRequestVO paramVO) throws Exception {
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

				// VIEWTYPE이 시청일 경우 서버 정보 및 파일정보 조회
				if( "S".equals(paramVO.getViewType()) ) {
					if( "Y".equals(paramVO.getBaseGb()) ) {
						
						/* WI-FI 사용자의 요청에 따라 SUB_NODE_CD값을 순차적으로 응답하기 위하여 모든 노드에 대하여
						 * 현재 iPos값에 해당하는 순번의 노드값 반환(방송운영팀 요청에 의한 개발)			*/
						
						//20200825 - LTE 로드밸런싱
						if(!PlayIpVO.getTmpNodeGroup().equals("T") && PlayIpVO.getTmpNodeGroup().length() == 1) {
							szNodeCd = this.getNodeCdLoadBalancing1(PlayIpVO, paramVO);
						}else if (!PlayIpVO.getTmpNodeGroup().equals("T") && PlayIpVO.getTmpNodeGroup().length() == 2) {
							szNodeCd = this.getNodeCdLoadBalancing2(PlayIpVO, paramVO);
						}
						
						// 지역노드 정보 조회
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
						paramVO.setNodeCd(szNodeCd);
						
						lstCdnInfo	= this.getVodServer1(paramVO, PlayIpVO);
					} else {
						lstCdnInfo	= this.getVodServer2(paramVO, PlayIpVO);
					}	
				}
				
				// VIEWTYPE이 다운로드일 경우 서버 정보 및 파일정보 조회
				else if( "D".equals(paramVO.getViewType()) ) {
					if( "Y".equals(paramVO.getBaseGb()) ) {
						
						/* WI-FI 사용자의 요청에 따라 SUB_NODE_CD값을 순차적으로 응답하기 위하여 모든 노드에 대하여
						 * 현재 iPos값에 해당하는 순번의 노드값 반환(방송운영팀 요청에 의한 개발)			*/

						//20200825 - LTE 로드밸런싱
						if(!PlayIpVO.getTmpNodeGroup().equals("T") && PlayIpVO.getTmpNodeGroup().length() == 1) {
							szNodeCd = this.getNodeCdLoadBalancing1(PlayIpVO, paramVO);
						}else if (!PlayIpVO.getTmpNodeGroup().equals("T") && PlayIpVO.getTmpNodeGroup().length() == 2) {
							szNodeCd = this.getNodeCdLoadBalancing2(PlayIpVO, paramVO);
						}

						// 지역노드 정보 조회
						if( "".equals(szNodeCd) && "T".equals(paramVO.getTmpNodeGroup())) {
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
						paramVO.setNodeCd(szNodeCd);
						lstCdnInfo	= this.getVodServer3(paramVO, PlayIpVO);
					} else {
						lstCdnInfo	= this.getVodServer4(paramVO, PlayIpVO);
					}
					if(paramVO.getBitRate().equals("M9")) {
						lstCdnInfo.setVodFileName("");
					}
				}
				
				
				if(lstCdnInfo == null) {
					if( "Y".equals(paramVO.getBaseGb()) ) {
						if( "S".equals(paramVO.getViewType()) ) {
							lstCdnInfo	= this.getVodServer5(paramVO, PlayIpVO);
						} else if( "D".equals(paramVO.getViewType()) ) {
							lstCdnInfo	= this.getVodServer6(paramVO, PlayIpVO);
							if(paramVO.getBitRate().equals("M9")) {
								lstCdnInfo.setVodFileName("");
							}
						}
						
					}
					
					if( "N".equals(paramVO.getBaseGb()) ) {
						if( "S".equals(paramVO.getViewType()) ) {
							lstCdnInfo	= this.getVodServer7(paramVO, PlayIpVO);
						} else if( "D".equals(paramVO.getViewType()) ) {
							lstCdnInfo	= this.getVodServer8(paramVO, PlayIpVO);
							if(paramVO.getBitRate().equals("M9")) {
								lstCdnInfo.setVodFileName("");
							}
						}
						
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
		if(lstCdnInfo != null) {
			lstCdnInfo.setNodeGroup(PlayIpVO.getNodeGroup());

			lstCdnInfo.setServerIpv6PlayIp1(ipv6_change(paramVO, lstCdnInfo.getServerIpv6PlayIp1(), lstCdnInfo.getServerIpv6Port(), lstCdnInfo.getServerPlayIp1(), lstCdnInfo.getServerIpv6Node1(), paramVO.getReplay4dYn()));
			lstCdnInfo.setServerIpv6PlayIp2(ipv6_change(paramVO, lstCdnInfo.getServerIpv6PlayIp2(), lstCdnInfo.getServerIpv6Port(), lstCdnInfo.getServerPlayIp2(), lstCdnInfo.getServerIpv6Node2(), paramVO.getReplay4dYn()));
			lstCdnInfo.setServerIpv6PlayIp3(ipv6_change(paramVO, lstCdnInfo.getServerIpv6PlayIp3(), lstCdnInfo.getServerIpv6Port(), lstCdnInfo.getServerPlayIp3(), lstCdnInfo.getServerIpv6Node3(), paramVO.getReplay4dYn()));
			
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
		}
		else
		{
			lstCdnInfo = new AuthorizePlayIpVO();
		}
		
		return lstCdnInfo;
	}


	public AuthorizePlayIpVO getVodServer1(AuthorizeNViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_009_20190320_001";
		String szMsg	= "";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		paramVO.setNodeGroup(PlayIpVO.getNodeGroup());
		try {
			
			try {
				list = authorizeNViewDao.getVodServer1(paramVO);
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
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "]" + 
					" msg[" + String.format("%-21s", "svr_play_ip:" + ImcsConstants.RCV_MSG6 + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("41000000");*/
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return returnVO;
	}
	
	public AuthorizePlayIpVO getVodServer2(AuthorizeNViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_010_20190320_001";
		String szMsg	= "";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		paramVO.setNodeGroup(PlayIpVO.getNodeGroup());
		try {
			
			try {
				list = authorizeNViewDao.getVodServer2(paramVO);
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
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "]" + 
					" msg[" + String.format("%-21s", "svr_play_ip:" + ImcsConstants.RCV_MSG6 + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("41000000");*/
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return returnVO;
	}
	
	public AuthorizePlayIpVO getVodServer3(AuthorizeNViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_011_20190320_001";
		String szMsg	= "";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		paramVO.setNodeGroup(PlayIpVO.getNodeGroup());
		try {
			
			try {
				list = authorizeNViewDao.getVodServer3(paramVO);
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
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "]" + 
					" msg[" + String.format("%-21s", "svr_play_ip:" + ImcsConstants.RCV_MSG6 + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("41000000");*/
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return returnVO;
	}
	
	public AuthorizePlayIpVO getVodServer4(AuthorizeNViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_012_20190320_001";
		String szMsg	= "";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		paramVO.setNodeGroup(PlayIpVO.getNodeGroup());
		try {
			
			try {
				list = authorizeNViewDao.getVodServer4(paramVO);
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
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "]" + 
					" msg[" + String.format("%-21s", "svr_play_ip:" + ImcsConstants.RCV_MSG6 + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("41000000");*/
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return returnVO;
	}
	
	public AuthorizePlayIpVO getVodServer5(AuthorizeNViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_012_20190320_001";
		String szMsg	= "";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		paramVO.setNodeGroup(PlayIpVO.getNodeGroup());
		try {
			
			try {
				list = authorizeNViewDao.getVodServer5(paramVO);
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
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "]" + 
					" msg[" + String.format("%-21s", "svr_play_ip:" + ImcsConstants.RCV_MSG6 + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("41000000");*/
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return returnVO;
	}
	
	public AuthorizePlayIpVO getVodServer6(AuthorizeNViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_012_20190320_001";
		String szMsg	= "";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		paramVO.setNodeGroup(PlayIpVO.getNodeGroup());
		try {
			
			try {
				list = authorizeNViewDao.getVodServer6(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				returnVO = new AuthorizePlayIpVO();
				querySize	= list.size();
				returnVO = list.get(0);
				
				
				if(paramVO.getBitRate().equals("M9")) {
					returnVO.setVodFileName("");
				}
			}

			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "]" + 
					" msg[" + String.format("%-21s", "svr_play_ip:" + ImcsConstants.RCV_MSG6 + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("41000000");*/
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return returnVO;
	}
	
	public AuthorizePlayIpVO getVodServer7(AuthorizeNViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_012_20190320_001";
		String szMsg	= "";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		paramVO.setNodeGroup(PlayIpVO.getNodeGroup());
		try {
			
			try {
				list = authorizeNViewDao.getVodServer7(paramVO);
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
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "]" + 
					" msg[" + String.format("%-21s", "svr_play_ip:" + ImcsConstants.RCV_MSG6 + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("41000000");*/
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return returnVO;
	}
	
	public AuthorizePlayIpVO getVodServer8(AuthorizeNViewRequestVO paramVO, AuthorizePlayIpVO PlayIpVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_012_20190320_001";
		String szMsg	= "";
		int querySize	= 0;
		List<AuthorizePlayIpVO> list	= new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO returnVO = null;
		paramVO.setNodeGroup(PlayIpVO.getNodeGroup());
		try {
			
			try {
				list = authorizeNViewDao.getVodServer8(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				
				if(paramVO.getBitRate().equals("M9")) {
					returnVO = new AuthorizePlayIpVO();
					querySize	= list.size();
					returnVO = list.get(0);
					
					
					if(paramVO.getBitRate().equals("M9")) {
						returnVO.setVodFileName("");
					}
				}
				
			}

			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "]" + 
					" msg[" + String.format("%-21s", "svr_play_ip:" + ImcsConstants.RCV_MSG6 + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("41000000");*/
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return returnVO;
	}
	
	
	/**
	 * 지역노드 정보 조회
	 * @param paramVO
	 * @return
	 */
	public String getNodeCd(AuthorizePlayIpVO playIpVO, AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId	= "lgvod178_008_20180525_001";
		String szNodeCd	= "";
		List<String> list	= new ArrayList<String>();

		try {
			
			try {
				list = authorizeNViewDao.getNodeCd(playIpVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				szNodeCd	= StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return szNodeCd;
	}

	/**
	 * 이벤트 타입조회
	 * @param 	AuthorizeNViewRequestVO
	 * @return	int
	 */
	public int getEventType(AuthorizeNViewRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId		= "lgvod178_s01_20180525_001";
    	String szEventType	= "";		
		List<String> list   = new ArrayList<String>();
		
		try {
			
			try{
				list = authorizeNViewDao.getEventType(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szEventType	= list.get(0);
				paramVO.setEventType(szEventType);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			return -1;
		}
		
    	return 0;
	}


	/**
	 * SVOD 가 아닌 상품정보 조회
	 */
	public List<ContTypeVO> getProdInfo(AuthorizeNViewRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	String sqlId =  "lgvod178_001_20180525_001";		
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		String szMsg = "";
		
		try {
			
			try{
				list = authorizeNViewDao.getProdInfo(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if((list == null || list.isEmpty()) && paramVO.getnWatchYn().equals("Y") == false){
				szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts[    0]" + String.format("%-21s", "msg[conts_type" + ImcsConstants.RCV_MSG3 + "]");
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				paramVO.setResultCode("21000000");
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) {
//				paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//				szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts["+cache.getLastException().getErrorCode()+"]" + 
//						"msg[" + String.format("%-21s", "conts_type" + cache.getLastException().getErrorMessage() + "]");
//			} else {
//				paramVO.setResultCode("41000000");
//				szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[    0]"  + 
//						"msg[" + String.format("%-21s", "conts_type" + ImcsConstants.RCV_MSG2 + "]");
//			}
			
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return list;
    }
	
	
	
	/**
	 * 구매목록 조회
	 * @param	AuthorizeNViewRequestVO
	 * @result	Integer
	 */
	public Integer chkBuyCont(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();

		Integer nDataChk = 0;
		String sqlId	= "";
		
		try {
			if("1".equals(paramVO.getContsType())) {
				sqlId = "lgvod178_003_20171214_001";
				nDataChk = authorizeNViewDao.chkBuyContType1(paramVO);
			} else if("2".equals(paramVO.getContsType())) {
				sqlId = "lgvod178_004_20171214_001";
				nDataChk = authorizeNViewDao.chkBuyContType2(paramVO);
			}
			
//			imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, nDataChk, methodName, methodLine);
		
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
		return nDataChk;
		
	}
	
	
	/**
	 * 선물내역 조회
	 * @param	AuthorizeNViewRequestVO
	 * @result	Integer
	 */
	public Integer getPresentCnt(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		Integer nDataChk	= 0;
		String sqlId	= "lgvod178_002_20171214_001";
		
		try {
			
			nDataChk	= authorizeNViewDao.getPresentCnt(paramVO);
//			imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, nDataChk, methodName, methodLine);
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
		return nDataChk;
	}
	
	
	
	/**
	 *  워터마크 정보 조회
	 *  @param	AuthorizeNViewRequestVO
	 *  @result	String
	 */
	public String getWatermarkChk(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId		=  "lgvod178_006_20180525_001";
		String szWatermark	= "";		
		List<String> list   = null;

		try {
			
			try{
				list = authorizeNViewDao.getWatermarkChk(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szWatermark	= list.get(0);
			}
			
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			//throw new ImcsException();
		}
		
		return szWatermark;
    }
	
	
	
	/**
	 * 자막정보 조회
	 * @param paramVO
	 * @return AuthorizeNViewResponseVO
	 * @throws Exception
	 */
	public AuthorizeNViewResponseVO getSmiInfo(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod178_007_20180618_001";		
		List<AuthorizeNViewResponseVO> list   = new ArrayList<AuthorizeNViewResponseVO>();
		AuthorizeNViewResponseVO resultVO	= new AuthorizeNViewResponseVO();
		
		String szMsg = "";
		
		try {
			
			try{
				list = authorizeNViewDao.getSmiInfo(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}			
			
			if( list != null && !list.isEmpty()){
				resultVO	= list.get(0);
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)
//				paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else
//				paramVO.setResultCode("41000000");
//			
//			szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts["+cache.getLastException().getErrorCode()+"]" + 
//					"msg[" + String.format("%-21s", "smi_info" + cache.getLastException().getErrorMessage() + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
		return resultVO;
    }
	
	


	
	/**
	 * 시청이력 저장
	 */
	public Integer insertWatchHis(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod178_i01_20180525_001";
    	Integer querySize = 0;		
		String szMsg = "";
		szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_VO_WATCH_HISTORY_NSC] tableStart at";
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		imcsLog.serviceLog(szMsg, methodName, methodLine);
		
    	try {
			// 2020.03.26 - 모바일 아이들나라 프로젝트로 아래 로직 추가
			if("A".equals(paramVO.getApplType().substring(0, 1)) )
			{
				String kidsGb = authorizeNViewDao.getKidsGb(paramVO);
				if(null == kidsGb || kidsGb.isEmpty()){
					paramVO.setKidsGb(""); 
				} else {
					paramVO.setKidsGb(kidsGb);
				}
			}else {
				paramVO.setKidsGb("");
			}
    		
			try{
				authorizeNViewDao.insertWatchHis(paramVO);
				
				if (paramVO.getLinkChk().equals("1"))
					authorizeNViewDao.updateWatchHisNScreen(paramVO);
				else
					authorizeNViewDao.insertWatchHisNScreen(paramVO);
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID178, sqlId, null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_VO_WATCH_HISTORY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
					
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_VO_WATCH_HISTORY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
						
			
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts [" + cache.getLastException().getErrorCode() + "]" + 
			//		"msg[" + String.format("%-21s", "insert fail:" + cache.getLastException().getErrorMessage() + "]");	
			//imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
		return querySize;
	}
	
	/**
	 * 시청이력 저장 - 엔스크린
	 */
	public Integer insertWatchHisNScreen(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod178_i02_20180618_001";
    	Integer querySize = 0;		
		String szMsg = "";
		int	nRetVal		= 0;
		szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_VO_WATCH_HISTORY] tableStart at";
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		imcsLog.serviceLog(szMsg, methodName, methodLine);
		
    	try {
			
        	//############################################################################
			// 2018.08.03 - 시청UDR 유/무료 설정 프로젝트로 아래 로직 추가
			// 엔스크린 구매연동으로 시청 + 가입권한으로 볼 경우에만 수행한다.
        	
    		HashMap<String, String> nSysdateinfo = authorizeNViewDao.getSysdateInfo();
    		paramVO.setWatchDate(nSysdateinfo.get("WATCH_DATE"));
    		
        	//2019.03.18
        	if (paramVO.getnBuyDate().equals("N")) {
    			paramVO.setPayYn("Y");
    			
    			if (paramVO.getPpvProdFlag().equals("Y")) {    				
    				paramVO.setSysdateCurrent(nSysdateinfo.get("SYSDATE_CURRENT"));
    				paramVO.setSysdate1yearago(nSysdateinfo.get("SYSDATE_1YEARAGO"));
    			}

    			if(paramVO.getTerrYn().equals("Y") && paramVO.getPpvProdFlag().equals("Y"))
    			{
    				if(paramVO.getSysdateCurrent().equals(paramVO.getTerrEddate()))
    				{
    					paramVO.setPayYn("N");
    				}
    			}
        	}
        	
//        	if (paramVO.getTerrYn().equals("Y")) {
//        		if (paramVO.getSysdateCurrent().equals(paramVO.getTerrEddate())) {
//        			paramVO.setPayYn("N");
//        		}
//        	}
//        	
//        	if (paramVO.getPreviewPeriod().equals("Y")) {
//        		if (!paramVO.getOnairDate().equals(paramVO.getSysdate1yearago())) {
//        			HashMap<String, String> nNScreenWatchSubscriptionInfo = authorizeNViewDao.getNScreenWatchSubscriptionInfo();
//                	paramVO.setTerrType(nNScreenWatchSubscriptionInfo.get("TERR_TYPE"));
//                	paramVO.setSysdateYearago(nNScreenWatchSubscriptionInfo.get("AGO_DATE"));
//                	
//                	if (paramVO.getTerrType().equals("1")) {
//                		if (!paramVO.getSysdateYearago().equals("")) {
//                			// 공중파쪽 VOD의 경우에는 방송일 1년 후에 유료 전환이 된다.
//                			if (paramVO.getSysdateYearago().equals(paramVO.getOnairDate())) {
//                				if (!paramVO.getSysdateYearago().equals(paramVO.getTerrEddate())) {
//                					paramVO.setPayYn("Y");
//                				}
//                			}
//                		}
//                	} else if (paramVO.getTerrType().equals("2")) {
//                		if (!paramVO.getSysdateYearago().equals("")) {
//                			// CJ쪽 VOD의 경우에는 무료전환일 1년 후에 유료 전환이 된다.
//                			if (!paramVO.getSysdateYearago().equals(paramVO.getTerrEddate())) {
//                				paramVO.setPayYn("Y");
//                			}
//                		}
//                	}
//        		}
//        	}
        	
//        	checkKey.addVersionTuple("PT_VO_WATCH_HISTORY", paramVO.getnSaId());
/*        	if (!paramVO.getUdrTransYn().equals("N")) { //lgvod178.c 4201
        		paramVO.setCatId("");
        		paramVO.setAlbumId("");
        		paramVO.setSuggestedPrice("");
        		paramVO.setCpId("");
        		paramVO.setContentFileSize("");
        		paramVO.setPayYn("");
        		paramVO.setTerrYn("");
        		paramVO.setPreviewPeriod("");
        		paramVO.setTerrPeriod("");
        	}*/
        	//############################################################################
        	
//			binds.add(paramVO);
//			
//			querySize = cache.updateWithCacheVersion(new VersionUpdateExcutor() {
//				@Override
//				public int execute(List<Object> parameters) throws SQLException {
//					try{
//						AuthorizeNViewRequestVO newInput = (AuthorizeNViewRequestVO)parameters.get(0);
//						return authorizeNViewDao.insertWatchHisNScreen(newInput);
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//			}, binds, checkKey);
        	
			try{
				//tpacall
//				if(paramVO.getnBuyDate().equals("N")) {
//					paramVO.setAssetIdAll("|"+ paramVO.getCatId() +"|"+paramVO.getAlbumId()+"|"+paramVO.getSuggestedPrice()+"|"+paramVO.getCpId()+"|"+paramVO.getContentFileSize()+"|"+paramVO.getPayYn()+"|"+paramVO.getTerrYn()+"|"+paramVO.getPreviewPeriod()+"|"+paramVO.getTerrPeriod()+"|");
//				}
				querySize = authorizeNViewDao2.insertWatchHisNScreen2(paramVO);
				if(querySize > 0 ) {
					String setTimeNScreenChk = authorizeNViewDao2.setTimeNScreenChk(paramVO);

					if (setTimeNScreenChk == null)
						authorizeNViewDao2.insertSetTimeNScreen(paramVO);
					else
						authorizeNViewDao2.updateSetTimeNScreen(paramVO);
				}
				else
				{
					nRetVal = -1;
				}
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_VO_WATCH_HISTORY] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				if(nRetVal >= 0)
				{
					if (paramVO.getnBuyDate().equals("N")) {
						querySize = authorizeNViewDao2.insWatchMeta(paramVO);
						
						if(querySize <= 0)
						{
							nRetVal = -1;
						}
					}
				}

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_VO_WATCH_META] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				if(nRetVal == -1)
				{
					throw new Exception();
				}
					
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				System.out.println(e.getMessage());
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
			
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] insert [PT_VO_WATCH_HISTORY] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
						
			
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] sts [" + cache.getLastException().getErrorCode() + "]" + 
			//		"msg[" + String.format("%-21s", "insert fail:" + cache.getLastException().getErrorMessage() + "]");	
			//imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
			throw new ImcsException();
		}
		
		return querySize;
	}
	
	
	/**
	 * 구간정보 이미지 조회
	 * @param paramVO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getThumnailInfo(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_017_20180525_001";
		List<HashMap> list	= new ArrayList<HashMap>();
		HashMap<String, String> mThumnail	= new HashMap<String, String>();
		
		try {
			
			try {
				list = authorizeNViewDao.getThumnailInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID178, sqlId, null, "thumbnail_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				//mThumnail	= list.get(0);
			}			
			
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID178, sqlId, null, "thumbnail_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}


	/**
	 * REAL HD 파일명 조회
	 * @param paramVO
	 * @return
	 */
	public String getLiveHevcFileName(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId		= "lgvod178_019_20180525_001";
		String szFileName	= "";
		List<String> list	= new ArrayList<String>();

		try {
			
			try {
				list = authorizeNViewDao.getLiveHevcFile(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				szFileName	= StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return szFileName;
	}
	
	/**
	 * 노드 정보 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCdLoadBalancing1(AuthorizePlayIpVO playIpVO, AuthorizeNViewRequestVO paramVO ){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId	=  "lgvod178_023_20180525_001";
    	String szNodeCd	= "";		
		List<String> list   = new ArrayList<String>();
		
		try {
			
			try{
				list = authorizeNViewDao.getNodeCdLoadBalancing1(playIpVO);
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
	 * 노드 정보 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCdLoadBalancing2(AuthorizePlayIpVO playIpVO, AuthorizeNViewRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId	=  "lgvod178_023_20180525_001";
    	String szNodeCd	= "";		
		List<String> list   = new ArrayList<String>();
		
		try {
			
			try{
				list = authorizeNViewDao.getNodeCdLoadBalancing2(playIpVO);
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
     * 시즌 여부 조회
     * @param	GetNSContDtlRequestVO
     * @result	String
    **/
    public String getSeasonInfo(AuthorizeNViewRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "";		
		List<String> list   = new ArrayList<String>();
		String result = null;
		
		try {		
			
			switch ( paramVO.getFxType().toUpperCase() ) {
				/* 비디오 포털	*/
				case "N":
				case "H":				
				case "T":
				case "P":
				case "M":
					sqlId = "lgvod178_021_20180525_001";
					try{
						list  = authorizeNViewDao.getSeasonInfo(paramVO);
					}catch(DataAccessException e){
						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
					}
					break;
	
				default:
					break;
			}
		
			
			if( list != null && !list.isEmpty()){
				result = (String)list.get(0);
			}else{
				result = "";
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID178, sqlId, null, "season_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);	
			 
			 result = "";
		}
    	return result;
    }
    
    /**
	 * 암호키 조회
	 * @param paramVO
	 * @return
	 */
	public String getEnctyptKey(AuthorizeNViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_023_20171214_001";
		String enctyptKey	= "";
		List<String> list	= new ArrayList<String>();
		String szMsg	= "";

		try {
			
			try {
				list = authorizeNViewDao.getEnctyptKey();
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				enctyptKey	= StringUtil.nullToSpace(list.get(0));
			}else{
//				imcsLog.failLog(ImcsConstants.API_PRO_ID178, sqlId, null, "get_encrypt_key:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("31000000");
				throw new ImcsException();
			}
			
		} catch (Exception e) {
			//String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "] " + 
			//		"msg[" + String.format("%-21s", "get_encrypt_key:" + cache.getLastException().getErrorMessage() + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("31000000");
			
			throw new ImcsException();
			
		}

		return enctyptKey;
	}
	
	/**
	 * 엔스크린 가입여부 확인
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private int nScreenPairingChk(AuthorizeNViewRequestVO paramVO) throws Exception
	{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_024_20180525_001";
		int pairingCnt	= 0;
		List<String> resultList = new ArrayList<>();
		String szMsg = "";
		
		try
		{
			
			try {
				resultList = authorizeNViewDao.getNScreenPairingChk(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅						
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(resultList != null && resultList.size() > 0)
				pairingCnt = 1;
			else
				pairingCnt = 0;
		}
		catch(Exception e)
		{
			//String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "] " + 
			//		"msg[" + String.format("%-21s", "nScreen-Pairing-Chk:" + cache.getLastException().getErrorMessage() + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("21000000");
			
			throw new ImcsException();
		}
		
		return pairingCnt;
	}
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private List<String> nscreenSubscriptionChkProductCdList(AuthorizeNViewRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod997_006_20180525_001";		
		List<String> productCdList = new ArrayList<>();
		String szMsg = "";
		
		try
		{
			
			try {
				productCdList = authorizeNViewDao.getNScreenProductCdList(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅						
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(productCdList == null)
				productCdList = new ArrayList<>();
			
			szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  SQLID[" + sqlId + "] rcv[" + productCdList.size() + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			return productCdList;
		}
		catch(Exception e)
		{
			//szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "] " + 
			//		"msg[" + String.format("%-21s", "nscreenSubscriptionChkProductCdList:" + cache.getLastException().getErrorMessage() + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("21000000");
			
			throw new ImcsException();
		}
	}
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크
	 * @param paramVO
	 * @throws Exception
	 */
	private int nscreenSubscriptionChk(List<String> productCdList, AuthorizeNViewRequestVO paramVO
			, AuthorizeNViewResultVO resultListVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		// 2017.11.30 - 해당 쿼리는 lgvod997(getNSContStat)에서도 동일하게 사용하므로 주의 필요!!
		//String sqlId = "lgvod997_007_20180525_001";
		String sqlId = "lgvod178_s03_20180817_001";
		
		//List<HashMap<String, String>> albumProducInfotList = new ArrayList<>();
		List<HashMap<String, String>> albumProducInfotList = new ArrayList<>();
		
		String szMsg = "";
		int dataChk = 0;
		
		try
		{
			
			try {
				albumProducInfotList = authorizeNViewDao.getNScreenAlbumProducInfotList(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅						
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] rcv[" + albumProducInfotList.size() + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setPpvProdFlag("N");
			for(HashMap<String, String> hm : albumProducInfotList)
			{
				String tempChckProdId_1 = hm.get("P_PRODUCT_ID");
				String tempChckProdId_2 = hm.get("PRODUCT_ID");
				String tempAssetId = hm.get("CONTENTS_ID");
				
				if (tempChckProdId_2.equals("20000")) {
					paramVO.setPpvProdFlag("Y");
				} else {
					for(String proCd : productCdList)
					{
						if(proCd.equals(tempChckProdId_1) || proCd.equals(tempChckProdId_2))
						{
							resultListVO.setProductId(tempChckProdId_2); // 통계 때문에 사용
							paramVO.setProductId(tempChckProdId_2);
							paramVO.setAssetId(tempAssetId);
							paramVO.setUdrTransYn("N");
							dataChk = 1;
							
							break;
						}
					}
				}
				
				if(dataChk == 1)
					break;
			}
			
			return dataChk;
		}
		catch(Exception e)
		{
			//szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "] " + 
			//		"msg[" + String.format("%-21s", "nscreen-Subscription-Chk:" + cache.getLastException().getErrorMessage() + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("21000000");
			
			throw new ImcsException();
		}
	}
	
	public Integer Request_Param_Valid(AuthorizeNViewRequestVO paramVO){
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String msg = "";
		Integer result_set = 0;
		
		try {
			//2018.12.06 VR앱 사용자는 엔스크린 시청 불가능
			if("E".equals(paramVO.getApplType().substring(0, 1)) && "Y".equals(paramVO.getnWatchYn())) {
				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[VR앱 사용자는 엔스크린 보기가 지원되지 않습니다.]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				return -1;
			}
			
			//2018.08.22 VR앱 사용자는 다운로드 이어보기 시청 가능 appl_type 첫번째 문자가 E 이면 VR앱 사용자
			//2018.05.17 TV앱 사용자는 다운로드하기 불가 rd1.c_view_type == D 이면 다운로드하기, rd1.c_free_flag == Y 이면 TV앱 사용자 (VR / 공연 앱은 예외로 다운로드가 가능하다.)
			if("D".equals(paramVO.getViewType()) && "Y".equals(paramVO.getFreeFlag()) && (!"E".equals(paramVO.getApplType().substring(0, 1)) && !"L".equals(paramVO.getApplType().substring(0, 1))) ) {
				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[TV앱 사용자는 다운로드하실 수 없습니다.]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				return -1;
			}
		} catch (Exception e) {
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] 정상적인 INPUT PARAM이 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			result_set = -1;
		}
		
		return result_set;
	}
	
	/**
	 * M3U8정보 조회
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	getVodM3u8Search
	 */
	public List<AuthorizePlayIpVO> getVodM3u8Search(AuthorizeNViewRequestVO paramVO, AuthorizeNViewResponseVO tempVO ,String AWS_app, String AWS_web) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod178_017_20180525_001";
		
		List<M3u8AWSProfileVO> vodM3u8AWSList = new ArrayList<M3u8AWSProfileVO>();
		List<AuthorizePlayIpVO> authorizePlayIpList = new ArrayList<AuthorizePlayIpVO>();
		AuthorizePlayIpVO authorizePlayIp = null;
		
		// 이전에 조회한 node_group일 경우에는 CDN IP를 재사용하기 위해 변수값으로 제어한다.
		int i_dup_chk = 0;
		
		
		try {
			
			try {
				if(AWS_app.equals("N"))
				{
					vodM3u8AWSList = authorizeNViewDao.getVodProfile(paramVO);
				}
				else
				{
					// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
					vodM3u8AWSList = authorizeNViewDao.getVodProfileAWS(paramVO);
				}
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			
			if( vodM3u8AWSList == null || vodM3u8AWSList.isEmpty()){
				paramVO.setResultSet(-1);
			} else {
				for(int m3u8_cnt = 0 ; m3u8_cnt < vodM3u8AWSList.size() ; m3u8_cnt++)
				{
					authorizePlayIp = null;
					authorizePlayIp = new AuthorizePlayIpVO();
					i_dup_chk = 0;
					
					// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
					if( !(AWS_web.equals("Y") || (AWS_app.equals("Y") && vodM3u8AWSList.get(m3u8_cnt).getDistrFlag().equals("W"))) )
					{											
						// 2019.09.09 - Ipv6 : Hevc화질인 L 프로파일일 때에도 온넷 CDN만 줄 수 있도록 node_group을 H로 조회한다.
						if(vodM3u8AWSList.get(m3u8_cnt).getNodeGroup().equals("N") && vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))
						{
							vodM3u8AWSList.get(m3u8_cnt).setNodeGroup("H");							
						}
						
						// 2019.04.02 - 일반노드 배포인데, MP화질 O / VR X / 공연 O일 경우에는 온누리넷만 배포하므로 별도로 처리한다. (WIFI의 경우 H, LTE의 경우 E로 처리)
						if(vodM3u8AWSList.get(m3u8_cnt).getNodeGroup().equals("N")  && (vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("P") || vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("N")) && tempVO.getVrYn().equals("N") && tempVO.getMusicYn().equals("Y"))
						{
							vodM3u8AWSList.get(m3u8_cnt).setNodeGroup("H");
						}
	
						// 2019.04.30 - 일반노드 배포인데, MN화질일 경우에는 온누리넷만 배포하므로 별도로 처리한다. (WIFI의 경우 H, LTE의 경우 E로 처리) - 5G 2차 고음질 어셋
						if(vodM3u8AWSList.get(m3u8_cnt).getNodeGroup().equals("N") && vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("N"))
						{
							vodM3u8AWSList.get(m3u8_cnt).setNodeGroup("H");							
						}
						
						authorizePlayIpList.add(m3u8_cnt, authorizePlayIp);
						authorizePlayIpList.get(m3u8_cnt).setNodeGroup(vodM3u8AWSList.get(m3u8_cnt).getNodeGroup());
						
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
						
						// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
						if(AWS_app.equals("N"))
						{
							authorizePlayIpList.set(m3u8_cnt, getCdnInfo(authorizePlayIpList.get(m3u8_cnt), paramVO));
						}
						else
						{
							if(vodM3u8AWSList.get(m3u8_cnt).getServiceYn().equals("N"))
							{
								continue;
							}
							authorizePlayIpList.set(m3u8_cnt, getCdnInfo(authorizePlayIpList.get(m3u8_cnt), paramVO));
						}						
						
					}
					else
					{						
						authorizePlayIpList.add(m3u8_cnt, authorizePlayIp);
						authorizePlayIp.setServerPlayIp1(vodM3u8AWSList.get(m3u8_cnt).getAwsServerPlayIp1());
						authorizePlayIp.setServerPlayIp2(vodM3u8AWSList.get(m3u8_cnt).getAwsServerPlayIp2());
						authorizePlayIp.setServerPlayIp3(vodM3u8AWSList.get(m3u8_cnt).getAwsServerPlayIp3());
						authorizePlayIp.setServerType1("2");
						authorizePlayIp.setServerType2("2");
						authorizePlayIp.setServerType3("2");
						authorizePlayIp.setServerIpv6Node1("Z");
						authorizePlayIp.setServerIpv6Node2("Z");
						authorizePlayIp.setServerIpv6Node3("Z");
						authorizePlayIp.setServerIpv6PlayIp1(vodM3u8AWSList.get(m3u8_cnt).getAwsServerPlayIp1());
						authorizePlayIp.setServerIpv6PlayIp2(vodM3u8AWSList.get(m3u8_cnt).getAwsServerPlayIp2());
						authorizePlayIp.setServerIpv6PlayIp3(vodM3u8AWSList.get(m3u8_cnt).getAwsServerPlayIp3());
						authorizePlayIp.setServerIpv6Type1("2");
						authorizePlayIp.setServerIpv6Type1("2");
						authorizePlayIp.setServerIpv6Type1("2");
						
						// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
						if(vodM3u8AWSList.get(m3u8_cnt).getAwsServiceYn().equals("N"))
						{
							continue;
						}
						
						authorizePlayIpList.set(m3u8_cnt, authorizePlayIp);
					}
					
				}
				
				for(int m3u8_cnt = 0 ; m3u8_cnt < vodM3u8AWSList.size() ; m3u8_cnt++)
				{
					authorizePlayIpList.get(m3u8_cnt).setM3u8Type(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type());
					authorizePlayIpList.get(m3u8_cnt).setCastisM3u8(vodM3u8AWSList.get(m3u8_cnt).getCastisM3u8());
					authorizePlayIpList.get(m3u8_cnt).setOnnuriM3u8(vodM3u8AWSList.get(m3u8_cnt).getOnnuriM3u8());
										
					if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H") || vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))					
					{
						// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
						if( !(AWS_web.equals("Y") || (AWS_app.equals("Y") && vodM3u8AWSList.get(m3u8_cnt).getDistrFlag().equals("W"))) )
						{
							// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
							if(AWS_app.equals("Y"))
							{								
								if(vodM3u8AWSList.get(m3u8_cnt).getServiceYn().equals("N"))
								{
									continue;
								}
							}
														
							// 2019.02.16 - CastIs local IP Set
							if(authorizePlayIpList.get(m3u8_cnt).getServerType1().equals("1"))
							{
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHL(vodM3u8AWSList.get(m3u8_cnt).getCastisM3u8());
								}
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLL(vodM3u8AWSList.get(m3u8_cnt).getCastisM3u8());
								}
							}
							// 2019.02.16 - OnNet local IP Set
							else
							{
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHL(vodM3u8AWSList.get(m3u8_cnt).getOnnuriM3u8());
								}
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLL(vodM3u8AWSList.get(m3u8_cnt).getOnnuriM3u8());
								}
							}
	
							// 2019.02.16 - CastIs Near IP Set							
							if(authorizePlayIpList.get(m3u8_cnt).getServerType2().equals("1"))
							{
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHN(vodM3u8AWSList.get(m3u8_cnt).getCastisM3u8());
								}
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLN(vodM3u8AWSList.get(m3u8_cnt).getCastisM3u8());
								}
							}
							// 2019.02.16 - OnNet Near IP Set
							else
							{
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHN(vodM3u8AWSList.get(m3u8_cnt).getOnnuriM3u8());
								}
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLN(vodM3u8AWSList.get(m3u8_cnt).getOnnuriM3u8());
								}
							}
	
							// 2019.02.16 - CastIs Center IP Set
							if(authorizePlayIpList.get(m3u8_cnt).getServerType3().equals("1"))
							{
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHC(vodM3u8AWSList.get(m3u8_cnt).getCastisM3u8());
								}
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLC(vodM3u8AWSList.get(m3u8_cnt).getCastisM3u8());
								}
							}
							// 2019.02.16 - OnNet Center IP Set
							else
							{
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H"))									
								{
									tempVO.setVodFileNameHC(vodM3u8AWSList.get(m3u8_cnt).getOnnuriM3u8());
								}
								if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))
								{
									tempVO.setVodFileNameLC(vodM3u8AWSList.get(m3u8_cnt).getOnnuriM3u8());
								}
							}
						}
						else
						{
							// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
							if(vodM3u8AWSList.get(m3u8_cnt).getAwsServiceYn().equals("N"))
							{
								continue;
							}
							
							if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H"))									
							{
								tempVO.setVodFileNameHL(vodM3u8AWSList.get(m3u8_cnt).getAws_Filename1());								
								tempVO.setVodFileNameHN(vodM3u8AWSList.get(m3u8_cnt).getAws_Filename2());								
								tempVO.setVodFileNameHC(vodM3u8AWSList.get(m3u8_cnt).getAws_Filename3());
							}
							if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L"))
							{
								tempVO.setVodFileNameLL(vodM3u8AWSList.get(m3u8_cnt).getAws_Filename1());								
								tempVO.setVodFileNameLN(vodM3u8AWSList.get(m3u8_cnt).getAws_Filename2());								
								tempVO.setVodFileNameLC(vodM3u8AWSList.get(m3u8_cnt).getAws_Filename3());
							}
						}
						
						if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("H"))									
						{
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
						}
						else if(vodM3u8AWSList.get(m3u8_cnt).getM3u8Type().equals("L") && (paramVO.getViewType().equals("S") || (paramVO.getViewType().equals("D") && tempVO.getVrYn().equals("N"))))
						{
							tempVO.setRealHdServer1(authorizePlayIpList.get(m3u8_cnt).getServerPlayIp1());
							tempVO.setRealHdServer2(authorizePlayIpList.get(m3u8_cnt).getServerPlayIp2());
							tempVO.setRealHdServer3(authorizePlayIpList.get(m3u8_cnt).getServerPlayIp3());
							tempVO.setRealHdIpv6Server1(authorizePlayIpList.get(m3u8_cnt).getServerIpv6PlayIp1());
							tempVO.setRealHdIpv6Server2(authorizePlayIpList.get(m3u8_cnt).getServerIpv6PlayIp2());
							tempVO.setRealHdIpv6Server3(authorizePlayIpList.get(m3u8_cnt).getServerIpv6PlayIp3());
						}

						continue;
					}
				}
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID178, sqlId, null, "m3u8_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return authorizePlayIpList;
	}
	
	/**
	 * IPv4 -> IPv6로 변환
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	ipv6_change
	 */
	public String ipv6_change(AuthorizeNViewRequestVO paramVO, String ipv6_ip, String ipv6_port, String ipv4_ip, String cdn_type, String fourd_replay) throws Exception {
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
	
//	public String urlCall(String url, String copyTimeOut, AuthorizeNViewRequestVO paramVO) throws Exception{
//		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
//		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
//		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//		factory.setConnectTimeout(Integer.parseInt(copyTimeOut));
//		factory.setReadTimeout(Integer.parseInt(copyTimeOut));
//		String msg = ""; 
//		RestTemplate restTemplate = new RestTemplate(factory);
//	     
//		String flag = "";
//		String retStr = ""; 
//		try {
//			retStr = restTemplate.getForObject(url, String.class);
//			
//			
//			String[] arrData	= null;
//			arrData	= retStr.split("\\|");
//			if(arrData.length > 1 && arrData.length < 3) {
//				retStr = arrData[0];
//				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[URL 호출 : "+url+"]";
//				imcsLog.serviceLog(msg, methodName, methodLine);
//			} else {
//				flag = "1";
//				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[URL 호출 오류: "+url+"]";
//				imcsLog.serviceLog(msg, methodName, methodLine);
//			}
//			
//			if (retStr.equals("1")) {
//				flag = "1";
//			} else {
//				flag = retStr;
//			}
//			
//		} catch(Exception e) {
//			flag = "1";
//			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "]  msg[URL 호출 오류: "+url+"]";
//			imcsLog.serviceLog(msg, methodName, methodLine);
//		}
//		
//		return flag;
//	}
	
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private String chkCategory(AuthorizeNViewRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String chkCate = "";
		String szMsg = "";
		
		try
		{
			try {
				chkCate = authorizeNViewDao.chkCategory(paramVO);
				
				if(chkCate == null)
				{
					chkCate = "0";
				}
				
				
			} catch (DataAccessException e) {
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		}
		catch(Exception e)
		{
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			paramVO.setResultCode("21000000");
			throw new ImcsException();
		}
		
		return chkCate;
	}
	
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private List<String> kidProductCd(AuthorizeNViewRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		List<String> list = new ArrayList<>();
		String szMsg = "";
		
		try
		{
			try {
				list = authorizeNViewDao.kidProductCd(paramVO);
				
				if(list == null || list.isEmpty())
				{
					paramVO.setResultSet(-1);
				}
				
			} catch (DataAccessException e) {
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		}
		catch(Exception e)
		{
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			paramVO.setResultCode("21000000");
			throw new ImcsException();
		}
		
		return list;
	}
}