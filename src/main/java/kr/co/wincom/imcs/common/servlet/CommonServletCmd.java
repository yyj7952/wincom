//package kr.co.wincom.imcs.common.servlet;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.SocketTimeoutException;
//import java.net.URL;
//import java.util.Properties;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import kr.co.wincom.imcs.common.property.ImcsProperties;
//import kr.co.wincom.imcs.common.util.GlobalCom;
//import kr.co.wincom.imcs.common.util.StringUtil;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//public class CommonServletCmd {
//	private Log imcsLogger		= LogFactory.getLog("API_COMMON");
//	
//	long sTimeM, eTimeM;
//    long sTimeM2, eTimeM2;
//    int log_level = 1 ; // default : 1 
//
//    // 무중단 전환 관련 파라미터
//    //private static String mode = "N"; // 일반 : N, 무중단 : M
//    String mode = "N"; // 일반 : N, 무중단 : M
//    
//    
//    String SERVER_CONF = ImcsProperties.getProperty("filepath.server");
//    Properties serverManager = GlobalCom.getPropertyFile(SERVER_CONF);
//    private String server = serverManager.getProperty("main");
//    
//    // MPTCP 관련 파라미터
//    String MPTCP_CONF = ImcsProperties.getProperty("filepath.mptcp");
//    Properties mptcpManager = GlobalCom.getPropertyFile(MPTCP_CONF);
//    private String mptcp_yn = mptcpManager.getProperty("mptcp_yn");
//
//    // 99서버 화질검수 단말관련 파라미터
//    String VTS99_CONF = ImcsProperties.getProperty("filepath.vts99");
//    Properties vts99Manager = GlobalCom.getPropertyFile(VTS99_CONF);
//    private String check_yn = vts99Manager.getProperty("check_yn");
//
//    // 세션서버 인증 관련 파라미터
//    String SESSION_CONF = ImcsProperties.getProperty("filepath.session");
//    Properties sessionManager = GlobalCom.getPropertyFile(SESSION_CONF);
//    private String session_yn = sessionManager.getProperty("session_yn");
//    private String cTimeout = sessionManager.getProperty("ConnectTimeout");
//    private String rTimeout = sessionManager.getProperty("ReadTimeout");
//    
//    private HttpServletRequest	req;
//    //private HttpServletResponse res;
// 
//    public CommonServletCmd(HttpServletRequest req, HttpServletResponse res, String mode) {
//    	this.req	= req;
//    	//this.res	= res;
//    	this.mode 	= mode;
//    }
//	
//    
//	public String getServletCmd(){
//		// IMCSLog imcsLog = new IMCSLog(imcsLogger);
//		
//		Boolean bRetVal	= true;
//		
//		PrintWriter out = null;
//		HttpURLConnection conn = null;
//		
//		String szSvcName	= "";
//	    String szParam		= "";
//	
//	    
//	    String SESSION_CONF = ImcsProperties.getProperty("filepath.session");
//	    Properties p = GlobalCom.getPropertyFile(SESSION_CONF);
//	    session_yn = p.getProperty("session_yn");
//		    
//        try {
//        	// MMI 서버팜에서만 "KSC5601" 적용
//    	    //req.setCharacterEncoding("UTF-8");
//    		
//    		szSvcName	= req.getParameter("CMD"); // ServiceID
//    		szParam		= req.getParameter("PARAM"); // ServiceID
//    		
//    		// res.setContentType("text/html; charset=KSC5601");
//                        
//	        // 무중단 전환 설정 기능 추가 START
//	        if (szSvcName.contains("setMode")) {
//	            if( server == null ||  server.equals("") ){
//	                server = "127.0.01";
//	            }
//	            if(req.getRemoteAddr().startsWith(server) || req.getRemoteAddr().startsWith("localhost") || req.getRemoteAddr().startsWith("127.0.0.1") ) {
//	                if (szParam.equals("M")) {
//	                    setMode(szParam);
//	                    imcsLogger.debug(" [CommSvl     ] MJD START!!!" + ", IP= " + req.getRemoteAddr() + "[" + req.getLocalAddr() + ", " + req.getLocalName() 
//	                    	+ ", CommSvl     ] "	+  "무중단모드로 전환되었습니다.");
//	                } else if (szParam.equals("N")) {
//	                    setMode(szParam);
//	                    imcsLogger.debug(" [CommSvl     ] MJD END!!!" + ", IP= " + req.getRemoteAddr() + "[" + req.getLocalAddr() + ", " + req.getLocalName() + ", CommSvl     ] "  +  "일반모드로 전환되었습니다.");
//	                } else {
//	                	imcsLogger.debug(" [CommSvl     ] Current Mode is \"" + getMode() + "\", Request IP= " + req.getRemoteAddr() + "[" + req.getLocalAddr() + ", " + req.getLocalName() + ", CommSvl     ] "  +  "모드확인 : " +  getMode() + "    ※ N:일반 / M:무중단 ");
//	                }
//	            } else {
//	                imcsLogger.debug(" [CommSvl     ] unauthorized ip!! (" + szParam + "), Current Mode is " + getMode() + ", Request IP= " + req.getRemoteAddr());
//	                if (mode.equals("M")) {
//	                	imcsLogger.debug(" [" + req.getLocalAddr() + ", " + req.getLocalName() + ", CommSvl     ] "  +  "무중단모드");
//	                } else {
//	                	imcsLogger.debug(" [" + req.getLocalAddr() + ", " + req.getLocalName() + ", CommSvl     ] "  +  "일반모드");
//	                }
//	            }
//	            
//	            bRetVal = false;
//	        } 
//	        // 무중단 전환 설정 기능 추가 END
//	
//	        
//	        // 무중단 모드의 경우 호출함수명에 m_로 된 API를 호출한다. 무중단모드시 n_과 t_는 제외
//            /*if (mode.equals("M")) {
//                if(!szSvcName.startsWith("n_") && !szSvcName.startsWith("t_")){
//                    szSvcName = "m_" + szSvcName;
//                }
//            }*/
//
//	        // API명 15자리 제한
//	        if(szSvcName.length() > 15 ){
//	            szSvcName = szSvcName.substring(0, 15);
//	        }
//	        
//	        
//	        // SA보안인증 START - 2017.07.26 현재 NSC에는 보안인증 기능 X (ETC서버에만 적용되어있음)
//	        // 1. 무중단모드가 아닌 때, 2. 인증모드가 'Y' 일때, 3. 대상 API 일때, 4. SESSION 파라미터가 존재할때, 5. SA서버 응답이 Timeout이 아닐때
//	        if( !mode.equals("M") && session_yn.equals("Y") && sessionManager.containsValue(szSvcName)){ // 인증대상 API 이면 인증서버로 인증시도
//	            sTimeM2 = System.currentTimeMillis();
//	
//	            // PARAM (가번, MAC_ADDR ) 취득 
//	            String szParam_arr[]	= szParam.split("\\|");
//	            String szSaId			= szParam_arr[0].replace("SA_ID=","");
//	            String szStbMac			= szParam_arr[1].replace("STB_MAC=","");
//	
//	            // 인증 szParameter에서 app_id, session_id , app_type 취득
//	            // SESSION=APP_ID=APPID|SESSION_KEY=S_KEY|APP_TYPE=RUSA|
//	            String arrSessionParam[]	= null;
//	            String szServiceId		= "";
//	            String szSessionId		= "";
//	            String szAppType		= "";
//	            String szSAServerIp	= "";
//	
//	            // 전달받은 파라미터에 SESSION 파라미터가 존재한다면 세션인증정보 설정 
//	            if (req.getParameter("SESSION") != null && !req.getParameter("SESSION").equals("")) {
//	            	arrSessionParam		= req.getParameter("SESSION").split("\\|");
//	                szServiceId			= arrSessionParam[0].replace("APP_ID=","");
//	                szSessionId			= arrSessionParam[1].replace("SESSION_KEY=","");
//	                szAppType			= arrSessionParam[2].replace("APP_TYPE=","");
//	                
//	                szSAServerIp		= sessionManager.getProperty("sessionServerIP");
//	            } else {
//	            	imcsLogger.info(" [CommSvl] Session info null !! bypass ");
//	            }
//	
//	            // 인증대상 APP_TYPE 이 아니거나 인증제외가번인 경우 인증 통과 , 
//	            if(!sessionManager.containsValue(szAppType) || sessionManager.containsValue(szSaId)) { // 인증대상 API 이면 인증서버로 인증시도
//	                // 인증 통과
//	                eTimeM2 = System.currentTimeMillis();
//	                imcsLogger.info(" [CommSvl] Session info fail SvcName [" + szSvcName + "] AppType[" + szAppType + "]!! bypass, Session authorize Time = " + (eTimeM2 - sTimeM2));
//	            } else {
//	                // 인증 시도
//	            	try{
//	            		// 세션 인증을 위한 URL 설정
//	                    URL url = new URL("http://"+ szSAServerIp + "/samodule/user/auth/session?entrNo="+ szSaId + "&clientMac=" + szStbMac + "&serviceId=" + szServiceId + "&saUserSessionId=" + szSessionId);
//	                    conn = (HttpURLConnection) url.openConnection();
//	                    conn.setRequestMethod("POST");
//	
//	                    
//	                    // 타임 아웃 설정값 load 실패시 예외처리
//	                    this.cTimeout = StringUtil.replaceNull(this.cTimeout, "500");
//	                    this.rTimeout = StringUtil.replaceNull(this.rTimeout, "500");
//	                    
//	                    imcsLogger.info(" [CommSvl] getURL() : " + conn.getURL());	// URL 얻어오기
//	                    
//	                    if(conn.getResponseCode() == 200){
//	                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//	                        StringBuffer buffer = new StringBuffer();
//	                        String line = null;
//	                        while ((line = reader.readLine()) != null) {
//	                            buffer.append(line).append("\r\n");
//	                        }
//	                        
//	                        reader.close();
//	                        
//	                        imcsLogger.debug(" [CommSvl] Session check responseMessage =" + buffer.toString());	// 연동 결과 로그
//	                        String resultValue[] = buffer.toString().split("!\\^");
//	                
//	                        if(resultValue.length > 2 && resultValue[2].startsWith("valid")){
//	                        	imcsLogger.debug(" [CommSvl] authorized Session!! SVCNAME=" + szSvcName + " PARAM=" + szParam);    
//	                        }else{
//	                        	imcsLogger.debug(" [CommSvl] unauthorized Session!! SVCNAME=" + szSvcName + " PARAM=" + szParam);
//	                            bRetVal = false;
//	                        }
//	                    }else{ // SA서버 Timeout 발생시 인증 통과
//	                    	imcsLogger.info(" [CommSvl] Session Server Check Fail!! SVCNAME=" + szSvcName + " PARAM="+szParam);
//	                    }
//	                }catch(SocketTimeoutException e){
//	                    eTimeM2 = System.currentTimeMillis();
//	                    imcsLogger.info(" [CommSvl] Session Server SocketTimeoutException!! bypass, Session authorize Time = " + (eTimeM2 - sTimeM2));
//	                }
//	            	
//	                eTimeM2 = System.currentTimeMillis();
//	                imcsLogger.info(" [CommSvl] Session authorize Time = " + (eTimeM2 - sTimeM2) + ", URL = " + conn.getURL());
//	            }
//	        }
//	        // SA보안인증 END
//
//	        //System.out.println(req.getRemoteAddr());
//	        
//	        if(bRetVal){ // tmax tpcall start 
//	            // MPTCP BASE_CD 변환 START
//	            if(mptcp_yn.equals("Y") && szSvcName.startsWith("authorizeN")){
//	            	if(mptcpManager.containsValue(req.getRemoteAddr()) && szParam.contains("VIEW_TYPE=D")){
//	            		szParam = szParam.replace("|BASE_CD=W", "|BASE_CD=M").replace("|BASE_CD=L", "|BASE_CD=M").replace("|BASE_CD=G", "|BASE_CD=M");
//	            		imcsLogger.debug(" [CommSvl] MPTCP replace TPCALL SVCNAME=" + szSvcName + " PARAM="+szParam);
//	                }
//	            }
//	            // MPTCP BASE_CD 변환 END
//	
//	            // 99서버 화질검수단말 17자리 제외 처리 API 호출 설정 - 2016.03.15 by kmc
//	            if (check_yn.equals("Y") && szParam != null && !szParam.equals("") && szParam.contains("SA_ID")){
//	                String sa_id = szParam.substring(szParam.indexOf("=")+1, szParam.indexOf("|"));
//	                if(vts99Manager.containsValue(sa_id) && vts99Manager.containsKey(szSvcName)){
//	                    szSvcName = vts99Manager.getProperty(szSvcName);
//	                }
//	            }
//	            // 99서버 화질검수단말 17자리 제외 처리 API 호출 설정
//           
//	        }
//	        
//        } catch (Exception e) {
//        	imcsLogger.debug(" [CommSvl] Exception : " + e.toString());
//        } finally {
//        	if(conn != null) {conn.disconnect();}
//        	if(out != null)	{out.close();}
//        }
//        
//        return szParam;
//	}
//	
//	
//   /* public static String getMode() {
//        return mode;
//    }
//
//    public static void setMode(String mode) {
//      //  WebTUrlCmd.mode = mode;
//    }*/
//    
//	 public String getMode() {
//	        return mode;
//	}
//
//    public void setMode(String mode) {
//        this.mode = mode;
//    }
//	
//}
